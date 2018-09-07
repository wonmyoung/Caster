package com.casting.component.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CommonRecyclerView;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Cast;
import com.casting.model.CastingStatus;
import com.casting.model.LineGraphItem;
import com.casting.model.Member;
import com.casting.model.News;
import com.casting.model.Reply;
import com.casting.model.TimeLine;
import com.casting.model.TimeLineGroup;
import com.casting.model.global.ActiveMember;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestDetailedCast;
import com.casting.model.request.RequestNewsList;
import com.casting.model.request.RequestReplyList;
import com.casting.model.request.RequestTimeLineList;
import com.casting.view.ItemViewAdapter;
import com.casting.view.ObserverView;
import com.casting.view.insert.TrustGraph;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static com.casting.component.activity.CastingActivity.PageMode.CAST_INFO;
import static com.casting.component.activity.CastingActivity.PageMode.CAST_DONE;

public class CastingActivity extends BaseFCActivity implements ItemBindStrategy, IResponseListener, Observer {

    enum PageMode
    {
        CAST_INFO,                  // 1 Depth 캐스트 화면 상세

        NEWS_LIST,

        TIME_LINE_LIST,
        TIME_LINE_WRITE,
        TIME_LINE_REPLY_LIST,

        CAST_AS_ESSAY,
        CAST_AS_CHOICE,
        CAST_AS_TWO_CHOICE,
        CAST_DONE;
    }

    private class PageCurrentMode extends Observable
    {
        private PageMode    mPageMode = CAST_INFO;

        public void setPageMode(PageMode mode)
        {
            this.mPageMode = mode;

            setChanged();
            notifyObservers();
        }
    }

    private class ItemInsert implements ICommonItem, ItemConstant
    {
        private int mItemType;

        @Override
        public int getItemType() {
            return mItemType;
        }

        public void setItemType(int type)
        {
            this.mItemType = type;
        }
    }


    private ImageView    mCastImage;
    private TextView     mCastTitle;
    private TextView     mCastTopButton;
    private Button       mCastButton;
    private EditText     mTextInsertView;

    private CommonRecyclerView      mItemListView;
    private ItemViewAdapter         mItemViewAdapter;
    private LinearLayoutManager     mItemLayoutManager;

    private RecyclerView.OnScrollListener mScrollKeyboardFocus = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
            {
                if (mTextInsertView != null)
                {
                    UtilityUI.setForceKeyboardDown(CastingActivity.this, mTextInsertView);
                }
            }
        }
    };

    private Cast        mTargetCast;

    private PageCurrentMode     mPageCurrentMode = new PageCurrentMode();

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_casting);

        mCastImage = find(R.id.castCardBack);
        mCastTitle = find(R.id.castCardTitle);

        mCastTopButton = find(R.id.castCardTopButton);
        mCastTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onClickEventTopButton();
            }
        });

        mCastButton = find(R.id.castButton);
        mCastButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickEventCastButton();
            }
        });

        View head = new View(CastingActivity.this);
        head.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.dp225)));

        View foot = new View(CastingActivity.this);
        foot.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.dp200)));
        mItemViewAdapter = new ItemViewAdapter(this, this);
        mItemViewAdapter.setHeaderView(head);
        mItemViewAdapter.setFooterView(foot);

        mItemLayoutManager = new LinearLayoutManager(this);
        mItemListView = find(R.id.castingItemListView);
        mItemListView.setAdapter(mItemViewAdapter);
        mItemListView.setLayoutManager(mItemLayoutManager);

        mTargetCast = (Cast) getIntent().getSerializableExtra(CAST);

        if (mTargetCast != null)
        {
            mPageCurrentMode.addObserver(this);
            mPageCurrentMode.setPageMode((mTargetCast.isDone() ? CAST_DONE : CAST_INFO));

            String thumbNailPath = mTargetCast.getThumbnails()[0];

            EasyLog.LogMessage(this, "++ confirm getCastId = ", mTargetCast.getCastId());
            EasyLog.LogMessage(this, "++ confirm thumbNailPath = ", thumbNailPath);

            int radius = (int) getResources().getDimension(R.dimen.dp25);

            ImageLoader.loadRoundImage(this, mCastImage, mTargetCast.getThumbnails()[0], radius);

            mCastTitle.setText(mTargetCast.getTitle());

            RequestDetailedCast requestDetailedCast = new RequestDetailedCast();
            requestDetailedCast.setResponseListener(this);
            requestDetailedCast.setCast(mTargetCast);

            RequestHandler.getInstance().request(requestDetailedCast);
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

            case TIME_LINE:
            {
                Button button1 = holder.find(R.id.replyThisTimeLine);
                button1.setTag(R.id.position, position);
                button1.setOnClickListener(this);
                button1.setVisibility((mPageCurrentMode.mPageMode == PageMode.TIME_LINE_LIST ? View.VISIBLE : View.GONE));

                Button button2 = holder.find(R.id.shareTimeLineButton);
                button2.setTag(R.id.position, position);
                button2.setOnClickListener(this);
                break;
            }

            case TIME_LINE_WRITE:
            {
                Member member = ActiveMember.getInstance().getMember();

                ImageView imageView = holder.find(R.id.userImage);
                TextView textView1 = holder.find(R.id.userNickName);
                TextView textView2 = holder.find(R.id.userId);

                if (member != null)
                {
                    ImageLoader.loadImage(this, imageView, member.getUserPicThumbnail());

                    textView1.setText(member.getNickName());
                    textView2.setText(member.getUserId());
                }

                mTextInsertView = holder.find(R.id.userTimeLineInsert);
                break;
            }

            case TIME_LINE_REPLY_WRITE:
            {
                mTextInsertView = holder.find(R.id.userTimeLineInsert);

                Button button = holder.find(R.id.replyWriteButton);
                button.setTag(R.id.position, position);
                button.setOnClickListener(this);
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

            case CURRENT_CASTING_STATUS:
            {
                CastingStatus currentStatus = (CastingStatus) item;

                for (int i = 1 ; i <= 5 ; i++)
                {
                    CastingStatus.CastingOption castingOption = currentStatus.getCastingOption((i - 1));

                    String packageName = getPackageName();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("castingSelection");
                    stringBuilder.append(i);

                    String textViewId = stringBuilder.toString();

                    stringBuilder.append("Percent");

                    String percentageViewId = stringBuilder.toString();

                    Resources res = getResources();

                    int id0 = res.getIdentifier(textViewId, "id", packageName);
                    int id1 = res.getIdentifier(percentageViewId, "id", packageName);

                    TextView textView = holder.find(id0);

                    View percentageView = holder.find(id1);

                    if (castingOption != null)
                    {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(castingOption.Name);

                        int totalWidth = (int) res.getDimension(R.dimen.dp200);
                        int percentage = castingOption.getPercentage();

                        int percentageWidth = (totalWidth * percentage) / 100;

                        EasyLog.LogMessage("++ confirm percentageWidth = " + percentageWidth);

                        int drawableId = (i == 1 ?
                                R.drawable.shape_main_color_round15 :
                                R.drawable.shape_gray_color_round15);
                        UtilityUI.setBackGroundDrawable(percentageView, drawableId);

                        percentageView.getLayoutParams().width = percentageWidth;
                        percentageView.requestLayout();
                    }
                    else
                    {
                        textView.setVisibility(View.GONE);

                        percentageView.setVisibility(View.GONE);
                    }
                }
                break;
            }

            case INSERT_TRUST_GRAPH:
            {
                TrustGraph trustGraph = holder.find(R.id.cardItemTrustGraph);

                TextView textView = holder.find(R.id.cardItemTrustText);

                ObserverView<TextView> observerView = new ObserverView<TextView>(textView) {
                    @Override
                    protected void updateView(Observable observable, Object o) throws Exception
                    {
                        if (o instanceof TrustGraph.Point)
                        {
                            TrustGraph.Point point = (TrustGraph.Point) o;

                            Object value = point.getValue();

                            if (value instanceof Integer)
                            {
                                int n = (int) value;

                                mRoot.setText(Integer.toString(n));
                            }
                        }
                    }
                };
                trustGraph.addObserver(observerView);

                int size = trustGraph.getPointArrayListSize();
                if (size == 0)
                {
                    trustGraph.addPoint("가망없음", 0);
                    trustGraph.addPoint("조금 헷갈려요", 25);
                    trustGraph.addPoint("하프 앤 하프", 50);
                    trustGraph.addPoint("거의 틀림 없어요", 75);
                    trustGraph.addPoint("올인", 100);
                    trustGraph.setDefaultSelected();
                }
                break;
            }
        }
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if (observable instanceof PageCurrentMode)
        {
            switch (mPageCurrentMode.mPageMode)
            {
                case CAST_INFO:
                    mCastButton.setVisibility(View.VISIBLE);
                    mCastButton.setText("캐스트 하기");

                    mCastTopButton.setVisibility(View.GONE);
                    break;

                case NEWS_LIST:
                    mCastButton.setVisibility(View.GONE);

                    mCastTopButton.setVisibility(View.GONE);
                    break;

                case TIME_LINE_LIST:
                    mCastButton.setVisibility(View.GONE);

                    mCastTopButton.setVisibility(View.VISIBLE);
                    mCastTopButton.setText("글쓰기");
                    break;

                case TIME_LINE_WRITE:
                    mCastButton.setVisibility(View.GONE);

                    mCastTopButton.setVisibility(View.VISIBLE);
                    mCastTopButton.setText("완료");
                    break;

                case CAST_AS_CHOICE:
                    mCastButton.setVisibility(View.VISIBLE);
                    mCastButton.setText("캐스트 완료");

                    mCastTopButton.setVisibility(View.GONE);
                    break;

                case CAST_AS_ESSAY:
                    mCastButton.setVisibility(View.VISIBLE);
                    mCastButton.setText("캐스트 완료");

                    mCastTopButton.setVisibility(View.GONE);
                    break;

                case CAST_AS_TWO_CHOICE:
                    mCastButton.setVisibility(View.VISIBLE);
                    mCastButton.setText("캐스트 완료");

                    mCastTopButton.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    protected void onClickEvent(View v)
    {
        int position = (int) v.getTag(R.id.position);

        ICommonItem item = mItemViewAdapter.getItem(position);

        int itemType = (item != null ? item.getItemType() : -1);

        switch (itemType)
        {
            case NEWS:
            {
                News news = (News) item;

                onClickEventNews(v, news);
                break;
            }

            case TIME_LINE_GROUP:
            {
                TimeLineGroup timeLineGroup = (TimeLineGroup) item;

                onClickEventTimeLineGroup(v, timeLineGroup);
                break;
            }

            case TIME_LINE:
            {
                TimeLine timeLine = (TimeLine) item;

                onClickEventTimeLine(v, timeLine);
                break;
            }
        }
    }

    private void onClickEventTopButton()
    {
        switch (mPageCurrentMode.mPageMode)
        {
            case TIME_LINE_LIST:
                mPageCurrentMode.setPageMode(PageMode.TIME_LINE_WRITE);

                mItemViewAdapter.clear();
                mItemViewAdapter.notifyDataSetChanged();

                ItemInsert itemInsert = new ItemInsert();
                itemInsert.setItemType(ItemConstant.TIME_LINE_WRITE);
                mItemViewAdapter.addItem(itemInsert);
                mItemViewAdapter.notifyDataSetChanged();
                break;

            case TIME_LINE_WRITE:
                break;
        }
    }

    private void onClickEventCastButton()
    {
        if (mTargetCast != null)
        {
            Cast.Type type = mTargetCast.getCastType();

            if (type != null)
            {
                mItemViewAdapter.clear();
                mItemViewAdapter.notifyDataSetChanged();

                switch (type)
                {
                    case CHOICE:
                        mPageCurrentMode.setPageMode(PageMode.CAST_AS_CHOICE);
                        break;

                    case TWO_CHOICE:
                        mPageCurrentMode.setPageMode(PageMode.CAST_AS_TWO_CHOICE);
                        break;

                    case ESSAY:
                        mPageCurrentMode.setPageMode(PageMode.CAST_AS_ESSAY);

                        ItemInsert itemInsert = new ItemInsert();
                        itemInsert.setItemType(INSERT_TRUST_GRAPH);

                        mItemViewAdapter.addItem(itemInsert);
                        mItemViewAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    private void onClickEventNews(View v, News news)
    {
        if (mPageCurrentMode.mPageMode == PageMode.CAST_INFO)
        {
            String castId = (mTargetCast == null ? null : mTargetCast.getCastId());

            if (TextUtils.isEmpty(castId))
            {
                mPageCurrentMode.setPageMode(PageMode.NEWS_LIST);

                mItemViewAdapter.clear();
                mItemViewAdapter.notifyDataSetChanged();

                RequestNewsList requestNewsList = new RequestNewsList();
                requestNewsList.setResponseListener(this);
                requestNewsList.setCastId(castId);

                RequestHandler.getInstance().request(requestNewsList);
            }
        }
    }

    private void onClickEventTimeLineGroup(View v, TimeLineGroup timeLineGroup)
    {
        mPageCurrentMode.setPageMode(PageMode.TIME_LINE_LIST);

        mItemViewAdapter.clear();
        mItemViewAdapter.notifyDataSetChanged();

        RequestTimeLineList requestTimeLineList = new RequestTimeLineList();
        requestTimeLineList.setCast(mTargetCast);
        requestTimeLineList.setResponseListener(this);

        RequestHandler.getInstance().request(requestTimeLineList);
    }

    private void onClickEventTimeLine(View v, TimeLine timeLine)
    {
        switch (v.getId())
        {
            case R.id.replyThisTimeLine:
            {
                if (mPageCurrentMode.mPageMode == PageMode.TIME_LINE_LIST)
                {
                    mPageCurrentMode.setPageMode(PageMode.TIME_LINE_REPLY_LIST);

                    mItemViewAdapter.clear();
                    mItemViewAdapter.notifyDataSetChanged();

                    RequestReplyList requestReplyList = new RequestReplyList();
                    requestReplyList.setResponseListener(this);
                    requestReplyList.setTargetTimeLine(timeLine);

                    RequestHandler.getInstance().request(requestReplyList);
                }
                break;
            }

            case R.id.shareTimeLineButton:
            {
                break;
            }
        }
    }

    @Override
    public void onBackPressed()
    {

        switch (mPageCurrentMode.mPageMode)
        {
            case CAST_INFO:
            {
                finish();
                break;
            }

            case TIME_LINE_LIST:
            case NEWS_LIST:
            {
                mPageCurrentMode.setPageMode(CAST_INFO);

                mItemViewAdapter.clear();
                mItemViewAdapter.notifyDataSetChanged();

                RequestDetailedCast requestDetailedCast = new RequestDetailedCast();
                requestDetailedCast.setResponseListener(this);
                requestDetailedCast.setCast(mTargetCast);

                RequestHandler.getInstance().request(requestDetailedCast);
                break;
            }

            case TIME_LINE_WRITE:
            case TIME_LINE_REPLY_LIST:
            {
                mPageCurrentMode.setPageMode(PageMode.TIME_LINE_LIST);

                // mItemListView.removeOnScrollListener(mScrollKeyboardFocus);

                mItemViewAdapter.clear();
                mItemViewAdapter.notifyDataSetChanged();

                RequestTimeLineList requestTimeLineList = new RequestTimeLineList();
                requestTimeLineList.setCast(mTargetCast);
                requestTimeLineList.setResponseListener(this);

                RequestHandler.getInstance().request(requestTimeLineList);
                break;
            }

            case CAST_AS_CHOICE:
            case CAST_AS_ESSAY:
            case CAST_AS_TWO_CHOICE:
                mPageCurrentMode.setPageMode(CAST_INFO);

                mItemViewAdapter.clear();
                mItemViewAdapter.notifyDataSetChanged();

                RequestDetailedCast requestDetailedCast = new RequestDetailedCast();
                requestDetailedCast.setResponseListener(this);
                requestDetailedCast.setCast(mTargetCast);

                RequestHandler.getInstance().request(requestDetailedCast);
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
        else if (request.isRight(RequestNewsList.class))
        {

            ArrayList<ICommonItem> itemArrayList = new ArrayList<>();

            loadDummyNewsList(itemArrayList);

            mItemViewAdapter.setItemList(itemArrayList);
            mItemViewAdapter.notifyDataSetChanged();
        }
        else if (request.isRight(RequestTimeLineList.class))
        {
            ArrayList<ICommonItem> itemArrayList = new ArrayList<>();

            loadDummyTimeLineList(itemArrayList);

            mItemViewAdapter.setItemList(itemArrayList);
            mItemViewAdapter.notifyDataSetChanged();
        }
        else if (request.isRight(RequestReplyList.class))
        {
            RequestReplyList requestReplyList = (RequestReplyList) request;

            ArrayList<ICommonItem> itemArrayList = new ArrayList<>();

            loadDummyReplyList(itemArrayList);

            itemArrayList.add(0, requestReplyList.getTargetTimeLine());

            ItemInsert itemInsert = new ItemInsert();
            itemInsert.setItemType(TIME_LINE_REPLY_WRITE);

            itemArrayList.add(1, itemInsert);

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

        CastingStatus castingStatus = new CastingStatus();
        castingStatus.addStatus("한화 이글스", 80);
        castingStatus.addStatus("SK", 60);
        castingStatus.addStatus("두산 베어스", 20);
        castingStatus.addStatus("롯데 자이언츠", 10);
        castingStatus.addStatus("기아 타이거즈", 5);
        commonItems.add(castingStatus);
    }

    private void loadDummyNewsList(ArrayList<ICommonItem> commonItems)
    {
        for (int i = 0 ; i < 100 ; i++)
        {
            News news = new News();
            news.setNewsTitle("주요 뉴스");
            news.setNews("미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다. 미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.");
            news.setNewsTime("1시간전");

            commonItems.add(news);
        }
    }

    private void loadDummyTimeLineList(ArrayList<ICommonItem> commonItems)
    {
        for (int i = 0 ; i < 100 ; i++)
        {
            TimeLine timeLine = new TimeLine();

            commonItems.add(timeLine);
        }
    }

    private void loadDummyReplyList(ArrayList<ICommonItem> commonItems)
    {
        for (int i = 0 ; i < 100 ; i++)
        {
            Reply reply = new Reply();

            commonItems.add(reply);
        }
    }
}
