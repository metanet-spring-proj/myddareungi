package com.metanet.myddareungi.domain.fileupload.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.metanet.myddareungi.config.CustomUserDetails;
import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.domain.files.controller.UploadFileController;
import com.metanet.myddareungi.domain.files.model.UploadFile;
import com.metanet.myddareungi.domain.files.service.IUploadFileService;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import com.metanet.myddareungi.domain.notification.service.INotificationService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static
org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UploadFileController.class)
@Import(SecurityConfig.class)
public class UploadFileControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@MockitoBean
	IUploadFileService uploadFileService;
	
	@MockitoBean
    INotificationService notificationService;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    JwtCookieUtils jwtCookieUtils;

    @MockitoBean
    MemberAuthService memberAuthService;
    
    /** CustomUserDetails 기반 인증 객체 생성 헬퍼 */
    private Authentication customAuth() {
        CustomUserDetails userDetails = new CustomUserDetails(
                23L, "testuser", "password", List.of());
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
 // ── POST /api/files 

    @Test
    @DisplayName("POST /api/files - 파일 업로드 성공 201 Created")
    void uploadFile_성공_201() throws Exception {
        // given
        when(uploadFileService.getLastFileId()).thenReturn(1L);
        doNothing().when(uploadFileService).uploadFile(any());
        doNothing().when(notificationService).insert(anyLong(), anyString(), anyString(), anyLong());
        when(memberAuthService.getMembersByRole("ADMIN")).thenReturn(List.of(
                Member.builder().userId(22L).role("ADMIN").build(),
                Member.builder().userId(24L).role("ADMIN").build()));

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", "col1,col2\nval1,val2".getBytes());

        // when & then
        mockMvc.perform(multipart("/api/files")
                        .file(file)
                        .with(authentication(customAuth()))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().string("파일 업로드 성공"));

        verify(notificationService).insert(
                eq(22L),
                eq("FILE UPLOAD"),
                eq("CSV파일이 업로드 되었습니다."),
                eq(1L));
        verify(notificationService).insert(
                eq(24L),
                eq("FILE UPLOAD"),
                eq("CSV파일이 업로드 되었습니다."),
                eq(1L));
        verify(notificationService, never()).insert(
                eq(23L),
                eq("FILE UPLOAD"),
                eq("CSV파일이 업로드 되었습니다."),
                eq(1L));
    }
    
    
    @Test
    @DisplayName("POST /api/files - 빈 파일 400 Bad Request")
    void uploadFile_빈파일_400() throws Exception {
        // given
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.csv", "text/csv", new byte[0]);

        // when & then
        mockMvc.perform(multipart("/api/files")
                        .file(emptyFile)
                        .with(authentication(customAuth()))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("파일이 비어있거나 전송되지 않았습니다."));
    }
    @Test
    @DisplayName("POST /api/files - 비로그인 - 리디렉션")
    void uploadFile_비로그인_리디렉션() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", "data".getBytes());

        // when & then
        mockMvc.perform(multipart("/api/files")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }
    // ── GET /api/files ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("GET /api/files - 전체 파일 목록 조회 - 200 OK")
    void getAllFile_200() throws Exception {
        // given
        UploadFile uploadFile = new UploadFile();
        uploadFile.setFileId(1L);
        uploadFile.setFileName("test.csv");
        uploadFile.setStatus("PENDING");
        when(uploadFileService.getAllFile()).thenReturn(List.of(uploadFile));

        // when & then
        mockMvc.perform(get("/api/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("test.csv"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    @DisplayName("GET /api/files - 비로그인 - 리디렉션")
    void getAllFile_비로그인_리디렉션() throws Exception {
        mockMvc.perform(get("/api/files"))
                .andExpect(status().is3xxRedirection());
    }
 // ── GET /api/files/my ───────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/files/my - 내 파일 목록 조회 - 200 OK")
    void getMyFiles_200() throws Exception {
        // given
        UploadFile uploadFile = new UploadFile();
        uploadFile.setUploaderId(23L);
        uploadFile.setFileName("my.csv");
        when(uploadFileService.getAllFilesByUploaderId(23L)).thenReturn(List.of(uploadFile));

        // when & then
        mockMvc.perform(get("/api/files/my")
                        .with(authentication(customAuth())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("my.csv"));
    }
    
 // ── GET /api/files/{fileId}/downloads ───────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("GET /api/files/{fileId}/downloads - DB에 파일 없음 - 404 Not Found")
    void downloadFile_DB없음_404() throws Exception {
        // given
        when(uploadFileService.getFile(999L)).thenReturn(null);

        // when & then
        mockMvc.perform(get("/api/files/999/downloads"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/files/{fileId}/downloads - 파일 다운로드 성공 - 200 OK")
    void downloadFile_성공_200() throws Exception {
        // given - 실제 임시 파일 생성
        Path tmpFile = Files.createTempFile("test", ".csv");
        Files.write(tmpFile, "col1,col2\nval1,val2".getBytes());

        UploadFile uploadFile = new UploadFile();
        uploadFile.setFileId(1L);
        uploadFile.setFileName("test.csv");
        uploadFile.setUuidFileName(tmpFile.getFileName().toString());
        uploadFile.setStoragePath(tmpFile.getParent().toString());
        when(uploadFileService.getFile(1L)).thenReturn(uploadFile);

        // when & then
        mockMvc.perform(get("/api/files/1/downloads"))
                .andExpect(status().isOk());

        Files.deleteIfExists(tmpFile); // 테스트 후 정리
    }

    // ── DELETE /api/files/{fileId} ──────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/files/{fileId} - 파일 삭제 성공 - 200 OK")
    void deleteFile_성공_200() throws Exception {
        // given - 존재하지 않는 파일명으로 설정 (Files.deleteIfExists는 없어도 예외 없음)
        UploadFile uploadFile = new UploadFile();
        uploadFile.setFileId(1L);
        uploadFile.setStoragePath(System.getProperty("java.io.tmpdir"));
        uploadFile.setUuidFileName("not_exist_file.csv");
        when(uploadFileService.getFile(1L)).thenReturn(uploadFile);
        doNothing().when(uploadFileService).deleteFile(1L);

        // when & then
        mockMvc.perform(delete("/api/files/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("파일 삭제 성공"));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/files/{fileId} - 파일 없음 - 404 Not Found")
    void deleteFile_없음_404() throws Exception {
        // given
        when(uploadFileService.getFile(999L)).thenReturn(null);

        // when & then
        mockMvc.perform(delete("/api/files/999").with(csrf()))
                .andExpect(status().isNotFound());
    }

    // ── PATCH /api/files/{fileId}/approve ───────────────────────────────────────

    @Test
    @DisplayName("PATCH /api/files/{fileId}/approve - 승인 성공 - 200 OK")
    void approveFile_성공_200() throws Exception {
        // given
        doNothing().when(uploadFileService).reviewFile(1L, "APPROVED", 23L);

        // when & then
        mockMvc.perform(patch("/api/files/1/approve")
                        .with(authentication(customAuth()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("파일 APPROVE 처리 완료"));
    }

    @Test
    @DisplayName("PATCH /api/files/{fileId}/approve - 대상 파일 없음 - 404 Not Found")
    void approveFile_없음_404() throws Exception {
        // given
        doThrow(new IllegalArgumentException("파일을 찾을 수 없습니다."))
                .when(uploadFileService).reviewFile(999L, "APPROVED", 23L);

        // when & then
        mockMvc.perform(patch("/api/files/999/approve")
                        .with(authentication(customAuth()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    // ── PATCH /api/files/{fileId}/reject ────────────────────────────────────────

    @Test
    @DisplayName("PATCH /api/files/{fileId}/reject - 거절 성공 - 200 OK")
    void rejectFile_성공_200() throws Exception {
        // given
        doNothing().when(uploadFileService).reviewFile(1L, "REJECTED", 23L);

        // when & then
        mockMvc.perform(patch("/api/files/1/reject")
                        .with(authentication(customAuth()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("파일 REJECTED 처리 완료"));
    }

    @Test
    @DisplayName("PATCH /api/files/{fileId}/reject - 대상 파일 없음 - 404 Not Found")
    void rejectFile_없음_404() throws Exception {
        // given
        doThrow(new IllegalArgumentException("파일을 찾을 수 없습니다."))
                .when(uploadFileService).reviewFile(999L, "REJECTED", 23L);

        // when & then
        mockMvc.perform(patch("/api/files/999/reject")
                        .with(authentication(customAuth()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
