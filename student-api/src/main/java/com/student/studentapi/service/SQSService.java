package com.student.studentapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SQSService {

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;
	
	@Value("${cloud.aws.endpoint.url}")
	private String endPoint;
	
	
	public void publishMessageToQueue(String message, String StudentId) throws Exception {
		try {
			log.info("event=publishMessageToQueue, status=publishStart, studentId={}", StudentId);
			queueMessagingTemplate.send(endPoint, MessageBuilder.withPayload(message).build());			
			log.info("event=publishMessageToQueue, status=publishEnd, studentId={}", StudentId);
		} catch (Exception e) {
			log.info("event=publishMessageToQueue, status=publishFailed, studentId={}", StudentId);
		}
	}
}
