package com.metanet.myddareungi.domain.member.service;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.metanet.myddareungi.domain.member.dto.PasswordQuestionRequestDto;
import com.metanet.myddareungi.domain.member.dto.PasswordResetRequestDto;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.repository.MemberMapper;

@Profile("!view")
@Service
public class MemberPasswordRecoveryService {

	private static final String GENERIC_FAILURE_MESSAGE = "입력한 정보가 일치하지 않습니다.";

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;

	public MemberPasswordRecoveryService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
		this.memberMapper = memberMapper;
		this.passwordEncoder = passwordEncoder;
	}

	public String loadQuestion(PasswordQuestionRequestDto requestDto) {
		Member member = findVerifiedMember(requestDto.getLoginId(), requestDto.getEmail());
		return member.getQuestion();
	}

	public void resetPassword(PasswordResetRequestDto requestDto) {
		if (!requestDto.getNewPassword().equals(requestDto.getNewPasswordConfirm())) {
			throw new PasswordRecoveryException("새 비밀번호 확인이 일치하지 않습니다.");
		}

		Member member = findVerifiedMember(requestDto.getLoginId(), requestDto.getEmail());
		String storedAnswer = member.getAnswer() == null ? "" : member.getAnswer().trim();
		String requestAnswer = requestDto.getAnswer() == null ? "" : requestDto.getAnswer().trim();

		if (!storedAnswer.equals(requestAnswer)) {
			throw new PasswordRecoveryException(GENERIC_FAILURE_MESSAGE);
		}

		memberMapper.updatePasswordByUserId(
			member.getUserId(),
			passwordEncoder.encode(requestDto.getNewPassword())
		);
	}

	private Member findVerifiedMember(String loginId, String email) {
		Member member = memberMapper.findByLoginIdAndEmail(loginId.trim(), email.trim());
		if (member == null) {
			throw new PasswordRecoveryException(GENERIC_FAILURE_MESSAGE);
		}
		return member;
	}
}
