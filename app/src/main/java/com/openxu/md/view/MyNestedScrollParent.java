package com.openxu.md.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.openxu.md.R;
import com.openxu.md.util.FLog;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

/**
 * author : openXu
 * created time : 19/3/25 下午9:02
 * blog : http://blog.csdn.net/xmxkf
 * github : http://blog.csdn.net/xmxkf
 * class name : MyNestedScrollParent
 * discription :
 */
public class MyNestedScrollParent extends RelativeLayout implements NestedScrollingParent {
    public MyNestedScrollParent(Context context) {
        this(context, null);
    }
    public MyNestedScrollParent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyNestedScrollParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private NestedScrollingParentHelper helper;
    private void init() {
        helper = new NestedScrollingParentHelper(this);
    }


    private ImageView imageView1, imageView2;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
    }

    /*************************实现NestedScrollingParent方法，交给NestedScrollingParentHelper处理*********************************/
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        helper.onNestedScrollAccepted(child, target, axes);
    }
    @Override
    public int getNestedScrollAxes() {
        return helper.getNestedScrollAxes();
    }
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        imageView1.setX(imageView1.getX()+dx/2);
        imageView1.setY(imageView1.getY()+dy/2);
        consumed[0] = dx/2;
        consumed[1] = dy/2;
        FLog.w("1、滚动前父控件处理("+dx+", "+dy+")  消耗("+consumed[0]+", "+consumed[1]+")");
        //NestedScrollingParentHelper中没有onNestedPreScroll，因为这个方法是需要父控件重写消费事件的，得自己实现
//        helper.onNestedPreScroll(target, dx, dy, consumed);
    }
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //子控件消耗事件后调用，父控件可以消费掉子控件未消费完的事件
        FLog.w("3、子控件消耗事件后父控件继续处理:已消耗("+dxConsumed+", "+dyConsumed+")  剩余未消耗("+dxUnconsumed+", "+dyUnconsumed+")");
        imageView2.setX(imageView2.getX()+dxUnconsumed);
        imageView2.setY(imageView2.getY()+dyUnconsumed);
    }
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        FLog.w("4、检测到快速滚动onNestedPreFling("+velocityY+" "+velocityY+")");
        return false;
    }
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        FLog.w("6、快速滑动onNestedFling:是否被消费了："+consumed);
        return consumed;
    }
    @Override
    public void onStopNestedScroll(View child) {
        FLog.w("7、嵌套滚动结束onStopNestedScroll");
        super.onStopNestedScroll(child);
    }
}
