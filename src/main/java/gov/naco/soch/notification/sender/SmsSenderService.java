package gov.naco.soch.notification.sender;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SmsSenderService {

	// @Value("${twilio.smsnumber}")
	// private String sms_number;

	@Value("${notificationSmsApiEndpoint}")
	private String smsApiEndpoint;

	@Value("${notificationSmsApiUsername}")
	private String smsApiUserName;

	@Value("${notificationSmsApiPin}")
	private String smsApiPin;

	@Value("${notificationSmsApiSignature}")
	private String smsApiSignature;

	private static final Logger logger = LoggerFactory.getLogger(SmsSenderService.class);

	@Autowired
	private RestTemplate restTemplate;

	public void sendSms(String mobileNumber, String smsTemplate) {
		try {
			//HttpHeaders headers = new HttpHeaders();
			//headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			if (mobileNumber.length() <= 10) {
				mobileNumber = "91" + mobileNumber;
			}
			String encodedMessageTemplate = URLEncoder.encode(smsTemplate, StandardCharsets.UTF_8.name());
			UriComponents builder = UriComponentsBuilder.fromHttpUrl(smsApiEndpoint)
					.queryParam("username", smsApiUserName).queryParam("pin", smsApiPin)
					.queryParam("message", encodedMessageTemplate).queryParam("mnumber", mobileNumber)
					.queryParam("signature", smsApiSignature).build();
			//HttpEntity<?> entity = new HttpEntity<>(headers);
			// ---------------------
			logger.info("Going to send SMS to mobileNumber-->{}:", mobileNumber);
			logger.info("SMS API URL :" + builder.toUriString());
			TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
			BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
					socketFactoryRegistry);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
					.setConnectionManager(connectionManager).build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					httpClient);
			ResponseEntity<String> response = new RestTemplate(requestFactory).exchange(builder.toUriString(),
					HttpMethod.GET, null, String.class);

			logger.info("Response : " + response);
			logger.info("SMS API Response Code :" + response.getStatusCode().value());

			// ---------------------
			/*
			 * logger.info("Going to send SMS to mobileNumber-->{}:", mobileNumber);
			 * logger.info("SMS API URL :"+builder.toUriString());
			 * restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
			 * String.class);
			 */
			logger.info("Sent SMS to mobileNumber-->{}:", mobileNumber);
			// Message.creator(new PhoneNumber(mobileNumber), new PhoneNumber(sms_number),
			// smsTemplate).create();
		} catch (Exception e) {
			logger.error("Exception in sendSms->", e);
		}

	}

}
