package com.metanet.myddareungi.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordQuestionRequestDto {

	@NotBlank(message = "아이디는 필수입니다.")
	@Size(max = 50, message = "아이디는 50자 이하로 입력해주세요.")
	private String loginId;

	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "이메일 형식을 확인해주세요.")
	@Size(max = 100, message = "이메일은 100자 이하로 입력해주세요.")
	private String email;

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
}
