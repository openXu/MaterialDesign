package com.openxu.md.view.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.openxu.md.R;
import com.openxu.md.anim.AngleEvaluator;
import com.openxu.md.util.FLog;
import com.openxu.md.util.FontUtil;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import static com.openxu.md.view.chart.BaseChart.TOUCH_EVENT_TYPE.EVENT_NULL;


/**
 * autour : openXu
 * date : 2017/7/24 10:46
 * className : BaseChart
 * version : 1.0
 * description : 图表基类
 */
public abstract class BaseChart extends View implements NestedScrollingChild {

    protected String TAG = "BaseChart";
    protected int ScrWidth,ScrHeight;   //屏幕宽高
    protected RectF rectChart;          //图表矩形

    protected PointF centerPoint;       //chart中心点坐标

    protected int total;           //总数量

    /**可设置属性*/
    protected int startAngle = -90;  //开始的角度
    protected int backColor = Color.WHITE;
    protected int lineWidth = 1;     //辅助线宽度
    protected String loadingStr = "loading...";
    protected int defColor = Color.rgb(220, 220, 220);     //底色


    protected Paint paint;
    protected Paint paintLabel;

    /**动画相关统一属性，也可以设置，需要写set方法*/
    protected long animDuration = 1000;
    protected ValueAnimator anim;
    protected boolean startDraw = false;

    /**正在加载*/
    protected boolean isLoading = true;
    protected boolean drawLine = true;
    protected boolean drawBottomLine = true;
    protected boolean debug = true;


    /**手指抬起后，动画*/
    protected ValueAnimator touchAnim;
    protected GestureDetector mGestureDetector;

    NestedScrollingChildHelper childHelper;
    public void setLoading(boolean loading) {
        isLoading = loading;
        invalidate();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public BaseChart(Context context) {
        this(context, null);
    }

    public BaseChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        init(context, attrs, defStyle);
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public void init() {
        TAG = getClass().getSimpleName();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrHeight = dm.heightPixels;
        ScrWidth = dm.widthPixels;

        //画笔初始化
        paint = new Paint();
        paint.setAntiAlias(true);

        paintLabel = new Paint();
        paintLabel.setAntiAlias(true);

        mGestureDetector = new GestureDetector(getContext(), new MyOnGestureListener());
        childHelper = new NestedScrollingChildHelper(this);
        childHelper.setNestedScrollingEnabled(true);
        //启用或禁用嵌套滚动的方法，设置为true，并且当前界面的View的层次结构是支持嵌套滚动的(也就是需要NestedScrollingParent嵌套NestedScrollingChild)，才会触发嵌套滚动。一般这个方法内部都是直接代理给NestedScrollingChildHelper的同名方法即可。
        setNestedScrollingEnabled(true);
    }

    public abstract void init(Context context, AttributeSet attrs, int defStyleAttr);
    public int getTotal() {
        return total;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerPoint = new PointF(getMeasuredWidth()/2, getMeasuredHeight()/2);
        rectChart = new RectF(getPaddingLeft(),getPaddingTop(),getMeasuredWidth()-getPaddingRight(),
                getMeasuredHeight()-getPaddingBottom());
    }

    protected TOUCH_EVENT_TYPE touchEventType = EVENT_NULL;
    /**需要拦截的事件方向*/
    public enum TOUCH_EVENT_TYPE{
        EVENT_NULL,  /*不处理事件*/
        EVENT_X,  /*拦截X轴方向的事件*/
        EVENT_Y,  /*拦截Y轴方向的事件*/
        EVENT_XY  /*拦截XY轴方向的事件*/
    }
    /**设置事件方向*/
    public void setTouchEventType(TOUCH_EVENT_TYPE touchEventType) {
        this.touchEventType = touchEventType;
    }

    protected boolean touchEnable = false;      //是否超界，控件的大小是否足以显示内容，是否需要滑动来展示。子控件根据计算赋值
    protected float mDownX, mDownY;

    private final int[] mScrollOffset = new int[2];
    private final int[] consumed = new int[2];
    private int moveX, moveY;
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
               /* if (dispatchNestedPreScroll(moveX, moveY, consumed, mScrollOffset)) {
                    moveX -= consumed[0];
                    moveY -= consumed[1];
                    FLog.i("2、父控件消耗后剩余：("+moveX+", "+moveY+")");
                }*/
                //2、自己处理
                consumeBySelf(moveX, moveY, consumedBySelf);
                FLog.i("2、子控件自己消耗("+consumedBySelf[0]+", "+consumedBySelf[1]+")");
                //3、自己处理之后再交给父控件处理, 会调用NestedScrollingParent.onNestedScroll()
                if(dispatchNestedScroll(-consumedBySelf[0], -consumedBySelf[1],
                    consumedBySelf[0]-moveX, consumedBySelf[1]-moveY, mScrollOffset)){
                    FLog.i("3、父控件处理剩余的滚动：("+mScrollOffset[0]+", "+mScrollOffset[1]+")");
                    //💗：更新事件，防止出现抖动
                    event.offsetLocation(mScrollOffset[0], -mScrollOffset[1]);
                }

                lastTouchPoint.x = (int)event.getX();
                lastTouchPoint.y = (int)event.getY();
                onTouchMoved(lastTouchPoint);
                invalidate();
                result = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastTouchPoint.x = 0;
                lastTouchPoint.y = 0;
                onTouchMoved(null);
                //7、嵌套滚动结束, 会调用NestedScrollingParent.onStopNestedScroll()
                stopNestedScroll();
                break;
        }
        return result;

    }


    protected void onTouchMoved(PointF point){
    }

    public void onDraw(Canvas canvas){
        //画布背景
//        canvas.drawColor(backColor);
        if(debug) {
            drawDebug(canvas);
        }
        drawDefult(canvas);
        if(isLoading){
            drawLoading(canvas);
            return;
        }
        if(!startDraw){
            startDraw = true;
            startAnimation(canvas);
        }else{
            drawChart(canvas);
        }
    }


    public void drawLoading(Canvas canvas) {
        paintLabel.setTextSize(35);
        float NullTextLead = FontUtil.getFontLeading(paintLabel);
        float NullTextHeight = FontUtil.getFontHeight(paintLabel);
        float textY = centerPoint.y-NullTextHeight/2+NullTextLead;
        paintLabel.setColor(getContext().getResources().getColor(R.color.text_color_def));
        canvas.drawText(loadingStr, centerPoint.x- FontUtil.getFontlength(paintLabel, loadingStr)/2,  textY, paintLabel);
    }

    /**绘制图表基本框架*/
    public abstract void drawDefult(Canvas canvas);
    /**绘制debug辅助线*/
    public void drawDebug(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);//设置空心
        paint.setStrokeWidth(lineWidth);
        //绘制边界--chart区域
        paint.setColor(Color.BLUE);
        RectF r = new RectF(0,0,getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRect(r, paint);
        paint.setColor(Color.RED);
        canvas.drawRect(rectChart, paint);
    }
    /**绘制图表*/
    public abstract void drawChart(Canvas canvas);
    /**创建动画*/
    protected abstract ValueAnimator initAnim();
    /**动画值变化之后计算数据*/
    protected abstract void evaluatorData(ValueAnimator animation);


    public void setAnimDuration(long duration){
        this.animDuration = duration;
    }
    protected void startAnimation(Canvas canvas) {
        if(anim!=null){
            anim.cancel();
        }
//        FLog.w(TAG, "开始绘制动画");
        anim = initAnim();
        if(anim==null){
            drawChart(canvas);
        }else{
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.addUpdateListener((ValueAnimator animation)->{
                evaluatorData(animation);
                invalidate();
            });
            anim.setDuration(animDuration);
            anim.start();
        }
    }

    protected int[] consumedBySelf = new int[2];
    protected void consumeBySelf(int dx, int dy, int[] consumedBySelf){}

    protected int mMoveLen = 0;       //滚动距离
    /**手指松开后滑动动画效果*/
    protected void startFlingAnimation(float velocitX, float velocitY) {
        if(touchAnim!=null){
            touchAnim.cancel();
        }
        touchAnim = ValueAnimator.ofObject(new AngleEvaluator(), 1f, 0f);
        touchAnim.setInterpolator(new DecelerateInterpolator());   //越来越慢
        touchAnim.addUpdateListener((ValueAnimator animation)->{
            float velocityAnim = (float)animation.getAnimatedValue();
            consumeBySelf((int)(velocitX /100 * velocityAnim), (int)(velocitY /100 * velocityAnim), consumedBySelf);
            invalidate();
        });
        touchAnim.setDuration(1000+(int)+Math.max(Math.abs(velocitX), Math.abs(velocitY))/4);
        touchAnim.start();
    }


    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            FLog.e(TAG,"onFling------------>velocityX="+velocityX+"    velocityY="+velocityY);
            startFlingAnimation(velocityX, velocityY);
           /* if(EVENT_X == touchEventType){
                startFlingAnimation(velocityX, velocityY);
            }else if(EVENT_Y == touchEventType){
                startFlingAnimation(velocityY);
            }*/
            return false;
        }
    }



    /*******************嵌套滚动******************/

    //设置嵌套滑动是否能用
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
    }
    //判断嵌套滑动是否可用
    @Override
    public boolean isNestedScrollingEnabled() {
        return childHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }
    @Override
    public void stopNestedScroll() {
        childHelper.stopNestedScroll();
    }
    @Override
    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
