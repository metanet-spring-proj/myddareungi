package com.metanet.myddareungi.domain.files.service;

import java.util.List;

import com.metanet.myddareungi.domain.files.model.UploadFile;

public interface IUploadFileService {
	void uploadFile(UploadFile file);
	UploadFile getFile(long fileId);
	List<UploadFile> getAllFile();
	List<UploadFile> getAllFilesByUploaderId(long uploaderId);

	void deleteFile(long fileId);
	String getUuid(long fileid);
}
