package com.casting.interfaces;

public interface Constants {

    String DEFINE_CAST      = "DEFINE_CAST";
    String DEFINE_ALARM     = "DEFINE_ALARM";
    String CAST_CARD_POSITION = "CAST_CARD_POSITION";

    String MEMBER_ID        = "MEMBER_ID";
    String MEMBER_EMAIL     = "MEMBER_EMAIL";
    String MEMBER_PW        = "MEMBER_PW";
    String AUTH_TOKEN       = "AUTH_TOKEN";

    String FOLLOWING_LIST_MODE   = "FOLLOWING_LIST_MODE";

    String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    int SECOND = 1000;
    int MINUTE = SECOND * 60;
    int HOUR = MINUTE * 60;
    int DAY = HOUR * 24;

    int LOAD_CASTING_PAGE = 1;
    int CASTING_DONE_CODE = 100;

    int followingList   = 101;
    int followerList    = 102;
}
