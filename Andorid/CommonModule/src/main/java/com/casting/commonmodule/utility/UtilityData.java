package com.casting.commonmodule.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilityData {

    /**
     * 제공된 권한이 허용되어 있는지 확인한다.
     *
     * @param oContext
     *            Context
     * @param strDeniedPermissions
     *            String 거부된 퍼미션 정보
     * @return boolean
     */
    public static boolean hasSelfPermission(Context oContext, String strDeniedPermissions)
    {
        if (UtilityData.isBelowMOS() || strDeniedPermissions == null)
        {
            return true;
        }

        return ContextCompat.checkSelfPermission(oContext, strDeniedPermissions) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     *
     * @return booleans ..
     */
    public static boolean isBelowMOS() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
			/* API Level 이 22 이며, M 버전이 아닐 때 true */
            if (isMNC()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * M preview 1, 2 OS 버전 인경우.
     *
     * @return boolean
     */
    public static boolean isMNC() {
        return "MNC".equals(Build.VERSION.CODENAME);
    }

    public static String confirmMobileNumber(Context context) {
        if (context != null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String mdn = telephonyManager.getLine1Number();

                if (!TextUtils.isEmpty(mdn) && mdn.contains("+82")) {
                    mdn = mdn.replace("+82" , "01");
                }
                return mdn;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean confirmViewNeedtobeRefreshed(View view) {
        try {
            if (view != null) {
                long currentTimeDate = System.currentTimeMillis();
                long lastUpdatedDate = (long) view.getTag();

                int minute = 1000 * 60;
                int refreshStandard = minute * 30;

                long diffTime = (currentTimeDate - lastUpdatedDate);
                if ((diffTime / refreshStandard) > 1) {
                    view.setTag(currentTimeDate);

                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public static void exportSQLiteDB(Context context){
        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.voltsoft.edu.engword//databases//cardEngWord.db";
                String backupDBPath = "cardEngWord.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }

                if (backupDB.exists()){
                    Toast.makeText(context, "DB Export Complete!!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSubString(String strOriginal ,int start , int end) {
        try {
            return strOriginal.substring(start , end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean confirmNetworkParameterValid(int position , Object ... params) {
        try {
            return (params[position] != null);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean confirmEnglishCharacter(Character inputCharacter) {
        if (((inputCharacter >= 0x61 && inputCharacter <= 0x7A)) || (inputCharacter >=0x41 && inputCharacter <= 0x5A)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean confirmNetworkAccessable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null) {
            return false;
        } else {
            return (activeNetwork.isAvailable());
        }
    }

    public static String confirmAppVersion(Context context) {
        String version = "0.0.0";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String confirmHashKey(Context context) {
        String strHashKey = null;
        try {
            String strPackageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(strPackageName , PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                strHashKey = new String(Base64.encode(messageDigest.digest() , 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strHashKey;
    }

    public static Class<?> confirmColumnType(Class instance , String fieldName) {
        for (Field field : instance.getFields()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return field.getType();
            }
        }
        return null;
    }

    public static Field[] confirmColumns(Class instance) {
        return instance.getFields();
    }

    public static Field[] confirmColumns(Object instance) {
        return confirmColumns(instance.getClass());
    }

    public static String convertStringFromJSON(JSONObject jsonObject , String jsonField) {
        try {
            return jsonObject.getString(jsonField);
        } catch (Exception e) {
            return null;
        }
    }

    public static int convertIntegerFromJSON(JSONObject jsonObject , String jsonField) {
        try {
            return jsonObject.getInt(jsonField);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String createMD5HashCode(String strOriginalData) {
        String MD5 = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strOriginalData.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            MD5 = strOriginalData.hashCode()+"";
        }
        return MD5;
    }

    public static String createCurrentDataString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss" , Locale.getDefault());
        Date currentTime = new Date();
        String strCurrentData = simpleDateFormat.format(currentTime);
        return strCurrentData;
    }
}
