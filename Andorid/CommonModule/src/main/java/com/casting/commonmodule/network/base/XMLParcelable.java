package com.casting.commonmodule.network.base;

public interface XMLParcelable extends ResponseParcelable<String> {

    @Override
    void parse(String s);
}
