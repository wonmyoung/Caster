package com.casting.component.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.component.CommonActivity;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CommonRecyclerView;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Ranking;
import com.casting.model.RankingList;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestRankingList;
import com.casting.view.CustomTabLayout;
import com.casting.view.ItemViewAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class RankingListActivity extends CommonActivity implements ItemBindStrategy, IResponseListener, TabLayout.OnTabSelectedListener {

    private CustomTabLayout     mCustomTabLayout;

    private CommonRecyclerView  mListView;
    private ItemViewAdapter     mListViewAdapter;
    private LinearLayoutManager mListViewManager;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_cast_chart);

        mCustomTabLayout = find(R.id.rankingListTab);
        mCustomTabLayout.addOnTabSelectedListener(this);
        mCustomTabLayout.addTextTab("적중율");
        mCustomTabLayout.addTextTab("리워드");

        mListViewAdapter = new ItemViewAdapter(this, this);
        mListView = find(R.id.castChartListView);
        mListView.setAdapter(mListViewAdapter);
        mListViewManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mListViewManager);
        mListView.setHasFixedSize(true);
    }

    @Override
    public void bindBodyItemView
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {
        switch (viewType)
        {
            case RANKING:

                Ranking ranking = (Ranking) item;

                String level = ranking.getLevel();
                level = (!TextUtils.isEmpty(level) ? level.trim() : null);

                String userName = ranking.getUserName();
                String avatar = ranking.getAvatar();
                int hitRatio = ranking.getHitRatio();
                int reward = ranking.getReward();


                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" ");
                stringBuilder.append(avatar);

                CircleImageView circleImageView = holder.find(R.id.rankingUserImage);

                int radius = (int) getResources().getDimension(R.dimen.dp25);

                ImageLoader.loadRoundImage(this, circleImageView, stringBuilder.toString(), radius);

                TextView textView1 = holder.find(R.id.rankingIndex);
                textView1.setText(String.format(Locale.KOREA, "%d", (position + 1)));

                TextView textView2 = holder.find(R.id.rankingName);

                if (TextUtils.isEmpty(userName))
                {
                    int color = UtilityUI.getColor(this, R.color.color_999999);

                    SpannableString spannableString = new SpannableString("이름 없음");
                    spannableString.setSpan(new ForegroundColorSpan(color),0,5,0);
                    spannableString.setSpan(new RelativeSizeSpan(0.8f),0,5,0);
                    textView2.setText(spannableString);
                }
                else
                {
                    textView2.setText(userName);
                }


                TextView textView3 = holder.find(R.id.userGrade);

                if (TextUtils.isEmpty(level))
                {
                    textView3.setVisibility(View.GONE);
                }
                else if (TextUtils.isDigitsOnly(level))
                {
                    textView3.setVisibility(View.GONE);
                }
                else
                {
                    textView3.setText(level);
                }

                TextView textView4 = holder.find(R.id.userCastCorrectRate);

                if (hitRatio > -1)
                {
                    textView4.setText(String.format(Locale.KOREA, "%d", hitRatio));
                }
                else
                {
                    int color = UtilityUI.getColor(this, R.color.color_999999);

                    SpannableString spannableString = new SpannableString("값 없음");
                    spannableString.setSpan(new ForegroundColorSpan(color),0,4,0);
                    spannableString.setSpan(new RelativeSizeSpan(0.8f),0,4,0);
                    textView4.setText(spannableString);
                }

                TextView textView5 = holder.find(R.id.userCash);

                if (reward > -1)
                {
                    textView5.setText(String.format(Locale.KOREA, "%d", reward));
                }
                else
                {
                    int color = UtilityUI.getColor(this, R.color.color_999999);

                    SpannableString spannableString = new SpannableString("값 없음");
                    spannableString.setSpan(new ForegroundColorSpan(color),0,4,0);
                    spannableString.setSpan(new RelativeSizeSpan(0.8f),0,4,0);
                    textView5.setText(spannableString);
                }
                break;
        }
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        EasyLog.LogMessage(this, ">> onThreadResponseListen ");
        EasyLog.LogMessage(this, ">> onThreadResponseListen request = " + response.getSourceRequest().getClass().getSimpleName());
        EasyLog.LogMessage(this, ">> onThreadResponseListen response = " + response.getClass().getSimpleName());

        RankingList rankingList = (RankingList) response.getResponseModel();

        int size = (rankingList == null ? 0 : rankingList.getListSize());
        if (size > 0)
        {
            mListViewAdapter.setItemList(rankingList.getRankingList());
            mListViewAdapter.notifyDataSetChanged();
        }

        EasyLog.LogMessage(this, "-- onThreadResponseListen size = " + size);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab)
    {
        RequestRankingList requestRankingList = new RequestRankingList();
        requestRankingList.setResponseListener(this);

        int position = tab.getPosition();
        if (position == 0)
        {
            // TODO 정렬기준값 없음
        }
        else if (position == 1)
        {
            // TODO 정렬기준값 없음
        }

        RequestHandler.getInstance().request(requestRankingList);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab)
    {
        mListViewAdapter.clear();
        mListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab)
    {

    }
}
