package com.openxu.md.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

/**
 * Author: openXu
 * Time: 2019/3/14 11:37
 * class: FConversUtils
 * Description: 各种转换工具类
 */
public class FConversUtils {

    /***/

    /**根据手机的分辨率从 dp 的单位 转成为 px(像素)*/
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**根据手机的分辨率从 px(像素) 的单位 转成为 dp  */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**将px值转换为sp值，保证文字大小不变*/
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /**将sp值转换为px值，保证文字大小不变 */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
