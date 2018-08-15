package com.casting.commonmodule.network.exceptions;

public abstract class NetworkException extends Exception {

    private NetworkExceptionEnum mExceptionEnum;
    private String mErrorMessage;

    public NetworkException(NetworkExceptionEnum exceptionEnum) {
        mExceptionEnum = exceptionEnum;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.mErrorMessage = mErrorMessage;
    }
}
