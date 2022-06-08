package com.storage.fileserver.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileServerException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -998996573193602516L;

	private HttpStatus statusCode;
	private String message;
}
