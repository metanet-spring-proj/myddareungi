package com.metanet.myddareungi.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequestDto {

	@NotBlank(message = "이름은 필수입니다.")
	@Size(max = 50, message = "이름은 50자 이하로 입력해주세요.")
	private String userName;

	@NotBlank(message = "아이디는 필수입니다.")
	@Size(max = 50, message = "아이디는 50자 이하로 입력해주세요.")
	private String loginId;

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "이메일 형식을 확인해주세요.")
	@Size(max = 100, message = "이메일은 100자 이하로 입력해주세요.")
	private String email;

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(max = 255, message = "비밀번호는 255자 이하로 입력해주세요.")
	private String password;

	@NotBlank(message = "비밀번호 확인은 필수입니다.")
	@Size(max = 255, message = "비밀번호 확인은 255자 이하로 입력해주세요.")
	private String passwordConfirm;

	@NotBlank(message = "비밀번호 찾기 질문은 필수입니다.")
	@Size(max = 100, message = "질문은 100자 이하로 입력해주세요.")
	private String question;

	@NotBlank(message = "비밀번호 찾기 답변은 필수입니다.")
	@Size(max = 255, message = "답변은 255자 이하로 입력해주세요.")
	private String answer;

	private boolean agreeToTerms;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isAgreeToTerms() {
		return agreeToTerms;
	}

	public void setAgreeToTerms(boolean agreeToTerms) {
		this.agreeToTerms = agreeToTerms;
	}
}
