package com.openxu.md.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openxu.md.anim.AngleEvaluator;
import com.openxu.md.util.FLog;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

/**
 * author : openXu
 * created time : 19/3/25 下午9:02
 * blog : http://blog.csdn.net/xmxkf
 * github : http://blog.csdn.net/xmxkf
 * class name : MyNestedScrollChild
 * discription :
 */
public class MyNestedScrollChild extends TextView implements NestedScrollingChild {
    public MyNestedScrollChild(Context context) {
        this(context, null);
    }
    public MyNestedScrollChild(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyNestedScrollChild(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private NestedScrollingChildHelper helper;
    private void init() {
        helper = new NestedScrollingChildHelper(this);
        mGestureDetector = new GestureDetector(getContext(), new MyOnGestureListener());
        /**
         * 子控件开启嵌套滚动
         * 发现如果不实现NestedScrollingChild也可以调用这个方法，因为我项目的targetSdkVersion=28 >21 ，View中已经实现了嵌套滚动相关功能
         * 但是minSdkVersion = 15，为了兼容5.0以下版本，老老实实实现NestedScrollingChild，并覆盖接口中的方法，交给NestedScrollingChildHelper处理
         */
        setNestedScrollingEnabled(true);
    }
    private final int[] mScrollOffset = new int[2];
    private final int[] consumed = new int[2];
    private int moveX, moveY;
    private int consumedX, consumedY;
    protected PointF lastTouchPoint;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastTouchPoint = new PointF(event.getX(), event.getY());
                consumed[0] = 0;
                consumed[1] = 0;
                //开始嵌套滚动，会调用NestedScrollingParent.onStartNestedScroll()
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL);
                result = true;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int)(event.getX() - lastTouchPoint.x);
                moveY = (int)(event.getY() - lastTouchPoint.y);
                FLog.i("--->滑动事件移动距离：("+moveX+", "+moveY+")");
                //1、给父控件先处理，会调用NestedScrollingParent.onNestedPreScroll()
                if (dispatchNestedPreScroll(moveX, moveY, consumed, mScrollOffset)) {
                    moveX -= consumed[0];
                    moveY -= consumed[1];
                }
                //2、自己处理
                consumedX = moveX/2;
                consumedY = moveY/2;
                FLog.i("2、父控件消耗后剩余：("+moveX+", "+moveY+") 子控件自己消耗("+consumedX+", "+consumedY+")");
                setX(getX()+consumedX);
                setY(getY()+consumedY);
                //3、自己处理之后再交给父控件处理, 会调用NestedScrollingParent.onNestedScroll()
                dispatchNestedScroll(consumedX, consumedY, moveX-consumedX, moveY-consumedY, mScrollOffset);
                lastTouchPoint.x = (int)event.getX();
                lastTouchPoint.y = (int)event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //7、嵌套滚动结束, 会调用NestedScrollingParent.onStopNestedScroll()
                stopNestedScroll();
                result = super.onTouchEvent(event);
                break;
        }
        return result;

    }
    private GestureDetector mGestureDetector;
    protected ValueAnimator anim;
    //监听快速滑动
    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //4、告诉父级发生快速滑动了,让父级先处理, 会调用NestedScrollingParent.onNestedPreFling()
            if(!dispatchNestedPreFling(velocityX, velocityY)){
                //5、父级没有处理时，自己处理
                FLog.i("5、自己处理快速滑动("+velocityX+", "+velocityY+")");
                startFlingAnimation(velocityX, velocityY);
                //6、告诉父级发生快速滑动了, 事件已被消费了。会调用NestedScrollingParent.onNestedPreFling()
                dispatchNestedFling(velocityX, velocityY, true);
            }
            return false;
        }
    }
    /**快速滑动后，根据滑动速度开启一个动画，实现子控件滑动*/
    protected void startFlingAnimation(float velocitX, float velocitY) {
        if(anim!=null)
            anim.cancel();
        anim = ValueAnimator.ofObject(new AngleEvaluator(), 1f, 0f);
        anim.setInterpolator(new DecelerateInterpolator());   //越来越慢
        anim.addUpdateListener((ValueAnimator animation)->{
            float velocityAnim = (float)animation.getAnimatedValue();
            setX(getX()+(int)(velocitX /100 * velocityAnim));
            setY(getY()+(int)(velocitY /100 * velocityAnim));
            invalidate();
        });
        anim.setDuration(1000+(int)+Math.max(Math.abs(velocitX), Math.abs(velocitY))/4);
        anim.start();
    }

    /*************************实现NestedScrollingChild方法，交给NestedScrollingChildHelper处理*********************************/
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        helper.setNestedScrollingEnabled(enabled);
    }
    @Override
    public boolean isNestedScrollingEnabled() {
        return helper.isNestedScrollingEnabled();
    }
    @Override
    public boolean startNestedScroll(int axes) {
        return helper.startNestedScroll(axes);
    }
    @Override
    public void stopNestedScroll() {
        helper.stopNestedScroll();
    }
    @Override
    public boolean hasNestedScrollingParent() {
        return helper.hasNestedScrollingParent();
    }
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return helper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return helper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return helper.dispatchNestedFling(velocityX, velocityY, consumed);
    }
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return helper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
