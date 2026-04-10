package com.metanet.myddareungi.domain.member.service;

public class InvalidSignupRequestException extends RuntimeException {

	public InvalidSignupRequestException(String message) {
		super(message);
	}
}
