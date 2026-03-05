package com.rm.exception;
import org.springframework.http.HttpStatus;

public enum PieceSuccess implements SuccessCode{
	SUCCESS(HttpStatus.ACCEPTED,"S001","정상 처리됐습니다.");
	private final HttpStatus status;
	private final String code;
	private final String msg;
	
    PieceSuccess(HttpStatus status, String code, String msg) {
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
