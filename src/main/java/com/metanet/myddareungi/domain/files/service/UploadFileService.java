package com.metanet.myddareungi.domain.files.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.files.repository.IUploadFileRepository;
import com.metanet.myddareungi.domain.notification.service.INotificationService;

@Service
@Transactional
public class UploadFileService implements IUploadFileService {

	@Autowired
	IUploadFileRepository uploadFileRepository;

	@Autowired
	INotificationService notificationService;
	
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
	public void reviewFile(long fileId, String status, long reviewedBy) {
	    UploadFile file = uploadFileRepository.getFile(fileId);
	    
	    if(file == null) {
	    	throw new IllegalArgumentException();
	    }
	    
	    file.setStatus(status);
	    file.setReviewedBy(reviewedBy);
	    uploadFileRepository.reviewFile(file);

	    notificationService.insert(
				file.getUploaderId(),
				status, 
				"파일이 [" + status + "] 처리 되었습니다.",
				file.getFileId());
	}

	@Override
	public long getLastFileId() {
		return uploadFileRepository.getLastFileId();
	}

	@Override
	public List<UploadFile> getFilesByUploaderIdPaged(long uploaderId, int offset, int size) {
		return uploadFileRepository.getFilesByUploaderIdPaged(uploaderId, offset, size);
	}

	@Override
	public int countFilesByUploaderId(long uploaderId) {
		return uploadFileRepository.countFilesByUploaderId(uploaderId);
	}

}
