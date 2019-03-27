package com.openxu.md.util;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;


/**
 * autour : openXu
 * date : 2016/7/24 14:12
 * className : FontUtil
 * version : 1.0
 * description : 文字相关处理帮助类(自定义控件专用)
 */
public class FontUtil {
    /**
     * @param paint
     * @param str
     * @return 返回指定笔和指定字符串的长度
     * @add yujiangtao 16/8/5
     */
    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }
    /**
     * @return 返回指定笔的文字高度
     * @add yujiangtao 16/8/5
     */
    public static float getFontHeight(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }
    /**
     * @return 返回指定笔离文字顶部的基准距离
     * @add yujiangtao 16/8/5
     */
    public static float getFontLeading(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading- fm.ascent;
    }


    /**
     * 获取标签和值组成的SpannableString对象
     * @param lable 标签字符串，将包裹为浅灰色
     * @param value 值字符串，将包裹为深灰色
     * @return
     */
    public static SpannableString getLableValueSpan (String lable, String value){
        lable = TextUtils.isEmpty(lable)?"":lable;
        value = TextUtils.isEmpty(value)?"":value;
        String space = "   ";
        SpannableString spanString = new SpannableString(lable+space+value);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#939393"));
        spanString.setSpan(span, 0, lable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span = new ForegroundColorSpan(Color.parseColor("#5E5E5E"));
        spanString.setSpan(span, lable.length()+space.length()-1, lable.length()+space.length()+value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }
    public static SpannableString getSpecifyLableValueSpan (String lable, String value, String preColor, String afterColor){
        lable = TextUtils.isEmpty(lable)?"":lable;
        value = TextUtils.isEmpty(value)?"":value;
        String space = "   ";
        SpannableString spanString = new SpannableString(lable+space+value);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(preColor));
        spanString.setSpan(span, 0, lable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span = new ForegroundColorSpan(Color.parseColor(afterColor));
        spanString.setSpan(span, lable.length()+space.length()-1, lable.length()+space.length()+value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }
}
