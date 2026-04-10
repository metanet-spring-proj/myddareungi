package com.metanet.myddareungi.domain.member.service;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.metanet.myddareungi.domain.member.dto.SignupRequestDto;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.repository.MemberMapper;

@Profile("!view")
@Service
public class MemberSignupService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;

	public MemberSignupService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
		this.memberMapper = memberMapper;
		this.passwordEncoder = passwordEncoder;
	}

	public Member signUp(SignupRequestDto requestDto) {
		validateRequest(requestDto);

		String loginId = requestDto.getLoginId().trim();
		String email = requestDto.getEmail().trim();

		if (memberMapper.findByLoginId(loginId) != null) {
			throw new DuplicateMemberException("이미 사용 중인 아이디입니다.");
		}

		if (memberMapper.findByEmail(email) != null) {
			throw new DuplicateMemberException("이미 사용 중인 이메일입니다.");
		}

		Member member = new Member();
		member.setUserName(requestDto.getUserName().trim());
		member.setLoginId(loginId);
		member.setEmail(email);
		member.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		member.setRole("USER");
		member.setQuestion(requestDto.getQuestion().trim());
		member.setAnswer(requestDto.getAnswer().trim());

		memberMapper.insertMember(member);

		Member createdMember = memberMapper.findByLoginId(loginId);
		if (createdMember == null) {
			throw new IllegalStateException("회원가입 이후 사용자 정보를 찾을 수 없습니다.");
		}

		return createdMember;
	}

	private void validateRequest(SignupRequestDto requestDto) {
		if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
			throw new InvalidSignupRequestException("비밀번호 확인이 일치하지 않습니다.");
		}

		if (!requestDto.isAgreeToTerms()) {
			throw new InvalidSignupRequestException("이용 약관과 개인정보 처리방침 동의가 필요합니다.");
		}
	}
}
