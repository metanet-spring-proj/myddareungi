package com.metanet.myddareungi.domain.member.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.repository.MemberMapper;

@Profile("!view")
@Service
public class MemberAuthService implements UserDetailsService {

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

	public UserDetails toUserDetails(Member member) {
		return User.withUsername(member.getLoginId())
			.password(member.getPassword())
			.authorities(resolveAuthority(member.getRole()))
			.build();
	}

	private Member getMember(String loginId) {
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
}
