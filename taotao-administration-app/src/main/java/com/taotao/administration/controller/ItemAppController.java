package com.taotao.administration.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.administration.utils.IDUtils;
import com.taotao.administration.utils.TaotaoUtils;
import com.taotao.administration.utils.TreeNode;
import com.taotao.itemservice.model.Item;
import com.taotao.itemservice.model.ItemCategory;
import com.taotao.itemservice.model.ItemPageBean;
import com.taotao.itemservice.service.ItemCategoryService;
import com.taotao.itemservice.service.ItemInfoService;
import com.taotao.pay.message.model.TransactionMessage;
import com.taotao.pay.message.service.TransactionMessageService;
/**
 * 商品相关内容管理客户端
 * @author Administrator
 *
 */
@Controller
public class ItemAppController {
	
	@Autowired
	private ItemCategoryService itemCategoryService;
	@Autowired
	private ItemInfoService itemInfoService;
	@Autowired
	private TransactionMessageService transactionMessageService;
	
	@Value("${ADDITEMTOESQUEUE}")
	private String ADDITEMTOESQUEUE;
	@Value("${unInstock}")
	private Integer unInstock;
	@Value("${trackerServerAddr}")
	private String trackerServerAddr;
	@Value("{RESHELFITEMQUEUE}")
	private String RESHELFITEMQUEUE;
	@Value("${INSTOCKITEMQUEUE}")
	private String INSTOCKITEMQUEUE;
	
	/**
	 * 根据父类id查询子类异步查询接口
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<TreeNode> findItemCateList(@RequestParam(defaultValue="0",name="id")Long parentId) {
		List<ItemCategory> itemCategoryList = itemCategoryService.findItemCategoryByParentId(parentId);
		
		if(itemCategoryList == null) {
			return null;
		}
		
		//创建返回的节点对象
		List<TreeNode> nodes = new ArrayList<>();
		for (ItemCategory itemCategory : itemCategoryList) {
			TreeNode node = new TreeNode();
			node.setId(itemCategory.getId());
			node.setState(itemCategory.getIsParent() == 1?"closed":"open");
			node.setText(itemCategory.getName());
			System.out.println("日志=========》返回的节点的is_parent" + itemCategory.getIsParent());
			
			nodes.add(node);
		}
		
		return nodes;
	}
	
	/**
	 * 添加商品信息接口(需要将商品数据同步到搜索库中，事务采用可靠消息最终一致性事务)
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoUtils saveItemInfo(Item item, String queueType) throws Exception {
		//消息预发送
		TransactionMessage transactionMessage = new TransactionMessage();
		transactionMessage.setCreateTime(new Date());
		transactionMessage.setConsumerQueue(ADDITEMTOESQUEUE);
		transactionMessage.setMessageBody(String.valueOf(item.getId()));
		transactionMessage.setMessageDataType("JSON");
		transactionMessage.setMessageId(UUID.randomUUID().toString());
		transactionMessageService.saveAndWaitingConfirm(transactionMessage);
		
		//业务逻辑
		item.setId(IDUtils.genItemId());
		item.setCreated(new Date());
		item.setUpdated(new Date());
		item.setStatus(unInstock);
		itemInfoService.addItem(item, queueType);
		
		//确认并发送消息
		transactionMessageService.confirmAndSendMessage(transactionMessage.getMessageId());
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 上传图片接口
	 */
	@RequestMapping("/pic/upload")
	@ResponseBody
	public Map uploadImage(MultipartFile uploadFile, HttpServletRequest request) {
		//创建一个返回的数据map
		Map dataMap = new HashMap();
		
		//获取fastdfs初始化的文件地址
		String path = request.getSession().getServletContext().getRealPath("client.conf");
		System.out.println("日志===========》获取到的加载文件地址：" + path);
		
		//上传图片到文件服务器
		String url = uploadImageToImageServer(uploadFile, path);
		
		//上传图片失败
		if(url == null) {
			dataMap.put("error", 1);
			dataMap.put("message", "图片上传失败");
			return dataMap;
		}
		
		dataMap.put("url", url);
		dataMap.put("error", 0);
		return dataMap;
	}
	
	/**
	 * 上传图片到文件服务器
	 * confFileName:初始化文件的真实路径
	 */
	private String uploadImageToImageServer(MultipartFile uploadFile, String path) {
		try {
			String imageAddr = "";
			//获取文件的扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String file_ext_name = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			
			ClientGlobal.init(path);
			
			//创建trackerserver对象
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			
			//创建storageclient对象
			StorageServer storageServer = null;
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			
			//利用storageclient对象上传图片
			String[] imageUrl = storageClient.upload_file(uploadFile.getBytes(), file_ext_name, null);
			
			//拼装上传的图片地址
			for (int i = 0; i < imageUrl.length; i++) {
				if(i < imageUrl.length - 1) {
					imageAddr += imageUrl[i] + "/";
				} else {
					imageAddr += imageUrl[i];
				}
			}
			
			imageAddr = trackerServerAddr + imageAddr;
			System.out.println("日志===========》拼装完成的图片地址是：" + imageAddr);
			
			return imageAddr;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常===========》上传图片到服务器失败");
			return null;
		}
	}
	
	/**
	 * 分页查询商品信息接口 
	 */
	@RequestMapping("/manager/getitemList")
	@ResponseBody
	public ItemPageBean getItemList(int page, int rows) {
		//获得当前页的大小
		page = (page - 1) * rows;
		
		//调用商品服务接口查询分页信息
		ItemPageBean itemPageBean = itemInfoService.findItems(page, rows);
		
		return itemPageBean;
	}
	
	/**
	 * 批量删除商品信息接口
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public TaotaoUtils deleteItem(String ids, String queueType) {
		try {
			//调用商品服务批量删除服务
			itemInfoService.deleteItems(ids, queueType);
			
			return TaotaoUtils.ok();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常============》批量删除商品异常");
			return TaotaoUtils.build(502, "批量删除商品异常");
		}
	}
	
	/**
	 * 批量商品下架接口
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public TaotaoUtils instock(String ids) {
		try {
			/**
			 * 调用商品服务，将数据库中商品状态改为下架状态(0:未上架 1:已上架 2:已下架 3:已删除)
			 * 利用可靠消息最终一致性，实现数据库中的数据与搜索库中的数据一致
			 */
			//消息预发送
			TransactionMessage transactionMessage = new TransactionMessage();
			transactionMessage.setCreateTime(new Date());
			transactionMessage.setConsumerQueue(INSTOCKITEMQUEUE);
			transactionMessage.setMessageBody(ids);
			transactionMessage.setMessageDataType("JSON");
			transactionMessage.setMessageId(UUID.randomUUID().toString());
			transactionMessageService.saveAndWaitingConfirm(transactionMessage);
			
			//业务逻辑
			itemInfoService.updateItemsStatus(ids, "itemService-queue");
			
			//消息确认并发送
			transactionMessageService.confirmAndSendMessage(transactionMessage.getMessageId());
			
			return TaotaoUtils.ok();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常============》商品批量上架异常");
			return TaotaoUtils.build(503, "批量上架发生异常");
		}
	}
	
	/**
	 * 批量上架接口
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public TaotaoUtils reshelf(String ids) {
		try {
			
			//消息预发送
			TransactionMessage transactionMessage = new TransactionMessage();
			transactionMessage.setCreateTime(new Date());
			transactionMessage.setConsumerQueue(RESHELFITEMQUEUE);
			transactionMessage.setMessageBody(ids);
			transactionMessage.setMessageDataType("JSON");
			transactionMessage.setMessageId(UUID.randomUUID().toString());
			transactionMessageService.saveAndWaitingConfirm(transactionMessage);
			
			//业务逻辑
			itemInfoService.updateItemsStatus(ids, "itemService-queue");
			
			//消息确认并发送
			transactionMessageService.confirmAndSendMessage(transactionMessage.getMessageId());
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常===============》商品上架发生异常:" + e);
			return TaotaoUtils.build(504, "商品上架异常");
		}
		
		return TaotaoUtils.ok();
	}
	
	/**
	 * 编辑商品接口
	 * @throws Exception 
	 */
	@RequestMapping()
	@ResponseBody
	public TaotaoUtils editItemInfo(Item item, String itemDesc) throws Exception {
		
		return TaotaoUtils.ok();
	}
}
