package com.rm.exception;

public class PreliminarySessionDoesntExistException extends BusinessException{
    public PreliminarySessionDoesntExistException() {
        super(PieceError.PRELIMINARYSESSION_NOTFOUND);
    }
}
