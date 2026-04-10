package com.metanet.myddareungi.common.exception.member;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long userId) {
        super("존재하지 않는 회원입니다. ID: " + userId);
    }
}
