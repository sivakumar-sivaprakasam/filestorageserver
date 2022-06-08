package com.storage.fileserver.service;

import java.util.List;

import com.storage.fileserver.model.UploadedFile;

public interface SearchService {
	
	List<UploadedFile> searchFiles(String fileName, long fileSize, long fileLength, String fileType);

}
