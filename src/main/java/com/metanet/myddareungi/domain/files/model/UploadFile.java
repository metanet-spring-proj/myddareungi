package com.metanet.myddareungi.domain.files.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class UploadFile {
	long fileId;
	long uploaderId;
	String fileName;
	String uuidFileName;
	String storagePath;
	long fileSize;
	String status;
	String reviewedBy;
	Timestamp reviewedAt;
	Timestamp uploadAt;
}	
