package com.casting.commonmodule.network.exceptions;

public enum ENetworkExceptions {

    NETWORK_NOT_AVAILABLE("NETWORK_NOT_AVAILABLE" , -10000),
    NOFOUND_PARAMETER("NOFOUND_PARAMETER" , -20000),
    PARSING_ERROR("PARSING_ERROR" , -30000),
    SERVER_REPONSE_ERROR("SERVER_REPONSE_ERROR" , -40000);

    private int mErrorCode;
    private String mErrorMessage;

    ENetworkExceptions(String errorMessage , int errorCode) {
        mErrorMessage = errorMessage;
        mErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
