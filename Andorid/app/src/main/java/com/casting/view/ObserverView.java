package com.casting.view;

import android.view.View;

import java.util.Observable;
import java.util.Observer;

public abstract class ObserverView<V extends View> implements Observer {

    private V v;

    public ObserverView(V root)
    {
        v = root;
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
