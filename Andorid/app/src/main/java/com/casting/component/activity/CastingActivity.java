package com.casting.component.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CommonRecyclerView;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Cast;
import com.casting.model.CurrentCastingStatus;
import com.casting.model.LineGraphItem;
import com.casting.model.News;
import com.casting.model.TimeLineGroup;
import com.casting.model.request.RequestDetailedCast;
import com.casting.view.ItemViewAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

public class CastingActivity extends BaseFCActivity implements ItemBindStrategy, IResponseListener {


    private ImageView    mCastImage;
    private TextView     mCastTitle;

    private CommonRecyclerView      mItemListView;
    private ItemViewAdapter         mItemViewAdapter;
    private LinearLayoutManager     mItemLayoutManager;

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_casting);

        mCastImage = find(R.id.castCardBack);
        mCastTitle = find(R.id.castCardTitle);

        View head = new View(CastingActivity.this);
        head.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.dp235)));

        View foot = new View(CastingActivity.this);
        foot.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.dp200)));
        mItemViewAdapter = new ItemViewAdapter(this, this);
        mItemViewAdapter.setHeaderView(head);
        mItemViewAdapter.setFooterView(foot);
        mItemListView = find(R.id.castingItemListView);
        mItemListView.setAdapter(mItemViewAdapter);
        mItemLayoutManager = new LinearLayoutManager(this);
        mItemListView.setLayoutManager(mItemLayoutManager);

        Cast cast = (Cast) getIntent().getSerializableExtra(CAST);

        if (cast != null)
        {
            String thumbNailPath = cast.getThumbnails()[0];

            EasyLog.LogMessage(this, "++ confirm getCastId = ", cast.getCastId());
            EasyLog.LogMessage(this, "++ confirm thumbNailPath = ", thumbNailPath);

            int radius = (int) getResources().getDimension(R.dimen.dp25);

            ImageLoader.loadRoundImage(this, mCastImage, cast.getThumbnails()[0], radius);

            mCastTitle.setText(cast.getTitle());

            RequestDetailedCast requestDetailedCast = new RequestDetailedCast();
            requestDetailedCast.setResponseListener(this);
            requestDetailedCast.setCast(cast);

            RequestHandler.getInstance().request(requestDetailedCast);
        }
    }

    @Override
    protected void onClickEvent(View v)
    {
        int position = (int) v.getTag(R.id.position);

        ICommonItem item = mItemViewAdapter.getItem(position);

        switch (item.getItemType())
        {
            case NEWS:
            {
                break;
            }

            case GRAPH_LINE:
            {
                break;
            }
        }
    }

    @Override
    public void bindBodyItemView
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {
        holder.itemView.setTag(R.id.position, position);
        holder.itemView.setOnClickListener(this);

        switch (viewType)
        {
            case NEWS:
            {
                News news = (News) item;

                TextView textView1 = holder.find(R.id.cardItemTitle);
                textView1.setText(news.getNewsTitle());

                TextView textView2 = holder.find(R.id.newsHighlightText);
                textView2.setText(news.getNews());

                TextView textView3 = holder.find(R.id.newsTime);
                textView3.setText(news.getNewsTime());
                break;
            }

            case GRAPH_LINE:
            {
                final LineGraphItem lineGraphItem = (LineGraphItem) item;

                LineChart lineChart = holder.find(R.id.lineChart);

                LineDataSet lineDataSet = new LineDataSet(lineGraphItem.getPointEntries(), "속성명1");
                lineDataSet.setLineWidth(1);
                lineDataSet.setCircleRadius(2);
                lineDataSet.setCircleColor(Color.parseColor("#FFD6E8F5"));
                lineDataSet.setCircleColorHole(Color.BLUE);
                lineDataSet.setColor(Color.parseColor("#FFD6E8F5"));
                lineDataSet.setDrawFilled(true);

                if (Utils.getSDKInt() >= 18)
                {
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.shape_line_graph_file_gradient);

                    lineDataSet.setFillDrawable(drawable);
                }
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setDrawCircleHole(true);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setDrawHorizontalHighlightIndicator(false);
                lineDataSet.setDrawHighlightIndicators(false);
                lineDataSet.setDrawValues(false);

                LineData lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(Color.BLACK);
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float v, AxisBase axisBase)
                    {
                        return lineGraphItem.getEntrieName((int) v);
                    }
                });
                xAxis.enableGridDashedLine(8, 24, 0);

                YAxis yLAxis = lineChart.getAxisLeft();
                yLAxis.setTextColor(Color.BLACK);

                YAxis yRAxis = lineChart.getAxisRight();
                yRAxis.setDrawLabels(false);
                yRAxis.setDrawAxisLine(false);
                yRAxis.setDrawGridLines(false);

                Description description = new Description();
                description.setText("");

                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.setDescription(description);
                lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
                lineChart.invalidate();
                break;
            }

        }
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest request = response.getSourceRequest();

        if (request.isRight(RequestDetailedCast.class))
        {
            // TODO

            ArrayList<ICommonItem> itemArrayList = new ArrayList<>();

            loadDummyItemList(itemArrayList);

            mItemViewAdapter.setItemList(itemArrayList);
            mItemViewAdapter.notifyDataSetChanged();
        }
    }

    private void loadDummyItemList(ArrayList<ICommonItem> commonItems)
    {
        News news = new News();
        news.setNewsTitle("주요 뉴스");
        news.setNews("미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다. 미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.");
        news.setNewsTime("1시간전");
        commonItems.add(news);

        TimeLineGroup timeLineGroup = new TimeLineGroup();
        commonItems.add(timeLineGroup);

        LineGraphItem lineGraphItem = new LineGraphItem();
        lineGraphItem.addPointEntrie("2017-12-20" , 400);
        lineGraphItem.addPointEntrie("2018-01-15" , 450);
        lineGraphItem.addPointEntrie("2018-02-10" , 800);
        lineGraphItem.addPointEntrie("2018-03-12" , 1200);
        lineGraphItem.addPointEntrie("2018-04-15" , 2500);
        lineGraphItem.addPointEntrie("2018-05-11" , 1600);
        lineGraphItem.addPointEntrie("2018-06-11" , 800);
        commonItems.add(lineGraphItem);

        CurrentCastingStatus currentCastingStatus = new CurrentCastingStatus();
        commonItems.add(currentCastingStatus);
    }
}
