package com.taotao.trade.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.taotao.order.model.Order;
import com.taotao.order.model.OrderItem;
import com.taotao.order.model.OrderRecord;
import com.taotao.order.service.OrderService;
import com.taotao.pay.message.model.TransactionMessage;
import com.taotao.pay.message.service.TransactionMessageService;
import com.taotao.trade.app.service.AlipayTradeServiceSingleton;
import com.taotao.trade.app.utils.JsonUtils;
import com.taotao.trade.app.utils.RefundBizContent;
import com.taotao.trade.app.utils.SingletonMap;
import com.taotao.trade.app.utils.TaotaoUtils;
import com.taotao.trade.app.utils.TradeStatic;
import com.taotao.user.model.User;
/**
 * 支付系统服务对外提供支付接口
 * @author Administrator
 *
 */
@Controller
public class TradePaymentManagerController {
	private final static Logger logger = LoggerFactory.getLogger(TradePaymentManagerController.class);
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private TransactionMessageService transactionMessageService;
	
	@Value("${PAY_TIME_OUT}")
	private String PAY_TIME_OUT;
	@Value("${NOTIFY_URL}")
	private String NOTIFY_URL;
	@Value("${SHOP_PRIVATE_KEY}")
	private String SHOP_PRIVATE_KEY;
	@Value("${SHOP_ALIPAY_PUBLIC_KEY}")
	private String SHOP_ALIPAY_PUBLIC_KEY;
	@Value("${ALIPAY_GATWAY}")
	private String ALIPAY_GATWAY;
	@Value("${APPID}")
	private String APPID;
	
	/**
	 * 支付按钮接口,生成并展示二维码
	 */
	@RequestMapping("/trade/payment")
	public String createAndShowQRCode(String orderId, HttpServletRequest request, Model model) {
		//初始化
		Configs.init("conf/zfbinfo.properties");
		AlipayTradeService tradeService = AlipayTradeServiceSingleton.getInstance().getAlipayTradeService();
		
		// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = orderId;

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "淘淘商城当面付扫码消费";
        
        //调用订单系统服务，根据订单号查询订单
        Order order = orderService.findOrderById(orderId);
        //判断订单是否为未支付订单
        if(order == null || !order.getStatus().equals(TradeStatic.NO_PAY)) {
        	return "订单不存在或者订单不是未支付的订单";
        }

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(order.getOrderAmount());

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";
        
        //调用订单系统服务，查询订单商品联系表
        List<OrderItem> orderItems = orderService.findOrderItemByOrderId(orderId);

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "";
        for (OrderItem orderItem : orderItems) {
			body += orderItem.getProductName() + orderItem.getNum() + "件,共" + orderItem.getTotalFee() + "元,";
		}
        System.out.println("日志==================》订单描述:" + body);

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时
        String timeoutExpress = PAY_TIME_OUT;

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        for (OrderItem orderItem : orderItems) {
        	GoodsDetail goods = GoodsDetail.newInstance(orderItem.getItemId()+"", orderItem.getProductName(), orderItem.getPrice(), orderItem.getNum());
        	// 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
            .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
            .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
            .setTimeoutExpress(timeoutExpress)
            .setNotifyUrl(NOTIFY_URL)//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
            .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        String filePath = "";
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                filePath = String.format(request.getSession().getServletContext().getRealPath("QRCode")+ "/qr-%s.png",
                    response.getOutTradeNo());
                logger.info("filePath:" + filePath);
                                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                break;

            case FAILED:
                logger.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                break;

            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        
        //获取图片访问路径
        System.out.println("日志=============>filepath:"+filePath);
        String image = "";
        String[] split = filePath.split("/");
        image ="/" + "QRCode" + "/" + split[split.length - 1];
        System.out.println("日志============》二维码图片访问地址" + image);
        
        model.addAttribute("image", image);
        model.addAttribute("orderId", orderId);
        
        return "trade";
	}
	
    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }
    
    /**
     * 支付宝回调(确保接口幂等，回调会重复请求)
     * @throws Exception 
     */
    @RequestMapping("/trade/callback")
    @ResponseBody
    public String alipayCallBack(HttpServletRequest request) throws Exception {
    	/*User user = (User) request.getAttribute("user");
    	Long userId = user.getId();*/
    	long userId = 5l;
    	//解析参数，获得新的params
    	Map<String, String> params = paraseParams(request);
    	String orderId = params.get("out_trade_no");
    	System.out.println("日志=============》支付宝回调的参数:" + JsonUtils.objectToJson(params));
    	
    	//调用订单服务，查询订单信息
    	Order order = orderService.findOrderById(orderId);
    	if(order.getStatus().equals("已支付")) {
    		return "此订单已支付，不用重复支付";
    	}
    	
    	//支付宝签名验证
    	boolean rsaCheckV1 = AlipaySignature.rsaCheckV1(params, Configs.getAlipayPublicKey(), "UTF-8", Configs.getSignType());
    	System.out.println("日志===========》支付宝验证签名结果:" + rsaCheckV1);
    	
    	if(rsaCheckV1) {
    		//支付宝签名验证通过,验证其他参数
    		TaotaoUtils checkOrderMessage = checkOrderMessage(orderId, params);
    		System.out.println("日志===========》检验订单信息结果:"+ JsonUtils.objectToJson(checkOrderMessage));
    		if(checkOrderMessage.getStatus() != 200) {
    			return JsonUtils.objectToJson(checkOrderMessage);
    		}
    		
    		/**
    		 * 检验订单通过，更新订单状态，给账户积分加款，会计记账
    		 */
    		//预发送消息
    		String messageId = UUID.randomUUID().toString();
    		TransactionMessage transactionMessage = new TransactionMessage();
    		transactionMessage.setConsumerQueue("createAccountQueue");//消息的queue通道名称
    		transactionMessage.setCreateTime(new Date());
    		transactionMessage.setMessageBody(JsonUtils.objectToJson(order));//订单和物流信息消息
    		transactionMessage.setMessageDataType("JSON");//消息数据类型
    		transactionMessage.setMessageId(messageId);
    		transactionMessage.setField1(String.valueOf(userId));//将扩展字段field1作为userId字段
    		transactionMessage.setMessageSendTimes(0);//发送次数
    		transactionMessage.setVersion(1);
			transactionMessageService.saveAndWaitingConfirm(transactionMessage);
			System.out.println("日志=================》预发送创建会计记账消息");
    		
			//调用订单服务TCC事务接口,更新订单状态和修改积分系统
			Map<String, String> paramsData = new HashMap<String, String>();
			paramsData.put("orderId", orderId);
			String requestNo = UUID.randomUUID().toString();//随机生成请求号
			paramsData.put("requestNo", requestNo);
			paramsData.put("userNo", String.valueOf(userId));
			paramsData.put("amount", params.get("total_amount"));
			paramsData.put("balance", params.get("total_amount"));
			paramsData.put("bankTrxNo", messageId);
			paramsData.put("remark", order.getRemark());
			paramsData.put("tradeNo", params.get("trade_no"));
    		orderService.updateOrderStatusAndUpdatePointAccountByTcc(paramsData);
    		System.out.println("日志================》更新订单状态为已支付");
    		
    		
    		//确认并发送消息，会计记账
    		transactionMessageService.confirmAndSendMessage(messageId);
    		System.out.println("日志=================》确认并发送消息到消息队列系统中");
    		
    		//成功完成支付，将结果放到单例map中
    		Map<String, String> map = SingletonMap.getInstance().getMap();
    		map.put(orderId, "success");
    		return "success";
    	} else {
    		
    		return "支付宝验证签名失败";
    	}
    	
    }
    
    /**
     * 解析支付宝回调参数
     */
    private Map<String, String> paraseParams(HttpServletRequest request) {
    	Map<String, String> dataMap = new HashMap<String, String>();
    	Map parameterMap = request.getParameterMap();
    	for(Iterator iterator = parameterMap.keySet().iterator();iterator.hasNext();) {
    		String key = (String) iterator.next();
    		String[] values = (String[]) parameterMap.get(key);
    		String data = "";
    		for(int i = 0; i < values.length; i++) {
    			data += (i == values.length - 1) ? values[i] : values[i] + ",";
    		}
    		
    		dataMap.put(key, data);
    	}
    	
    	return dataMap;
    }
    
    /**
     * 订单信息校验
     */
   private TaotaoUtils checkOrderMessage(String orderId, Map<String, String> params) {
	   //调用订单系统服务，根据订单号查询订单
	   Order order = orderService.findOrderById(orderId);
	   if(order == null) {
		   System.out.println("日志==============》无效的订单号"+orderId);
		   return TaotaoUtils.build(500, "无效的订单号");
	   }
	   
	   //校验总价格是否正确
	   String orderAmount = String.valueOf(order.getOrderAmount()) + ".00";//从数据库中查询订单总金额数
	   String totalAmount = params.get("total_amount");//从回调中获取总金额
	   if(!orderAmount.equals(totalAmount)) {
		   System.out.println("日志==============》订单总金额有误！orderAmount="+orderAmount+"totalAmount="+totalAmount);
		   return TaotaoUtils.build(500, "订单金额有误！");
	   }
	   
	   //TODO 其他校验业务-----------------
	   
	   //检验通过
	   return TaotaoUtils.ok();
   }
   
   /**
    * 支付宝扫码支付结果等待ajax请求
    */
   @RequestMapping("/trade/pay_status")
   @ResponseBody
   public TaotaoUtils payStatusConfirm(String orderId) {
	   String value = SingletonMap.getInstance().getMap().get(orderId);
	   System.out.println("value="+value);
	   if(value == null) {
		   return TaotaoUtils.build(500, "支付中");
	   }
	   
	   return TaotaoUtils.ok();
   }
   
   /**
    * 支付成功
    */
   @RequestMapping("/trade/success")
   public String paySuccess() {
	   
	   return "success";
   }
   
   /**
    * 支付宝退款
 * @throws Exception 
    */
   @RequestMapping("/trade/refund")
   @ResponseBody
   public String alipayRefund(String orderId, @RequestParam(defaultValue="",name="cancelReason")String cancelReason) throws Exception {
	   //调用订单系统查询订单交易信息
	   OrderRecord orderRecord = orderService.findOrderRecordById(orderId);
	   List<OrderItem> orderItems = orderService.findOrderItemByOrderId(orderId);
	   System.out.println("orderRecord----->"+orderRecord + "orderItems---->"+orderItems);
	 
	   
	   String tradeNo = orderRecord.getBankOrderNo();//支付宝交易号
	   if(tradeNo == null && orderId == null) {
		   
		   return "支付宝交易号和商户订单号不能都为空";
	   }
	   Long orderAmount = orderRecord.getOrderAmount();//总支付额
	   
	   AlipayClient alipayClient = new DefaultAlipayClient(ALIPAY_GATWAY,APPID,SHOP_PRIVATE_KEY,"json","GBK",SHOP_ALIPAY_PUBLIC_KEY,"RSA2");
	   //封装请求参数
	   AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
	   RefundBizContent bizContent = new RefundBizContent();
	   bizContent.setTrade_no(tradeNo);
	   bizContent.setOut_trade_no(orderId);
	   bizContent.setRefund_amount(Double.parseDouble(String.valueOf(orderAmount)));
	   bizContent.setRefund_reason(cancelReason);
	   
	   List<GoodsDetail> goodsDetails = new ArrayList<GoodsDetail>();
	   for(OrderItem orderItem : orderItems) {
		   GoodsDetail goodsDetail = new GoodsDetail();
		   goodsDetail.setGoodsId(String.valueOf(orderItem.getItemId()));
		   goodsDetail.setQuantity(orderItem.getNum());
		   goodsDetail.setBody(orderItem.getProductName()+orderItem.getNum()+"件共"+orderItem.getTotalFee()+"元");
		   goodsDetail.setGoodsName(orderItem.getProductName());
		   goodsDetail.setPrice(orderItem.getPrice());
		   goodsDetails.add(goodsDetail);
	   }
	   bizContent.setGoods_detail(goodsDetails);
	   
	   //将封装好的请求放到client中
	   AlipayTradeRefundResponse response = alipayClient.execute(request);
	   if(response.isSuccess()) {
		   //退款成功，更新订单状态，是否退款，退款金额，edittime，退款次数
		   orderService.refundOrder(cancelReason, orderId, TradeStatic.REFUND_STATUS, new Date(), TradeStatic.REFUND_YES, 1, Double.parseDouble(String.valueOf(orderAmount)));
		   //TODO 返回一些信息给客户端
		   return "success";
	   }
	   return "系统异常";
   }
}
