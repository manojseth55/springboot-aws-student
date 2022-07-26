package com.student.studentwrkr.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.student.studentwrkr.model.Student;

@Repository
public class StudentRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Student save(Student student) {
        dynamoDBMapper.save(student);
        return student;
    }
}
