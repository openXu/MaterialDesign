package com.openxu.md.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.openxu.md.util.FLog;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * author : openXu
 * created time : 19/3/24 下午12:15
 * blog : http://blog.csdn.net/xmxkf
 * github : http://blog.csdn.net/xmxkf
 * class name : TextBehavior
 * discription :
 */
public class TextBehavior extends CoordinatorLayout.Behavior<TextView> {
    public TextBehavior() {
        super();
        FLog.i("无参数构造方法");
    }
    public TextBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        FLog.i("两个参数构造方法");
    }
    //当CoordinatorLayout测量child的时候调用，如果这个方法返回了true，则不会调用child的measure()方法
    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, TextView child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        FLog.i("测量：onMeasureChild  child="+child);
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }
    //当CoordinatorLayout给child布局的时候调用
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, TextView child, int layoutDirection) {
        FLog.i("布局：onLayoutChild  child="+child);
        return super.onLayoutChild(parent, child, layoutDirection);
    }
    //当CoordinatorLayout将事件发送给child之前会调用此方法。如果Behavior希望拦截并接管事件流，则返回true。默认值总是返回false。
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, TextView child, MotionEvent ev) {
        FLog.i("事件拦截：onInterceptTouchEvent");
        return false;
    }
    // child的触摸事件响应之前会调用此方法，如果Behavior想要处理事件，应该返回true，默认返回false
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, TextView child, MotionEvent ev) {
        FLog.i("事件：onTouchEvent");
        return false;
    }


    /**************多个子控件的交互**************/


    /**
     * 确定child（设置了该Behavior的控件）是否让dependency（另一个同级子控件）作为布局依赖项，
     * 这个方法将至少调用一次以响应布局请求，如果返回true则将给定的子控件dependency作为依赖控件
     */
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        //如果dependency的类型是Button，则将他作为依赖
        return dependency instanceof Button;
    }
    /**
     * 当被依赖的控件dependency发生尺寸或位置变化时都会调用此方法，
     * 可以在此更新子控件,如果更新了子控件，请返回true
     */
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        //当Button位置变化时，让TextView跟着移动
        child.setTranslationX(dependency.getX()+50);
        child.setTranslationY(dependency.getY()+dependency.getHeight()+50);
        return true;
    }
    /**
     * 当dependency被移除出容器时调用此方法
     */
    @Override
    public void onDependentViewRemoved(@NonNull CoordinatorLayout parent, @NonNull TextView child, @NonNull View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }




}