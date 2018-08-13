package com.casting.commonmodule.network.exceptions;

public abstract class BaseNetworkException extends Exception {

    private ENetworkExceptions mENetworkExceptions;

    private int mErrorCode = -1;
    private String mErrorMessage;

    public BaseNetworkException(ENetworkExceptions ENetworkExceptions) {
        super(ENetworkExceptions.getErrorMessage());

        setmENetworkExceptions(ENetworkExceptions);

        mErrorCode = ENetworkExceptions.getErrorCode();
    }

    public ENetworkExceptions getNetworkExceptions() {
        return mENetworkExceptions;
    }

    public void setmENetworkExceptions(ENetworkExceptions mENetworkExceptions) {
        this.mENetworkExceptions = mENetworkExceptions;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = (mENetworkExceptions.getErrorCode() + errorCode);
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }
}
