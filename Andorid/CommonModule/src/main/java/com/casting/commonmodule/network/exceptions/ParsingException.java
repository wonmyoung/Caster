package com.casting.commonmodule.network.exceptions;

public class ParsingException extends NetworkException {

    public ParsingException() {
        super(NetworkExceptionEnum.PARSING_ERROR);
    }
}
