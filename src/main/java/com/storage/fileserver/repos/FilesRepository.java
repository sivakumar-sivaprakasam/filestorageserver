package com.storage.fileserver.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storage.fileserver.model.Files;

@Repository
public interface FilesRepository extends JpaRepository<Files, String> {

	Files findByFileName(String originalFilename);

	List<Files> findByFileNameContainsOrFileSizeLessThanEqualOrFileDurationLessThanEqualOrFileTypeContaining(
			String fileName, long fileSize, long fileDuration, String fileType);

}
