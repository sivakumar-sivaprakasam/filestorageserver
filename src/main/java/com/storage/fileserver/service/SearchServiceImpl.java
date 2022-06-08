package com.storage.fileserver.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.model.Files;
import com.storage.fileserver.repos.FilesRepository;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private FilesRepository repo;

	@Override
	public List<UploadedFile> searchFiles(String fileName, long fileSize, long fileLength, String fileType) {
		final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss z");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		List<Files> filesList = repo
				.findByFileNameContainsOrFileSizeLessThanEqualOrFileDurationLessThanEqualOrFileTypeContaining(fileName,
						fileSize, fileLength, fileType);
		return filesList.stream().map(x -> {
			UploadedFile uploadedFile = new UploadedFile();
			uploadedFile.setFileid(x.getId());
			uploadedFile.setName(x.getFileName());
			uploadedFile.setSize(x.getFileSize());
			uploadedFile.setCreated_at(formatter.format(x.getFileUploaded()));
			return uploadedFile;
		}).collect(Collectors.toList());
	}

}
