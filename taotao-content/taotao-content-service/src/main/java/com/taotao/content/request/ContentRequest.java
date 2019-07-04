package com.taotao.content.request;
/**
 * 封装广告内容请求顶级接口
 * @author Administrator
 *
 */
public interface ContentRequest {
	public void process();
	public long getContentId();
}
