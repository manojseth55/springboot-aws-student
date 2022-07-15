package com.student.studentapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentErrorResponse {
	
	@JsonProperty("error_id")
	private String errorId;
	private List<Error> errors;
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Error{
		private String message;
		private String code;
		private MetaData metaData;
	}
	
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MetaData{
		private String field;
	}

}
