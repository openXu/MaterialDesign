package com.openxu.md.view.chart.piechart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;


import com.openxu.md.R;
import com.openxu.md.util.FConversUtils;
import com.openxu.md.util.FLog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * autour : openXu
 * date : 2017/7/24 10:46
 * className : PieChartLayout
 * version : 1.0
 * description : 占比饼状图表
 */
public class PieChartLayout extends LinearLayout {

    private String TAG = "PieChartLayout";

    protected boolean isLoading = true;
    protected boolean debug = true;
    protected List<PieChartBean> dataList;
    protected List<ChartLable> lableList;

    private PieChart chartView;
    /**
     * 饼状图设置属性
     */
    private boolean showZeroPart = false;    //如果某部分占比为0， 是否显示
    private int centerLableSpace = FConversUtils.dip2px(getContext(), 1);   //中间文字行距
    //圆环宽度，如果值>0,则为空心圆环，内环为白色，可以在内环中绘制字
    private int ringWidth = FConversUtils.dip2px(getContext(), 20);    //圆环宽度
    private int lineLenth = FConversUtils.dip2px(getContext(), 20);    //指示线长度
    private int outSpace = FConversUtils.dip2px(getContext(), 5);
    private int textSpace = FConversUtils.dip2px(getContext(), 3);     //tag指示文字与线的距离
    private int tagTextSize = (int) getResources().getDimension(R.dimen.text_size_level_small);     //饼状图占比指示文字大小
    private int tagTextColor = getResources().getColor(R.color.text_color_light_gray); //文字颜色，如果为0，则根据扇形颜色一样;
    private PieChartLayout.TAG_MODUL tagModul = TAG_MODUL.MODUL_LABLE;   //TAG展示位置
    private PieChartLayout.TAG_TYPE tagType = TAG_TYPE.TYPE_NUM;         //TAG展示类型


    private PieChartLableView lableView;
    /**
     * 右侧lable设置属性
     */
    private int rectW = FConversUtils.dip2px(getContext(), 10);   //lable矩形宽高
    private int rectH = FConversUtils.dip2px(getContext(), 10);
    private int rectRaidus = 0;     //矩形圆角
    private int rectSpace = FConversUtils.dip2px(getContext(), 8);   //右侧标签上下间距
    private int leftSpace = FConversUtils.dip2px(getContext(), 5);   //右侧标签左右间距
    private int lableTextSize = (int) getResources().getDimension(R.dimen.text_size_level_small);   //饼状图占比指示文字大小
    private int lableTextColor = getResources().getColor(R.color.text_color_light_gray);


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
//        LogUtil.w(TAG, "dispatchTouchEvent分发事件 "+result);
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = super.onInterceptTouchEvent(ev);
//        LogUtil.e(TAG, "onInterceptTouchEvent拦截事件 "+result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
//        LogUtil.i(TAG, "onTouchEvent处理事件 "+result);
        return result;
    }

    //RGB颜色数组
    private int arrColorRgb[][] = {
            {86, 138, 220},                    //    UIColorFromRGB(0x568ADC),
            {112, 173, 71},    //    UIColorFromRGB(0x70AD47),
            {219, 69, 40},      //    UIColorFromRGB(0xDB4528),
            {255, 193, 2},             //    UIColorFromRGB(0xFFC102),
            {242, 141, 2},          //    UIColorFromRGB(0xF28D02),
            {41, 182, 180},         //    UIColorFromRGB(0x29B6B4),
            {187, 107, 201},       //    UIColorFromRGB(0xbb6bc9),
            {124, 117, 214},        //    UIColorFromRGB(0x7c75d6),

            {220, 170, 97},//    UIColorFromRGB(0xDCAA61),
            {125, 171, 88},//    UIColorFromRGB(0x7DAB58),
            {233, 200, 88},//    UIColorFromRGB(0xE9C858),
            {213, 150, 196},//    UIColorFromRGB(0xd596c4)
            {220, 127, 104},//    UIColorFromRGB(0xDC7F68),

    };


    /**
     * tag类型
     */
    public enum TAG_TYPE {
        TYPE_NUM,       //数量
        TYPE_PERCENT,   //百分比
    }

    public enum TAG_MODUL {
        MODEUL_NULL,      //不展示
        MODUL_CHART,      //在扇形图上显示tag
        MODUL_LABLE,      //在lable后面显示tag
    }

    public PieChartLayout(Context context) {
        this(context, null);
    }

    public PieChartLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.dataList = new ArrayList<>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof PieChart) {
                chartView = (PieChart) child;
            } else if (child instanceof PieChartLableView) {
                lableView = (PieChartLableView) child;
            }
        }
        FLog.i(TAG, "init" + chartView + lableView);
        setConfig();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void setConfig() {
        if (chartView != null) {
            chartView.setLoading(isLoading);
            chartView.setDebug(debug);
            chartView.setArrColorRgb(arrColorRgb);
            chartView.setTagType(tagType);
            chartView.setTagModul(tagModul);
            chartView.setTagTextColor(tagTextColor);
            chartView.setTagTextSize(tagTextSize);
            chartView.setShowZeroPart(showZeroPart);
            chartView.setCenterLableSpace(centerLableSpace);
            chartView.setRingWidth(ringWidth);
            chartView.setLineLenth(lineLenth);
            chartView.setOutSpace(outSpace);
            chartView.setTextSpace(textSpace);
            chartView.setData(dataList, lableList);
        }
        if (lableView != null) {
            lableView.setLoading(isLoading);
            lableView.setDebug(debug);
            lableView.setTagType(tagType);
            lableView.setTagModul(tagModul);
            lableView.setShowZeroPart(showZeroPart);
            lableView.setArrColorRgb(arrColorRgb);
            lableView.setTextColor(lableTextColor);
            lableView.setTextSize(lableTextSize);
            lableView.setRectW(rectW);
            lableView.setRectH(rectH);
            lableView.setRectRaidus(rectRaidus);
            lableView.setRectSpace(rectSpace);
            lableView.setLeftSpace(leftSpace);

            lableView.setData(dataList);
        }
    }

    public int getTotal() {
        return chartView.getTotal();
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
        setConfig();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setShowZeroPart(boolean showZeroPart) {
        this.showZeroPart = showZeroPart;
    }

    public void setCenterLableSpace(int centerLableSpace) {
        this.centerLableSpace = centerLableSpace;
    }

    public void setTagType(TAG_TYPE tagType) {
        this.tagType = tagType;
    }

    public void setTagModul(TAG_MODUL tagModul) {
        this.tagModul = tagModul;
    }

    public void setRingWidth(int ringWidth) {
        this.ringWidth = ringWidth;
    }

    public void setLineLenth(int lineLenth) {
        this.lineLenth = lineLenth;
    }

    public void setOutSpace(int outSpace) {
        this.outSpace = outSpace;
    }

    public void setTextSpace(int textSpace) {
        this.textSpace = textSpace;
    }

    public void setRectW(int rectW) {
        this.rectW = rectW;
    }

    public void setRectH(int rectH) {
        this.rectH = rectH;
    }

    public void setRectRaidus(int rectRaidus) {
        this.rectRaidus = rectRaidus;
    }

    public void setRectSpace(int rectSpace) {
        this.rectSpace = rectSpace;
    }

    public void setLeftSpace(int leftSpace) {
        this.leftSpace = leftSpace;
    }

    public void setTagTextSize(int tagTextSize) {
        this.tagTextSize = tagTextSize;
    }

    public void setTagTextColor(int tagTextColor) {
        this.tagTextColor = tagTextColor;
    }

    public void setLableTextSize(int lableTextSize) {
        this.lableTextSize = lableTextSize;
    }

    public void setLableTextColor(int lableTextColor) {
        this.lableTextColor = lableTextColor;
    }

    public void setArrColorRgb(int[][] arrColorRgb) {
        this.arrColorRgb = arrColorRgb;
    }

    /**
     * 设置数据
     */
    public void setChartData(Class clazz, String per, String name, List<? extends Object> dataList, List<ChartLable> lableList) {
        if (this.lableList != null) {
            this.lableList.clear();
        }
        this.lableList = lableList;
        this.dataList.clear();
        if (dataList != null) {
            try {
                Field filedPer = clazz.getDeclaredField(per);
                Field filedName = clazz.getDeclaredField(name);
                filedPer.setAccessible(true);
                filedName.setAccessible(true);
                for (Object obj : dataList) {
                    String perStr = filedPer.get(obj).toString();
                    PieChartBean bean = new PieChartBean(Integer.parseInt(perStr), (String) filedName.get(obj));
                    this.dataList.add(bean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setConfig();
    }


}
