package com.metanet.myddareungi.domain.member.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.metanet.myddareungi.domain.member.model.Member;

@Mapper
public interface MemberMapper {

	Member findByLoginId(@Param("loginId") String loginId);

	Member findByEmail(@Param("email") String email);

	int countUsers();

	int insertMember(Member member);
}
