package com.storage.fileserver.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.storage.fileserver.model.UploadedFile;
import com.storage.fileserver.service.SearchServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	SearchServiceImpl service;
	
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
	void testGetFileByName() throws Exception {
		Mockito.when(service.searchFiles("Test", 0, 0, "")).thenReturn(buildFilesList());

		mockMvc.perform(MockMvcRequestBuilders.get("/search").param("name", "Test").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is("Test.mp4")));
	}
}
