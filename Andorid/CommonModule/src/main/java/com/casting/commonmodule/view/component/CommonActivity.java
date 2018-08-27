package com.casting.commonmodule.view.component;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;

import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.commonmodule.utility.UtilityUI;

import java.util.List;

public abstract class CommonActivity extends AppCompatActivity implements View.OnClickListener, FragmentManager.OnBackStackChangedListener {

    /** ONE vod M OS에서 체크해야 할 퍼미션의 종류 */
	/* READ_EXTERNAL_STORAGE (API 16) STORAGE 그룹이다. */
    public static final String[] PERMISSIONS_NECESSARY = {
            Manifest.permission.RECORD_AUDIO, // STORAGE 그룹
            Manifest.permission.WRITE_EXTERNAL_STORAGE, // STORAGE 그룹
            Manifest.permission.READ_PHONE_STATE, // PHONE 그룹
    };

    /**
     * 연속 클릭 이벤트 방지 맵.
     */
    private SparseArray mPreviousClickEvent = new SparseArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        EasyLog.LogMessage(this, ">> onCreate ");

        try
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.removeOnBackStackChangedListener(this);
            fragmentManager.addOnBackStackChangedListener(this);

            init(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);

        EasyLog.LogMessage(this, ">> onCreate ");

        try
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.removeOnBackStackChangedListener(this);
            fragmentManager.addOnBackStackChangedListener(this);

            init(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected abstract void init(Bundle savedInstanceState) throws Exception;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            Fragment fragment = getVisibleTopFragment();

            if (fragment != null &&
                fragment instanceof CommonFragment)
            {
                CommonFragment commonFragment = (CommonFragment) fragment;

                if (!commonFragment.onBackPressed())
                {
                    return false;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public final void onBackStackChanged()
    {
        EasyLog.LogMessage(this, ">> onBackStackChanged ");

        Fragment fragment = getVisibleTopFragment();

        onFragmentVisible(fragment);
    }

    protected void onFragmentVisible(Fragment fragment)
    {
        String name = (fragment == null ? null : fragment.getClass().getSimpleName());

        EasyLog.LogMessage(this, ">> onFragmentVisible ", name);
    }

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

    protected <F extends CommonFragment> void popBackFragmentStack(F f)
    {
        if (f != null)
        {
            popBackFragmentStack(f.getClass().getSimpleName());
        }
    }

    protected void popBackFragmentStack()
    {
        getSupportFragmentManager().popBackStack();
    }

    protected void popBackFragmentStack(String tag)
    {
        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected int getVisibleTopFragmentCount()
    {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        return (fragmentList == null ? 0 : fragmentList.size());
    }

    protected Fragment getVisibleTopFragment()
    {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        if (fragmentList != null)
        {
            for (Fragment fragment: getSupportFragmentManager().getFragments())
            {
                if (fragment != null &&
                    fragment.isVisible())
                {
                    return fragment;
                }
            }
        }

        return null;
    }

    protected String[] getDeniedPermission() {

        if (UtilityData.isBelowMOS()) {
			/* M OS 미만 버전이면 모든 권한은 허용 상태이다 */
            return null;
        }

		/* Dangerous Permission 목록을 구한다. */
        if (null == PERMISSIONS_NECESSARY || 0 == PERMISSIONS_NECESSARY.length) {
			/* Dangerous Permission 이 존재하지 않는다. */
            return null;
        }

		/* 거부된 Permission 구한다. */
        StringBuilder oDeniedPermisssionStringBuilder = new StringBuilder();
        try
        {
            for (String permission : PERMISSIONS_NECESSARY) {

                if (!UtilityData.hasSelfPermission(this, permission)) {

                    if (0 < oDeniedPermisssionStringBuilder.length())
                    {
                        oDeniedPermisssionStringBuilder.append("/");
                    }

                    oDeniedPermisssionStringBuilder.append(permission);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

		/* 거부된 Permission 모음 문자열을 스트링배열로 변환한다. */
        String[] aResultPermission = null;

        if (0 < oDeniedPermisssionStringBuilder.length()) {
            try {
                aResultPermission = oDeniedPermisssionStringBuilder.toString().split("/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return aResultPermission;
    }
}
