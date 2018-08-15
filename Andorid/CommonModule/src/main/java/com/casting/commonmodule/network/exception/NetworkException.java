package com.casting.commonmodule.network.exception;

public class NetworkException extends Exception {

    private NetworkExceptionEnum    mExceptionEnum;
    private int                     mErrorCode;
    private String                  mErrorMessage;

    public NetworkException(NetworkExceptionEnum exceptionEnum)
    {
        mExceptionEnum = exceptionEnum;
    }

    public NetworkException(){}

    public NetworkExceptionEnum getExceptionEnum() {
        return mExceptionEnum;
    }

    public void setExceptionEnum(NetworkExceptionEnum exceptionEnum) {
        mExceptionEnum = exceptionEnum;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }
}
