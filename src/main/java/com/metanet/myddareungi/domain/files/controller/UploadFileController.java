package com.metanet.myddareungi.domain.files.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.files.service.IUploadFileService;

@RestController
@RequestMapping("/api/files")
public class UploadFileController {
	@Autowired
	private IUploadFileService uploadFileService;

	@PostMapping
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
		//			,Principal principal) {
		System.out.println("=== upload controller entered ===");
		String uploadDir = "C:/dev/upload";
		try {
			if (file.isEmpty()) {
				return ResponseEntity.badRequest().body("업로드할 파일이 없습니다.");
			}
			
			String originalFileName = file.getOriginalFilename();
			String fileExt = originalFileName.substring(originalFileName.lastIndexOf("."));
			
			if (!fileExt.equals(".csv")) {
			    return ResponseEntity.badRequest().body("CSV 파일만 업로드 가능합니다.");
			}
			
			UUID uuid = UUID.randomUUID();
			String savedFileName = uuid + fileExt;

			Path savePath = Paths.get(uploadDir, savedFileName);
			Files.createDirectories(savePath.getParent());
			file.transferTo(savePath.toFile());

			UploadFile uploadFile = new UploadFile();
			//			uploadFile.setUploaderId(principal.getName()); // 로그인 사용자 ID
			uploadFile.setUploaderId(41);
			uploadFile.setFileName(originalFileName);
			uploadFile.setUuidFileName(savedFileName);
			uploadFile.setStoragePath(uploadDir);
			uploadFile.setFileSize(file.getSize());
			uploadFile.setStatus("PENDING");
			uploadFile.setUploadAt(new Timestamp(System.currentTimeMillis()));
			System.out.println("파일 업로드 시작");
			uploadFileService.uploadFile(uploadFile);
			System.out.println("파일 업로드");
			return ResponseEntity.status(HttpStatus.CREATED).body("파일 업로드 성공");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("파일 업로드 실패");
		}
	}

	@GetMapping
	public ResponseEntity<List<UploadFile>> getAllFile() {
		List<UploadFile> fileList = uploadFileService.getAllFile();
		return ResponseEntity.ok(fileList);
	}
	
	@GetMapping("/my")
	public ResponseEntity<List<UploadFile>> getMyFiles() {
	    // TODO: JWT 구현 후 토큰에서 추출한 userId로 교체
		// JWT 적용 후 교체할 코드
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();
	    long mockUserId = 41;
	    
	    List<UploadFile> fileList = uploadFileService.getAllFilesByUploaderId(mockUserId);
	    return ResponseEntity.ok(fileList);
	}

	@GetMapping("/{fileId}/downloads")
	public ResponseEntity<?> getFile(@PathVariable long fileId) {
		try {
			UploadFile file = uploadFileService.getFile(fileId);

			if (file == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("해당 파일이 존재하지 않습니다.");
			}

			Path filePath = Paths.get(file.getStoragePath(), file.getUuidFileName());

			if (!Files.exists(filePath)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("서버에 파일이 존재하지 않습니다.");
			}

			byte[] fileBytes = Files.readAllBytes(filePath);

			String contentType = Files.probeContentType(filePath);
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			String encodedFileName = URLEncoder.encode(file.getFileName(), "UTF-8")
					.replaceAll("\\+", "%20");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(contentType));
			headers.setContentLength(fileBytes.length);
			headers.setContentDisposition(
					ContentDisposition.attachment()
					.filename(encodedFileName)
					.build()
					);

			return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("파일 다운로드 실패");
		}

	}
	
	@DeleteMapping("/{fileId}")
	public ResponseEntity<?> deleteFile(@PathVariable int fileId) {
        try {
            UploadFile file = uploadFileService.getFile(fileId);

            if (file == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("해당 파일이 존재하지 않습니다.");
            }

            Path filePath = Paths.get(file.getStoragePath(), file.getUuidFileName());
            Files.deleteIfExists(filePath);

            uploadFileService.deleteFile(fileId);

            return ResponseEntity.ok("파일 삭제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 삭제 실패");
        }
    }
	
	@PatchMapping("/{fileId}/approve")
	public ResponseEntity<?> approveFile(
	        @PathVariable long fileId) {
	    try {
	        // TODO: JWT 구현 후 토큰에서 추출한 adminId로 교체
	        long mockAdminId = 42;
	        
	        uploadFileService.reviewFile(fileId, "APPROVED", mockAdminId);
	        return ResponseEntity.ok("파일 APPROVE 처리 완료");
	        
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("파일 검토 처리 실패");
	    }
	}
	
	@PatchMapping("/{fileId}/reject")
	public ResponseEntity<?> rejectFile(
	        @PathVariable long fileId) {
	    try {
	        // TODO: JWT 구현 후 토큰에서 추출한 adminId로 교체
	        long mockAdminId = 42;
	        
	        uploadFileService.reviewFile(fileId, "REJECTED", mockAdminId);
	        return ResponseEntity.ok("파일 REJECTE 처리 완료");
	        
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("파일 검토 처리 실패");
	    }
	}




}
