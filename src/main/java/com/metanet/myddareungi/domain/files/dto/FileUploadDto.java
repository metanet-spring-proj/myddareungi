package com.metanet.myddareungi.domain.files.dto;

import lombok.Data;

@Data
public class FileUploadDto {
	private int fileId;
	private String fileName;
	private int fileSize;
	private String status;
}
