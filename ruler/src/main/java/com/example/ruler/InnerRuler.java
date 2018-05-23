package com.example.ruler;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.OverScroller;

/**
 * Created by apple on 2018/5/11.
 */

public class InnerRuler extends View{

    private final String TAG = "ruler";
    private Context mContext;
    //画笔
    private Paint mSmallScalePaint, mBigScalePaint, mTextPaint;
    //当前刻度值
    private float mCurrentScale = 0;
    //最大刻度数
    private int mMaxLength = 0;
    //长度、最小可滑动值、最大可滑动值
    private int mLength, mMinPositionX = 0, mMaxPositionX = 0;
    //控制滑动
    private OverScroller mOverScroller;
    //记录落点
    private float mLastX = 0;
    //拖动阈值,这里没有使用它，用了感觉体验不好
    private int mTouchSlop;
    //惯性最大最小速度
    private int mMaximumVelocity, mMinimumVelocity;
    //速度获取
    private VelocityTracker mVelocityTracker;
    //一半宽度
    private int mHalfWidth = 0;
    //回调接口
//    private RulerCallback mRulerCallback;
    //一格大刻度多少格小刻度
    private int mCount = 10;
    //提前刻画量
    private int mDrawOffset = 0;

    private ShellRuler mParent;

    public InnerRuler(Context context, ShellRuler shellRuler) {
        super(context);
        this.mParent = shellRuler;
        init(context);
    }

    public InnerRuler(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InnerRuler(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        mMaxLength = mParent.getmMaxScale() - mParent.getmMinScale();
        mCurrentScale = mParent.getmCurrentScale();
        mCount = mParent.getmCount();
        mDrawOffset = mCount * mParent.getmInterval() / 2;

        initPaints();

        mOverScroller = new OverScroller(context);
        //配置速度
        mVelocityTracker = VelocityTracker.obtain();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        //第一次进入，跳转到设定刻度
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                goToScale(mCurrentScale);
            }
        });
    }

    private void goToScale(float scale) {
        mCurrentScale = Math.round(scale);
//        scrollTo();
    }

    //把滑动偏移量scrollX 转化为刻度scale
    private float scrollXtoScale(int scrollX) {
        return ((float) (scrollX - mMinPositionX) / mLength) * mMaxLength + mParent.getmMinScale();
    }

    //把Scale转化为ScrollX
    private int scaleToScrollX(float scale) {
        return (int) ((scale - mParent.getmMinScale()) / mMaxLength * mLength + mMinPositionX);
    }

    //把移动后光标对准距离最近的刻度，就是回弹到最近刻度
    private void scrollBackToCurrentScale() {
        //渐变回弹
        mCurrentScale = Math.round(mCurrentScale);
        mOverScroller.startScroll(getScrollX(), 0, scaleToScrollX(mCurrentScale) - getScrollX(), 0, 1000);
        invalidate();

        //立刻回弹
//        scrollTo(scaleToScrollX(mCurrentScale),0);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(mOverScroller.getCurrX(), mOverScroller.getCurrY());

            //这是最后OverScroller的最后一次滑动，如果这次滑动完了mCurrentScale不是整数，则把尺子移动到最近的整数位置
            if (!mOverScroller.computeScrollOffset() && mCurrentScale != Math.round(mCurrentScale)) {
                //Fling完进行一次检测回滚
                scrollBackToCurrentScale();
            }
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLength = (mParent.getmMaxScale() - mParent.getmMinScale()) * mParent.getmInterval();
        mHalfWidth = w / 2;
        mMinPositionX = -mHalfWidth;
        mMaxPositionX = mLength - mHalfWidth;
    }

    private void initPaints() {
        mSmallScalePaint = new Paint();
        mSmallScalePaint.setStrokeWidth(mParent.getmSmallScaleWidth());
        mSmallScalePaint.setColor(mParent.getmScaleColor());
        mSmallScalePaint.setStrokeCap(Paint.Cap.ROUND);

        mBigScalePaint = new Paint();
        mBigScalePaint.setStrokeWidth(mParent.getmBigScaleWidth());
        mBigScalePaint.setColor(mParent.getmScaleColor());
        mBigScalePaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mParent.getmTextColor());
        mTextPaint.setTextSize(mParent.getmTextSize());
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    //API小于18则关闭硬件加速，否则setAntiAlias()方法不生效
    private void checkAPILevel() {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(LAYER_TYPE_NONE, null);
        }
     }
}
