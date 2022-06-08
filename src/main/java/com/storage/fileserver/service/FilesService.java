package com.storage.fileserver.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.model.Files;

public interface FilesService {
	Files saveFile(MultipartFile file);
	
	List<UploadedFile> getFiles();
	
	void deleteFile(String fileid);
	
	Files getFile(String fileid);
}
