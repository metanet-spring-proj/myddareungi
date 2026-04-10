package com.metanet.myddareungi.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String userName;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

	@NotBlank(message = "기존 비밀번호는 필수입니다.")
	private String oldPassword;
	
	@NotBlank(message = "새로운 비밀번호는 필수입니다.")
	@Size(max = 255, message = "비밀번호는 255자 이하로 입력해주세요.")
	private String newPassword;
    
	@NotBlank(message = "비밀번호 확인은 필수입니다.")
	@Size(max = 255, message = "비밀번호 확인은 255자 이하로 입력해주세요.")
	private String newPasswordConfirm;
}