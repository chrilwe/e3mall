package com.taotao.es.search.dao.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Repository;

import com.taotao.es.search.dao.BuildTransportClient;
import com.taotao.es.search.dao.EsSearchDao;
import com.taotao.es.search.model.Comment;
import com.taotao.es.search.model.EsSearchResultModel;
import com.taotao.es.search.model.ResultModel;

/**
 * 处理es搜索库中的数据
 * 
 * @author Administrator
 * 
 */
@Repository
public class EsSearchDaoImpl implements EsSearchDao {

	@Override
	public IndexResponse addDocument(EsSearchResultModel model, String itemType, String itemIndex)
			throws Exception {
		TransportClient client = BuildTransportClient.build();

		IndexResponse response = createDocument(client, model, itemType, itemIndex);
		client.close();
		return response;
	}

	private IndexResponse createDocument(TransportClient client,
			EsSearchResultModel model, String itemType, String itemIndex) throws Exception {
		IndexResponse response = client
				.prepareIndex(itemIndex, itemType, model.getId())
				.setSource(
						XContentFactory.jsonBuilder().startObject()
								.field("categoryName", model.getCategoryName())
								.field("image", model.getImage())
								.field("price", model.getPrice())
								.field("sellPoint", model.getSellPoint())
								.field("id", model.getId())
								.field("status", model.getStatus())
								.field("title", model.getTitle()).endObject())
				.get();
		System.out.println("日志----------------》状态:" + response.getResult());

		return response;
	}

	@Override
	public DeleteResponse deleteDocumentById(long id, String itemType, String itemIndex) throws Exception {
		TransportClient client = BuildTransportClient.build();
		return client.prepareDelete(itemIndex, itemType, String.valueOf(id)).get();
	}

	@Override
	public UpdateResponse updateDocument(EsSearchResultModel model, String itemType, String itemIndex) throws Exception {
		TransportClient client = BuildTransportClient.build();
		UpdateResponse response = client.prepareUpdate(itemIndex, itemType, model.getId()) 
				.setDoc(XContentFactory.jsonBuilder()
							.startObject()
								.field("categoryName", model.getCategoryName())
								.field("image", model.getImage())
								.field("price", model.getPrice())
								.field("sellPoint", model.getSellPoint())
								.field("title", model.getTitle())
								.field("status", model.getStatus())
							.endObject())
				.get();
		System.out.println("日志-----------》状态:"+response);
		client.close();
		
		return response;
	}
	
	/**
	 * 根据id查询 
	 */
	@Override
	public EsSearchResultModel findDocumentById(long id, String itemType, String itemIndex) throws Exception {
		TransportClient client = BuildTransportClient.build();
		EsSearchResultModel model = getResult(client, id, itemType, itemIndex);
		client.close();
		return model;
	}

	/**
	 * 根据id查询结果
	 */
	private EsSearchResultModel getResult(TransportClient client, long id, String itemType, String itemIndex) {
		// 获取结果
		GetResponse response = client.prepareGet(itemIndex, itemType,
				String.valueOf(id)).get();
		Map<String, Object> source = response.getSourceAsMap();
		
		if(source.size() <= 0 || source.equals("")) {
			return null;
		}
		
		String image = (String) source.get("image");
		String sellPoint = (String) source.get("sellPoint");
		long price = Long.parseLong(source.get("price").toString());
		String title = (String) source.get("title");
		String categoryName = (String) source.get("categoryName");

		EsSearchResultModel model = new EsSearchResultModel();
		model.setCategoryName(categoryName);
		model.setId(String.valueOf(id));
		model.setImage(image);
		model.setPrice(price);
		model.setSellPoint(sellPoint);
		model.setTitle(title);
		
		return model;
	}
	
	/**
	 * 关键字搜索,返回高亮关键字
	 * @throws Exception 
	 */
	@Override
	public ResultModel queryString(String keyword, int from, int size, int status, String itemIndex) throws Exception {
		List<EsSearchResultModel> result = new ArrayList<EsSearchResultModel>();
		ResultModel resultModel = new ResultModel();
		
		TransportClient client = BuildTransportClient.build();
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("title");
		highlightBuilder.preTags("<em style=\"color:red\">");
		highlightBuilder.postTags("</em>");
		SearchResponse response = client.prepareSearch(itemIndex)
			.setQuery(QueryBuilders.boolQuery()
					.must(QueryBuilders.matchQuery("title", keyword))
					.must(QueryBuilders.matchPhraseQuery("status", status)))
			.highlighter(highlightBuilder)
			.setFrom(from).setSize(size)
			.execute()
			.actionGet();
		
		SearchHits hits = response.getHits();
		long totalHits = hits.getTotalHits();
		if(hits != null) {
			System.out.println("日志----------------》总记录数:"+totalHits);
			for (SearchHit hit : hits) {
				Map<String, Object> source = hit.getSource();
				Map<String, HighlightField> highlightFields = hit.getHighlightFields();
				HighlightField highlightField = highlightFields.get("title");
				if(highlightField != null) {
					Text[] fragments = highlightField.fragments();
					String highlightTitle = "";
					for (Text text : fragments) {
						highlightTitle += text;
					}
					source.put("title", highlightTitle);
				}
				
				EsSearchResultModel model = new EsSearchResultModel();
				model.setCategoryName(source.get("categoryName").toString());
				model.setId(source.get("id").toString());
				model.setImage(source.get("image").toString());
				model.setPrice(Long.parseLong(source.get("price").toString()));
				model.setSellPoint(source.get("sellPoint").toString());
				model.setTitle(source.get("title").toString());
				model.setStatus(Integer.parseInt(source.get("status").toString()));
				
				result.add(model);
			}
			
			resultModel.setItemList(result);
			resultModel.setRecordCount(totalHits);
			resultModel.setTotalPages((Integer.parseInt(String.valueOf(totalHits))%size == 0) ? (Integer.parseInt(String.valueOf(totalHits))/size) : Integer.parseInt(String.valueOf(totalHits))/size + 1);
		}

		client.close();
		return resultModel;
	}

	@Override
	public ResultModel queryType(String itemType, int from, int size, int status, String itemIndex) throws Exception {
		List<EsSearchResultModel> result = new ArrayList<EsSearchResultModel>();
		ResultModel resultModel = new ResultModel();
		
		TransportClient client = BuildTransportClient.build();
		SearchResponse response = client.prepareSearch(itemIndex)
			  .setQuery(QueryBuilders.matchQuery("status", status))
			  .setTypes(itemType)
			  .setFrom(from)
			  .setSize(size)
			  .execute()
			  .get();
		
		SearchHits hits = response.getHits();
		long totalHits = hits.getTotalHits();
		if(hits != null) {
			for (SearchHit hit : hits) {
				Map<String, Object> source = hit.getSource();
				
				EsSearchResultModel model = new EsSearchResultModel();
				model.setCategoryName(source.get("categoryName").toString());
				model.setId(source.get("id").toString());
				model.setImage(source.get("image").toString());
				model.setPrice(Long.parseLong(source.get("price").toString()));
				model.setSellPoint(source.get("sellPoint").toString());
				model.setTitle(source.get("title").toString());
				model.setStatus(Integer.parseInt(source.get("status").toString()));
				
				result.add(model);
			}
			
			resultModel.setItemList(result);
			resultModel.setRecordCount(totalHits);
			resultModel.setTotalPages(Integer.parseInt(String.valueOf(totalHits))/size);
		}
		client.close();
		return resultModel;
	}

	@Override
	public void updateItemStatus(String itemIndex, String itemType, int status,
			long itemId) throws Exception {
		TransportClient client = BuildTransportClient.build();
		UpdateResponse response = client.prepareUpdate(itemIndex, itemType, String.valueOf(itemId)) 
				.setDoc(XContentFactory.jsonBuilder()
							.startObject()
								.field("status", status)
							.endObject())
				.get();
		System.out.println(response);
	}

	@Override
	public IndexResponse addDocument(Comment comment, String commentType,
			String commentIndex) throws Exception {
		TransportClient client = BuildTransportClient.build();
		
		IndexResponse response = client
				.prepareIndex(commentIndex, commentType, comment.getId())
				.setSource(
						XContentFactory.jsonBuilder().startObject()
								.field("itemId", comment.getItemId())
								.field("id", comment.getId())
								.field("message", comment.getMessage())
								.field("userId", comment.getUserId())
								.field("created", comment.getCreated())
								.field("updated", comment.getUpdated())
								.endObject())
				.get();
		
		System.out.println("日志-----------------》创建document状态:"+response);
		client.close();
		return response;
	}
	
	/**
	 * 按照日期降序排序
	 */
	@Override
	public List<Comment> findCommentsOrderByDate(String commentIndex,
			String commentType) throws Exception {
		List<Comment> result = new ArrayList<Comment>();
		TransportClient client = BuildTransportClient.build();
		
		SortBuilder sort = SortBuilders.fieldSort("created")
				.order(SortOrder.DESC);
				
		SearchResponse response = client.prepareSearch(commentIndex)
				.setTypes(commentType)
				.addSort(sort)
				.get();
		
		SearchHits hits = response.getHits();
		for (SearchHit searchHit : hits) {
			Map<String, Object> source = searchHit.getSource();
			
			Comment comment = new Comment();
			comment.setCreated(source.get("created").toString());
			comment.setId(source.get("id").toString());
			comment.setItemId(Long.parseLong(source.get("itemId").toString()));
			comment.setMessage(source.get("message").toString());
			comment.setUserId(Long.parseLong(source.get("userId").toString()));
			
			result.add(comment);
		}
		return result;
	}
}
