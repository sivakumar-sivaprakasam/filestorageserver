package com.storage.fileserver.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

@Slf4j
public class FileServerUtils {

	private static final String VIDEO_FILES_CONTENT_TYPE = "video/";
	private static final String TEMP_FILE_SUFFIX = ".tmp";

	public static Long getDuration(MultipartFile file) throws Exception {
		long duration = 0L;
		if (file.getContentType().startsWith(VIDEO_FILES_CONTENT_TYPE)) {
			try {
				final File tmpFile = File.createTempFile(file.getOriginalFilename(), TEMP_FILE_SUFFIX);
				tmpFile.deleteOnExit();
				FileUtils.copyInputStreamToFile(file.getInputStream(), tmpFile);
				MultimediaObject multiMediaObject = new MultimediaObject(tmpFile);
				MultimediaInfo objectInfo = multiMediaObject.getInfo();
				duration = objectInfo.getDuration() / 1000;
			} catch (IOException ex) {
				log.error("Error while computing video length", ex);
			} catch (EncoderException ex) {
				log.error("Error while computing video length", ex);
				throw ex;
			}
		}
		return duration;
	}
}
