package mdmcloud.sdk;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

public class HttpClientFactory {

	public static CloseableHttpClient create(int maxTotal, int maxPerRoute, int timeout, int retryExecutionCount) {
		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(timeout).build();
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		poolingHttpClientConnectionManager.setMaxTotal(maxPerRoute);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxTotal);
		poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
		return HttpClientBuilder.create().setConnectionManager(poolingHttpClientConnectionManager)
				.addInterceptorFirst(new HttpRequestInterceptorImpl()).setRetryHandler(new HttpRequestRetryHandlerImpl(retryExecutionCount)).build();
	}

	private static class HttpRequestInterceptorImpl implements HttpRequestInterceptor {

		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			request.setHeader("token", Api.getToken());
		}

	}

	private static class HttpRequestRetryHandlerImpl implements HttpRequestRetryHandler {

		private int retryExecutionCount;

		public HttpRequestRetryHandlerImpl(int retryExecutionCount) {
			this.retryExecutionCount = retryExecutionCount;
		}

		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			if (executionCount > retryExecutionCount) {
				return false;
			}
			if (exception instanceof InterruptedIOException) {
				return false;
			}
			if (exception instanceof UnknownHostException) {
				return false;
			}
			if (exception instanceof ConnectTimeoutException) {
				return true;
			}
			if (exception instanceof SSLException) {
				return false;
			}
			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {
				return true;
			}
			return false;
		}

	};
}
