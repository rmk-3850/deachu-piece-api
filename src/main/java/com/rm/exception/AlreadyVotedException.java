package com.rm.exception;

public class AlreadyVotedException extends BusinessException{
    public AlreadyVotedException() {
        super(PieceError.ALREADY_VOTED);
    }
}
