package com.casting.commonmodule.view.component;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casting.commonmodule.utility.EasyLog;

public abstract class CommonFragment extends Fragment implements View.OnClickListener {

    protected View              mFragmentRootLayout;

    protected int               mFragmentRootLayoutId;

    /**
     * 연속 클릭 이벤트 방지 맵.
     */
    private SparseArray mPreviousClickEvent = new SparseArray();

    public CommonFragment(int resourceId) {
        mFragmentRootLayoutId = resourceId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EasyLog.LogMessage(">> onAttach "+this.getClass().getSimpleName()+" ");
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater,
                             ViewGroup container, Bundle savedInstanceState) {

        EasyLog.LogMessage(">> onCreateView "+this.getClass().getSimpleName()+" ");

        mFragmentRootLayout = layoutInflater.inflate(mFragmentRootLayoutId , null);

        try
        {
            init(layoutInflater, container, savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return mFragmentRootLayout;
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

        Activity a = getActivity();

        if (a instanceof View.OnClickListener)
        {
            View.OnClickListener clickListener = (View.OnClickListener) a;

            clickListener.onClick(v);
        }

        onClickEvent(v);
    }

    protected abstract void onClickEvent(View v);

    protected abstract void init
            (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws Exception;

    protected abstract boolean onBackPressed();

    @SuppressWarnings("unchecked")
    protected <V extends View> V find(int id)
    {
        return (mFragmentRootLayout != null ? (V) mFragmentRootLayout.findViewById(id) : null);
    }

    protected void finish()
    {
        Activity a = getActivity();

        if (a != null && !a.isFinishing())
        {
            if (a instanceof CommonActivity)
            {
                CommonActivity commonActivity = (CommonActivity) a;
                commonActivity.popBackFragmentStack(this.getClass().getSimpleName());
            }
            else
            {
                a.onBackPressed();
            }
        }
    }
}
