package mdmcloud.sdk;

import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;

public class Api {

	private static String token;

	private static int timeout = 5000;

	private static int retryExecutionCount = 2;

	public static void initToken(String token) {
		Api.token = token;
	}

	public static String getToken() {
		return token;
	}

	private static CloseableHttpClient httpClient = HttpClientFactory.create(200, 10, timeout, retryExecutionCount);

	public static void init(int maxTotal, int maxPerRoute) {
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpClient = HttpClientFactory.create(maxTotal, maxPerRoute, timeout, retryExecutionCount);
	}

	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}

}
