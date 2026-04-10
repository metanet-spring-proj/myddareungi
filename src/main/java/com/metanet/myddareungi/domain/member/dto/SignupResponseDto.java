package com.metanet.myddareungi.domain.member.dto;

public class SignupResponseDto {

	private Long userId;
	private String loginId;
	private String message;

	public static SignupResponseDto created(Long userId, String loginId, String message) {
		SignupResponseDto response = new SignupResponseDto();
		response.setUserId(userId);
		response.setLoginId(loginId);
		response.setMessage(message);
		return response;
	}

	public static SignupResponseDto failure(String message) {
		SignupResponseDto response = new SignupResponseDto();
		response.setMessage(message);
		return response;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
