package com.tutao.practicedemo.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tutao.practicedemo.R;
import com.tutao.practicedemo.model.TuvPoint;
import com.tutao.practicedemo.util.TuvUtils;

/**
 * Created by jingting on 2018/3/30.
 */

@SuppressWarnings("unused")
public class CountView extends View {
    public static final String DEFAULT_TEXT_COLOR = "#cccccc";
    public static final float DEFAULT_TEXT_SIZE = 15f;
    private static final int COUNT_ANIM_DURING = 250;

    private Paint mTextPaint;
    private float mTextSize;
    private int mTextColor;
    private int mEndTextColor;

    private int mCount;
    //mTexts[0]是不变的部分,mTexts[1]原来的部分,mTexts[2]变化后的部分
    private String[] mTexts;
    //表示各部分的坐标
    private TuvPoint[] mTextPoints;

    private float mMaxOffsetY;
    private float mMinOffsetY;

    private float mOldOffsetY;
    private float mNewOffsetY;
    private float mFraction;

    private boolean mCountToBigger;

    public CountView(Context context) {
        this(context, null);
    }

    public CountView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountView);
        mCount = typedArray.getInt(R.styleable.CountView_cv_count, 0);
        mTextColor = typedArray.getColor(R.styleable.CountView_cv_text_color, Color.parseColor(DEFAULT_TEXT_COLOR));
        mTextSize = typedArray.getDimension(R.styleable.CountView_cv_text_size, TuvUtils.sp2px(context, 15f));
        typedArray.recycle();
        init();
    }

    private void init() {
        mTexts = new String[3];
        mTextPoints = new TuvPoint[3];
        mTextPoints[0] = new TuvPoint();
        mTextPoints[1] = new TuvPoint();
        mTextPoints[2] = new TuvPoint();
        calculateChangeNum(0);

        mMinOffsetY = 0;
        mMaxOffsetY = mTextSize;

        mEndTextColor = Color.argb(0, Color.red(mTextColor), Color.green(mTextColor), Color.blue(mTextColor));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
        calculateChangeNum(0);
        requestLayout();
    }

    private int getContentWidth() {
        return (int) Math.ceil(mTextPaint.measureText(String.valueOf(mCount)));
    }

    private int getContentHeight() {
        return (int) mTextSize;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        mEndTextColor = Color.argb(0, Color.red(mTextColor), Color.green(mTextColor), Color.blue(mTextColor));
        postInvalidate();
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        requestLayout();
    }

    public void setTextOffsetY(float offsetY) {
        this.mOldOffsetY = offsetY;
        if (mCountToBigger) {
            this.mNewOffsetY = offsetY - mMaxOffsetY;
        } else {
            this.mNewOffsetY = mMaxOffsetY + offsetY;
        }
        mFraction = (mMaxOffsetY - Math.abs(mOldOffsetY)) / (mMaxOffsetY - mMinOffsetY);
        calculateLocation();
        postInvalidate();
    }

    public void calculateLocation() {
        String text = String.valueOf(mCount);
        float textWidth = mTextPaint.measureText(text) / text.length();
        float unChangeWidth = textWidth * mTexts[0].length();

        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        float y = getPaddingTop() + (getContentHeight() - fontMetricsInt.bottom - fontMetricsInt.top) / 2;

        mTextPoints[0].x = getPaddingLeft();
        mTextPoints[1].x = getPaddingLeft() + unChangeWidth;
        mTextPoints[2].x = getPaddingLeft() + unChangeWidth;

        mTextPoints[0].y = y;
        mTextPoints[1].y = y - mOldOffsetY;
        mTextPoints[2].y = y - mNewOffsetY;
    }

    /**
     * 计算不变，原来，和改变后各部分的数字
     *
     * @param i
     */
    public void calculateChangeNum(int change) {
        if (change == 0) {
            mTexts[0] = String.valueOf(mCount);
            mTexts[1] = "";
            mTexts[2] = "";
            return;
        }

        String oldNum = String.valueOf(mCount);
        String newNum = String.valueOf(mCount + change);

        for (int i = 0; i < oldNum.length(); i++) {
            char oldC = oldNum.charAt(i);
            char newC = newNum.charAt(i);
            if (oldC != newC) {
                //不变的部分
                mTexts[0] = i == 0 ? "" : newNum.substring(0, i);
                //原来的部分
                mTexts[1] = oldNum.substring(i);
                //变化后的部分
                mTexts[2] = newNum.substring(i);
                Log.d("TAG", "不变的部分" + mTexts[0] + "原来的部分" + mTexts[1] + "变化后的部分" + mTexts[2]);
                break;
            }
        }
        mCount += change;
        startAnim(change > 0);
    }

    private void startAnim(boolean isToBigger) {
        mCountToBigger = isToBigger;
        ObjectAnimator textOffsetY = ObjectAnimator.ofFloat(this, "textOffsetY",
                mMinOffsetY, mCountToBigger ? mMaxOffsetY : -mMaxOffsetY);
        textOffsetY.setDuration(COUNT_ANIM_DURING);
        textOffsetY.start();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle data = new Bundle();
        data.putParcelable("superData", super.onSaveInstanceState());
        data.putInt("count", mCount);
        data.putFloat("textSize", mTextSize);
        data.putInt("textColor", mTextColor);
        return data;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle data = (Bundle) state;
        Parcelable superData = data.getParcelable("superData");
        super.onRestoreInstanceState(state);

        mCount = data.getInt("count", 0);
        mTextSize = data.getFloat("textSize", TuvUtils.sp2px(getContext(), DEFAULT_TEXT_SIZE));
        mTextColor = data.getInt("textColor", Color.parseColor(DEFAULT_TEXT_COLOR));

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //不变的部分
        mTextPaint.setColor(mTextColor);
        canvas.drawText(String.valueOf(mTexts[0]), mTextPoints[0].x, mTextPoints[0].y, mTextPaint);

        //变化前部分
        mTextPaint.setColor((Integer) TuvUtils.evaluate(mFraction, mEndTextColor, mTextColor));
        canvas.drawText(String.valueOf(mTexts[1]), mTextPoints[1].x, mTextPoints[1].y, mTextPaint);

        //变化后部分
        mTextPaint.setColor((Integer) TuvUtils.evaluate(mFraction, mTextColor, mEndTextColor));
        canvas.drawText(String.valueOf(mTexts[2]), mTextPoints[2].x, mTextPoints[2].y, mTextPaint);
    }
}
