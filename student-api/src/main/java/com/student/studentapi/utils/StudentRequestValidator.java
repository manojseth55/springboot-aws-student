package com.student.studentapi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	private static void invalidException() {

	}

	@SuppressWarnings("deprecation")
	public Optional<StudentErrorResponse> studentValidateRequest(Student student) {

		List<Error> list = new ArrayList<>();
		Error error = new Error();
		MetaData metaData = new MetaData();
		if (student == null) {
			error.setCode(MISSING);
			error.setMessage("data must be not null");
			metaData.setField("");
			error.setMetaData(metaData);
			list.add(error);
		} else {
			if (StringUtils.isEmpty(student.getName())) {
				error.setCode(MISSING);
				error.setMessage("name must be not null");
				metaData.setField("name");
				error.setMetaData(metaData);
				list.add(error);
			}
		}

		return list.isEmpty() ? Optional.empty()
				: Optional
						.of(StudentErrorResponse.builder().errorId(UUID.randomUUID().toString()).errors(list).build());
	}
}
