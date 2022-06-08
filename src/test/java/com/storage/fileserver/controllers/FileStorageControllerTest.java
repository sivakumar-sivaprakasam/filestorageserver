package com.storage.fileserver.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.storage.fileserver.model.Files;
import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.service.FilesServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class FileStorageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	FilesServiceImpl service;

	private List<UploadedFile> buildFilesList() {
		List<UploadedFile> filesList = new ArrayList<>();
		UploadedFile uploadedFile = new UploadedFile();
		uploadedFile.setFileid("Test");
		uploadedFile.setName("Test.mp4");
		uploadedFile.setSize(1000);
		uploadedFile.setCreated_at("05-01-2022 00:00:00 UTC");
		filesList.add(uploadedFile);

		return filesList;
	}

	@Test
	public void testGetAllFiles() throws Exception {
		Mockito.when(service.getFiles()).thenReturn(buildFilesList());

		mockMvc.perform(MockMvcRequestBuilders.get("/files").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is("Test.mp4")));
	}

	@Test
	public void testGetFile() throws Exception {
		Files vf = new Files();
		vf.setFileName("Test.mp4");
		vf.setFileContent(null);
		Mockito.when(service.getFile(anyString())).thenReturn(vf);

		mockMvc.perform(MockMvcRequestBuilders.get("/files/test").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteFile() throws Exception {
		Files vf = new Files();
		vf.setFileName("Test.mp4");
		vf.setFileType("video/mp4");
		vf.setFileContent(null);
		Mockito.when(service.getFile(anyString())).thenReturn(vf);

		mockMvc.perform(MockMvcRequestBuilders.delete("/files/test").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void testUploadFile() throws Exception {
		Files vf = new Files();
		vf.setFileName("sample.mp4");
		vf.setFileType("video/mp4");
		vf.setFileContent(null);

		List<String> mimeList = new ArrayList<>();
		mimeList.add("video/mp4");
		ReflectionTestUtils.setField(service, "allowedMimeTypes", mimeList);

		byte[] bArr = java.nio.file.Files.readAllBytes(Paths.get("src", "test", "resources", "sample.mp4"));
		MockMultipartFile mockFile = new MockMultipartFile("content", "sample.mp4", "video/mp4", bArr);
		Mockito.when(service.saveFile(mockFile)).thenReturn(vf);

		mockMvc.perform(MockMvcRequestBuilders.multipart("/files").file(mockFile)).andExpect(status().isCreated());
	}

}
