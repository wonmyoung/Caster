package com.casting.model.request;

import android.content.ContentValues;

import com.casting.commonmodule.network.NetworkRequest;
import com.casting.commonmodule.network.base.NetworkParcelable;
import com.casting.commonmodule.network.parse.JSONParcelable;
import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.model.Cast;
import com.casting.model.CastList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestCastList extends NetworkRequest implements JSONParcelable<CastList> {

    public enum Order {
        Popular, Available, RewardBig, Done, Applied;
    }

    private String  mEmailAddress;
    private int     PageIndex;
    private int     Size = 10;
    private Order   mOrder;

    @Override
    public String getHttpMethod()
    {
        return HttpGet;
    }

    @Override
    public ContentValues getHttpRequestHeader() {
        return null;
    }

    @Override
    public ContentValues getHttpRequestParameter() {
        return null;
    }

    @Override
    public JSONParcelable getNetworkParcelable() {
        return this;
    }

    @Override
    public CastList parse(JSONObject jsonObject)
    {
        CastList castList = new CastList();

        JSONArray jsonArray = UtilityData.convertJsonArrayFromJson(jsonObject, "surveyInfo");

        int length = (jsonArray != null ? jsonArray.length() : 0);
        if (length > 0)
        {
            for (int i = 0 ; i < length ; i++)
            {
                try
                {
                    JSONObject o = jsonArray.getJSONObject(i);

                    String id = UtilityData.convertStringFromJSON(o, "_id");
                    String title = UtilityData.convertStringFromJSON(o, "title");
                    String description = UtilityData.convertStringFromJSON(o, "description");
                    String totalReward = UtilityData.convertStringFromJSON(o, "totalReward");
                    String status = UtilityData.convertStringFromJSON(o, "status");
                    String minimum = UtilityData.convertStringFromJSON(o, "minimum");
                    String endDate = UtilityData.convertStringFromJSON(o, "endDate");
                    String reference = UtilityData.convertStringFromJSON(o, "reference");
                    String questionType = UtilityData.convertStringFromJSON(o, "questionType");
                    String numOfQuestion = UtilityData.convertStringFromJSON(o, "numOfQuestion");
                    String updated_at = UtilityData.convertStringFromJSON(o, "updated_at");
                    String created_at = UtilityData.convertStringFromJSON(o, "created_at");
                    String startDate = UtilityData.convertStringFromJSON(o, "startDate");

                    EasyLog.LogMessage(this, "++ RequestCastList parse id = " + id);
                    EasyLog.LogMessage(this, "++ RequestCastList parse title = " + title);
                    EasyLog.LogMessage(this, "++ RequestCastList parse description = " + description);
                    EasyLog.LogMessage(this, "++ RequestCastList parse totalReward = " + totalReward);
                    EasyLog.LogMessage(this, "++ RequestCastList parse status = " + status);
                    EasyLog.LogMessage(this, "++ RequestCastList parse minimum = " + minimum);
                    EasyLog.LogMessage(this, "++ RequestCastList parse endDate = " + endDate);
                    EasyLog.LogMessage(this, "++ RequestCastList parse reference = " + reference);
                    EasyLog.LogMessage(this, "++ RequestCastList parse questionType = " + questionType);
                    EasyLog.LogMessage(this, "++ RequestCastList parse numOfQuestion = " + numOfQuestion);
                    EasyLog.LogMessage(this, "++ RequestCastList parse updated_at = " + updated_at);
                    EasyLog.LogMessage(this, "++ RequestCastList parse created_at = " + created_at);
                    EasyLog.LogMessage(this, "++ RequestCastList parse startDate = " + startDate);

                    Cast cast = new Cast();
                    cast.setCastId(id);
                    cast.setTitle(title);
                    cast.setDescription(description);
                    cast.setEndDate(endDate);

                    castList.addCast(cast);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return castList;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        this.Size = size;
    }

    public Order getOrder() {
        return mOrder;
    }

    public void setOrder(Order order) {
        this.mOrder = order;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String address) {
        this.mEmailAddress = address;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }
}
