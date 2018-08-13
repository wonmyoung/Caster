package com.casting.commonmodule.view;

import java.util.Observable;

public class BaseObservable extends Observable {

    private int  mObservableType;

    public BaseObservable(int observableType) {
        mObservableType = observableType;
    }

    public int getType() {
        return mObservableType;
    }

    public void setType(int observableType) {
        mObservableType = observableType;
    }
}
