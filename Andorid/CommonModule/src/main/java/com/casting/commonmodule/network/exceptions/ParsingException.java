package com.casting.commonmodule.network.exceptions;

public class ParsingException extends BaseNetworkException {

    public ParsingException() {
        super(ENetworkExceptions.PARSING_ERROR);
    }
}
