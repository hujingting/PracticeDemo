package com.example.ruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ShellRuler extends ViewGroup{

    private final String TAG = "ruler";
    private Context mContext;
    //内部的尺子
    private InnerRuler mInnerRuler;
    //最小最大刻度值(以0.1kg为单位)
    private int mMinScale = 464, mMaxScale = 2000;
    //中间光标画笔
    private Paint mCPaint, mOutLinePaint;
    //光标宽度、高度
    private int mCursorWidth = 8, mCursorHeight = 70;
    //大小刻度的长度
    private int mSmallScaleLength = 30, mBigScaleLength = 60;
    //大小刻度的粗细
    private int mSmallScaleWidth = 3, mBigScaleWidth = 5;
    //数字字体大小
    private int mTextSize = 28;
    //数字Text距离顶部高度
    private int mTextMarginTop = 120;
    //刻度间隔
    private int mInterval = 18;
    //数字Text颜色
    private
    @ColorInt
    int mTextColor = getResources().getColor(R.color.colorLightBlack);
    //刻度颜色
    private
    @ColorInt
    int mScaleColor = getResources().getColor(R.color.colorGray);
    //初始的当前刻度
    private float mCurrentScale = 0;
    //一格大刻度多少格小刻度
    private int mCount = 10;
    //光标drawable
    private Drawable mCursorDrawable;
    //尺子两端的padding
    private int mPaddingStartAndEnd = 0;


    public ShellRuler(Context context) {
        this(context, null);
    }

    public ShellRuler(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShellRuler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BooheeRuler, 0, 0);
        mMinScale = typedArray.getInteger(R.styleable.BooheeRuler_minScale, mMinScale);
        mMaxScale = typedArray.getInteger(R.styleable.BooheeRuler_maxScale, mMaxScale);
        mCursorWidth = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_cursorWidth, mCursorWidth);
        mCursorHeight = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_cursorHeight, mCursorHeight);
        mSmallScaleWidth = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_smallScaleWidth, mSmallScaleWidth);
        mSmallScaleLength = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_smallScaleLength, mSmallScaleLength);
        mBigScaleWidth = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_bigScaleWidth, mBigScaleWidth);
        mBigScaleLength = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_bigScaleLength, mBigScaleLength);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_numberTextSize, mTextSize);
        mTextMarginTop = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_textMarginTop, mTextMarginTop);
        mInterval = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_scaleInterval, mInterval);
        mTextColor = typedArray.getColor(R.styleable.BooheeRuler_numberTextColor, mTextColor);
        mScaleColor = typedArray.getColor(R.styleable.BooheeRuler_scaleColor, mScaleColor);
        mCurrentScale = typedArray.getFloat(R.styleable.BooheeRuler_currentScale, (mMaxScale + mMinScale) / 2);
        mCount = typedArray.getInt(R.styleable.BooheeRuler_count, mCount);
        mCursorDrawable = typedArray.getDrawable(R.styleable.BooheeRuler_cursorDrawable);
        if (mCursorDrawable == null) {
            mCursorDrawable = getResources().getDrawable(R.drawable.cursor_shape);
        }
        mPaddingStartAndEnd = typedArray.getDimensionPixelSize(R.styleable.BooheeRuler_paddingStartAndEnd, mPaddingStartAndEnd);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
