package com.metanet.myddareungi.domain.member.repository;

import org.apache.ibatis.annotations.Mapper;

import com.metanet.myddareungi.domain.member.model.Member;

@Mapper
public interface MemberMapper {

	Member findByLoginId(String loginId);

	int countUsers();

	int insertMember(Member member);
}
