package com.student.studentwrkr.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.studentwrkr.model.Student;
import com.student.studentwrkr.service.StudentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StudentWrkrProcessor implements Processor {

    @Autowired
    private StudentService studentService;

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            String request = exchange.getIn().getBody(String.class);
            Student student = new ObjectMapper().readValue(request, Student.class);
            log.info("event=studentWrkrProcessor, status=processStart, studentId={}, studentData={}", student.getId(), student);
            studentService.saveStudent(student);
            log.info("event=studentWrkrProcessor, status=processEnd, studentId={}", student.getId());
        } catch (Exception e) {
            log.info("event=studentWrkrProcessor, status=processFailed, exception={}, studentId={}", e);
        }

    }
}
