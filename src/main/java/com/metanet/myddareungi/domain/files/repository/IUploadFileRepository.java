package com.metanet.myddareungi.domain.files.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.metanet.myddareungi.domain.files.model.UploadFile;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IUploadFileRepository {
	void uploadFile(UploadFile file);
	List<UploadFile> getAllFile();
	List<UploadFile> getAllFilesByUploaderId(long uploaderId);
	
	UploadFile getFile(long fileId);
	
	void deleteFile(long fileId);
	void reviewFile(UploadFile file);
	Long getLastFileId();

	List<UploadFile> getFilesByUploaderIdPaged(@Param("uploaderId") long uploaderId,
											   @Param("offset") int offset,
											   @Param("size") int size);

	int countFilesByUploaderId(long uploaderId);
}
