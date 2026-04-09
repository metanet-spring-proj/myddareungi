package com.metanet.myddareungi.domain.files.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.files.repository.IUploadFileRepository;

@Service
public class UploadFileService implements IUploadFileService {

	@Autowired
	IUploadFileRepository uploadFileRepository;

	@Override
	public void uploadFile(UploadFile file) {
		System.out.println("서비스 업로드");
		uploadFileRepository.uploadFile(file);
	}

	@Override
	public UploadFile getFile(long fileId) {
		return uploadFileRepository.getFile(fileId);
	}

	@Override
	public List<UploadFile> getAllFile() {
		return uploadFileRepository.getAllFile();
	}

	@Override
	public List<UploadFile> getAllFilesByUploaderId(long uploaderId) {
	    return uploadFileRepository.getAllFilesByUploaderId(uploaderId);
	}
	
	@Override
	public void deleteFile(long fileId) {
		uploadFileRepository.deleteFile(fileId);
	}

	@Override
	public String getUuid(long fileId) {
		return uploadFileRepository.getUuid(fileId);
	}


}
