package com.rm.exception;
import org.springframework.http.HttpStatus;

public enum PieceError implements ErrorCode{
    SYNCHRONIZATION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR,"E103","유튜브 동기화에 실패했습니다."),
    NOMORE_PIECE_ISLEFT(HttpStatus.INTERNAL_SERVER_ERROR,"E104","영상의 개수가 부족합니다."),
    FINALSESSION_NOTFOUND(HttpStatus.NOT_FOUND,"E105","진행된 본투표가 없습니다."),
    PRELIMINARYSESSION_NOTFOUND(HttpStatus.NOT_FOUND,"E106","진행된 예비투표가 없습니다."),
    ALREADY_VOTED(HttpStatus.ALREADY_REPORTED,"E107","이미 투표했습니다.");
	private final HttpStatus status;
	private final String code;
	private final String msg;
	
    PieceError(HttpStatus status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
