package com.example.ruler;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
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

    public InnerRuler(Context context) {
        this(context, null);
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

//        maxLength =
    }

}
