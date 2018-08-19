package com.casting.component.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.casting.R;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.menudrawer.MenuDrawer;
import com.casting.commonmodule.view.menudrawer.Position;

public class MainActivity extends BaseFCActivity implements MenuDrawer.OnDrawerStateChangeListener
{

    private MenuDrawer  mMenuDrawer;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        int menuDrawerWidth = (UtilityUI.getWidthOfScreen(this) / 10) * 9;

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setContentView(R.layout.layout_main_activity);
        mMenuDrawer.setMenuView(R.layout.layout_menu_left_drawer);
        mMenuDrawer.setMenuSize(menuDrawerWidth);
        mMenuDrawer.setOnDrawerStateChangeListener(this);
        mMenuDrawer.setDropShadowSize(2);
        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
    }

    @Override
    protected void onClickEvent(View v)
    {

    }

    @Override
    public void onDrawerStateChange(int oldState, int newState)
    {

    }

    @Override
    public void onDrawerSlide(float openRatio, int offsetPixels)
    {

    }
}
