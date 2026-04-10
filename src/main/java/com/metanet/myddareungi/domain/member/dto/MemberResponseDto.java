
package com.metanet.myddareungi.domain.member.dto;

import java.time.LocalDateTime;

import com.metanet.myddareungi.domain.member.model.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {

	private Long userId;
	private String loginId;
	private String name;
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static MemberResponseDto of(Member member) {
		MemberResponseDto dto = new MemberResponseDto();
		dto.userId = member.getUserId();
		dto.loginId = member.getLoginId();
		dto.name = member.getUserName();
		dto.email = member.getEmail();
		dto.createdAt = member.getCreatedAt();
		dto.updatedAt = member.getUpdatedAt();
		return dto;
	}
	
}