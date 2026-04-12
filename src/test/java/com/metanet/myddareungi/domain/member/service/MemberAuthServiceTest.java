package com.metanet.myddareungi.domain.member.service;

import com.metanet.myddareungi.common.exception.member.MemberNotFoundException;
import com.metanet.myddareungi.domain.member.dto.MemberUpdateRequestDto;
import com.metanet.myddareungi.domain.member.model.Member;
import com.metanet.myddareungi.domain.member.repository.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberAuthServiceTest {

    @Mock
    MemberMapper memberMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberAuthService memberAuthService;

    // ── getMemberByUserId() ───────────────────
    @Test
    @DisplayName("getMemberByUserId() - 존재하는 userId → Member 반환")
    void getMemberByUserId_정상반환() {
        // given
        Member fakeMember = Member.builder()
                .userId(1L)
                .loginId("testUser")
                .userName("홍길동")
                .build();
        when(memberMapper.getMemberByUserId(1L)).thenReturn(fakeMember);

        // when
        Member result = memberAuthService.getMemberByUserId(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getLoginId()).isEqualTo("testUser");
    }

    @Test
    @DisplayName("getMemberByUserId() - 존재하지 않는 userId → MemberNotFoundException 발생")
    void getMemberByUserId_없는아이디_예외발생() {
        // given
        when(memberMapper.getMemberByUserId(999L)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> memberAuthService.getMemberByUserId(999L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("존재하지 않는 회원입니다");
    }

    // ── getMemberList() ───────────────────────
    @Test
    @DisplayName("getMemberList() - 전체 회원 목록을 반환한다")
    void getMemberList_목록반환() {
        // given
        List<Member> fakeList = List.of(
                Member.builder().userId(1L).loginId("user1").build(),
                Member.builder().userId(2L).loginId("user2").build()
        );
        when(memberMapper.getMemberList()).thenReturn(fakeList);

        // when
        List<Member> result = memberAuthService.getMemberList();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLoginId()).isEqualTo("user1");
        assertThat(result.get(1).getLoginId()).isEqualTo("user2");
    }

    // ── updateMember() ────────────────────────
    @Test
    @DisplayName("updateMember() - 정상적으로 수정 후 수정된 Member를 반환한다")
    void updateMember_정상수정() {
        // given
        Long userId = 1L;
        Member existingMember = Member.builder()
                .userId(userId)
                .loginId("user1")
                .userName("홍길동")
                .email("old@test.com")
                .build();

        MemberUpdateRequestDto dto = new MemberUpdateRequestDto();
        dto.setUserName("김철수");
        dto.setEmail("new@test.com");

        Member updatedMember = Member.builder()
                .userId(userId)
                .loginId("user1")
                .userName("김철수")
                .email("new@test.com")
                .build();

        when(memberMapper.getMemberByUserId(userId)).thenReturn(existingMember);
        when(memberMapper.getMemberByUserId(userId)).thenReturn(existingMember)
                .thenReturn(updatedMember); // 두 번째 호출(수정 후 조회) 시 수정된 값 반환

        // when
        Member result = memberAuthService.updateMember(userId, dto);

        // then
        assertThat(result.getUserName()).isEqualTo("김철수");
        assertThat(result.getEmail()).isEqualTo("new@test.com");
        verify(memberMapper).updateMember(any(Member.class));
    }

    @Test
    @DisplayName("updateMember() - 존재하지 않는 userId → MemberNotFoundException 발생")
    void updateMember_없는아이디_예외발생() {
        // given
        when(memberMapper.getMemberByUserId(999L)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> memberAuthService.updateMember(999L, new MemberUpdateRequestDto()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    // ── deleteMember() ────────────────────────
    @Test
    @DisplayName("deleteMember() - 정상적으로 memberMapper.deleteMember()가 호출된다")
    void deleteMember_정상삭제() {
        // given
        Member fakeMember = Member.builder().userId(1L).build();
        when(memberMapper.getMemberByUserId(1L)).thenReturn(fakeMember);

        // when
        memberAuthService.deleteMember(1L);

        // then
        verify(memberMapper, times(1)).deleteMember(1L);
    }

    @Test
    @DisplayName("deleteMember() - 존재하지 않는 userId → MemberNotFoundException 발생")
    void deleteMember_없는아이디_예외발생() {
        // given
        when(memberMapper.getMemberByUserId(999L)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> memberAuthService.deleteMember(999L))
                .isInstanceOf(MemberNotFoundException.class);
    }
}