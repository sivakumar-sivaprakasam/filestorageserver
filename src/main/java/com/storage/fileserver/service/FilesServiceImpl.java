package com.storage.fileserver.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.storage.fileserver.exceptions.FileServerException;
import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.model.Files;
import com.storage.fileserver.repos.FilesRepository;
import com.storage.fileserver.utils.FileServerUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FilesServiceImpl implements FilesService {

	@Value("${server.compression.mime-types}")
	private List<String> allowedMimeTypes;

	@Autowired
	private FilesRepository repo;

	@Override
	@Transactional
	public Files saveFile(MultipartFile file) {
		log.info("Received file {} with content type {}", file.getOriginalFilename(), file.getContentType());
		if (!allowedMimeTypes.contains(file.getContentType())) {			
			throw new FileServerException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
		}
		Files tblFile = repo.findByFileName(file.getOriginalFilename());
		if (null != tblFile) {
			throw new FileServerException(HttpStatus.CONFLICT, "File exists");
		}
		try {
			tblFile = new Files();
			tblFile.setFileName(file.getOriginalFilename());
			tblFile.setFileType(file.getContentType());
			tblFile.setFileSize(file.getSize());
			tblFile.setFileContent(file.getBytes());
			tblFile.setFileDuration(FileServerUtils.getDuration(file));
			Files savedFile = repo.save(tblFile);
			log.info("File saved successfully!!! Id: {}", savedFile.getId());
			return savedFile;
		} catch (Exception ex) {
			throw new FileServerException(HttpStatus.BAD_REQUEST, "Bad request");
		}
	}

	@Override
	public List<UploadedFile> getFiles() {
		List<Files> filesList = repo.findAll();
		final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss z");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		return filesList.stream().map(x -> {
			UploadedFile uploadedFile = new UploadedFile();
			uploadedFile.setFileid(x.getId());
			uploadedFile.setName(x.getFileName());
			uploadedFile.setSize(x.getFileSize());
			uploadedFile.setCreated_at(formatter.format(x.getFileUploaded()));
			return uploadedFile;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteFile(String fileid) {
		Optional<Files> existingFile = repo.findById(fileid);
		if (existingFile.isEmpty()) {
			throw new FileServerException(HttpStatus.NOT_FOUND, "File not found");
		}
		repo.delete(existingFile.get());
	}

	@Override
	public Files getFile(String fileid) {
		Optional<Files> existingFile = repo.findById(fileid);
		if (existingFile.isEmpty()) {
			throw new FileServerException(HttpStatus.NOT_FOUND, "File not found");
		}
		return existingFile.get();
	}
}
