package com.student.studentwrkr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.studentwrkr.model.Student;
import com.student.studentwrkr.repository.StudentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentServiceImplementation implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void saveStudent(Student student) throws Exception {
        try {
            log.info("event=saveStudent, status=saveStudentStart, studentId={}", student.getStudentId());
            Student ref = studentRepository.save(student);
            log.info("event=saveStudent, status=saveStudentEnd, studentId={}, savedStudent={}", student.getStudentId(), ref);
        } catch (Exception e) {
            log.warn("event=saveStudent, status=saveStudentFailed, exception={}, studentId={}", e,
                    student.getStudentId());
        }
    }

}
