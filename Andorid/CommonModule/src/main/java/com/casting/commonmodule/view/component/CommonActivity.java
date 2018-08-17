package com.casting.commonmodule.view.component;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

public abstract class CommonActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 연속 클릭 이벤트 방지 맵.
     */
    private SparseArray mPreviousClickEvent = new SparseArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            init(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        try
        {
            init(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onClick(View v)
    {
        int viewId = v.getId();
        int previousEventTime = (int) mPreviousClickEvent.get(viewId, 0);
        if ((SystemClock.elapsedRealtime() - previousEventTime) < 700) {
            return;
        }
        mPreviousClickEvent.put(viewId, (int) SystemClock.elapsedRealtime());

        onClickEvent(v);
    }

    protected abstract void onClickEvent(View v);

    protected abstract void init(Bundle savedInstanceState) throws Exception;

    @SuppressWarnings("unchecked")
    protected <V extends View> V find(int id)
    {
        return (V) findViewById(id);
    }

    protected final void post(Runnable r)
    {
        (new Handler(getMainLooper())).post(r);
    }

    protected final void post(Runnable r, int delay)
    {
        (new Handler(getMainLooper())).postDelayed(r, delay);
    }

    protected void replaceFragment(int containerId, Fragment fragment)
    {
        try
        {
            String tag = fragment.getClass().getSimpleName();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId , fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void replaceFragment(int containerId, Fragment fragment, String tag)
    {
        try
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId , fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void addFragmentStack(int containerId, Fragment fragment)
    {
        try
        {
            String tag = fragment.getClass().getSimpleName();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(containerId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void addFragmentStack(int containerId, Fragment fragment, String tag)
    {
        try
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(containerId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
