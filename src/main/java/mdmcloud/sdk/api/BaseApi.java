package mdmcloud.sdk.api;

import java.io.IOException;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

import mdmcloud.sdk.Api;

public class BaseApi {

	public <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) {
		try {
			return Api.getHttpClient().execute(request, responseHandler);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
