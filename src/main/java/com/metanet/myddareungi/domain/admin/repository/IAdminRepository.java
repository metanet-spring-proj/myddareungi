package com.metanet.myddareungi.domain.admin.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.metanet.myddareungi.domain.member.model.Member;

@Mapper
public interface IAdminRepository {
    // 검토 대기 수 조회
    int countPendingFiles();

    // 금일 업로드 수 조회
    int countTodayUploads();

    // 관리자 정보 조회 (JWT 미사용 시 직접 조회용)
    Member getAdminInfo(@Param("userId") long userId);
}
