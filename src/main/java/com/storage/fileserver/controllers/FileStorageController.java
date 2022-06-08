package com.storage.fileserver.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.model.Files;
import com.storage.fileserver.model.FileUploadResultMessage;
import com.storage.fileserver.service.FilesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/files")
public class FileStorageController {

	@Autowired
	private FilesService service;

	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@Operation(description = "Upload a file")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "File uploaded", headers = @Header(name = "Location", description = "Created file location", schema = @Schema(type = "String")), content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "409", description = "File exists", content = @Content),
			@ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content) })
	public ResponseEntity<FileUploadResultMessage> uploadFile(
			@RequestPart(value = "content") @NotNull @NotEmpty MultipartFile file) {
		Files savedFile = service.saveFile(file);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{fileid}")
				.buildAndExpand(savedFile.getId()).toUri();
		log.info("URI of newly uploaded file: {}", location);
		return ResponseEntity.status(HttpStatus.CREATED).location(location)
				.body(new FileUploadResultMessage("File uploaded"));
	}

	@GetMapping
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "File list", content = @Content(mediaType = "application/json", schema = @Schema(type = "array"))) })
	public ResponseEntity<List<UploadedFile>> getFilesList() {
		return ResponseEntity.ok().body(service.getFiles());
	}

	@GetMapping("/{fileid}")
	public ResponseEntity<?> getFile(@PathVariable(name = "fileid", required = true) String fileId) {
		Files existingFile = service.getFile(fileId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + existingFile.getFileName() + "\"")
				.body(existingFile.getFileContent());
	}

	@DeleteMapping("/{fileid}")
	public ResponseEntity<FileUploadResultMessage> deleteFile(
			@PathVariable(name = "fileid", required = true) String fileId) {
		service.deleteFile(fileId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(new FileUploadResultMessage("File was successfully removed"));
	}
}
