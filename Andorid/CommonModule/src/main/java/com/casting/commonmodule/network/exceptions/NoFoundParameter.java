package com.casting.commonmodule.network.exceptions;

public class NoFoundParameter extends NetworkException {

    public NoFoundParameter() {
        super(NetworkExceptionEnum.NOFOUND_PARAMETER);
    }
}
