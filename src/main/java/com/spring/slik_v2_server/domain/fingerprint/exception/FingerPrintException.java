package com.spring.slik_v2_server.domain.fingerprint.exception;

import lombok.Getter;

@Getter
public class FingerPrintException extends RuntimeException {

	private final FingerPrintErrorCode fingerPrintErrorCode;

	public FingerPrintException(FingerPrintErrorCode fingerPrintErrorCode) {
		super(fingerPrintErrorCode.getMessage());
		this.fingerPrintErrorCode = fingerPrintErrorCode;
	}

	public FingerPrintException(String message, FingerPrintErrorCode fingerPrintErrorCode) {
		super(message);
		this.fingerPrintErrorCode = fingerPrintErrorCode;
	}
}
