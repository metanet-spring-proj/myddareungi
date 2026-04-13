package com.metanet.myddareungi.domain.member.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.metanet.myddareungi.domain.member.dto.MemberResponseDto;
import com.metanet.myddareungi.domain.member.dto.MemberUpdateRequestDto;
import com.metanet.myddareungi.domain.member.dto.SignupRequestDto;
import com.metanet.myddareungi.domain.member.dto.SignupResponseDto;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.service.DuplicateMemberException;
import com.metanet.myddareungi.domain.member.service.InvalidSignupRequestException;
import com.metanet.myddareungi.domain.member.service.MemberAuthService;
import com.metanet.myddareungi.domain.member.service.MemberSignupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Profile("!view")
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "회원 관련 API")
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberSignupService memberSignupService;
	private final MemberAuthService memberAuthService;

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
	
    @Operation(summary = "회원 단건 조회", description = "userId로 특정 회원 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "회원 없음")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<MemberResponseDto> getMember(
        @Parameter(description = "조회할 회원 ID", required = true)
        @PathVariable Long userId
    ) {
        Member member = memberAuthService.getMemberByUserId(userId);
        return ResponseEntity.ok(MemberResponseDto.of(member));
    }
    
    @Operation(summary = "회원 전체 조회", description = "가입된 모든 회원 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getMemberList() {
        List<MemberResponseDto> members = memberAuthService.getMemberList()
            .stream()
            .map(MemberResponseDto::of)
            .toList();
        return ResponseEntity.ok(members);
    }
	
    @Operation(summary = "회원 정보 수정", description = "userId에 해당하는 회원 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값"),
        @ApiResponse(responseCode = "404", description = "회원 없음")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateMember(
        @Parameter(description = "수정할 회원 ID", required = true)
        @PathVariable Long userId,
        @Valid @RequestBody MemberUpdateRequestDto requestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String message = extractFirstErrorMessage(bindingResult);
            return ResponseEntity.badRequest()
                .body(java.util.Map.of("message", message));
        }
        Member updatedMember = memberAuthService.updateMember(userId, requestDto);
        return ResponseEntity.ok(MemberResponseDto.of(updatedMember));
    }
    
    @Operation(summary = "회원 탈퇴", description = "userId에 해당하는 회원을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "회원 없음")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteMember(
        @Parameter(description = "삭제할 회원 ID", required = true)
        @PathVariable Long userId
    ) {
        memberAuthService.deleteMember(userId);
        return ResponseEntity.noContent().build();
    }
    
    
    
	private String extractFirstErrorMessage(BindingResult bindingResult) {
		FieldError fieldError = bindingResult.getFieldError();
		return fieldError != null ? fieldError.getDefaultMessage() : "회원가입 입력값을 확인해주세요.";
	}
}
