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

	@PostMapping(value = "/save", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postStudent(@RequestBody Student student) {
		try {
			log.info("event =postStudent, status=studentPayloadRecieved, body={}", student);
			Optional<StudentErrorResponse> errorResponse = studentRequestValidator.studentValidateRequest(student);
			if (errorResponse.isPresent()) {
				log.info("event =postStudent, status=validationFailed, httpStatus={} errorResponse={}",
						HttpStatus.BAD_REQUEST, errorResponse);
				return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			} else {
				student.setId(UUID.randomUUID().toString());
				sqsService.publishMessageToQueue(new ObjectMapper().writeValueAsString(student), student.getId());
				log.info("event =postStudent, status=studentControllerEnd, studentId={}", student.getId());
				return ResponseEntity.status(HttpStatus.CREATED).body(student);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
}
