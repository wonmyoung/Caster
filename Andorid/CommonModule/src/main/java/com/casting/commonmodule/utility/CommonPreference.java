package com.casting.commonmodule.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Observable;

public class CommonPreference extends Observable {

    private static SharedPreferences moPref;

    private static class LazyHolder
    {
        private static CommonPreference mInstance = new CommonPreference();
    }

    public static CommonPreference getInstance()
    {
        return LazyHolder.mInstance;
    }

    public static void init(Application application)
    {
        moPref = application.getSharedPreferences(CommonPreference.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * SharedPreference의 모든값을 삭제한다.
     *
     * @return 성공여부
     */
    public boolean clearSharedValue()
    {
        if (moPref == null)
        {
            return false;
        }
        try
        {
            SharedPreferences.Editor editor = moPref.edit();
            editor.clear();
            editor.commit();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * 전달받은 Key값의 Data를 반환한다. (Boolean)
     *
     * @param strShareKey
     *            : Key값
     * @param bDefault
     *            : 실패시 전달받은 default값
     * @return boolean
     */
    public boolean getSharedValueByBoolean(String strShareKey, boolean bDefault) {

        if (!checkParam(strShareKey))
        {
            return bDefault;
        }
        try
        {
            return moPref.getBoolean(strShareKey, bDefault);
        } catch (Exception e) {
            return bDefault;
        }
    }

    /**
     * 전달받은 Key값의 Data를 반환한다. (Float)
     *
     * @param strShareKey
     *            : Key값
     * @param fDefault
     *            : 실패시 전달받은 default값
     * @return boolean
     */
    public float getSharedValueByFloat(String strShareKey, float fDefault) {
        if (!checkParam(strShareKey)) {
            return fDefault;
        }
        try {
            return moPref.getFloat(strShareKey, fDefault);
        } catch (Exception e) {
            return fDefault;
        }
    }

    /**
     * 전달받은 Key값의 Data를 반환한다. (Integer)
     *
     * @param strShareKey
     *            : Key값
     * @param nDefault
     *            : 실패시 전달받은 default값
     * @return int
     */
    public int getSharedValueByInt(String strShareKey, int nDefault) {
        if (!checkParam(strShareKey)) {
            return nDefault;
        }
        try {
            return moPref.getInt(strShareKey, nDefault);
        } catch (Exception e) {
            return nDefault;
        }
    }

    /**
     * 전달받은 Key값의 Data를 반환한다. (Long)
     *
     * @param strShareKey
     *            : Key값
     * @param lDefault
     *            : 실패시 전달받은 default값
     * @return long
     */
    public long getSharedValueByLong(String strShareKey, long lDefault) {
        if (!checkParam(strShareKey)) {
            return lDefault;
        }
        try {
            return moPref.getLong(strShareKey, lDefault);
        } catch (Exception e) {
            return lDefault;
        }
    }

    /**
     * 전달받은 Key값의 Data를 반환한다. (String)
     *
     * @param strShareKey
     *            : Key값
     * @param strDefault
     *            : 실패시 전달받은 default값
     * @return String
     */
    public String getSharedValueByString(String strShareKey, String strDefault) {
        if (!checkParam(strShareKey)) {
            return strDefault;
        }
        try {
            return moPref.getString(strShareKey, strDefault);
        } catch (Exception e) {
            return strDefault;
        }
    }

    /**
     *
     * @return {@link SharedPreferences.Editor}
     */
    public SharedPreferences.Editor getSharedPreferencesEditor()
    {
        SharedPreferences.Editor editor = null;

        if (moPref != null)
        {
            editor = moPref.edit();
        }

        return editor;
    }

    /**
     * 전달받은 Key값으로 Data를 저장한다. (Boolean)
     *
     * @param strShareKey
     *            : Key값
     * @param bValue
     *            : 저장할 Data
     * @return 성공여부
     */
    public boolean setSharedValueByBoolean(String strShareKey, boolean bValue) {
        if (!checkParam(strShareKey))
        {
            return false;
        }

        try
        {
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putBoolean(strShareKey, bValue);
            editor.commit();

            setChanged();
            notifyObservers();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * 전달받은 Key값으로 Data를 저장한다. (Float)
     *
     * @param strShareKey
     *            : Key값
     * @param fValue
     *            : 저장할 Data
     * @return 성공여부
     */
    public boolean setSharedValueByFloat(String strShareKey, float fValue)
    {
        if (!checkParam(strShareKey))
        {
            return false;
        }

        try
        {
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putFloat(strShareKey, fValue);
            editor.commit();

            setChanged();
            notifyObservers();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * 전달받은 Key값으로 Data를 저장한다. (Integer)
     *
     * @param strShareKey
     *            : Key값
     * @param nValue
     *            : 저장할 Data
     * @return 성공여부
     */
    public boolean setSharedValueByInt(String strShareKey, int nValue)
    {
        if (!checkParam(strShareKey))
        {
            return false;
        }

        try
        {
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putInt(strShareKey, nValue);
            editor.commit();

            setChanged();
            notifyObservers();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * 전달받은 Key값으로 Data를 저장한다. (Long)
     *
     * @param strShareKey
     *            : Key값
     * @param lValue
     *            : 저장할 Data
     * @return 성공여부
     */
    public boolean setSharedValueByLong(String strShareKey, long lValue)
    {
        if (!checkParam(strShareKey))
        {
            return false;
        }

        try
        {
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putLong(strShareKey, lValue);
            editor.commit();

            setChanged();
            notifyObservers();
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * 전달받은 Key값으로 Data를 저장한다. (String)
     *
     * @param strShareKey
     *            : Key값
     * @param strValue
     *            : 저장할 Data
     * @return 성공여부
     */
    public boolean setSharedValueByString(String strShareKey, String strValue)
    {

        if (!checkParam(strShareKey))
        {
            return false;
        }

        try
        {
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putString(strShareKey, strValue);
            editor.commit();

            setChanged();
            notifyObservers();
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * Key값 및 mobjPref객체가 정상적인지 판단한다.
     *
     * @param strShareKey
     *            : Key값
     * @return true - 정상, false - Error
     */
    private boolean checkParam(String strShareKey)
    {
        if (TextUtils.isEmpty(strShareKey))
        {
            EasyLog.LogMessage(this, "-- checkParam strShareKey is null ");
            return false;
        }
        if (moPref == null)
        {
            EasyLog.LogMessage(this, "-- checkParam moPref is null ");
            return false;
        }

        return true;
    }
}
