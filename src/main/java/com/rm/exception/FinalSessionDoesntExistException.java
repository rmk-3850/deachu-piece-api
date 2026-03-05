package com.rm.exception;

public class FinalSessionDoesntExistException extends BusinessException{
    public FinalSessionDoesntExistException() {
        super(PieceError.FINALSESSION_NOTFOUND);
    }
}
