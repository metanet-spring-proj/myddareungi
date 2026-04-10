package com.metanet.myddareungi.domain.member.service;

import com.metanet.myddareungi.config.CustomUserDetails;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.metanet.myddareungi.common.exception.member.MemberNotFoundException;
import com.metanet.myddareungi.domain.member.dto.MemberUpdateRequestDto;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.repository.MemberMapper;

@Profile("!view")
@Service
public class MemberAuthService implements UserDetailsService, IMemberService {

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;

	public MemberAuthService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
		this.memberMapper = memberMapper;
		this.passwordEncoder = passwordEncoder;
	}

	public Member authenticate(String loginId, String rawPassword) {
		Member member = getMember(loginId);
		if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}
		return member;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return toUserDetails(getMember(username));
	}

	public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
		Member member = memberMapper.getMemberByUserId(userId);
		if (member == null) {
			throw new UsernameNotFoundException("존재하지 않는 회원 식별자입니다.");
		}
		return toUserDetails(member);
	}

	public UserDetails toUserDetails(Member member) {
		return new CustomUserDetails(
			member.getUserId(),
			member.getLoginId(),
			member.getPassword(),
			resolveAuthority(member.getRole())
		);
	}

	public Member getMember(String loginId) {
		Member member = memberMapper.findByLoginId(loginId);
		if (member == null) {
			throw new UsernameNotFoundException("존재하지 않는 로그인 아이디입니다.");
		}
		return member;
	}

	private List<GrantedAuthority> resolveAuthority(String role) {
		String authority = role;
		if (authority == null || authority.isBlank()) {
			authority = "ROLE_USER";
		} else if (!authority.startsWith("ROLE_")) {
			authority = "ROLE_" + authority;
		}
		return List.of(new SimpleGrantedAuthority(authority));
	}
	


	//전체 회원 조회
	@Override
	public List<Member> getMemberList() {
        return memberMapper.getMemberList();

	}

	// 회원 정보 수정
	@Override
	public Member updateMember(Long userId, MemberUpdateRequestDto dto) {
	    Member member = memberMapper.getMemberByUserId(userId);
	    if (member == null) {
	        throw new MemberNotFoundException(userId);
	    }

	    Member updatedMember = Member.builder()
	        .userId(userId)
	        .userName(dto.getUserName())
	        .email(dto.getEmail())
	        .build();

	    memberMapper.updateMember(updatedMember);
	    return memberMapper.getMemberByUserId(userId);
	}
	//회원 삭제(탈퇴)
	@Override
	public void deleteMember(Long userId) {
		Member member = memberMapper.getMemberByUserId(userId);
        if (member == null) {
            throw new MemberNotFoundException(userId);
        }
        memberMapper.deleteMember(userId);
	}

	@Override
	public Member getMemberByUserId(Long userId) {
		Member member = memberMapper.getMemberByUserId(userId);
        if (member == null) {
            throw new MemberNotFoundException(userId);
        }
        return member;
	}


}
