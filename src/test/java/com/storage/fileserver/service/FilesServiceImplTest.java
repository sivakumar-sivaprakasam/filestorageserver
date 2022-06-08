package com.storage.fileserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.storage.fileserver.exceptions.FileServerException;
import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.model.Files;
import com.storage.fileserver.repos.FilesRepository;

@ExtendWith(MockitoExtension.class)
public class FilesServiceImplTest {

	@InjectMocks
	private FilesServiceImpl service;

	@Mock
	private FilesRepository repo;

	private List<Files> buildFilesList() {
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
	public void testGetAllFiles_EmptyList() {
		Mockito.when(repo.findAll()).thenReturn(new ArrayList<Files>());

		List<UploadedFile> files = service.getFiles();
		assertEquals(0, files.size());
	}

	@Test
	public void testGetAllFiles() {
		Mockito.when(repo.findAll()).thenReturn(buildFilesList());

		List<UploadedFile> files = service.getFiles();
		assertEquals(1, files.size());
		assertEquals("Test", files.get(0).getFileid());
	}

	@Test
	public void testGetFile_NotFound() {
		Mockito.when(repo.findById(anyString())).thenReturn(Optional.empty());
		Throwable exception = assertThrows(FileServerException.class, () -> service.getFile("Test"));
		assertEquals(FileServerException.class, exception.getClass());
	}

	@Test
	public void testGetFile_ReturnExistingFile() {
		Mockito.when(repo.findById(anyString())).thenReturn(Optional.of(buildFilesList().get(0)));

		Files returned = service.getFile("Test");
		assertEquals("Test", returned.getId());
	}

	@Test
	public void testDeleteFile_NotFound() {
		Mockito.when(repo.findById(anyString())).thenReturn(Optional.empty());
		Throwable exception = assertThrows(FileServerException.class, () -> service.deleteFile("Test"));
		assertEquals(FileServerException.class, exception.getClass());
	}

	@Test
	public void testDeleteFile() {
		List<Files> files = buildFilesList();
		Mockito.when(repo.findById(anyString())).thenReturn(Optional.of(files.get(0)));
		doNothing().when(repo).delete(files.get(0));

		service.deleteFile("Test");
	}

	@Test
	public void testUploadFile_InvalidType_ThrowException() {
		List<String> mimeList = new ArrayList<>();
		mimeList.add("video/mp4");
		ReflectionTestUtils.setField(service, "allowedMimeTypes", mimeList);
		MockMultipartFile mockFile = new MockMultipartFile("Test", "Test.txt", MediaType.APPLICATION_JSON_VALUE,
				"Test".getBytes());

		Throwable exception = assertThrows(FileServerException.class, () -> service.saveFile(mockFile));
		assertEquals(FileServerException.class, exception.getClass());
	}

	@Test
	public void testUploadFile_FileExist_ThrowException() {
		List<String> mimeList = new ArrayList<>();
		mimeList.add("video/mp4");
		ReflectionTestUtils.setField(service, "allowedMimeTypes", mimeList);
		MockMultipartFile mockFile = new MockMultipartFile("Test", "Test.txt", "video/mp4", "Test".getBytes());
		List<Files> files = buildFilesList();
		Mockito.when(repo.findByFileName(anyString())).thenReturn(files.get(0));

		Throwable exception = assertThrows(FileServerException.class, () -> service.saveFile(mockFile));
		assertEquals(FileServerException.class, exception.getClass());
	}

	@Test
	public void testUploadFile_FileNotExists_ThrowsException() {
		List<String> mimeList = new ArrayList<>();
		mimeList.add("video/mp4");
		ReflectionTestUtils.setField(service, "allowedMimeTypes", mimeList);
		MockMultipartFile mockFile = new MockMultipartFile("Test", "Test.txt", "video/mp4", "Test".getBytes());
		Mockito.when(repo.findByFileName(anyString())).thenReturn(null);

		Throwable exception = assertThrows(FileServerException.class, () -> service.saveFile(mockFile));
		assertEquals(FileServerException.class, exception.getClass());
	}

	@Test
	public void testUploadFile_FileNotExists_SavesFile() throws IOException {
		List<String> mimeList = new ArrayList<>();
		mimeList.add("video/mp4");
		ReflectionTestUtils.setField(service, "allowedMimeTypes", mimeList);
		byte[] bArr = java.nio.file.Files.readAllBytes(Paths.get("src", "test", "resources", "sample.mp4"));
		MockMultipartFile mockFile = new MockMultipartFile("Test", "Test.txt", "video/mp4", bArr);
		Mockito.when(repo.findByFileName(anyString())).thenReturn(null);
		Files vf = new Files();
		vf.setFileDuration(30);
		Mockito.when(repo.save(any())).thenReturn(vf);

		Files savedFile = service.saveFile(mockFile);
		assertEquals(30, savedFile.getFileDuration());
	}

}
