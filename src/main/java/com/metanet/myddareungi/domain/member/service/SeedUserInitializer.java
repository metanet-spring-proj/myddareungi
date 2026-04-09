package com.metanet.myddareungi.domain.member.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.metanet.myddareungi.config.SeedUserProperties;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.repository.MemberMapper;

@Profile("!view")
@Component
public class SeedUserInitializer implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(SeedUserInitializer.class);

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	private final SeedUserProperties seedUserProperties;

	public SeedUserInitializer(
		MemberMapper memberMapper,
		PasswordEncoder passwordEncoder,
		SeedUserProperties seedUserProperties
	) {
		this.memberMapper = memberMapper;
		this.passwordEncoder = passwordEncoder;
		this.seedUserProperties = seedUserProperties;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (!seedUserProperties.isReady()) {
			log.warn("개발용 시드 계정 설정이 비어 있어 USERS 초기 계정 생성을 건너뜁니다.");
			return;
		}

		if (memberMapper.countUsers() > 0) {
			return;
		}

		Member seedMember = new Member();
		seedMember.setLoginId(seedUserProperties.getLoginId());
		seedMember.setPassword(passwordEncoder.encode(seedUserProperties.getPassword()));
		seedMember.setUserName(seedUserProperties.getUserName());
		seedMember.setEmail(seedUserProperties.getEmail());
		seedMember.setRole(seedUserProperties.getRole());
		seedMember.setQuestion(seedUserProperties.getQuestion());
		seedMember.setAnswer(seedUserProperties.getAnswer());

		memberMapper.insertMember(seedMember);
		log.info("개발용 로그인 계정을 USERS 테이블에 생성했습니다. loginId={}", seedMember.getLoginId());
	}
}
