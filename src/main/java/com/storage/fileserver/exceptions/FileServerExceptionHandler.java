package com.storage.fileserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

import com.storage.fileserver.model.FileUploadResultMessage;

@RestControllerAdvice
public class FileServerExceptionHandler {

	@ExceptionHandler(FileServerException.class)
	public ResponseEntity<FileUploadResultMessage> handleExceptions(FileServerException ex, WebRequest request) {
		return ResponseEntity.status(ex.getStatusCode()).body(new FileUploadResultMessage(ex.getMessage()));
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<FileUploadResultMessage> handleSizeExceededException(MultipartException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FileUploadResultMessage(ex.getMessage()));
	}
}
