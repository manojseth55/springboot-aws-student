package com.student.studentapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@Configuration
public class SQSConfiguration {

	@Value("${cloud.aws.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secret-key}")
	private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    
	@Bean
	public QueueMessagingTemplate queueMessagingTemplate() {
		return new QueueMessagingTemplate(buildAmazonSQSAsync());
	}

	private AmazonSQSAsync buildAmazonSQSAsync() {
		return AmazonSQSAsyncClientBuilder
				.standard()
				.withRegion(region)
				.withCredentials(
						new AWSStaticCredentialsProvider(
								new BasicAWSCredentials(accessKey, secretKey))
					).build(); 
	}

}
