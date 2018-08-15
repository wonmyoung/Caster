package com.casting.commonmodule.network.base;

public interface ResponseParcelable<R> {

    void parse(R r);
}
