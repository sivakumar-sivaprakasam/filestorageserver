package com.storage.fileserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadedFile {
	private String fileid;
	private String name;
	private long size;
	private String created_at;
}
