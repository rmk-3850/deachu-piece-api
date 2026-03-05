package com.rm.piece;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalStatus {
	PENDING("A01","승인 대기"),APPROVED("A02","승인 완료"),DENIED("A03","승인 거절"),REVOKED("A04","승인 박탈");
	
	private final String code;
	private final String msg;
}
