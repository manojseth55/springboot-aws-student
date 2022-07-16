package com.student.studentwrkr.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.student.studentwrkr.processor.StudentWrkrProcessor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentWrkrRoute {

    private final StudentWrkrProcessor studentWrkrProcessor;

    public static final String STUDENT_WORKER_ROUTE = "studentWrkrRoute";
    public static final String UTF_8 = "UTF-8";
    // SQS configurations
    private static final String SQS_SCHEMA = "aws-sqs://";
    private static final String SQS_CLIENT_SUFFIX = "?amazonSQSClient=#amazonSQSClient";

    private String studentSQSQueues = "student-crud-queue";

    @Value("${sqs.no.consumers:5}")
    private String numberOfConsumers;
    @Value("${sqs.max.no.messages:2}")
    private int maxNumberOfMessages;
    @Value("${sqs.messageAttributeNames:All}")
    private String messageAttributeNames;
    @Value("${sqs.receiveMessageWaitTimeSeconds:0}")
    private String receiveMessageWaitTimeSeconds;
    @Value("${sqs.backoffErrorThreshold:0}")
    private int backoffErrorThreshold;
    @Value("${sqs.backoffIdleThreshold:0}")
    private int backoffIdleThreshold;
    @Value("${sqs.greedy:false}")
    private String greedy;
    @Value("${sqs.runLoggingLevel:TRACE}")
    private String runLoggingLevel;
    @Value("${sqs.sendEmptyMessageWhenIdle:false}")
    private String sendEmptyMessageWhenIdle;

    @Value("${camel.maxRedeliveryCount:5}")
    private int maxRedeliveryCount;

    @Value("${camel.redeliveryDelayMs:1000}")
    private long redeliveryDelayMs;

    @Value("${camel.backOffMultiplier:2}")
    private int backOffMultiplier;

    @Bean
    RoutesBuilder myRouter() {
        return new RouteBuilder() {

            String fromUris = SQS_SCHEMA + studentSQSQueues + SQS_CLIENT_SUFFIX + "&concurrentConsumers="
                    + numberOfConsumers + "&maxMessagesPerPoll=" + maxNumberOfMessages + "&messageAttributeNames="
                    + messageAttributeNames + "&receiveMessageWaitTimeSeconds=" + receiveMessageWaitTimeSeconds
                    + "&backoffErrorThreshold=" + backoffErrorThreshold + "&backoffIdleThreshold="
                    + backoffIdleThreshold + "&greedy=" + greedy + "&runLoggingLevel=" + runLoggingLevel
                    + "&sendEmptyMessageWhenIdle=" + sendEmptyMessageWhenIdle + "?autoCreateQueue=false";

            @Override
            public void configure() throws Exception {
                from(fromUris).routeId(STUDENT_WORKER_ROUTE)
                        .routeDescription("Reads message from queues and publishes to StudentWrkr")
                        .log(LoggingLevel.INFO, log, "event=studentWrkrRoute, status=studentWrkrRouteStart")
                        .log(LoggingLevel.INFO, log,
                                "event=studentWrkrRoute, status=beforeProcessing, studentWrkrReceivedFromSQS=${body}")
                        .process(studentWrkrProcessor).end();

            }
        };
    }

}
