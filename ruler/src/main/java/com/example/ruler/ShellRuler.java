package com.example.ruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class ShellRuler extends ViewGroup {

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
        initRuler(context);
    }

    private void initRuler(Context context) {
        mContext = context;
        mInnerRuler = new InnerRuler(context, this);
        addView(mInnerRuler);

        mCPaint = new Paint();

        mOutLinePaint = new Paint();
        mOutLinePaint.setStrokeWidth(0);
        mOutLinePaint.setColor(mScaleColor);

        initDrawable();
    }

    private void initDrawable() {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                mCursorDrawable.setBounds((getWidth() - mCursorWidth) / 2, 0, (getWidth() + mCursorWidth) / 2, mCursorHeight);
                return false;
            }
        });
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int newWidthSize = widthSize - mPaddingStartAndEnd * 2;
        if (newWidthSize <= 0) {
            Log.d(TAG, "mPaddingStartAndEnd设置过大，设置无效！");
            newWidthSize = widthSize;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(newWidthSize, widthMode), MeasureSpec.makeMeasureSpec(heightSize, heightMode));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mInnerRuler.layout(0, 0, r - l, b - t);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画上面的轮廓线
        canvas.drawLine(0, 0, canvas.getWidth(), 0, mOutLinePaint);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //画中间的选定光标，要在这里画，因为dispatchDraw()执行在onDraw()后面，这样子光标才能不被尺子的刻度遮蔽
        mCursorDrawable.draw(canvas);
    }

    public Context getmContext() {
        return mContext;
    }

    public InnerRuler getmInnerRuler() {
        return mInnerRuler;
    }

    public int getmMinScale() {
        return mMinScale;
    }

    public int getmMaxScale() {
        return mMaxScale;
    }

    public Paint getmCPaint() {
        return mCPaint;
    }

    public Paint getmOutLinePaint() {
        return mOutLinePaint;
    }

    public int getmCursorWidth() {
        return mCursorWidth;
    }

    public int getmCursorHeight() {
        return mCursorHeight;
    }

    public int getmSmallScaleLength() {
        return mSmallScaleLength;
    }

    public int getmBigScaleLength() {
        return mBigScaleLength;
    }

    public int getmSmallScaleWidth() {
        return mSmallScaleWidth;
    }

    public int getmBigScaleWidth() {
        return mBigScaleWidth;
    }

    public int getmTextSize() {
        return mTextSize;
    }

    public int getmTextMarginTop() {
        return mTextMarginTop;
    }

    public int getmInterval() {
        return mInterval;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public int getmScaleColor() {
        return mScaleColor;
    }

    public float getmCurrentScale() {
        return mCurrentScale;
    }

    public int getmCount() {
        return mCount;
    }

    public Drawable getmCursorDrawable() {
        return mCursorDrawable;
    }

    public int getmPaddingStartAndEnd() {
        return mPaddingStartAndEnd;
    }
}
