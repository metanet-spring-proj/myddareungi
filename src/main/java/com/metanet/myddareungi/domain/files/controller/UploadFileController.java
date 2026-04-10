package com.metanet.myddareungi.domain.files.controller;

import com.metanet.myddareungi.config.CustomUserDetails;
import org.springframework.security.core.Authentication;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.files.service.IUploadFileService;
import com.metanet.myddareungi.domain.notification.service.INotificationService;

@RestController
@RequestMapping("/api/files")
public class UploadFileController {

	private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private INotificationService notificationService;

	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@PostMapping
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
		log.info("파일 도착: {}", file.getOriginalFilename());
		System.out.println("=== upload controller entered ===");
		try {
			if (file.isEmpty()) {
				return ResponseEntity.badRequest().body("파일이 비어있거나 전송되지 않았습니다.");
			}

			String originalFileName = file.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String uuidFileName = uuid + "_" + originalFileName;

			File dir = new File(uploadDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File saveFile = new File(uploadDir, uuidFileName);
			file.transferTo(saveFile);

			UploadFile uploadFile = new UploadFile();
			uploadFile.setFileName(originalFileName);
			uploadFile.setUuidFileName(uuidFileName);
			uploadFile.setFileSize(file.getSize());
			uploadFile.setStoragePath(uploadDir);
			uploadFile.setStatus("PENDING");

			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			uploadFile.setUploaderId(userDetails.getUserId());

			System.out.println("파일 DB 저장 시작: " + originalFileName);
			uploadFileService.uploadFile(uploadFile);

			return ResponseEntity.status(HttpStatus.CREATED).body("파일 업로드 성공");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("파일 업로드 중 오류 발생: " + e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<List<UploadFile>> getAllFile() {
		List<UploadFile> fileList = uploadFileService.getAllFile();
		return ResponseEntity.ok(fileList);
	}

	@GetMapping("/my")
	public ResponseEntity<List<UploadFile>> getMyFiles(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		long userId = userDetails.getUserId();

		List<UploadFile> fileList = uploadFileService.getAllFilesByUploaderId(userId);
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
							.build());

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
			@PathVariable long fileId, Authentication authentication) {
		try {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			long adminId = userDetails.getUserId();

			uploadFileService.reviewFile(fileId, "APPROVED", adminId);
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
			@PathVariable long fileId, Authentication authentication) {
		try {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			long adminId = userDetails.getUserId();

			uploadFileService.reviewFile(fileId, "REJECTED", adminId);
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
