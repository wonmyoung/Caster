package com.casting.view;

import android.view.View;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * 데코레이터 이면서 동시에 옵저버인 추상 객체
 *
 * @param <V>
 */
public abstract class ObserverView<V extends View> implements Observer {

    protected V mRoot;

    public ObserverView(V root)
    {
        mRoot = root;
    }

    @Override
    public final void update(Observable observable, Object o)
    {
        try
        {
            updateView(observable, o);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected abstract void updateView(Observable observable, Object o) throws Exception;
}
