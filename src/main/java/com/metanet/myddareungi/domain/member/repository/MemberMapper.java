package com.metanet.myddareungi.domain.member.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.metanet.myddareungi.domain.member.model.Member;

@Mapper
public interface MemberMapper {

	Member findByLoginId(@Param("loginId") String loginId);

	Member findByEmail(@Param("email") String email);

	Member findByLoginIdAndEmail(@Param("loginId") String loginId, @Param("email") String email);

	int insertMember(Member member);
	
	//회원 단건 조회
    Member getMemberByUserId(@Param("userId") Long userId);
    
    //회원 전체 조회 
    List<Member> getMemberList();
    
    //회원 정보 수정
    int updateMember(Member member);
    
    //회원 삭제(탈퇴)
    int deleteMember(@Param("userId") Long userId);  


	int updatePasswordByUserId(@Param("userId") Long userId, @Param("password") String password);

    //전체 관리자 조회
    List<Member> getAdminMemberList();
}
