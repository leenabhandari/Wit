package com.wit.farmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ApiException extends RuntimeException{
	
	public ApiException(String msg) {
		super(msg);
	}

}
