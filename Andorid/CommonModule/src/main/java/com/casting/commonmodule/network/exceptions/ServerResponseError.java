package com.casting.commonmodule.network.exceptions;

public class ServerResponseError extends NetworkException {

    public ServerResponseError() {
        super(NetworkExceptionEnum.SERVER_REPONSE_ERROR);
    }
}
