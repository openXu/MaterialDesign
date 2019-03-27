package com.openxu.md.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * author : openXu
 * created time : 19/3/24 下午12:15
 * blog : http://blog.csdn.net/xmxkf
 * github : http://blog.csdn.net/xmxkf
 * class name : ButtonBehavior
 * discription :
 */
public class ButtonBehavior extends CoordinatorLayout.Behavior<Button> {
    public ButtonBehavior() {
        super();
    }
    public ButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, Button child, MotionEvent ev) {
        return true; //返回true表示将Button的事件委托给ButtonBehavior处理
    }
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, Button child, MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN || ev.getAction()==MotionEvent.ACTION_MOVE) {
            //处理Button的事件，让其跟随手指移动
            child.setTranslationX(ev.getX());
            child.setTranslationY(ev.getY());
        }
        return true;
    }
}
