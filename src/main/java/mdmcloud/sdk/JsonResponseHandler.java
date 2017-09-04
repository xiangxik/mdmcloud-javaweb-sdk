package mdmcloud.sdk;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class JsonResponseHandler<T> implements ResponseHandler<T> {

	private Class<T> clazz;
	private TypeReference<T> typeReference;

	public JsonResponseHandler(Class<T> clazz) {
		this.clazz = clazz;
	}

	public JsonResponseHandler(TypeReference<T> typeReference) {
		this.typeReference = typeReference;
	}

	@Override
	public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			String str = EntityUtils.toString(entity, "utf-8");
			return clazz == null ? JSON.parseObject(str, typeReference) : JSON.parseObject(str, clazz);
		} else {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	}

}
