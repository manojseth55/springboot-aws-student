package com.student.studentapi.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.bucket.name}")
    private String bucket;

    public void writeToS3(String request, String studentId) throws Exception {
        try {
            log.info("event=writeToS3, status=writeToS3Start, studentId={}", studentId);
            String key = "student/";
            String date = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).format(DateTimeFormatter.ofPattern("ddMMyyyy"))
                    .concat("/");
            String s3Key = key.concat("json/").concat(date).concat(studentId);
            amazonS3.putObject(bucket, s3Key, request);
            log.info("event=writeToS3, status=writeToS3End, studentId={}", studentId);
        } catch (Exception e) {
            log.info("event=writeToS3, status=writeToS3Failed, studentId={}, exception", studentId, e);
        }
    }

}
