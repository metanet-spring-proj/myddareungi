package com.metanet.myddareungi.domain.notification.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.metanet.myddareungi.config.CustomUserDetails;
import com.metanet.myddareungi.config.JwtCookieUtils;
import com.metanet.myddareungi.config.JwtTokenProvider;
import com.metanet.myddareungi.config.SecurityConfig;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import com.metanet.myddareungi.domain.notification.model.Notification;
import com.metanet.myddareungi.domain.notification.service.INotificationService;
import com.metanet.myddareungi.domain.notification.service.SseEmitterService;


@WebMvcTest(NotificationController.class)
@Import(SecurityConfig.class)
public class NotificationControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	INotificationService notificationService;

	@MockitoBean
	SseEmitterService sseEmitterService;

	@MockitoBean
	JwtTokenProvider jwtTokenProvider;

	@MockitoBean
	JwtCookieUtils jwtCookieUtils;

	@MockitoBean
	MemberAuthService memberAuthService;

	private Authentication customAuth() {
		CustomUserDetails userDetails = new CustomUserDetails(
				1L, "testuser", "password", List.of());
		return new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
	}
	//GET /api/v1/notifications/all

	@Test
	@WithMockUser
	@DisplayName("GET /all - 전체 알림 목록 조회 - 200 OK")
	void getNotifications_200() throws Exception {
		// given
		Notification n = new Notification();
		n.setNotificationId(1L);
		n.setMessage("CSV파일이 업로드 되었습니다.");
		when(notificationService.findAll()).thenReturn(List.of(n));

		// when & then
		mockMvc.perform(get("/api/v1/notifications/all"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].notificationId").value(1L))
		.andExpect(jsonPath("$[0].message").value("CSV파일이 업로드 되었습니다."));
	}

	@Test
	@DisplayName("GET /all - 비로그인 → 리디렉션")
	void getNotifications_비로그인_리디렉션() throws Exception {
		mockMvc.perform(get("/api/v1/notifications/all"))
		.andExpect(status().is3xxRedirection());
	}

	//GET /api/v1/notifications/unread

	@Test
	@DisplayName("GET /unread - 읽지 않은 알림 조회 - 200 OK")
	void getMyUnreadNotifications_200() throws Exception {
		// given
		Notification n = new Notification();
		n.setNotificationId(2L);
		n.setIsRead(0);
		when(notificationService.findUnreadById(1L)).thenReturn(List.of(n));

		// when & then
		mockMvc.perform(get("/api/v1/notifications/unread")
				.with(authentication(customAuth())))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].notificationId").value(2L))
		.andExpect(jsonPath("$[0].isRead").value(0));
	}

	@Test
	@DisplayName("GET /unread - 비로그인 → 리디렉션")
	void getMyUnreadNotifications_비로그인_리디렉션() throws Exception {
		mockMvc.perform(get("/api/v1/notifications/unread"))
		.andExpect(status().is3xxRedirection());
	}

	//GET /api/v1/notifications

	@Test
	@DisplayName("GET / - 내 알림 목록 조회 - 200 OK")
	void getMyNotifications_200() throws Exception {
		// given
		Notification n = new Notification();
		n.setNotificationId(3L);
		n.setUserId(1L);
		when(notificationService.findAllById(1L)).thenReturn(List.of(n));

		// when & then
		mockMvc.perform(get("/api/v1/notifications")
				.with(authentication(customAuth())))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].userId").value(1L));
	}

	//PATCH /api/v1/notifications/{notificationId}/read

	@Test
	@WithMockUser
	@DisplayName("PATCH /{notificationId}/read - 단건 읽음 처리 - 200 OK")
	void markAsRead_200() throws Exception {
		// given
		doNothing().when(notificationService).markAsRead(1L);

		// when & then
		mockMvc.perform(patch("/api/v1/notifications/1/read")
				.with(csrf()))
		.andExpect(status().isOk());

		verify(notificationService, times(1)).markAsRead(1L);
	}

	//PATCH /api/v1/notifications/read

	@Test
	@DisplayName("PATCH /read - 전체 읽음 처리 - 200 OK")
	void markReadAll_200() throws Exception {
		// given
		doNothing().when(notificationService).markAllRead(1L);

		// when & then
		mockMvc.perform(patch("/api/v1/notifications/read")
				.with(authentication(customAuth()))
				.with(csrf()))
		.andExpect(status().isOk());

		verify(notificationService, times(1)).markAllRead(1L);
	}

	//DELETE /api/v1/notifications/{notificationId}

	@Test
	@WithMockUser
	@DisplayName("DELETE /{notificationId} - 알림 삭제 - 204 No Content")
	void deleteNotification_204() throws Exception {
		// given
		doNothing().when(notificationService).deleteById(1L);

		// when & then
		mockMvc.perform(delete("/api/v1/notifications/1").with(csrf()))
		.andExpect(status().isNoContent());

		verify(notificationService, times(1)).deleteById(1L);
	}

	//GET /api/v1/notifications/subscribe

	@Test
	@DisplayName("GET /subscribe - SSE 연결 - 200 OK, text/event-stream")
	void subscribe_200() throws Exception {
		// given
		when(sseEmitterService.addEmitter(1L)).thenReturn(new SseEmitter(0L));

		// when & then
		mockMvc.perform(get("/api/v1/notifications/subscribe")
				.with(authentication(customAuth())))
		.andExpect(status().isOk());

		verify(sseEmitterService, times(1)).addEmitter(1L);
	}

	@Test
	@DisplayName("GET /subscribe - 비로그인 - 리디렉션")
	void subscribe_비로그인_리디렉션() throws Exception {
		mockMvc.perform(get("/api/v1/notifications/subscribe"))
		.andExpect(status().is3xxRedirection());
	}

}

