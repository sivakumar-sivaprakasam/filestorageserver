package com.storage.fileserver.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_files")
public class Files {

	@Id
	@GenericGenerator(name = "uuid_gen", strategy = "uuid2")
	@GeneratedValue(generator = "uuid_gen")
	private String id;

	@Column(unique = true)
	private String fileName;

	private long fileSize;

	private String fileType;

	@Lob
	private byte[] fileContent;

	private long fileDuration;

	@CreationTimestamp
	private Timestamp fileUploaded;

}
