package com.metanet.myddareungi.domain.member.dto;

public class PasswordResetResponseDto {

	private boolean success;
	private String message;

	public static PasswordResetResponseDto success(String message) {
		PasswordResetResponseDto response = new PasswordResetResponseDto();
		response.setSuccess(true);
		response.setMessage(message);
		return response;
	}

	public static PasswordResetResponseDto failure(String message) {
		PasswordResetResponseDto response = new PasswordResetResponseDto();
		response.setSuccess(false);
		response.setMessage(message);
		return response;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
