package com.metanet.myddareungi.domain.member.dto;

public class PasswordQuestionResponseDto {

	private boolean success;
	private String question;
	private String message;

	public static PasswordQuestionResponseDto success(String question) {
		PasswordQuestionResponseDto response = new PasswordQuestionResponseDto();
		response.setSuccess(true);
		response.setQuestion(question);
		response.setMessage("비밀번호 찾기 질문을 불러왔습니다.");
		return response;
	}

	public static PasswordQuestionResponseDto failure(String message) {
		PasswordQuestionResponseDto response = new PasswordQuestionResponseDto();
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

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
