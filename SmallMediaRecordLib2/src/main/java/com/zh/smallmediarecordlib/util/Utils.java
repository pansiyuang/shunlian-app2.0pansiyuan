package com.zh.smallmediarecordlib.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by zhanghe on 2018/10/17.
 */

public class Utils {

    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}
