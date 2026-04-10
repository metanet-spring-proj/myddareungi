package com.metanet.myddareungi.domain.auth.controller;

import jakarta.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metanet.myddareungi.domain.member.dto.PasswordQuestionRequestDto;
import com.metanet.myddareungi.domain.member.dto.PasswordQuestionResponseDto;
import com.metanet.myddareungi.domain.member.dto.PasswordResetRequestDto;
import com.metanet.myddareungi.domain.member.dto.PasswordResetResponseDto;
import com.metanet.myddareungi.domain.member.service.MemberPasswordRecoveryService;
import com.metanet.myddareungi.domain.member.service.PasswordRecoveryException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Profile("!view")
@RestController
@RequestMapping("/api/v1/auth/password")
@Tag(name = "인증")
public class PasswordRecoveryController {

	private final MemberPasswordRecoveryService memberPasswordRecoveryService;

	public PasswordRecoveryController(MemberPasswordRecoveryService memberPasswordRecoveryService) {
		this.memberPasswordRecoveryService = memberPasswordRecoveryService;
	}

	@Operation(summary = "비밀번호 찾기 질문 조회", description = "아이디와 이메일이 일치하면 저장된 비밀번호 찾기 질문을 반환합니다.")
	@PostMapping("/question")
	public ResponseEntity<PasswordQuestionResponseDto> loadQuestion(
		@Valid @RequestBody PasswordQuestionRequestDto requestDto,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest()
				.body(PasswordQuestionResponseDto.failure(extractFirstErrorMessage(bindingResult)));
		}

		try {
			return ResponseEntity.ok(
				PasswordQuestionResponseDto.success(memberPasswordRecoveryService.loadQuestion(requestDto))
			);
		} catch (PasswordRecoveryException ex) {
			return ResponseEntity.badRequest()
				.body(PasswordQuestionResponseDto.failure(ex.getMessage()));
		}
	}

	@Operation(summary = "비밀번호 재설정", description = "아이디, 이메일, 답변이 모두 일치하면 새 비밀번호로 갱신합니다.")
	@PostMapping("/reset")
	public ResponseEntity<PasswordResetResponseDto> resetPassword(
		@Valid @RequestBody PasswordResetRequestDto requestDto,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest()
				.body(PasswordResetResponseDto.failure(extractFirstErrorMessage(bindingResult)));
		}

		try {
			memberPasswordRecoveryService.resetPassword(requestDto);
			return ResponseEntity.ok(
				PasswordResetResponseDto.success("비밀번호가 재설정되었습니다. 로그인 페이지로 이동합니다.")
			);
		} catch (PasswordRecoveryException ex) {
			return ResponseEntity.badRequest()
				.body(PasswordResetResponseDto.failure(ex.getMessage()));
		}
	}

	private String extractFirstErrorMessage(BindingResult bindingResult) {
		FieldError fieldError = bindingResult.getFieldError();
		return fieldError != null ? fieldError.getDefaultMessage() : "입력값을 다시 확인해주세요.";
	}
}
