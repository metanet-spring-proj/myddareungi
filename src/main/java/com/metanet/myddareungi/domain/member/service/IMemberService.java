package com.metanet.myddareungi.domain.member.service;

import java.util.List;

import com.metanet.myddareungi.domain.member.dto.MemberUpdateRequestDto;
import com.metanet.myddareungi.domain.member.model.Member;

public interface IMemberService {
	Member getMemberByUserId(Long userId);
	List<Member> getMemberList();
	Member updateMember(Long userId, MemberUpdateRequestDto dto);
	void deleteMember(Long userId);
}
