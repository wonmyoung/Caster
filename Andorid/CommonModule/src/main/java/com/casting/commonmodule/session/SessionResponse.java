package com.casting.commonmodule.session;

import com.casting.commonmodule.model.BaseModel;
import com.casting.commonmodule.model.BaseResponse;

public class SessionResponse<M extends BaseModel> extends BaseResponse<M>
{
    private SessionType mSessionType;

    public SessionType getSessionType() {
        return mSessionType;
    }

    public void setSessionType(SessionType sessionType) {
        mSessionType = sessionType;
    }
}
