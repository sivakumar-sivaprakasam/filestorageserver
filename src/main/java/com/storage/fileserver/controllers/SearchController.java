package com.storage.fileserver.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.service.SearchService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/search")
public class SearchController {
	@Autowired
	private SearchService service;

	@GetMapping
	@Operation(description = "Search Files by Name, Duration, Size and Type")
	public ResponseEntity<List<UploadedFile>> searchFiles(
			@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "0") long size,
			@RequestParam(required = false, defaultValue = "0") long duration,
			@RequestParam(required = false, defaultValue = "") String type) {
		List<UploadedFile> files = service.searchFiles(name, size, duration, type);
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}
}
