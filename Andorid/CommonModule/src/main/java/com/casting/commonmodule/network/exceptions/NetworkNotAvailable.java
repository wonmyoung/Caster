package com.casting.commonmodule.network.exceptions;

public class NetworkNotAvailable extends BaseNetworkException {

    public NetworkNotAvailable() {
        super(ENetworkExceptions.NETWORK_NOT_AVAILABLE);
    }
}
