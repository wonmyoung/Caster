package com.casting;

import com.casting.commonmodule.model.BaseRequest;
import com.casting.commonmodule.session.SessionType;
import com.casting.commonmodule.view.component.CommonApplication;
import com.casting.model.request.Follow;
import com.casting.model.request.Login;
import com.casting.model.request.PostCast;
import com.casting.model.request.PostReply;
import com.casting.model.request.PutMember;
import com.casting.model.request.RegisterMember;
import com.casting.model.request.RequestCastingStatus;
import com.casting.model.request.RequestDetailedCast;
import com.casting.model.request.RequestCastList;
import com.casting.model.request.RequestFollowerList;
import com.casting.model.request.RequestFollowingList;
import com.casting.model.request.RequestMember;
import com.casting.model.request.RequestNews;
import com.casting.model.request.RequestNewsList;
import com.casting.model.request.RequestRankingList;
import com.casting.model.request.RequestReplyList;
import com.casting.model.request.RequestTimeLine;
import com.casting.model.request.SettingMenu;

public class FutureCasting extends CommonApplication {

    public static final String HTTP_PROTOCOL    = "http://";
    public static final String SERVER_DOMAIN    = "ec2-13-125-159-59.ap-northeast-2.compute.amazonaws.com";
    public static final String SERVER_PORT      = ":3000";
    public static final String SERVER_TARGET    = "";

    @Override
    public String create(BaseRequest request)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTTP_PROTOCOL);
        stringBuilder.append(SERVER_DOMAIN);
        stringBuilder.append(SERVER_PORT);
        stringBuilder.append(SERVER_TARGET);

        // 1.회원가입 (적용완료)
        if (request.isRight(RegisterMember.class))
        {
            stringBuilder.append("/accounts/join");
        }
        // 2. 로그인 (적용완료)
        else if (request.isRight(Login.class))
        {
            stringBuilder.append(createLoginUrl((Login) request));
        }
        // 3. 메인화면 (적용완료)
        else if (request.isRight(RequestCastList.class))
        {
            stringBuilder.append(createCastListUrl((RequestCastList) request));
        }
        // 3.0.1 캐스트 상세화면 (적용완료)
        else if (request.isRight(RequestDetailedCast.class))
        {
            RequestDetailedCast requestDetailedCast = (RequestDetailedCast) request;

            stringBuilder.append("/survey/surveyInfo/detail/");
            stringBuilder.append(requestDetailedCast.getSurveyInfoId());
        }
        // 3.0.2 주요 뉴스 (적용완료)
        else if (request.isRight(RequestNews.class))
        {
            RequestNews requestNews = (RequestNews) request;

            stringBuilder.append("/survey/surveyInfo/detail/");
            stringBuilder.append(requestNews.getSurveyId());
        }
        // 3.0.3 타임라인
        else if (request.isRight(RequestTimeLine.class))
        {
            // TODO V0.5 규격서에 URL 존재 하지 않음
        }
        // 3.0.4 캐스트 현황
        else if (request.isRight(RequestCastingStatus.class))
        {
            // TODO V0.5 규격서에 URL 존재 하지 않음 , JSON 데이터 규격이 올바르지 않음 , 옵션이름필드=옵션이름 , 데이터필드(퍼센트지)=실제 데이터 로 출력 되어야함
        }
        // 3.1. 뉴스 게시판 (뉴스 리스트) (적용완료)
        else if (request.isRight(RequestNewsList.class))
        {
            RequestNewsList requestNewsList = (RequestNewsList) request;

            stringBuilder.append("/article/");
            stringBuilder.append(requestNewsList.getCastId());
        }
        // 3.2 댓글 게시판 (댓글 리스트)
        else if (request.isRight(RequestReplyList.class))
        {
            RequestReplyList requestReplyList = (RequestReplyList) request;

            stringBuilder.append("/comment/commentInfo/");
            stringBuilder.append(requestReplyList.getCastId());
        }
        // 3.2.1 댓글 작성
        else if (request.isRight(PostReply.class))
        {
            stringBuilder.append("/comment/reply");
        }
        // 3.4 캐스트 하기
        else if (request.isRight(PostCast.class))
        {
            PostCast postCast = (PostCast) request;

            stringBuilder.append("/survey/apply/");
            stringBuilder.append(postCast.getCastId());
        }
        // 4.메뉴 (메인 화면 좌측 메뉴 정보) = 4.1.1 프로필로 통합 // TODO V0.5 세션 요구에 막혀있음
        else if (request.isRight(RequestMember.class))
        {
            stringBuilder.append("/accounts/accountInfo");
        }
        // 4.1.4 프로필 수정 // TODO V0.5 세션 요구에 막혀있음
        else if (request.isRight(PutMember.class))
        {
            stringBuilder.append("/accounts/accountsModify");
        }
        // 4.1.5 팔로잉 리스트 // TODO V0.5 세션 요구에 막혀있음
        else if (request.isRight(RequestFollowingList.class))
        {
            stringBuilder.append("/follow/followingList");
        }
        // 4.1.5 팔로워 리스트 // TODO V0.5 세션 요구에 막혀있음
        else if (request.isRight(RequestFollowerList.class))
        {
            stringBuilder.append("/follow/followerList");
        }
        // 4.1.5 팔로우 / 언팔로우
        else if (request.isRight(Follow.class))
        {
            Follow follow = (Follow) request;

            stringBuilder.append("/follow/following");
            stringBuilder.append(follow.getUserId());
        }
        // 4.2 캐스트 순위
        else if (request.isRight(RequestRankingList.class))
        {
            stringBuilder.append("/accounts/ranking");
        }
        // 4.3 메뉴 / 설정
        else if (request.isRight(SettingMenu.class))
        {
            stringBuilder.append("/accounts/set/premit");
        }

        return stringBuilder.toString();
    }

    private String createLoginUrl(Login login)
    {
        SessionType sessionType = login.getSessionType();

        if (sessionType == SessionType.FACEBOOK)
        {
            return "/auth/facebook";
        }
        else if (sessionType == SessionType.KAKAO)
        {
            return null;
        }
        else if (sessionType == SessionType.GOOGLE)
        {
            return null;
        }
        else
        {
            return "/accounts/login";
        }
    }

    private String createCastListUrl(RequestCastList requestCastList)
    {
        switch (requestCastList.getOrder())
        {
            case Available:
                return "/survey/surveyInfo";

            case Popular:
                return "/survey/surveyInfo/bestSurvey";

            case RewardBig:
                return "/survey/surveyInfo/topReward";

            // 4.1.2 종료된 캐스트 // TODO 세션 요구에 막혀 있음
            case Done:
                return "/accounts/accountInfo/ajax/finished";

            // 4.1.3 참여중 캐스트 // TODO 세션 요구에 막혀 있음
            case Applied:
                return "/accounts/accountInfo/ajax/progress";

            default:
                return null;
        }
    }
}
