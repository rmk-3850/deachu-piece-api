package com.rm.exception;

public class PieceNotFoundException extends BusinessException{
    public PieceNotFoundException() {
        super(PieceError.PIECE_NOT_FOUND);
    }
}
