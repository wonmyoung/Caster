package com.casting.component.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.casting.model.Cast;
import com.casting.model.CastingOption;
import com.casting.model.CastingStatus;
import com.casting.model.NewsList;
import com.casting.model.Ranking;
import com.casting.model.RankingList;
import com.casting.model.DoublePieChartItem;
import com.casting.model.PieChartItem;
import com.casting.model.ReplyList;
import com.casting.model.TimeLineList;
import com.casting.model.request.PostCast;
import com.casting.model.request.PostReply;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static com.casting.component.activity.CastingActivity.PageMode.CAST_AS_CHOICE;
import static com.casting.component.activity.CastingActivity.PageMode.CAST_AS_ESSAY;
import static com.casting.component.activity.CastingActivity.PageMode.CAST_AS_TWO_CHOICE;
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

        public boolean isCastingMode()
        {
            return (mPageMode == CAST_AS_ESSAY ||
                    mPageMode == CAST_AS_CHOICE ||
                    mPageMode == CAST_AS_TWO_CHOICE);
        }
    }

    private ImageView    mCastImage;
    private TextView     mCastTitle;
    private TextView     mCastDescription;
    private TextView     mCastTopButton;
    private Button       mCastButton;

    private CommonRecyclerView      mItemListView;
    private ItemViewAdapter         mItemViewAdapter;
    private LinearLayoutManager     mItemLayoutManager;

    private Cast        mTargetCast;

    private PageCurrentMode     mPageCurrentMode = new PageCurrentMode();

    @Override
    protected void init(@Nullable Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.activity_casting);

        mCastImage = find(R.id.castCardBack);
        mCastTitle = find(R.id.castCardTitle);
        mCastDescription = find(R.id.castCardDescription);

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

        mTargetCast = (Cast) getIntent().getSerializableExtra(DEFINE_CAST);

        if (mTargetCast != null)
        {
            mPageCurrentMode.addObserver(this);
            mPageCurrentMode.setPageMode((mTargetCast.isDone() ? CAST_DONE : CAST_INFO));

            String thumbNailPath = mTargetCast.getThumbnail(0);

            EasyLog.LogMessage(this, "++ confirm getCastId = ", mTargetCast.getCastId());
            EasyLog.LogMessage(this, "++ confirm thumbNailPath = ", thumbNailPath);

            if (!TextUtils.isEmpty(thumbNailPath))
            {
                int radius = (int) getResources().getDimension(R.dimen.dp25);

                ImageLoader.loadRoundImage(this, mCastImage, thumbNailPath, radius);
            }
            else
            {
                UtilityUI.setBackGroundDrawable(mCastImage, R.drawable.shape_gray_color_alpha80_round10);
            }

            mCastTitle.setText(mTargetCast.getTitle());
            mCastDescription.setText(mTargetCast.getDescription());

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
                stringBuilder.append("/uploads/account/");
                stringBuilder.append(timeLine.getUserId());
                CircleImageView circleImageView = holder.find(R.id.userImage);

                UtilityUI.setThumbNailRoundedImageView(this, circleImageView, stringBuilder.toString(), R.dimen.dp25);

                String createdDate = timeLine.getCreated_at();
                String comments = timeLine.getComments();

                TextView textView1 = holder.find(R.id.userTimeLine);
                textView1.setText(comments);

                StringBuilder createdDateBuilder = new StringBuilder();
                createdDateBuilder.append(FutureCastingUtil.getTimeFormattedString(createdDate));
                createdDateBuilder.append(" 전");
                TextView textView2 = holder.find(R.id.userTimeLineTime);
                textView2.setText(createdDateBuilder);

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
                            String userId = timeLine.getUserId();

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
                            stringBuilder.append("/uploads/account/");
                            stringBuilder.append(userId);
                            EasyLog.LogMessage(this, "++ timeLine Group thumbNail path = ", stringBuilder.toString());

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
                    stringBuilder.append("/uploads/account/");
                    stringBuilder.append(member.getUserId());

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

                TextView textView1 = holder.find(R.id.userTimeLineTime);
                textView1.setText(formattedCreatedDate);

                TextView textView2 = holder.find(R.id.userTimeLine);
                textView2.setText(reply.getContent());
                break;
            }

            case TIME_LINE_REPLY_WRITE:
            {
                final ItemInsert itemInsert = (ItemInsert) item;

                EditText editText = holder.find(R.id.userTimeLineInsert);
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

                EditText editText = holder.find(R.id.insertCap);

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
                DecimalFormat decimalFormat = new DecimalFormat("#.");

                TextView textView1 = holder.find(R.id.rankingIndex);
                textView1.setText(decimalFormat.format((position + 1)));

                Ranking ranking = (Ranking) item;

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
        switch (mPageCurrentMode.mPageMode)
        {
            case CAST_INFO:

                String id = mTargetCast.getSurveyId();

                RequestCastingOption requestCastingOption = new RequestCastingOption();
                requestCastingOption.setResponseListener(this);
                requestCastingOption.setSurveyInfoId(id);

                RequestHandler.getInstance().request(requestCastingOption);
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
                    itemInsert.setItemType(TIME_LINE_REPLY_WRITE);

                    list.add(itemInsert);

                    ReplyList replyList = timeLine.getReplyList();

                    if (replyList != null)
                    {
                        EasyLog.LogMessage(this, "++ onClickEventTimeLine replyList size = " + replyList.getReplyListSize());

                        list.addAll(replyList.getReplyList());
                    }

                    mItemViewAdapter.setItemList(list);
                    mItemViewAdapter.notifyDataSetChanged();
                }
                break;
            }

            case R.id.shareTimeLineButton:
            {
                break;
            }
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

                mItemViewAdapter.clear();

                if (timeLineList != null)
                {
                    mItemViewAdapter.setItemList(timeLineList.getTimeLineList());
                }
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
            Reply reply = (Reply) response.getResponseModel();

            if (reply != null)
            {
                mItemViewAdapter.addItem(2, reply);
                mItemViewAdapter.notifyDataSetChanged();
            }
        }
        else if (request.isRight(PostCast.class))
        {

        }
    }

    private void onCastDetailedResponse(BaseResponse response)
    {
        int responseCode = response.getResponseCode();
        if (responseCode > 0)
        {
            mTargetCast = (Cast) response.getResponseModel();

            NewsList newsList = mTargetCast.getNewsList();

            TimeLineList timeLineList = mTargetCast.getTimeLineList();

            EasyLog.LogMessage(this, "++ onCastDetailedResponse newsList is null ?" + (newsList == null));
            EasyLog.LogMessage(this, "++ onCastDetailedResponse timeLineList is null ?" + (timeLineList == null));

            mItemViewAdapter.addItem(newsList);
            mItemViewAdapter.addItem(timeLineList);
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

    private void onCastingOptionResponse(BaseResponse response)
    {
        CastingOption castingOption = (CastingOption) response.getResponseModel();

        switch (castingOption.getCastType())
        {
            case CHOICE:
            {
                mPageCurrentMode.setPageMode(PageMode.CAST_AS_CHOICE);

                mItemViewAdapter.clear();

                ItemSelectOptions itemSelectOptions = new ItemSelectOptions();
                itemSelectOptions.setInsertTitle("내 캐스트는");
                itemSelectOptions.setVertical(true);
                itemSelectOptions.addOption("한화 이글스", "한화 이글스");
                itemSelectOptions.addOption("롯데 자이언츠", "롯데 자이언츠");
                itemSelectOptions.addOption("SK 와이번즈", "SK 와이번즈");
                itemSelectOptions.addOption("넥센 히어로즈", "넥센 히어로즈");
                itemSelectOptions.addOption("두산 베어스", "두산 베어스");
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
                itemSelectOptions.addOption("가망없음", 0);
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

    private void loadDummyItemList(ArrayList<ICommonItem> commonItems)
    {
        News news = new News();
        news.setNewsTitle("주요 뉴스");
        news.setContents("미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다. 미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.");
        news.setNewsTime("1시간전");
        commonItems.add(news);

        TimeLineList timeLineGroup = new TimeLineList();
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
            news.setContents("미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다. 미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.미국 코엔페이지는 11일 미국 증권거래위원호이의 비트코인 ETF 승인 결과가 8월 10일 발표될예정이라고 보도했습니다.");
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
            ranking.setItemType(ItemConstant.RANKING);

            list.add(ranking);
        }
    }
}
