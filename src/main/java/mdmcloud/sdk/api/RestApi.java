package mdmcloud.sdk.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.RequestBuilder;

import com.alibaba.fastjson.TypeReference;

import mdmcloud.sdk.JsonResponseHandler;
import mdmcloud.sdk.bean.Page;
import mdmcloud.sdk.bean.Result;

public abstract class RestApi<T> extends BaseApi {

	private String baseUrl;
	private Class<T> beanClass;
	private TypeReference<Page<T>> pageTypeReference;

	public RestApi(String baseUrl, Class<T> beanClass, TypeReference<Page<T>> pageTypeReference) {
		this.baseUrl = baseUrl;
		this.beanClass = beanClass;
		this.pageTypeReference = pageTypeReference;
	}

	public Page<T> page(Integer pageNo, Integer pageSize, Map<String, String> filterParameters) {
		RequestBuilder requestBuilder = RequestBuilder.get(baseUrl);
		if (pageNo != null) {
			requestBuilder.addParameter("page", pageNo.toString());
		}
		if (pageSize != null) {
			requestBuilder.addParameter("limit", pageSize.toString());
		}
		if (filterParameters != null) {
			for (Entry<String, String> parameter : filterParameters.entrySet()) {
				requestBuilder.addParameter(parameter.getKey(), parameter.getValue());
			}
		}
		return execute(requestBuilder.build(), new JsonResponseHandler<>(pageTypeReference));
	}

	public T get(Serializable id) {
		RequestBuilder requestBuilder = RequestBuilder.get(baseUrl + "/" + id);
		return execute(requestBuilder.build(), new JsonResponseHandler<>(beanClass));
	}

	public Result save(Map<String, String> parameters) {
		RequestBuilder requestBuilder = RequestBuilder.get(baseUrl + "/save");
		if (parameters != null) {
			for (Entry<String, String> parameter : parameters.entrySet()) {
				requestBuilder.addParameter(parameter.getKey(), parameter.getValue());
			}
		}
		return execute(requestBuilder.build(), new JsonResponseHandler<>(Result.class));
	}

	public Result update(Serializable id, Map<String, String> parameters) {
		if (parameters == null) {
			parameters = new HashMap<>();
		}
		parameters.put("id", id.toString());
		return save(parameters);
	}

	public Result delete(Serializable id) {
		RequestBuilder requestBuilder = RequestBuilder.get(baseUrl + "/delete");
		requestBuilder.addParameter("id", id.toString());
		return execute(requestBuilder.build(), new JsonResponseHandler<>(Result.class));
	}

}
