package com.metanet.myddareungi.domain.files.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.metanet.myddareungi.domain.files.model.UploadFile;

@Mapper
public interface IUploadFileRepository {
	void uploadFile(UploadFile file);
	List<UploadFile> getAllFile();
	List<UploadFile> getAllFilesByUploaderId(long uploaderId);
	
	UploadFile getFile(long fileId);
	
	void deleteFile(long fileId);
	void reviewFile(UploadFile file);
	
}
