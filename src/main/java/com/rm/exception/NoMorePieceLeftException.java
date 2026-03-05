package com.rm.exception;

public class NoMorePieceLeftException extends BusinessException{
    public NoMorePieceLeftException() {
        super(PieceError.NOMORE_PIECE_ISLEFT);
    }
}
