package com.casting.model;

import android.text.TextUtils;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.view.list.ICommonItem;
import com.casting.model.global.ItemConstant;

public class Cast extends BaseModel implements ICommonItem, ItemConstant {

    private String      CastId;
    private String      SurveyId;

    private double      RemainingTime;
    private String      Title;
    private String      QuestionType;
    private String[]    Tags;
    private int         TotalReward;
    private int         Minimum;
    private int         CasterNum;
    private int         NumOfQuestion;
    private String[]    Thumbnails;
    private String      Link;
    private int         Participants;
    private String      Reference;
    private String      Description;
    private String      EndDate;
    private String      StartDate;
    private String      Status;

    private NewsList       NewsList;
    private TimeLineList   TimeLineList;

    private CommonGraphItem     MyCastingGraph;
    private CommonGraphItem     MyCastingResultGraph;
    private CommonGraphItem     CastResultGraph;

    private RankingList         CastRankingList;

    private int   ItemType;

    private boolean CastingDone;

    public double getRemainingTime() {
        return RemainingTime;
    }

    public void setRemainingTime(double remainingTime) {
        RemainingTime = remainingTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String[] getTags() {
        return Tags;
    }

    public void setTags(String ... tags) {
        Tags = tags;
    }

    public int getTotalReward() {
        return TotalReward;
    }

    public void setTotalReward(int totalReward) {
        TotalReward = totalReward;
    }

    public String getThumbnail(int i)
    {
        String s = (Thumbnails != null && Thumbnails.length > i ? Thumbnails[i] : null);

        StringBuilder stringBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(s))
        {
            stringBuilder.append("http://ec2-13-125-159-59.ap-northeast-2.compute.amazonaws.com");
            stringBuilder.append(":3000");
            stringBuilder.append("/uploads/survey/");

            s = s.replace("\"", "");

            stringBuilder.append(s);
        }

        return stringBuilder.toString();
    }

    public void setThumbnails(String ... thumbnails) {
        Thumbnails = thumbnails;
    }

    public void setItemType(int itemType)
    {
        ItemType = itemType;
    }

    @Override
    public int getItemType()
    {
        return ItemType;
    }

    public String getCastId() {
        return CastId;
    }

    public void setCastId(String castId) {
        CastId = castId;
    }

    public boolean isCastingDone() {
        return CastingDone;
    }

    public void setCastingDone(boolean castingDone) {
        CastingDone = castingDone;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getCasterNum() {
        return CasterNum;
    }

    public void setCasterNum(int casterNum) {
        CasterNum = casterNum;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getParticipants() {
        return Participants;
    }

    public void setParticipants(int participants) {
        Participants = participants;
    }

    public String getSurveyId() {
        return SurveyId;
    }

    public void setSurveyId(String surveyId) {
        SurveyId = surveyId;
    }

    public NewsList getNewsList() {
        return NewsList;
    }

    public void setNewsList(NewsList newsList) {
        NewsList = newsList;
    }

    public void addNews(News news)
    {
        if (NewsList == null)
        {
            NewsList = new NewsList();
        }

        NewsList.addNews(news);
    }

    public TimeLineList getTimeLineList() {
        return TimeLineList;
    }

    public void setTimeLineList(TimeLineList timeLineList) {
        TimeLineList = timeLineList;
    }

    public void addTimeLine(TimeLine timeLine)
    {
        if (TimeLineList == null)
        {
            TimeLineList = new TimeLineList();
        }

        TimeLineList.addTimeLine(timeLine);
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public CommonGraphItem getMyCastingGraph() {
        return MyCastingGraph;
    }

    public void setMyCastingGraph(CommonGraphItem myCastingGraph) {
        MyCastingGraph = myCastingGraph;
    }

    public CommonGraphItem getMyCastingResultGraph() {
        return MyCastingResultGraph;
    }

    public void setMyCastingResultGraph(CommonGraphItem myCastingResultGraph) {
        MyCastingResultGraph = myCastingResultGraph;
    }

    public CommonGraphItem getCastResultGraph() {
        return CastResultGraph;
    }

    public void setCastResultGraph(CommonGraphItem castResultGraph) {
        CastResultGraph = castResultGraph;
    }

    public int getMinimum() {
        return Minimum;
    }

    public void setMinimum(int minimum) {
        Minimum = minimum;
    }

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public int getNumOfQuestion() {
        return NumOfQuestion;
    }

    public void setNumOfQuestion(int numOfQuestion) {
        NumOfQuestion = numOfQuestion;
    }

    public RankingList getCastRankingList() {
        return CastRankingList;
    }

    public void setCastRankingList(RankingList castRankingList) {
        CastRankingList = castRankingList;
    }
}
