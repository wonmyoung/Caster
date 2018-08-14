package com.casting.commonmodule.network.exceptions;

public class NetworkNotAvailable extends NetworkException {

    public NetworkNotAvailable() {
        super(NetworkExceptionEnum.NETWORK_NOT_AVAILABLE);
    }
}
