package com.casting.component.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.cardstack.SwipeStack;
import com.casting.commonmodule.view.cardstack.SwipeStackAdapter;
import com.casting.commonmodule.view.cardstack.SwipeStackController;
import com.casting.commonmodule.view.cardstack.SwipeStackListener;
import com.casting.commonmodule.view.cardstack.SwipeViewClickListener;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.menudrawer.MenuDrawer;
import com.casting.commonmodule.view.menudrawer.Position;
import com.casting.component.fragment.MainLeftSideMenu;
import com.casting.component.fragment.MainRightSideMenu;
import com.casting.model.Cast;
import com.casting.model.CastList;
import com.casting.model.global.ActiveMember;
import com.casting.model.request.RequestCastList;
import com.casting.view.CustomTabLayout;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends BaseFCActivity implements
        MenuDrawer.OnDrawerStateChangeListener, SeekBar.OnSeekBarChangeListener, TabLayout.OnTabSelectedListener, IResponseListener, SwipeStackListener {

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

    private class CardSwipeAdapter extends SwipeStackAdapter<Cast> implements SwipeViewClickListener {

        public CardSwipeAdapter()
        {
            super(MainActivity.this, R.layout.view_item_cast_card);
        }

        @Override
        protected void bindItemDataView
                (CompositeViewHolder viewHolder, int position, Cast item)
        {
            viewHolder.itemView.setTag(R.id.position, position);

            Context c = getBaseContext();

            ImageView imageView = viewHolder.find(R.id.castCardBack);

            String thumbNailPath = (item.getThumbnails() != null && item.getThumbnails().length > 0 ?
                                    item.getThumbnails()[0] : null);

            int radius = (int) c.getResources().getDimension(R.dimen.dp25);

            ImageLoader.loadRoundImage(c, imageView, thumbNailPath, radius);

            TextView textView = viewHolder.find(R.id.castCardTitle);
            textView.setText(item.getTitle());
        }


        @Override
        public void onViewClick(View v)
        {
            Object o = v.getTag(R.id.position);

            EasyLog.LogMessage(this, ">> onViewClick ");
            EasyLog.LogMessage(this, "++ onViewClick tag is null ? = ", Boolean.toString((o == null)));

            if (o != null && o instanceof Integer)
            {
                int position = (int) o;

                Cast cast = getItem(position);

                Intent intent = new Intent(MainActivity.this, CastingActivity.class);
                intent.putExtra(CAST, cast);

                startActivity(intent);
            }
        }
    }

    private abstract class CardSwipe implements Runnable
    {
        public int mPage = -1;

        @Override
        public void run()
        {
            try
            {
                int currentTabPage = mTabLayout.getSelectedTabPosition();
                if (currentTabPage == mPage)
                {
                    swipe();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        protected abstract void swipe() throws Exception;
    }

    private BothMenuDrawer mBothMenuDrawer;

    private Button  mTopButton1;
    private Button  mTopButton2;

    private CustomTabLayout     mTabLayout;

    private SwipeStack  mSwipeStack;
    private SeekBar     mMainSeekBar;
    private View        mMainSeekBarCover;
    private CardSwipeAdapter mSwipeStackAdapter;

    private Queue<CardSwipe> mSwipeExecutionQueue = new LinkedList<>();

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

        addFragmentStack(R.id.left_menu_frame, new MainLeftSideMenu());
        addFragmentStack(R.id.right_menu_frame, new MainRightSideMenu());

        mTopButton1 = find(R.id.main_TopMenuButton1);
        mTopButton1.setOnClickListener(this);
        mTopButton2 = find(R.id.main_TopMenuButton2);
        mTopButton2.setOnClickListener(this);

        mTabLayout = find(R.id.main_TabLayout);
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.addTextTab("참여가능 캐스트");
        mTabLayout.addTextTab("BEST 10 캐스트");
        mTabLayout.addTextTab("최고보상 캐스트");

        mSwipeStackAdapter = new CardSwipeAdapter();
        mSwipeStack = find(R.id.main_SwipeCardStack);
        mSwipeStack.setAdapter(mSwipeStackAdapter);
        mSwipeStack.addStackListener(this);

        mMainSeekBar = find(R.id.main_seekBarController);
        mMainSeekBar.setOnSeekBarChangeListener(this);
        mMainSeekBarCover = find(R.id.main_seekBarControllerCover);
        mMainSeekBarCover.setAlpha(0.0f);
        mMainSeekBarCover.setVisibility(View.GONE);
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
        try
        {
            BaseRequest request = response.getSourceRequest();

            if (request instanceof RequestCastList)
            {
                onCastListResponse(response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        int translationY = (int) ViewHelper.getTranslationY(seekBar);
        if (translationY == 0)
        {
            int progress = seekBar.getProgress();

            int cardCount = (mSwipeStackAdapter.getCount() - 1);
            final int selectedCard = (cardCount * progress) / 100;

            int currentPosition = mSwipeStack.getCurrentPosition();
            if (currentPosition < selectedCard)
            {
                int swipeCount = (selectedCard - currentPosition);

                swipeCardStack(swipeCount);
            }
            else
            {
                int rollBackCount = (currentPosition - selectedCard);

                rollBackCardStack(rollBackCount);
            }

            mMainSeekBarCover.setClickable(true);
            mMainSeekBarCover.animate().alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationStart(Animator animator)
                        {
                            mMainSeekBarCover.setVisibility(View.VISIBLE);
                        }
                    });
        }
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
        mMainSeekBar.setProgress(0);

        mMainSeekBarCover.setClickable(false);
        mMainSeekBarCover.animate().alpha(0.0f)
                .setListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        mMainSeekBarCover.setVisibility(View.GONE);
                    }
                });

        mSwipeStackAdapter.clear();
        mSwipeStackAdapter.notifyDataSetChanged();

        mSwipeExecutionQueue.clear();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {

    }

    @Override
    public void onViewSwipedToLeft(int position)
    {

    }

    @Override
    public void onViewSwipedToRight(int position)
    {

    }

    @Override
    public void onStackEmpty()
    {
        mMainSeekBar.setProgress(0);

        mMainSeekBarCover.setClickable(false);
        mMainSeekBarCover.animate().alpha(0.0f)
                .setListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        mMainSeekBarCover.setVisibility(View.GONE);
                    }
                });

        mSwipeStack.resetStack();

        mSwipeExecutionQueue.clear();
    }

    @Override
    public void onStackTopVisible(int position)
    {
        if (mSwipeExecutionQueue.peek() != null)
        {
            CardSwipe cardSwipe = mSwipeExecutionQueue.poll();

            mSwipeStack.post(cardSwipe);

            int queueSize = mSwipeExecutionQueue.size();
            if (queueSize == 0)
            {
                mMainSeekBarCover.setClickable(false);
                mMainSeekBarCover.animate().alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter()
                        {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                mMainSeekBarCover.setVisibility(View.GONE);
                            }
                        });
            }
        }
        else
        {
            int itemCount = (mSwipeStackAdapter.getCount() - 1);

            int progress = (position * 100) / itemCount;

            mMainSeekBar.setProgress(progress);
        }
    }

    @Override
    public void onStackRollBack(int position)
    {

    }

    private void swipeCardStack(int swipeCount)
    {
        if (swipeCount > 0)
        {
            for (int i = 0 ; i < swipeCount ; i++)
            {
                CardSwipe cardSwipe = new CardSwipe()
                {
                    @Override
                    protected void swipe() throws Exception
                    {
                        SwipeStackController swipeStackController = mSwipeStack.getSwipeStackController();
                        swipeStackController.swipeView();
                    }
                };
                cardSwipe.mPage = mTabLayout.getSelectedTabPosition();

                mSwipeExecutionQueue.add(cardSwipe);
            }

            int queueSize = mSwipeExecutionQueue.size();
            if (queueSize == swipeCount)
            {
                mSwipeExecutionQueue.poll().run();
            }
        }
    }

    private void rollBackCardStack(int rollbackCount)
    {
        if (rollbackCount > 0)
        {
            for (int i = 0 ; i < rollbackCount ; i++)
            {
                CardSwipe cardSwipe = new CardSwipe()
                {
                    @Override
                    protected void swipe() throws Exception
                    {
                        SwipeStackController swipeStackController = mSwipeStack.getSwipeStackController();
                        swipeStackController.rollBack();
                    }
                };
                cardSwipe.mPage = mTabLayout.getSelectedTabPosition();

                mSwipeExecutionQueue.add(cardSwipe);
            }

            int queueSize = mSwipeExecutionQueue.size();
            if (queueSize == rollbackCount)
            {
                mSwipeExecutionQueue.poll().run();
            }
        }
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
            cast.setTitle(" 비트코인의 7월25일의 자정가격은 얼마일까요 ? [" + i + "]");
            cast.setTags("비트코인", "더미데이터", "바톤컴퍼니", "가즈아!!");
            cast.setThumbnails(
                    "http://1.bp.blogspot.com/-suPZ9GdewYU/WjL2nodqGpI/AAAAAAABlZY/MgopnrYkJyQHGPnjnhp2ynzoz11h0PTHgCK4BGAYYCw/s1600/960x0.jpg");
            cast.setCastType(Cast.Type.ESSAY);
            list.add(cast);
        }
    }
}
