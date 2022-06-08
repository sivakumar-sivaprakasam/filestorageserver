package com.storage.fileserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.model.Files;
import com.storage.fileserver.repos.FilesRepository;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {
	
	@InjectMocks
	private SearchServiceImpl service;

	@Mock
	private FilesRepository repo;
	
	private List<Files> buildFile() {
		List<Files> filesList = new ArrayList<>();
		Files testFile = new Files();
		testFile.setId("Test");
		testFile.setFileName("Test.mp4");
		testFile.setFileSize(100);
		testFile.setFileUploaded(Timestamp.valueOf(LocalDateTime.now()));
		filesList.add(testFile);
		return filesList;
	}

	@Test
	public void testSearchFiles() {
		Mockito.when(repo.findByFileNameContainsOrFileSizeLessThanEqualOrFileDurationLessThanEqualOrFileTypeContaining(anyString(), anyLong(), anyLong(), anyString())).thenReturn(buildFile());

		List<UploadedFile> files = service.searchFiles(anyString(), anyLong(), anyLong(), anyString());
		assertEquals(1, files.size());
		assertEquals("Test", files.get(0).getFileid());
	}

}
