package com.openxu.md.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.openxu.md.util.FLog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Author: openXu
 * Time: 2019/3/28 11:13
 * class: MySrl
 * Description:
 */
public class MySrl extends SwipeRefreshLayout {


    public MySrl(@NonNull Context context) {
        this(context, null);
    }

    public MySrl(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        FLog.w("1、滚动前父控件处理("+dx+", "+dy+")  消耗("+consumed[0]+", "+consumed[1]+")");
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        FLog.w("3、子控件消耗事件后父控件继续处理:已消耗("+dxConsumed+", "+dyConsumed+")  剩余未消耗("+dxUnconsumed+", "+dyUnconsumed+")");
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }
}
