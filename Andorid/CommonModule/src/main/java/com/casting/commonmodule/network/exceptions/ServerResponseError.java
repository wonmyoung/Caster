package com.casting.commonmodule.network.exceptions;

public class ServerResponseError extends BaseNetworkException {

    public ServerResponseError() {
        super(ENetworkExceptions.SERVER_REPONSE_ERROR);
    }
}
