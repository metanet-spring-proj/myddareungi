package com.metanet.myddareungi.domain.member.controller;

import jakarta.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metanet.myddareungi.domain.member.dto.SignupRequestDto;
import com.metanet.myddareungi.domain.member.dto.SignupResponseDto;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.DuplicateMemberException;
import com.metanet.myddareungi.domain.member.service.InvalidSignupRequestException;
import com.metanet.myddareungi.domain.member.service.MemberSignupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Profile("!view")
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "회원 관련 API")
public class MemberApiController {

	private final MemberSignupService memberSignupService;

	public MemberApiController(MemberSignupService memberSignupService) {
		this.memberSignupService = memberSignupService;
	}

	@Operation(summary = "회원가입", description = "회원가입 정보를 저장하고 생성된 회원 식별자를 반환합니다.")
	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signUp(
		@Valid @RequestBody SignupRequestDto requestDto,
		BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest()
				.body(SignupResponseDto.failure(extractFirstErrorMessage(bindingResult)));
		}

		try {
			Member createdMember = memberSignupService.signUp(requestDto);
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(SignupResponseDto.created(
					createdMember.getUserId(),
					createdMember.getLoginId(),
					"회원가입이 완료되었습니다."
				));
		} catch (InvalidSignupRequestException ex) {
			return ResponseEntity.badRequest()
				.body(SignupResponseDto.failure(ex.getMessage()));
		} catch (DuplicateMemberException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(SignupResponseDto.failure(ex.getMessage()));
		}
	}

	private String extractFirstErrorMessage(BindingResult bindingResult) {
		FieldError fieldError = bindingResult.getFieldError();
		return fieldError != null ? fieldError.getDefaultMessage() : "회원가입 입력값을 확인해주세요.";
	}
}
