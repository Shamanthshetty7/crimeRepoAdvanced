package com.endava.CrimeReportingSystem.response;

import lombok.Data;

@Data
public class ApiGenericResponse<T> {
	
	private String message;
    private T data;

    public ApiGenericResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
