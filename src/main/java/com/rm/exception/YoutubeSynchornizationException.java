package com.rm.exception;

public class YoutubeSynchornizationException extends BusinessException {

	public YoutubeSynchornizationException() {
		super(PieceError.SYNCHRONIZATION_FAILURE);
	}
}
