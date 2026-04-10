package com.metanet.myddareungi.domain.member.service;

public class DuplicateMemberException extends RuntimeException {

	public DuplicateMemberException(String message) {
		super(message);
	}
}
