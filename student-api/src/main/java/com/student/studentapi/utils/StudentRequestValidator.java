package com.student.studentapi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.student.studentapi.model.Student;
import com.student.studentapi.model.StudentErrorResponse;
import com.student.studentapi.model.StudentErrorResponse.Error;
import com.student.studentapi.model.StudentErrorResponse.MetaData;

@Component
public class StudentRequestValidator {

    private static final String MISSING = "MISSING_REQUIRED";
    private static final String INVALID = "INVALID_DATA";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    private static void invalidException() {

    }

    @SuppressWarnings("deprecation")
    public Optional<StudentErrorResponse> studentValidateRequest(Student student) {

        List<Error> list = new ArrayList<>();
        if (student == null) {
            list.add(new Error("data must be not null", MISSING, new MetaData()));
        } else {
            if (StringUtils.isEmpty(student.getName())) {
                list.add(new Error("name must be not null", MISSING, new MetaData("name")));
            }
            if (StringUtils.isEmpty(student.getEmail())) {
                list.add(new Error("email must be not null", MISSING, new MetaData("email")));
            } else {
                if (!isValidEmail(student.getEmail())) {
                    list.add(new Error("email is not a valid email address", INVALID, new MetaData("email")));
                }
            }
            if (StringUtils.isEmpty(student.getPhone())) {
                list.add(new Error("phone must be not null", MISSING, new MetaData("phone")));
            } else {
                if (!student.getPhone().matches("\\d{10}")) {
                    list.add(new Error("phone must be 10 digits only", INVALID, new MetaData("phone")));

                }

            }
        }

        return list.isEmpty() ? Optional.empty()
                : Optional
                        .of(StudentErrorResponse.builder().errorId(UUID.randomUUID().toString()).errors(list).build());
    }

    private static boolean isValidEmail(String email) {

        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
