package nl.elstarit.crypto.configuration;

import nl.elstarit.crypto.property.ExternalApiProperties;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class RestTemplateConfiguration {

	private static final long DEFAULT_KEEP_ALIVE_DURATION_MS = 5;
	private static final long KEEP_ALIVE_MULTIPLIER = 1000;

	@Autowired
	private ExternalApiProperties externalApiProperties;

	@Bean
	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.build();

		SSLConnectionSocketFactory sslCSF = new SSLConnectionSocketFactory(sslContext,
				NoopHostnameVerifier.INSTANCE);

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslCSF)
				.register("http", new PlainConnectionSocketFactory())
				.build();

		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(externalApiProperties.getTimeouts().getConnectTimeout())
				.setConnectionRequestTimeout(externalApiProperties.getTimeouts().getConnectionRequestTimeout())
				.setSocketTimeout(externalApiProperties.getTimeouts().getSocketTimeout())
				.build();

		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(config)
				.setConnectionManager(poolingHttpClientConnectionManager(socketFactoryRegistry))
				.setKeepAliveStrategy(connectionKeepAliveStrategy())
				.setSSLSocketFactory(sslCSF)
				.build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		return new RestTemplate(requestFactory);
	}

	private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry) {
		PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		result.setDefaultMaxPerRoute(externalApiProperties.getPool().getMaxPerRoute());
		result.setMaxTotal(externalApiProperties.getPool().getMaxTotal());
		return result;
	}

	private ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
		return (response, context) -> DEFAULT_KEEP_ALIVE_DURATION_MS * KEEP_ALIVE_MULTIPLIER;
	}
}
