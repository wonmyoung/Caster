package com.casting.component.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.cardstack.SwipeStack;
import com.casting.commonmodule.view.cardstack.SwipeStackAdapter;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.menudrawer.MenuDrawer;
import com.casting.commonmodule.view.menudrawer.Position;
import com.casting.component.fragment.LeftSideMenu;
import com.casting.component.fragment.RightSideMenu;
import com.casting.model.Cast;
import com.casting.model.CastList;
import com.casting.model.global.ActiveMember;
import com.casting.model.request.RequestCastList;

import java.util.ArrayList;

public class MainActivity extends BaseFCActivity implements
        MenuDrawer.OnDrawerStateChangeListener, SeekBar.OnSeekBarChangeListener, TabLayout.OnTabSelectedListener, IResponseListener
{

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

    private class MainCardSwipeAdapter extends SwipeStackAdapter<Cast>
    {

        public MainCardSwipeAdapter()
        {
            super(MainActivity.this, R.layout.view_item_cast_card);
        }

        @Override
        protected void bindItemDataView
                (CompositeViewHolder viewHolder, int position, Cast item)
        {
            Context c = getBaseContext();

            ImageView imageView = viewHolder.find(R.id.castCardBack);

            int radius = (int) c.getResources().getDimension(R.dimen.dp25);

            ImageLoader.loadRoundImage(c, imageView, item.getThumbnails()[0], radius);
        }
    }

    private BothMenuDrawer mBothMenuDrawer;

    private Button  mTopButton1;
    private Button  mTopButton2;

    private TabLayout   mTabLayout;

    private SwipeStack  mSwipeStack;
    private SeekBar     mMainSeekBar;
    private MainCardSwipeAdapter mSwipeStackAdapter;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        int menuDrawerWidth = (UtilityUI.getWidthOfScreen(this) / 10) * 9;

        mBothMenuDrawer = new BothMenuDrawer();
        mBothMenuDrawer.attach(this);
        mBothMenuDrawer.setContentView(R.layout.activity_main);
        mBothMenuDrawer.setLeftMenuView(R.layout.left_menu_frame, menuDrawerWidth);
        mBothMenuDrawer.setRightMenuView(R.layout.right_menu_frame, menuDrawerWidth);
        mBothMenuDrawer.setDrawerListener(this);

        addFragmentStack(R.id.left_menu_frame, new LeftSideMenu());
        addFragmentStack(R.id.right_menu_frame, new RightSideMenu());

        mTopButton1 = find(R.id.main_TopMenuButton1);
        mTopButton1.setOnClickListener(this);
        mTopButton2 = find(R.id.main_TopMenuButton2);
        mTopButton2.setOnClickListener(this);

        mTabLayout = find(R.id.main_TabLayout);
        mTabLayout.addOnTabSelectedListener(this);

        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setText("참여가능 캐스트");

        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setText("BEST 10 캐스트");

        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab3.setText("최고보상 캐스트");

        mTabLayout.addTab(tab1);
        mTabLayout.addTab(tab2);
        mTabLayout.addTab(tab3);

        mSwipeStackAdapter = new MainCardSwipeAdapter();
        mSwipeStack = find(R.id.main_SwipeCardStack);
        mSwipeStack.setAdapter(mSwipeStackAdapter);

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

    @SuppressWarnings("unchecked")
    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest baseRequest = response.getSourceRequest();

        if (baseRequest instanceof RequestCastList)
        {
            try
            {
                onCastListResponse(response);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void onCastListResponse(BaseResponse<CastList> response) throws Exception
    {
        int responseCode = response.getResponseCode();
        if (responseCode > 0)
        {
            CastList castList = response.getResponseModel();

            mSwipeStackAdapter.setItemList(castList.getCastList());
            mSwipeStackAdapter.notifyDataSetChanged();
        }
        else
        {
            //TODO 에러 처리 정의 필요
            // 더미 데이터 로드
            ArrayList<Cast> castList = new ArrayList<>();

            loadDummyCastList(castList);

            mSwipeStackAdapter.setItemList(castList);
            mSwipeStackAdapter.notifyDataSetChanged();
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if (fromUser)
        {
            int cardCount = (mSwipeStackAdapter.getCount() - 1);
            int selectedCard = (cardCount * progress) / 100;
            EasyLog.LogMessage("confirm" , "selectedCard = ", Integer.toString(selectedCard));
        }
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
        RequestCastList requestCastList = new RequestCastList();
        requestCastList.setEmailAddress(ActiveMember.getInstance().getEmailAddress());
        requestCastList.setResponseListener(this);

        switch (tab.getPosition())
        {
            case 0:
            {
                requestCastList.setOrder(RequestCastList.Order.Available);
                break;
            }

            case 1:
            {
                requestCastList.setOrder(RequestCastList.Order.Popular);
                requestCastList.setSize(10);
                break;
            }

            case 2:
            {
                requestCastList.setOrder(RequestCastList.Order.RewardBig);
                break;
            }
        }

        RequestHandler.getInstance().request(requestCastList);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {
        mSwipeStack.resetStack();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {

    }

    private void loadDummyCastList(ArrayList<Cast> list)
    {
        if (list == null) {
            list = new ArrayList<>();
        }

        for (int i = 0 ; i < 100 ; i++)
        {
            Cast cast = new Cast();
            cast.setRemainingTime(60 * 60 * 1000);
            cast.setRewardCash(20000);
            cast.setTitle("비트코인의 7월 25일 지정가격은 얼마일까요 ?");
            cast.setTags("비트코인", "더미데이터", "바톤컴퍼니", "가즈아!!");
            cast.setThumbnails(
                    "http://1.bp.blogspot.com/-suPZ9GdewYU/WjL2nodqGpI/AAAAAAABlZY/MgopnrYkJyQHGPnjnhp2ynzoz11h0PTHgCK4BGAYYCw/s1600/960x0.jpg");

            list.add(cast);
        }
    }
}
