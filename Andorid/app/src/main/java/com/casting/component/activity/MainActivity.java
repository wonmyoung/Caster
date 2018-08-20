package com.casting.component.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.casting.R;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.cardstack.SwipeStack;
import com.casting.commonmodule.view.cardstack.SwipeStackAdapter;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.menudrawer.MenuDrawer;
import com.casting.commonmodule.view.menudrawer.Position;
import com.casting.model.Cast;

public class MainActivity extends BaseFCActivity implements MenuDrawer.OnDrawerStateChangeListener, SeekBar.OnSeekBarChangeListener, TabLayout.OnTabSelectedListener {

    private class BothMenuDrawer
    {
        private MenuDrawer  mLMenuDrawer;
        private MenuDrawer  mRMenuDrawer;

        void attach(Activity activity)
        {
            mLMenuDrawer = MenuDrawer.attach(activity, MenuDrawer.Type.OVERLAY, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
            mLMenuDrawer.setDropShadowSize(2);
            mLMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
            mRMenuDrawer = MenuDrawer.attach(activity, MenuDrawer.Type.OVERLAY, Position.RIGHT, MenuDrawer.MENU_DRAG_WINDOW);
            mRMenuDrawer.setDropShadowSize(2);
            mRMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        }

        void setContentView(int layoutId)
        {
            mLMenuDrawer.setContentView(layoutId);
            mRMenuDrawer.setContentView(layoutId);
        }

        void setLeftMenuView(int layoutId, int width)
        {
            mLMenuDrawer.setMenuView(layoutId);
            mLMenuDrawer.setMenuSize(width);
        }

        void setRightMenuView(int layoutId, int width)
        {
            mRMenuDrawer.setMenuView(layoutId);
            mRMenuDrawer.setMenuSize(width);
        }

        void setDrawerListener(MenuDrawer.OnDrawerStateChangeListener drawerListener)
        {
            mLMenuDrawer.setOnDrawerStateChangeListener(drawerListener);
            mRMenuDrawer.setOnDrawerStateChangeListener(drawerListener);
        }
    }

    private class MainCardSwipeAdapter extends SwipeStackAdapter<Cast> {

        public MainCardSwipeAdapter() {
            super(MainActivity.this, R.layout.main_cast_card);
        }

        @Override
        protected void bindItemDataView
                (CompositeViewHolder viewHolder, int position, Cast item)
        {

        }
    }

    private BothMenuDrawer mBothMenuDrawer;

    private Button  mTopButton1;
    private Button  mTopButton2;

    private TabLayout   mTabLayout;

    private SwipeStack  mSwipeStack;
    private SeekBar     mMainSeekBar;
    private MainCardSwipeAdapter    mMainCardSwipeAdapter;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        int menuDrawerWidth = (UtilityUI.getWidthOfScreen(this) / 10) * 9;

        mBothMenuDrawer = new BothMenuDrawer();
        mBothMenuDrawer.attach(this);
        mBothMenuDrawer.setContentView(R.layout.layout_main_activity);
        mBothMenuDrawer.setLeftMenuView(R.layout.layout_menu_left_drawer, menuDrawerWidth);
        mBothMenuDrawer.setRightMenuView(R.layout.layout_menu_right_drawer, menuDrawerWidth);
        mBothMenuDrawer.setDrawerListener(this);

        mTopButton1 = find(R.id.main_TopMenuButton1);
        mTopButton1.setOnClickListener(this);
        mTopButton2 = find(R.id.main_TopMenuButton2);
        mTopButton2.setOnClickListener(this);

        mTabLayout = find(R.id.main_TabLayout);
        mTabLayout.addOnTabSelectedListener(this);

        mMainCardSwipeAdapter = new MainCardSwipeAdapter();
        mSwipeStack = find(R.id.main_SwipeCardStack);
        mSwipeStack.setAdapter(mMainCardSwipeAdapter);

        mMainSeekBar = find(R.id.main_seekBarController);
        mMainSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void onClickEvent(View v)
    {
        if (v.equals(mTopButton1))
        {
            mBothMenuDrawer.mLMenuDrawer.openMenu();
        }
        else if (v.equals(mTopButton2))
        {
            mBothMenuDrawer.mRMenuDrawer.openMenu();
        }
    }

    @Override
    public void onDrawerStateChange(int oldState, int newState)
    {

    }

    @Override
    public void onDrawerSlide(float openRatio, int offsetPixels)
    {

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {

    }
}
