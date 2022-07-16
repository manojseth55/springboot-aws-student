package com.student.studentwrkr.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
@ConfigurationProperties
public class SQSConfiguration {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Bean
    public AmazonSQS amazonSQSClient() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonSQS client = AmazonSQSClientBuilder.standard().withRegion(Regions.US_WEST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
        return client;
    }

}
