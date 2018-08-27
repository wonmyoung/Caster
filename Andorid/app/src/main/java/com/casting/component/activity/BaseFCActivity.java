package com.casting.component.activity;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.casting.commonmodule.utility.EasyLog;
import com.casting.commonmodule.utility.UtilityData;
import com.casting.commonmodule.view.component.CommonActivity;

public abstract class BaseFCActivity extends CommonActivity {

    private static final int REQUEST_START_APP = 1;

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int end = grantResults.length;
        if (end > 0) {

            for (int i = 0; i < end; i++)
            {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                {
                    onPermissionsDenied(permissions[i]);
                    return;
                }
            }
        }

        onPermissionsGranted();
    }

    protected void onPermissionsDenied(String deniedPermission)
    {
        EasyLog.LogMessage(this, " >> onPermissionsDenied ", deniedPermission);
    }

    protected void onPermissionsGranted()
    {
        EasyLog.LogMessage(this, " >> onPermissionsGranted");
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (!isRuntimePermissionAllowed())
        {
            String[] deniedPermissions = getDeniedPermission();

            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_START_APP);
        }
    }

    /**
     * 거부된 권한이 있을 경우 false 반환. <br>

     * @return boolean <br>
     *         true : 런타임 퍼미션 모두 허용된 상태 / false : 런타임 퍼미션이 하나라도 불허용 된 상태
     */
    public boolean isRuntimePermissionAllowed()
    {
        if (UtilityData.isBelowMOS())
        {
            return true;
        }
        else
        {
            try
            {
                String[] aDeniedPermission = getDeniedPermission();

                return (aDeniedPermission == null || aDeniedPermission.length == 0);
            }
            catch (Exception e)
            {
                e.printStackTrace();

                return false;
            }
        }
    }
}
