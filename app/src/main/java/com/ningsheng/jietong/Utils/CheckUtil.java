package com.ningsheng.jietong.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.PermissionChecker;
import android.widget.TextView;

/**
 * Created by zhangheng on 2015/12/4.
 */
public class CheckUtil {
    /**
     * 检测是否有访问SDCard的权限
     *
     * @param mContext
     * @return true 有 false 没有
     */
    public  static boolean checkSDCardPower(Context mContext) {
        return PackageManager.PERMISSION_GRANTED == PermissionChecker.checkCallingOrSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 检测是否存在可以使用的SDCard
     * @return true 存在 false 不存在
     */
    public static boolean isExistSDcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
    public static boolean isNull(TextView textView) {
        return isNull(textView.getText().toString());
    }

    public static boolean isNull(String text) {
        if (text == null || "".equals(text)||"null".equals(text)) {
            return true;
        }
        return false;
    }

    public static boolean isNull(double d) {
        if (d > 0) {
            return true;
        }
        return false;
    }
}
