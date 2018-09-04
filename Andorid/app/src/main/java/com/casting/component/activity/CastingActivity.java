package com.casting.component.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.casting.model.NewsGroup;
import com.casting.model.TimeLineGroup;
import com.casting.model.request.RequestDetailedCast;
import com.casting.view.ItemViewAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

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

        View head = new View(this);
        head.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.dp235)));

        View foot = new View(this);
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

    }

    @Override
    public void bindBodyItemView
            (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
    {

        switch (viewType)
        {
            case GRAPH_LINE:
                LineChart lineChart = holder.find(R.id.lineChart);

                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(1, 1));
                entries.add(new Entry(2, 2));
                entries.add(new Entry(3, 0));
                entries.add(new Entry(4, 4));
                entries.add(new Entry(5, 3));

                LineDataSet lineDataSet = new LineDataSet(entries, "속성명1");
                lineDataSet.setLineWidth(2);
                lineDataSet.setCircleRadius(6);
                lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
                lineDataSet.setCircleColorHole(Color.BLUE);
                lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
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
        if (commonItems == null) {
            commonItems = new ArrayList<>();
        }

        NewsGroup newsGroup = new NewsGroup();
        commonItems.add(newsGroup);

        TimeLineGroup timeLineGroup = new TimeLineGroup();
        commonItems.add(timeLineGroup);

        LineGraphItem lineGraphItem = new LineGraphItem();
        commonItems.add(lineGraphItem);

        CurrentCastingStatus currentCastingStatus = new CurrentCastingStatus();
        commonItems.add(currentCastingStatus);
    }
}
