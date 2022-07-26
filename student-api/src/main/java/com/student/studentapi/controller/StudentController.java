package com.student.studentapi.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.studentapi.model.Student;
import com.student.studentapi.model.StudentErrorResponse;
import com.student.studentapi.service.S3Service;
import com.student.studentapi.service.SQSService;
import com.student.studentapi.utils.StudentRequestValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
@RequestMapping("/student")
public class StudentController {

	private final StudentRequestValidator studentRequestValidator;
	private final SQSService sqsService;
	private final S3Service s3Service;

	@PostMapping(value = "/save", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postStudent(@RequestBody Student student) throws Exception {
		try {
			log.info("event =postStudent, status=studentPayloadRecieved, body={}", student);
			Optional<StudentErrorResponse> errorResponse = studentRequestValidator.studentValidateRequest(student);
			if (errorResponse.isPresent()) {
				log.info("event =postStudent, status=validationFailed, httpStatus={} errorResponse={}",
						HttpStatus.BAD_REQUEST, errorResponse);
				return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			} else {
				student.setStudentId(UUID.randomUUID().toString());
				s3Service.writeToS3(new ObjectMapper().writeValueAsString(student), student.getStudentId());
				sqsService.publishMessageToQueue(new ObjectMapper().writeValueAsString(student), student.getStudentId());
				log.info("event =postStudent, status=studentControllerEnd, studentId={}", student.getStudentId());
				return ResponseEntity.status(HttpStatus.CREATED).body(student);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
}
