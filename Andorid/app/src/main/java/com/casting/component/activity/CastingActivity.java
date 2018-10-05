package com.casting.component.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.casting.FutureCasting;
import com.casting.FutureCastingUtil;
import com.casting.R;
import com.casting.commonmodule.IResponseListener;
import com.casting.commonmodule.RequestHandler;
import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.model.BaseResponse;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityUI;
import com.casting.commonmodule.view.CircleImageView;
import com.casting.commonmodule.view.image.ImageLoader;
import com.casting.commonmodule.view.list.CommonRecyclerView;
import com.casting.commonmodule.view.list.CompositeViewHolder;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.interfaces.ItemBindStrategy;
import com.casting.model.Alarm;
import com.casting.model.BarChartItem;
import com.casting.model.Cast;
import com.casting.model.CastingOption;
import com.casting.model.CastingStatus;
import com.casting.model.CommonGraphItem;
import com.casting.model.NewsList;
import com.casting.model.Ranking;
import com.casting.model.RankingList;
import com.casting.model.DoublePieChartItem;
import com.casting.model.PieChartItem;
import com.casting.model.ReplyList;
import com.casting.model.TimeLineList;
import com.casting.model.request.PostCast;
import com.casting.model.request.PostReply;
import com.casting.model.request.RequestCastEnding;
import com.casting.model.request.RequestCastingOption;
import com.casting.view.CustomTabLayout;
import com.casting.view.insert.InsertOptionsBoolean;
import com.casting.view.insert.InsertOptionsScrollable;
import com.casting.view.insert.InsertOptionsVertical;
import com.casting.model.insert.ItemBooleanOption;
import com.casting.model.insert.ItemScrollableOption;
import com.casting.model.insert.ItemSelectOptions;
import com.casting.model.LineGraphItem;
import com.casting.model.Member;
import com.casting.model.News;
import com.casting.model.Reply;
import com.casting.model.TimeLine;
import com.casting.model.global.ActiveMember;
import com.casting.model.global.ItemConstant;
import com.casting.model.request.RequestCast;
import com.casting.model.request.RequestNewsList;
import com.casting.model.request.RequestReplyList;
import com.casting.model.request.RequestTimeLineList;
import com.casting.view.ItemViewAdapter;
import com.casting.view.ObserverView;
import com.casting.view.insert.InsertOptionsHorizontal;
import com.casting.model.insert.ItemInsert;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import static com.casting.component.activity.CastingActivity.PageMode.CASTING_CLOSED;
import static com.casting.component.activity.CastingActivity.PageMode.CASTING_DONE;
import static com.casting.component.activity.CastingActivity.PageMode.CAST_AS_CHOICE;
import static com.casting.component.activity.CastingActivity.PageMode.CAST_AS_ESSAY;
import static com.casting.component.activity.CastingActivity.PageMode.CAST_AS_TWO_CHOICE;
import static com.casting.component.activity.CastingActivity.PageMode.CAST_INFO;
import static com.casting.component.activity.CastingActivity.PageMode.TIME_LINE_LIST;

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
        CASTING_DONE,
        CASTING_CLOSED;
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

        public boolean isCastingMode()
        {
            return (mPageMode == CAST_AS_ESSAY ||
                    mPageMode == CAST_AS_CHOICE ||
                    mPageMode == CAST_AS_TWO_CHOICE);
        }
    }

    private ImageView       mCastImage;
    private TextView        mCastTitle;
    private TextView        mCastDescription;
    private TextView        mCastTopButton;
    private TextView        mTopTagView1;
    private TextView        mTopTagView2;
    private Button          mCastButton;
    private TextView        mTopCastingNumberView;
    private TextView        mTopCastingStatusView;

    private CommonRecyclerView      mItemListView;
    private ItemViewAdapter         mItemViewAdapter;
    private LinearLayoutManager     mItemLayoutManager;

    private Cast        mTargetCast;
    private int         mCastCardPosition;

    private PageCurrentMode     mPageCurrentMode = new PageCurrentMode();

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_casting);

        find(R.id.topLogo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        mCastImage = find(R.id.castCardBack);
        mCastImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (mPageCurrentMode.mPageMode != CAST_INFO)
                {
                    mPageCurrentMode.setPageMode(CAST_INFO);

                    mItemViewAdapter.clear();
                    mItemViewAdapter.notifyDataSetChanged();

                    RequestCast requestCast = new RequestCast();
                    requestCast.setResponseListener(CastingActivity.this);
                    requestCast.setCast(mTargetCast);

                    RequestHandler.getInstance().request(requestCast);
                }
            }
        });
        mCastTitle = find(R.id.castCardTitle);
        mCastDescription = find(R.id.castCardDescription);
        UtilityUI.addEmptyTextAsGone(mCastDescription);

        mTopTagView1 = find(R.id.castTag1);
        mTopTagView2 = find(R.id.castTag2);
        UtilityUI.addEmptyTextAsGone(mTopTagView1, mTopTagView2);
        mTopCastingNumberView = find(R.id.castTopText1);
        mTopCastingStatusView = find(R.id.castTopText2);
        UtilityUI.addEmptyTextAsGone(mTopCastingNumberView, mTopCastingStatusView);

        find(R.id.replyThisCast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mPageCurrentMode.setPageMode(PageMode.TIME_LINE_WRITE);

                ArrayList<ICommonItem> itemArrayList = new ArrayList<>();

                ItemInsert itemInsert = new ItemInsert();
                itemInsert.setItemType(ItemConstant.TIME_LINE_WRITE);
                itemArrayList.add(itemInsert);

                mItemViewAdapter.setItemList(itemArrayList);
                mItemViewAdapter.notifyDataSetChanged();
            }
        });
        find(R.id.shareThisCast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onClickCastShare();
            }
        });

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

        Intent intent = getIntent();

        Cast cast = (Cast) intent.getSerializableExtra(DEFINE_CAST);

        mCastCardPosition = intent.getIntExtra(CAST_CARD_POSITION, -1);

        if (cast != null)
        {
            updateCastInfo(cast);

            PageMode pageMode = null;

            if (FutureCastingUtil.isPast(cast.getEndDate()))
            {
                pageMode = CASTING_CLOSED;
            }
            else if (cast.isCastingDone())
            {
                pageMode = CASTING_DONE;
            }
            else
            {
                pageMode = CAST_INFO;
            }

            mPageCurrentMode.addObserver(this);
            mPageCurrentMode.setPageMode(pageMode);

            RequestCast requestCast = new RequestCast();
            requestCast.setResponseListener(this);
            requestCast.setCast(mTargetCast);

            RequestHandler.getInstance().request(requestCast);
        }
    }

    @SuppressWarnings("unchecked")
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

                TextView textView1 = holder.find(R.id.insertItemTitle);
                textView1.setText(news.getNewsTitle());

                TextView textView2 = holder.find(R.id.newsHighlightText);
                textView2.setText(news.getContents());

                TextView textView3 = holder.find(R.id.newsTime);
                textView3.setText(news.getNewsTime());
                break;
            }

            case NEWS_GROUP:
            {
                NewsList newsList = (NewsList) item;

                int size = newsList.getNewsSize();
                if (size > 0)
                {
                    News news = newsList.getNewsArrayList().get(0);

                    TextView textView1 = holder.find(R.id.insertItemTitle);
                    textView1.setText("주요 뉴스");

                    TextView textView2 = holder.find(R.id.newsHighlightText);
                    textView2.setText(news.getContents());

                    TextView textView3 = holder.find(R.id.newsTime);
                    textView3.setText(news.getNewsTime());
                }
                break;
            }

            case TIME_LINE:
            {
                TimeLine timeLine = (TimeLine) item;

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(FutureCasting.HTTP_PROTOCOL);
                stringBuilder.append(FutureCasting.SERVER_DOMAIN);
                stringBuilder.append(FutureCasting.SERVER_PORT);
                stringBuilder.append("/");
                stringBuilder.append(timeLine.getUserAvatar());

                CircleImageView circleImageView = holder.find(R.id.userImage);

                EasyLog.LogMessage(this, "++ TIME_LINE thumbNail path = ", stringBuilder.toString());

                UtilityUI.setThumbNailRoundedImageView(this, circleImageView, stringBuilder.toString(), R.dimen.dp25);

                String createdDate = timeLine.getCreated_at();
                String comments = timeLine.getComments();
                String userId = timeLine.getUserId();
                String userName = timeLine.getUserName();
                int replySize = timeLine.getReplyListSize();

                TextView textView1 = holder.find(R.id.userNickName);
                textView1.setText(userName);

                TextView textView2 = holder.find(R.id.userId);
                textView2.setText(userId);

                TextView textView3 = holder.find(R.id.userTimeLine);
                textView3.setText(comments);

                StringBuilder createdDateBuilder = new StringBuilder();
                createdDateBuilder.append(FutureCastingUtil.getTimeFormattedString(createdDate));
                createdDateBuilder.append(" 전");
                TextView textView4 = holder.find(R.id.userTimeLineTime);
                textView4.setText(createdDateBuilder);

                TextView textView5 = holder.find(R.id.replyThisTimeLineCount);

                if (mPageCurrentMode.mPageMode == TIME_LINE_LIST && replySize > 0)
                {
                    textView5.setVisibility(View.VISIBLE);
                    textView5.setText(String.format(Locale.KOREAN, "%d", replySize));
                }
                else
                {
                    textView5.setVisibility(View.GONE);
                }

                Button button1 = holder.find(R.id.replyThisTimeLine);
                button1.setTag(R.id.position, position);
                button1.setOnClickListener(this);
                button1.setVisibility((mPageCurrentMode.mPageMode == PageMode.TIME_LINE_LIST ? View.VISIBLE : View.GONE));



                Button button2 = holder.find(R.id.shareTimeLineButton);
                button2.setTag(R.id.position, position);
                button2.setOnClickListener(this);
                break;
            }

            case TIME_LINE_GROUP:
            {
                TimeLineList timeLineList = (TimeLineList) item;

                int size = timeLineList.getTimeLineListSize();
                if (size > 0)
                {
                    Resources res = getResources();

                    for (int i = 1 ; i <= 3 ; i++)
                    {
                        int lineId = res.getIdentifier("timeLine"+i, "id", getPackageName());

                        View lineView = holder.find(lineId);

                        TimeLine timeLine = timeLineList.getTimeLine((i - 1));

                        if (timeLine != null)
                        {
                            String comments = timeLine.getComments();
                            String createdDate = timeLine.getCreated_at();
                            String userAvatar = timeLine.getUserAvatar();

                            lineView.setVisibility(View.VISIBLE);

                            int textId1 = res.getIdentifier("user"+i+"TimeLine", "id", getPackageName());
                            int textId2 = res.getIdentifier("user"+i+"TimeLineTime", "id", getPackageName());

                            TextView textView1 = holder.find(textId1);
                            textView1.setText(comments);

                            StringBuilder createdDateBuilder = new StringBuilder();
                            createdDateBuilder.append(FutureCastingUtil.getTimeFormattedString(createdDate));
                            createdDateBuilder.append(" 전");
                            TextView textView2 = holder.find(textId2);
                            textView2.setText(createdDateBuilder);

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(FutureCasting.HTTP_PROTOCOL);
                            stringBuilder.append(FutureCasting.SERVER_DOMAIN);
                            stringBuilder.append(FutureCasting.SERVER_PORT);
                            stringBuilder.append("/");
                            stringBuilder.append(userAvatar);

                            EasyLog.LogMessage(this, "++ TIME_LINE_GROUP thumbNail path = ", stringBuilder.toString());

                            int imageId = res.getIdentifier("user"+i+"Image", "id", getPackageName());

                            ImageView imageView = holder.find(imageId);

                            UtilityUI.setThumbNailRoundedImageView(this, imageView, stringBuilder.toString(), R.dimen.dp25);
                        }
                        else
                        {
                            lineView.setVisibility(View.GONE);
                        }

                    }
                }
                break;
            }

            case TIME_LINE_WRITE:
            {
                Member member = ActiveMember.getInstance().getMember();

                if (member != null)
                {
                    ImageView imageView = holder.find(R.id.userImage);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(FutureCasting.HTTP_PROTOCOL);
                    stringBuilder.append(FutureCasting.SERVER_DOMAIN);
                    stringBuilder.append(FutureCasting.SERVER_PORT);
                    stringBuilder.append("/");
                    stringBuilder.append(member.getUserAvatar());

                    EasyLog.LogMessage(this, "++ TIME_LINE_WRITE thumbNail path = ", stringBuilder.toString());

                    UtilityUI.setThumbNailRoundedImageView(this, imageView, stringBuilder.toString(), R.dimen.dp25);

                    TextView textView1 = holder.find(R.id.userNickName);
                    textView1.setText(member.getUserName());

                    TextView textView2 = holder.find(R.id.userId);
                    textView2.setText(member.getUserId());
                }

                final ItemInsert itemInsert = (ItemInsert) item;

                EditText editText = holder.find(R.id.userTimeLineInsert);
                editText.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        itemInsert.setInsertedData(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {

                    }
                });
                break;
            }

            case TIME_LINE_REPLY:
            {
                Reply reply = (Reply) item;

                String createdDate = reply.getCreated_at();
                String formattedCreatedDate = FutureCastingUtil.getTimeFormattedString(createdDate);
                formattedCreatedDate += " 전";

                String userName = reply.getUserName();
                String userId = reply.getUserId();
                String userAvatar = reply.getUserAvatar();

                TextView textView1 = holder.find(R.id.userNickName);
                textView1.setText(userName);

                TextView textView2 = holder.find(R.id.userNickName);
                textView2.setText(userId);

                TextView textView3 = holder.find(R.id.userTimeLineTime);
                textView3.setText(formattedCreatedDate);

                TextView textView4 = holder.find(R.id.userTimeLine);
                textView4.setText(reply.getContent());

                CircleImageView circleImageView = holder.find(R.id.userImage);

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(FutureCasting.HTTP_PROTOCOL);
                stringBuilder.append(FutureCasting.SERVER_DOMAIN);
                stringBuilder.append(FutureCasting.SERVER_PORT);
                stringBuilder.append("/");
                stringBuilder.append(userAvatar);

                EasyLog.LogMessage(this, "++ TIME_LINE_REPLY thumbNail path = ", stringBuilder.toString());

                UtilityUI.setThumbNailRoundedImageView(this, circleImageView, stringBuilder.toString(), R.dimen.dp25);
                break;
            }

            case TIME_LINE_REPLY_WRITE:
            {
                final ItemInsert itemInsert = (ItemInsert) item;

                String userName = itemInsert.getInsertTitle();

                TextView textView = holder.find(R.id.userNickName);
                textView.setText(userName);

                EditText editText = holder.find(R.id.userTimeLineInsert);
                editText.setText((String) itemInsert.getInsertedData());
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        itemInsert.setInsertedData(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

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

            case BAR_CHART:
            {
                BarChartItem barChartItem = (BarChartItem) item;

                String title = barChartItem.getItemTitle();
                String bottomTitle = barChartItem.getItemBottomTitle();

                List<BarEntry> entryList = barChartItem.getPointEntries();

                TextView textView1 = holder.find(R.id.insertItemTitle);
                textView1.setText(title);

                TextView textView2 = holder.find(R.id.insertItemBottomTitle);
                textView2.setText(bottomTitle);

                BarDataSet barDataSet = new BarDataSet(entryList, "");
                barDataSet.setColors(
                        UtilityUI.getColor(this, R.color.mainColor0),
                        UtilityUI.getColor(this, R.color.color_999999));

                BarData barData = new BarData(barDataSet);
                barData.setDrawValues(false);

                BarChart barChart = holder.find(R.id.barChart);
                barChart.setData(barData);
                barChart.setFitBars(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisLeft().setDrawAxisLine(false);
                barChart.getAxisLeft().setDrawLabels(false);
                barChart.getAxisRight().setDrawGridLines(false);
                barChart.getAxisRight().setDrawAxisLine(false);
                barChart.getAxisRight().setDrawLabels(false);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.getXAxis().setDrawAxisLine(false);
                barChart.getXAxis().setDrawLabels(false);
                barChart.getDescription().setEnabled(false);
                barChart.getLegend().setEnabled(false);
                barChart.setDragDecelerationFrictionCoef(0.95f);
                barChart.animateXY(1000,1000);
                break;
            }

            case CURRENT_CASTING_STATUS:
            {
                CastingStatus currentStatus = (CastingStatus) item;

                LinearLayout linearLayout = holder.find(R.id.castingSelectionList);

                for (int i = 1 ; i <= 10 ; i++)
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
                        View view = linearLayout.getChildAt((i-1));
                        view.setVisibility(View.GONE);
                    }
                }
                break;
            }

            case SELECT_SCROLLABLE_OPTION:
            {
                ItemScrollableOption itemScrollableOption = (ItemScrollableOption) item;
                itemScrollableOption.deleteObservers();

                InsertOptionsScrollable insertOptionsScrollable = holder.find(R.id.scrollableOptionsView);
                insertOptionsScrollable.setScrollableOption(itemScrollableOption);

                TextView textView1 = holder.find(R.id.insertItemTitle);
                textView1.setText(itemScrollableOption.getInsertTitle());

                TextView textView2 = holder.find(R.id.insertItemTextPrefix);
                textView2.setText(itemScrollableOption.getBottomPrefix());

                ObserverView<TextView> observerView = new ObserverView<TextView>(
                        (TextView) holder.find(R.id.insertItemText)) {
                    @Override
                    protected void updateView(Observable observable, Object o) throws Exception
                    {
                        if (o instanceof Integer)
                        {
                            int n = (int) o;

                            DecimalFormat decimalFormat = new DecimalFormat("#,###");

                            mRoot.setText(decimalFormat.format(n));
                        }
                    }
                };
                itemScrollableOption.addObserver(observerView);
                itemScrollableOption.notifySelectedData();
                break;
            }

            case SELECT_VERTICAL_OPTIONS:
            {
                ItemSelectOptions itemSelectOptions = (ItemSelectOptions) item;
                itemSelectOptions.deleteObservers();

                InsertOptionsVertical insertOptionsVertical = holder.find(R.id.insertOptionsVertical);
                insertOptionsVertical.setSelectOptions(itemSelectOptions);

                TextView textView1 = holder.find(R.id.insertItemTitle);
                textView1.setText(itemSelectOptions.getInsertTitle());

                TextView textView2 = holder.find(R.id.insertItemTextPrefix);
                textView2.setText(itemSelectOptions.getBottomPrefix());

                ObserverView<TextView> observerView = new ObserverView<TextView>(
                        (TextView) holder.find(R.id.insertItemText)) {
                    @Override
                    protected void updateView(Observable observable, Object o) throws Exception
                    {
                        if (o instanceof ItemSelectOptions.Option)
                        {
                            ItemSelectOptions.Option option = (ItemSelectOptions.Option) o;

                            Object value = option.getValue();

                            if (value instanceof String)
                            {
                                mRoot.setText((String) value);
                            }
                        }
                    }
                };
                itemSelectOptions.addObserver(observerView);
                itemSelectOptions.notifySelectedData();
                break;
            }

            case SELECT_HORIZONTAL_OPTIONS:
            {
                ItemSelectOptions itemSelectOptions = (ItemSelectOptions) item;
                itemSelectOptions.deleteObservers();

                InsertOptionsHorizontal insertOptionsHorizontal = holder.find(R.id.insertOptionsHorizontal);
                insertOptionsHorizontal.setSelectOptions(itemSelectOptions);

                TextView textView1 = holder.find(R.id.insertItemTitle);
                textView1.setText(itemSelectOptions.getInsertTitle());

                TextView textView2 = holder.find(R.id.insertItemTextPrefix);
                textView2.setText(itemSelectOptions.getBottomPrefix());

                ObserverView<TextView> observerView = new ObserverView<TextView>(
                        (TextView) holder.find(R.id.insertItemText)) {
                    @Override
                    protected void updateView(Observable observable, Object o) throws Exception
                    {
                        if (o instanceof ItemSelectOptions.Option)
                        {
                            ItemSelectOptions.Option option = (ItemSelectOptions.Option) o;

                            Object value = option.getValue();

                            if (value instanceof Integer)
                            {
                                int n = (int) value;

                                String strValue = Integer.toString(n);

                                mRoot.setText(strValue);
                            }
                        }
                    }
                };
                itemSelectOptions.addObserver(observerView);
                itemSelectOptions.notifySelectedData();
                break;
            }

            case SELECT_BOOLEAN_OPTION:
            {
                ItemBooleanOption itemBooleanOption = (ItemBooleanOption) item;
                itemBooleanOption.deleteObservers();

                InsertOptionsBoolean insertOptionsBoolean = holder.find(R.id.insertItemBoolean);
                insertOptionsBoolean.setSelectedBoolean(itemBooleanOption);

                itemBooleanOption.notifySelectedData();
                break;
            }

            case INSERT_BUYING_CAP:
            {
                final ItemInsert itemInsert = (ItemInsert) item;

                int cap = ActiveMember.getInstance().getUserCap();

                DecimalFormat decimalFormat = new DecimalFormat("#,### cap 남음");

                final TextView textView = holder.find(R.id.remainingCapText);
                textView.setText(decimalFormat.format(cap));

                final EditText editText = holder.find(R.id.insertCap);

                if (itemInsert.getInsertedData() == null)
                {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after)
                        {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count)
                        {
                            try
                            {
                                int cap = ActiveMember.getInstance().getUserCap();

                                int remainingCap = 0 , insertedCap = 0;

                                String string = s.toString();

                                if (!TextUtils.isEmpty(string))
                                {
                                    insertedCap = Integer.parseInt(string);

                                    remainingCap = (cap - insertedCap);

                                    if (remainingCap < 0) {
                                        remainingCap = 0;
                                    }
                                }
                                else
                                {
                                    remainingCap = cap;
                                }

                                DecimalFormat decimalFormat = new DecimalFormat("#,### cap 남음");

                                textView.setText(decimalFormat.format(remainingCap));

                                itemInsert.setInsertedData(insertedCap);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s)
                        {

                        }
                    });
                }

                break;
            }

            case INSERT_REASON_MESSAGE:
            {
                final ItemInsert itemInsert = (ItemInsert) item;

                EditText editText = holder.find(R.id.insertReason);
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent)
                    {
                        if (action == EditorInfo.IME_ACTION_DONE)
                        {
                            mCastButton.performClick();
                        }

                        return false;
                    }
                });

                if (itemInsert.getInsertedData() == null)
                {
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after)
                        {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count)
                        {
                            itemInsert.setInsertedData(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s)
                        {

                        }
                    });
                }
                break;
            }

            case DOUBLE_PIE_CHART:
            {
                DoublePieChartItem doublePieChartItem = (DoublePieChartItem) item;

                TextView textView1 = holder.find(R.id.insertItemTitle);
                textView1.setText(doublePieChartItem.getItemTitle());

                TextView textView2 = holder.find(R.id.insertItemBottomTitle);
                textView2.setText(doublePieChartItem.getItemBottomTitle());

                PieChartItem pieChartItem1 = doublePieChartItem.getPieChartItem1();

                PieDataSet dataSet1 = new PieDataSet(pieChartItem1.getPointEntries(),"Countries");
                dataSet1.setSliceSpace(3f);
                dataSet1.setSelectionShift(5f);
                dataSet1.setColors(
                        UtilityUI.getColor(this, R.color.mainColor0),
                        UtilityUI.getColor(this, R.color.color_999999));

                PieData pieData1 = new PieData((dataSet1));
                pieData1.setDrawValues(false);

                PieChart pieChart1 = holder.find(R.id.pieChart1);
                pieChart1.setUsePercentValues(true);
                pieChart1.getDescription().setEnabled(false);
                pieChart1.getLegend().setEnabled(false);
                pieChart1.setDrawEntryLabels(false);
                pieChart1.setDrawHoleEnabled(true);
                pieChart1.setDragDecelerationFrictionCoef(0.95f);
                pieChart1.setHoleColor(Color.WHITE);
                pieChart1.setTransparentCircleRadius(0f);
                pieChart1.setHoleRadius(80f);
                pieChart1.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션
                pieChart1.setData(pieData1);

                PieChartItem pieChartItem2 = doublePieChartItem.getPieChartItem2();

                PieDataSet dataSet2 = new PieDataSet(pieChartItem2.getPointEntries(),"Countries");
                dataSet2.setSliceSpace(3f);
                dataSet2.setSelectionShift(5f);
                dataSet2.setColors(
                        UtilityUI.getColor(this, R.color.mainColor0),
                        UtilityUI.getColor(this, R.color.color_999999));

                PieData pieData2 = new PieData((dataSet2));
                pieData2.setDrawValues(false);

                PieChart pieChart2 = holder.find(R.id.pieChart2);
                pieChart2.setUsePercentValues(true);
                pieChart2.getDescription().setEnabled(false);
                pieChart2.getLegend().setEnabled(false);
                pieChart2.setDrawEntryLabels(false);
                pieChart2.setDrawHoleEnabled(true);
                pieChart2.setDragDecelerationFrictionCoef(0.95f);
                pieChart2.setHoleColor(Color.WHITE);
                pieChart2.setTransparentCircleRadius(0f);
                pieChart2.setHoleRadius(80f);
                pieChart2.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션
                pieChart2.setData(pieData2);
                break;
            }

            case RANKING_LIST:
            {
                RankingList rankingList = (RankingList) item;

                TextView textView = holder.find(R.id.insertItemTitle);
                textView.setText(rankingList.getItemListTitle());

                final RecyclerView recyclerView = holder.find(R.id.rankingList);

                recyclerView.setNestedScrollingEnabled(true);
                recyclerView.setHasFixedSize(true);

                ItemViewAdapter adapter = (recyclerView.getAdapter() == null ? null :
                        (ItemViewAdapter) recyclerView.getAdapter());

                if (adapter == null)
                {
                    adapter = new ItemViewAdapter(this, this);

                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                }

                ArrayList<Ranking> itemList = rankingList.getRankingList();

                adapter.setItemList(itemList);
                adapter.notifyDataSetChanged();

                CustomTabLayout customTabLayout = holder.find(R.id.rankingListTab);
                customTabLayout.addTextTab("적중률");
                customTabLayout.addTextTab("리워드");
                customTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
                {
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
                });

                break;
            }

            case RANKING:
            {
                Ranking ranking = (Ranking) item;

                DecimalFormat decimalFormat = new DecimalFormat("#.");
                String index = decimalFormat.format((position + 1));
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

                case TIME_LINE_REPLY_LIST:
                    mCastButton.setVisibility(View.GONE);

                    mCastTopButton.setVisibility(View.GONE);
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

                case CASTING_DONE:
                    mCastButton.setVisibility(View.GONE);

                    mCastTopButton.setVisibility(View.GONE);
                    break;

                case CASTING_CLOSED:
                    mCastButton.setVisibility(View.VISIBLE);
                    mCastButton.setText("다른  캐스트 하러 가기");

                    mCastTopButton.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void updateCastInfo(Cast cast)
    {
        mTargetCast = cast;

        String thumbNailPath = cast.getThumbnail(0);
        String[] tags = cast.getTags();
        String tag1 = (tags != null && tags.length > 0 ? tags[0] : null);
        String tag2 = (tags != null && tags.length > 1 ? tags[1] : null);
        String totalReward = String.format(Locale.KOREA, "%d 캡", cast.getTotalReward());
        String endDate = cast.getEndDate();
        String formattedEndDate = FutureCastingUtil.getTimeFormattedString(endDate);
        String castingNumberString = null;

        int castingNumber = cast.getCasterNum();
        if (castingNumber > 0)
        {
            castingNumberString = Integer.toString(cast.getCasterNum());
            castingNumberString += " 명 참여";
        }

        EasyLog.LogMessage(this, "++ updateCastInfo getCastId = ", cast.getCastId());
        EasyLog.LogMessage(this, "++ updateCastInfo thumbNailPath = ", thumbNailPath);
        EasyLog.LogMessage(this, "++ updateCastInfo isCastingDone = " + cast.isCastingDone());
        EasyLog.LogMessage(this, "++ updateCastInfo tag1 = " + tag1);
        EasyLog.LogMessage(this, "++ updateCastInfo tag2 = " + tag2);
        EasyLog.LogMessage(this, "++ updateCastInfo endDate = " + endDate);
        EasyLog.LogMessage(this, "++ updateCastInfo formattedEndDate = " + formattedEndDate);

        if (!TextUtils.isEmpty(thumbNailPath))
        {
            int radius = (int) getResources().getDimension(R.dimen.dp25);

            ImageLoader.loadRoundImage(this, mCastImage, thumbNailPath, radius);
        }
        else
        {
            UtilityUI.setBackGroundDrawable(mCastImage, R.drawable.shape_gray_color_alpha80_round10);
        }

        mTopTagView1.setText(tag1);
        mTopTagView2.setText(totalReward);

        mTopCastingNumberView.setText(castingNumberString);

        if (FutureCastingUtil.isPast(endDate))
        {
            mTopCastingStatusView.setText("종료");
            mTopCastingStatusView.setPadding(20,5,20,5);

            UtilityUI.setBackGroundDrawable(mTopCastingStatusView, R.drawable.shape_pink_color_round5_alpha80);
        }
        else if (cast.isCastingDone())
        {
            mTopCastingStatusView.setText("참여중");
            mTopCastingStatusView.setPadding(20,5,20,5);

            UtilityUI.setBackGroundDrawable(mTopCastingStatusView, R.drawable.shape_pink_color_round5_alpha80);
        }
        else if (!TextUtils.isEmpty(formattedEndDate))
        {
            formattedEndDate += " 전";

            mTopCastingStatusView.setText(formattedEndDate);
        }

        mCastTitle.setText(cast.getTitle());
        mCastDescription.setText(cast.getDescription());
    }

    @Override
    protected void onClickEvent(View v)
    {
        int position = (int) v.getTag(R.id.position);

        ICommonItem item = mItemViewAdapter.getItem(position);

        int itemType = (item != null ? item.getItemType() : -1);

        switch (itemType)
        {
            case NEWS_GROUP:
            {
                NewsList newsList = (NewsList) item;

                onClickEventNewsGroup(newsList);
                break;
            }

            case NEWS:
            {
                News news = (News) item;

                onClickEventNews(v, news);
                break;
            }

            case TIME_LINE_GROUP:
            {
                TimeLineList timeLineList = (TimeLineList) item;

                onClickEventTimeLineGroup(timeLineList);
                break;
            }

            case TIME_LINE:
            {
                TimeLine timeLine = (TimeLine) item;

                onClickEventTimeLine(v, timeLine);
                break;
            }

            case TIME_LINE_REPLY_WRITE:
            {
                ItemInsert itemInsert = (ItemInsert) item;

                onClickEventReplyWrite(v, itemInsert);
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

                ArrayList<ICommonItem> itemArrayList = new ArrayList<>();

                ItemInsert itemInsert = new ItemInsert();
                itemInsert.setItemType(ItemConstant.TIME_LINE_WRITE);
                itemArrayList.add(itemInsert);

                mItemViewAdapter.setItemList(itemArrayList);
                mItemViewAdapter.notifyDataSetChanged();
                break;

            case TIME_LINE_WRITE:
                // TODO API 없음
                break;
        }
    }

    private void onClickEventCastButton()
    {
        switch (mPageCurrentMode.mPageMode)
        {
            case CAST_INFO:

                String id = mTargetCast.getSurveyId();

                RequestCastingOption requestCastingOption = new RequestCastingOption();
                requestCastingOption.setResponseListener(this);
                requestCastingOption.setSurveyInfoId(id);

                RequestHandler.getInstance().request(requestCastingOption);
                break;

            case CAST_AS_CHOICE:
            case CAST_AS_ESSAY:
            case CAST_AS_TWO_CHOICE:

                PostCast postCast = new PostCast();
                postCast.setResponseListener(this);
                postCast.setSurveyId(mTargetCast.getSurveyId());
                postCast.setSurveyTitle(mTargetCast.getTitle());

                for (ICommonItem item : mItemViewAdapter.getItemList())
                {
                    int itemType = item.getItemType();

                    switch (itemType)
                    {
                        case SELECT_SCROLLABLE_OPTION:
                        {
                            ItemScrollableOption itemScrollableOption = (ItemScrollableOption) item;

                            int insertedData = itemScrollableOption.getInsertedData();

                            postCast.setPredict(Integer.toString(insertedData));
                            break;
                        }

                        case SELECT_HORIZONTAL_OPTIONS:
                        {
                            ItemSelectOptions itemSelect = (ItemSelectOptions) item;

                            ItemSelectOptions.Option option = (ItemSelectOptions.Option) itemSelect.getInsertedData();

                            postCast.setTrustPercent(String.valueOf(option.getValue()));
                            break;
                        }

                        case SELECT_BOOLEAN_OPTION:
                        {
                            ItemBooleanOption itemBooleanOption = (ItemBooleanOption) item;

                            boolean b = itemBooleanOption.getInsertedData();

                            postCast.setPredict((b ? "YES" : "NO"));
                            break;
                        }

                        case SELECT_VERTICAL_OPTIONS:
                        {
                            ItemSelectOptions itemSelect = (ItemSelectOptions) item;

                            ItemSelectOptions.Option option = (ItemSelectOptions.Option) itemSelect.getInsertedData();

                            postCast.setPredict(String.valueOf(option.getValue()));
                            break;
                        }

                        case INSERT_BUYING_CAP:
                        {
                            ItemInsert itemInsert = (ItemInsert) item;

                            Object o = itemInsert.getInsertedData();

                            int bettingCap = (o == null ? 0 : (int) o);
                            if (bettingCap <= ActiveMember.getInstance().getUserCap() && bettingCap > 0)
                            {
                                postCast.setBet(String.valueOf(bettingCap));
                            }
                            else if (bettingCap == 0)
                            {
                                Toast.makeText(this, "배팅 금액을 넣어주세요", Toast.LENGTH_SHORT).show();

                                return;
                            }
                            else
                            {
                                Toast.makeText(this, "배팅 금액이 소지액 보다 많습니다", Toast.LENGTH_SHORT).show();

                                return;
                            }
                            break;
                        }

                        case INSERT_REASON_MESSAGE:
                        {
                            ItemInsert itemInsert = (ItemInsert) item;

                            Object o = itemInsert.getInsertedData();

                            if (o != null)
                            {
                                postCast.setComment(String.valueOf(o));
                            }
                            else
                            {
                                Toast.makeText(this, "캐스팅 이유를 적어주세요", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            break;
                        }
                    }
                }

                RequestHandler.getInstance().request(postCast);
                break;

            case CASTING_DONE:
            case CASTING_CLOSED:

                finish();
                break;
        }
    }

    private void onClickEventNewsGroup(NewsList newsList)
    {
        if (mPageCurrentMode.mPageMode == PageMode.CAST_INFO)
        {
            String castId = (mTargetCast == null ? null : mTargetCast.getCastId());

            EasyLog.LogMessage(this, "++ onClickEventNews ");
            EasyLog.LogMessage(this, "++ onClickEventNews castId = " + castId);

            if (!TextUtils.isEmpty(castId))
            {
                mPageCurrentMode.setPageMode(PageMode.NEWS_LIST);

                mItemViewAdapter.setItemList(newsList.getNewsArrayList());
                mItemViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void onClickEventNews(View v, News news)
    {
        if (mPageCurrentMode.mPageMode == PageMode.CAST_INFO)
        {
            String castId = (mTargetCast == null ? null : mTargetCast.getCastId());

            EasyLog.LogMessage(this, "++ onClickEventNews ");
            EasyLog.LogMessage(this, "++ onClickEventNews castId = " + castId);

            if (!TextUtils.isEmpty(castId))
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

    private void onClickEventTimeLineGroup(TimeLineList timeLineGroup)
    {
        mPageCurrentMode.setPageMode(PageMode.TIME_LINE_LIST);

        EasyLog.LogMessage(this, ">> onClickEventTimeLineGroup size = " + timeLineGroup.getTimeLineList().size());

        mItemViewAdapter.setItemList(timeLineGroup.getTimeLineList());
        mItemViewAdapter.notifyDataSetChanged();
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

                    ArrayList<ICommonItem> list = new ArrayList<>();

                    list.add(timeLine);

                    ItemInsert itemInsert = new ItemInsert();
                    itemInsert.setInsertTitle(timeLine.getUserName());
                    itemInsert.setItemType(TIME_LINE_REPLY_WRITE);

                    list.add(itemInsert);

                    ReplyList replyList = timeLine.getReplyList();

                    if (replyList != null)
                    {
                        list.addAll(replyList.getReplyList());
                    }

                    mItemViewAdapter.setItemList(list);
                    mItemViewAdapter.notifyDataSetChanged();
                }
                break;
            }

            case R.id.shareTimeLineButton:
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");

                List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);

                if (!resInfo.isEmpty())
                {
                    List<Intent> shareIntentList = new ArrayList<>();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("캐스팅 ");
                    stringBuilder.append("\n");

                    Member member = timeLine.getMember();

                    if (member != null)
                    {
                        stringBuilder.append(member.getUserName());
                    }
                    stringBuilder.append("\n");
                    stringBuilder.append(timeLine.getComments());

                    for (ResolveInfo info : resInfo)
                    {
                        Intent shareIntent = (Intent) intent.clone();

                        if (info.activityInfo.packageName.toLowerCase().equals("com.facebook.katana"))
                        {
                            //facebook
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
                            // shareIntent.setType("image/jpg");
                            // shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+mImagePath));
                        }
                        else
                        {
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "제목");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
                        }
                        shareIntent.setPackage(info.activityInfo.packageName);
                        shareIntentList.add(shareIntent);
                    }

                    Intent chooserIntent = Intent.createChooser(shareIntentList.remove(0), "select");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
                    startActivity(chooserIntent);
                }
                break;
            }
        }
    }

    private void onClickCastShare()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);

        if (!resInfo.isEmpty())
        {
            List<Intent> shareIntentList = new ArrayList<>();

            String title = mTargetCast.getTitle();

            for (ResolveInfo info : resInfo)
            {
                Intent shareIntent = (Intent) intent.clone();

                if (info.activityInfo.packageName.toLowerCase().equals("com.facebook.katana"))
                {
                    //facebook
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, title);
                    // shareIntent.setType("image/jpg");
                    // shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+mImagePath));
                }
                else
                {
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "제목");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, title);
                }
                shareIntent.setPackage(info.activityInfo.packageName);
                shareIntentList.add(shareIntent);
            }

            Intent chooserIntent = Intent.createChooser(shareIntentList.remove(0), "select");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));

            startActivity(chooserIntent);
        }
    }

    private void onClickEventReplyWrite(View v, ItemInsert itemInsert)
    {
        switch (v.getId())
        {
            case R.id.replyWriteButton:

                ICommonItem item = mItemViewAdapter.getItem(0);

                if (item != null && item instanceof TimeLine)
                {
                    String insertedContent = (String) itemInsert.getInsertedData();

                    if (!TextUtils.isEmpty(insertedContent))
                    {
                        TimeLine timeLine = (TimeLine) item;

                        PostReply postReply = new PostReply();
                        postReply.setResponseListener(this);
                        postReply.setId(timeLine.getId());
                        postReply.setContent(insertedContent);

                        RequestHandler.getInstance().request(postReply);
                    }
                }
                break;
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

                RequestCast requestCast = new RequestCast();
                requestCast.setResponseListener(this);
                requestCast.setCast(mTargetCast);

                RequestHandler.getInstance().request(requestCast);
                break;
            }

            case TIME_LINE_WRITE:
            case TIME_LINE_REPLY_LIST:
            {
                mPageCurrentMode.setPageMode(PageMode.TIME_LINE_LIST);

                TimeLineList timeLineList = mTargetCast.getTimeLineList();

                EasyLog.LogMessage(this, "++ onBackPressed timeLineList size = " + timeLineList.getTimeLineListSize());

                mItemViewAdapter.clear();
                mItemViewAdapter.setItemList(timeLineList.getTimeLineList());
                mItemViewAdapter.notifyDataSetChanged();
                break;
            }

            case CAST_AS_CHOICE:
            case CAST_AS_ESSAY:
            case CAST_AS_TWO_CHOICE:

                mPageCurrentMode.setPageMode(CAST_INFO);

                mItemViewAdapter.clear();
                mItemViewAdapter.notifyDataSetChanged();

                RequestCast requestCast = new RequestCast();
                requestCast.setResponseListener(this);
                requestCast.setCast(mTargetCast);

                RequestHandler.getInstance().request(requestCast);
                break;

            case CASTING_DONE:
            case CASTING_CLOSED:

                finish();
                break;
        }
    }

    @Override
    public void onThreadResponseListen(BaseResponse response)
    {
        BaseRequest request = response.getSourceRequest();

        EasyLog.LogMessage(this, ">> onThreadResponseListen ");
        EasyLog.LogMessage(this, ">> onThreadResponseListen " + request.getClass().getSimpleName());

        if (request.isRight(RequestCast.class))
        {
            onCastDetailedResponse(response);
        }
        else if (request.isRight(RequestNewsList.class))
        {
            onNewsListResponse(response);
        }
        else if (request.isRight(RequestTimeLineList.class))
        {
            onTimeLineResponse(response);
        }
        else if (request.isRight(RequestReplyList.class))
        {
            onReplyListResponse(response);
        }
        else if (request.isRight(RequestCastingOption.class))
        {
            onCastingOptionResponse(response);
        }
        else if (request.isRight(PostReply.class))
        {
            onPostReplyResponse(response);
        }
        else if (request.isRight(PostCast.class))
        {
            onCastResponse(response);
        }
        else if (request.isRight(RequestCastEnding.class))
        {
            onCastEndingResponse(response);
        }
    }

    private void onCastDetailedResponse(BaseResponse response)
    {
        int responseCode = response.getResponseCode();
        if (responseCode > 0)
        {
            Cast cast = (Cast) response.getResponseModel();

            updateCastInfo(cast);

            if (FutureCastingUtil.isPast(cast.getEndDate()))
            {
                mPageCurrentMode.setPageMode(CASTING_CLOSED);

                CommonGraphItem commonGraphItem1 = mTargetCast.getMyCastingGraph();
                CommonGraphItem commonGraphItem2 = mTargetCast.getCastResultGraph();
                CommonGraphItem commonGraphItem3 = mTargetCast.getMyCastingResultGraph();

                NewsList newsList = mTargetCast.getNewsList();

                RankingList rankingList = mTargetCast.getCastRankingList();

                mItemViewAdapter.clear();
                mItemViewAdapter.addItem(commonGraphItem1);
                mItemViewAdapter.addItem(commonGraphItem2);
                mItemViewAdapter.addItem(newsList);
                mItemViewAdapter.addItem(commonGraphItem3);
                mItemViewAdapter.addItem(rankingList);
                mItemViewAdapter.notifyDataSetChanged();
            }
            else
            {
                mItemViewAdapter.clear();

                NewsList newsList = mTargetCast.getNewsList();

                TimeLineList timeLineList = mTargetCast.getTimeLineList();

                CastingStatus castingStatus = mTargetCast.getCurrentCastingStatus();

                if(newsList != null) mItemViewAdapter.addItem(newsList);
                if(timeLineList != null) mItemViewAdapter.addItem(timeLineList);
                if(castingStatus != null) mItemViewAdapter.addItem(castingStatus);
                mItemViewAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            RequestCast requestCast = (RequestCast) response.getSourceRequest();

            String errorMessage = requestCast.getErrorMessage();

            if (TextUtils.isEmpty(errorMessage))
            {
                errorMessage = "(Bypass) 더미 데이터 노출";
            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

            //TODO API 에러 발생 , 바이패스 처리
            ArrayList<ICommonItem> itemArrayList = new ArrayList<>();

            loadDummyDoneCastInfoList(itemArrayList);

            mItemViewAdapter.setItemList(itemArrayList);
            mItemViewAdapter.notifyDataSetChanged();
        }
    }

    private void onNewsListResponse(BaseResponse response)
    {
        NewsList newsList = (NewsList) response.getResponseModel();

        int size = newsList.getNewsSize();
        if (size > 0)
        {
            mItemViewAdapter.setItemList(newsList.getNewsArrayList());
            mItemViewAdapter.notifyDataSetChanged();
        }
    }

    private void onTimeLineResponse(BaseResponse response)
    {
        TimeLineList timeLineList = (TimeLineList) response.getResponseModel();

        int size = timeLineList.getTimeLineListSize();
        if (size > 0)
        {
            mItemViewAdapter.setItemList(timeLineList.getTimeLineList());
            mItemViewAdapter.notifyDataSetChanged();
        }
    }

    private void onReplyListResponse(BaseResponse response)
    {
        ReplyList replyList = (ReplyList) response.getResponseModel();

        int size = replyList.getReplyListSize();
        if (size > 0)
        {
            mItemViewAdapter.setItemList(replyList.getReplyList());
            mItemViewAdapter.notifyDataSetChanged();
        }
    }

    private void onPostReplyResponse(BaseResponse response)
    {
        Reply reply = (Reply) response.getResponseModel();

        if (reply != null)
        {
            ArrayList<ICommonItem> list = new ArrayList<>();

            TimeLine timeLine = (TimeLine) mItemViewAdapter.getItem(0);
            list.add(timeLine);

            ItemInsert<String> itemInsert = new ItemInsert<>();
            itemInsert.setInsertTitle(timeLine.getUserName());
            itemInsert.setInsertedData(null);
            itemInsert.setItemType(TIME_LINE_REPLY_WRITE);
            list.add(itemInsert);

            ReplyList replyList = timeLine.getReplyList();

            if (replyList == null)
            {
                replyList = new ReplyList();
            }
            replyList.addReply(0, reply);

            list.addAll(replyList.getReplyList());

            mItemViewAdapter.clear();
            mItemViewAdapter.setItemList(list);
            mItemViewAdapter.notifyDataSetChanged();

            UtilityUI.setForceKeyboardDown(this, null);
        }
    }

    private void onCastResponse(BaseResponse response)
    {
        // TODO API 결함 이슈가 있음 , 일단 바이패스 처리
        RequestCastEnding requestCastEnding = new RequestCastEnding();
        requestCastEnding.setResponseListener(this);
        requestCastEnding.setSurveyId(mTargetCast.getSurveyId());

        RequestHandler.getInstance().request(requestCastEnding);
    }

    private void onCastEndingResponse(BaseResponse response)
    {
        Alarm alarm = (Alarm) response.getResponseModel();

        if (alarm != null)
        {
            mTargetCast.setCastingDone(true);

            alarm.setTargetCast(mTargetCast);

            {
                Intent intent = new Intent(this, AlarmActivity.class);
                intent.putExtra(DEFINE_ALARM, alarm);

                startActivity(intent);
            }

            Intent intent = getIntent();
            intent.putExtra(DEFINE_CAST, mTargetCast);
            intent.putExtra(CAST_CARD_POSITION, mCastCardPosition);

            setResult(CASTING_DONE_CODE, intent);

            finish();
        }
    }

    private void onCastingOptionResponse(BaseResponse response)
    {
        CastingOption castingOption = (CastingOption) response.getResponseModel();

        switch (castingOption.getCastType())
        {
            case CHOICE:
            {
                mPageCurrentMode.setPageMode(PageMode.CAST_AS_CHOICE);

                mItemViewAdapter.clear();

                String[] questions = castingOption.getQuestions();
                String question1 = (questions != null && questions.length > 0 ? questions[0].replace("\"","") : null);
                String question2 = (questions != null && questions.length > 1 ? questions[1].replace("\"","") : null);
                String question3 = (questions != null && questions.length > 2 ? questions[2].replace("\"","") : null);
                String question4 = (questions != null && questions.length > 3 ? questions[3].replace("\"","") : null);
                String question5 = (questions != null && questions.length > 4 ? questions[4].replace("\"","") : null);

                ItemSelectOptions itemSelectOptions1 = new ItemSelectOptions();
                itemSelectOptions1.setInsertTitle("내 캐스트는");
                itemSelectOptions1.setVertical(true);
                if (!TextUtils.isEmpty(question1)) itemSelectOptions1.addOption(question1, question1);
                if (!TextUtils.isEmpty(question2)) itemSelectOptions1.addOption(question2, question2);
                if (!TextUtils.isEmpty(question3)) itemSelectOptions1.addOption(question3, question3);
                if (!TextUtils.isEmpty(question4)) itemSelectOptions1.addOption(question4, question4);
                if (!TextUtils.isEmpty(question5)) itemSelectOptions1.addOption(question5, question5);

                mItemViewAdapter.addItem(itemSelectOptions1);

                ItemSelectOptions itemSelectOptions2 = new ItemSelectOptions();
                itemSelectOptions2.setInsertTitle("얼마나 확신하나요 ?");
                itemSelectOptions2.setHorizontal(true);
                itemSelectOptions2.addOption("가망없음", 5);
                itemSelectOptions2.addOption("조금 헷갈려요", 25);
                itemSelectOptions2.addOption("하프 앤 하프", 50);
                itemSelectOptions2.addOption("거의 틀림 없어요", 75);
                itemSelectOptions2.addOption("올인", 100);
                itemSelectOptions2.setBottomPrefix("%");
                mItemViewAdapter.addItem(itemSelectOptions2);

                ItemInsert<String> itemInsert1 = new ItemInsert<>();
                itemInsert1.setItemType(ItemConstant.INSERT_BUYING_CAP);
                mItemViewAdapter.addItem(itemInsert1);

                ItemInsert<String> itemInsert2 = new ItemInsert<>();
                itemInsert2.setItemType(ItemConstant.INSERT_REASON_MESSAGE);
                mItemViewAdapter.addItem(itemInsert2);

                mItemViewAdapter.notifyDataSetChanged();
                break;
            }

            case ESSAY:
            {
                mPageCurrentMode.setPageMode(PageMode.CAST_AS_ESSAY);

                mItemViewAdapter.clear();

                ItemScrollableOption itemScrollableOption = new ItemScrollableOption();
                itemScrollableOption.setInsertTitle("내 캐스트는");
                itemScrollableOption.setMinValuePrefix("최소");
                itemScrollableOption.setMinValue(0);
                itemScrollableOption.setMaxValuePrefix("최대");
                itemScrollableOption.setMaxValue(12500);
                itemScrollableOption.setScrollPrefix("내 캐스트는");
                itemScrollableOption.setBottomPrefix("cap");
                mItemViewAdapter.addItem(itemScrollableOption);

                ItemSelectOptions itemSelectOptions = new ItemSelectOptions();
                itemSelectOptions.setInsertTitle("얼마나 확신하나요 ?");
                itemSelectOptions.setHorizontal(true);
                itemSelectOptions.addOption("가망없음", 5);
                itemSelectOptions.addOption("조금 헷갈려요", 25);
                itemSelectOptions.addOption("하프 앤 하프", 50);
                itemSelectOptions.addOption("거의 틀림 없어요", 75);
                itemSelectOptions.addOption("올인", 100);
                itemSelectOptions.setBottomPrefix("%");
                mItemViewAdapter.addItem(itemSelectOptions);

                ItemInsert<String> itemInsert1 = new ItemInsert<>();
                itemInsert1.setItemType(ItemConstant.INSERT_BUYING_CAP);
                mItemViewAdapter.addItem(itemInsert1);

                ItemInsert<String> itemInsert2 = new ItemInsert<>();
                itemInsert2.setItemType(ItemConstant.INSERT_REASON_MESSAGE);
                mItemViewAdapter.addItem(itemInsert2);

                mItemViewAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    private void loadDummyDoneCastInfoList(ArrayList<ICommonItem> commonItems)
    {
        {
            PieChartItem pieChartItem1 = new PieChartItem();
            pieChartItem1.addPointEntrie(new PieEntry(80f,"Japan"));
            pieChartItem1.addPointEntrie(new PieEntry(20f,"USA"));

            PieChartItem pieChartItem2 = new PieChartItem();
            pieChartItem2.addPointEntrie(new PieEntry(90f,"아이템1"));
            pieChartItem2.addPointEntrie(new PieEntry(120f,"아이템2"));

            DoublePieChartItem doublePieChartItem = new DoublePieChartItem();
            doublePieChartItem.setPieChartItem1(pieChartItem1);
            doublePieChartItem.setPieChartItem2(pieChartItem2);
            doublePieChartItem.setItemTitle("내 캐스트는");
            doublePieChartItem.setItemBottomTitle("7,500");
            commonItems.add(doublePieChartItem);
        }

        {
            PieChartItem pieChartItem1 = new PieChartItem();
            pieChartItem1.addPointEntrie(new PieEntry(80f,"Japan"));
            pieChartItem1.addPointEntrie(new PieEntry(20f,"USA"));

            PieChartItem pieChartItem2 = new PieChartItem();
            pieChartItem2.addPointEntrie(new PieEntry(40f,"아이템1"));
            pieChartItem2.addPointEntrie(new PieEntry(20f,"아이템2"));

            DoublePieChartItem doublePieChartItem = new DoublePieChartItem();
            doublePieChartItem.setPieChartItem1(pieChartItem1);
            doublePieChartItem.setPieChartItem2(pieChartItem2);
            doublePieChartItem.setItemTitle("캐스트 결과");
            doublePieChartItem.setItemBottomTitle("8,500");
            commonItems.add(doublePieChartItem);
        }

        {
            BarChartItem barChartItem = new BarChartItem();
            barChartItem.setItemTitle("캐스트 결과");
            barChartItem.setItemBottomTitle("7,500");
            barChartItem.addPointEntry("샘플", 120);
            barChartItem.addPointEntry("샘플", 80);
            barChartItem.addPointEntry("샘플", 20);
            barChartItem.addPointEntry("샘플", 90);
            barChartItem.addPointEntry("샘플", 150);

            commonItems.add(barChartItem);
        }

        News news = new News();
        news.setNewsTitle("주요 뉴스");
        news.setContents("미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다. 미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.");
        news.setNewsTime("1시간전");

        commonItems.add(news);

        {
            PieChartItem pieChartItem1 = new PieChartItem();
            pieChartItem1.addPointEntrie(new PieEntry(80f,"Japan"));
            pieChartItem1.addPointEntrie(new PieEntry(20f,"USA"));

            PieChartItem pieChartItem2 = new PieChartItem();
            pieChartItem2.addPointEntrie(new PieEntry(70f,"아이템1"));
            pieChartItem2.addPointEntrie(new PieEntry(20f,"아이템2"));

            DoublePieChartItem doublePieChartItem = new DoublePieChartItem();
            doublePieChartItem.setPieChartItem1(pieChartItem1);
            doublePieChartItem.setPieChartItem2(pieChartItem2);
            doublePieChartItem.setItemTitle("내가 얻은 Cap");
            doublePieChartItem.setItemBottomTitle("7,500");
            commonItems.add(doublePieChartItem);
        }

        RankingList rankingList = new RankingList();
        rankingList.setItemListTitle("캐스트 순위");

        ArrayList<Ranking> itemList = new ArrayList<>();

        loadDummyChartItemList(itemList);

        rankingList.setRankingList(itemList);

        commonItems.add(rankingList);
    }

    private void loadDummyChartItemList(ArrayList<Ranking> list)
    {
        for (int i = 0 ; i < 10 ; i ++)
        {
            Ranking ranking = new Ranking();

            list.add(ranking);
        }
    }
}
