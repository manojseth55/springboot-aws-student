package com.student.studentwrkr.service;

import org.springframework.stereotype.Service;

import com.student.studentwrkr.model.Student;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentServiceImplementation implements StudentService {

    @Override
    public void saveStudent(Student student) throws Exception {
        try {
            log.info("event=saveStudent, status=saveStudentStart, studentId={}", student.getId());
        } catch (Exception e) {
            log.warn("event=saveStudent, status=saveStudentFailed, exception={}, studentId={}", e, student.getId());
        }
    }

}
