package com.tutao.practicedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by jingting on 2018/3/30.
 */

public class ThumbUpView extends LinearLayout implements View.OnClickListener{

    public static final float DEFAULT_DRAWABLE_PADDING = 4f;
    private ThumbView mThumbView;
    private CountView mCountView;

    private float mDrawablePadding;
    private int mTextColor;
    private int mCount;
    private float mTextSize;
    private boolean mlsThumbUp;
    private int mTopMargin;
    private boolean mNeedChangeChildView;

    public ThumbUpView(Context context) {
        this(context, null);
    }

    public ThumbUpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThumbUpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThumbUpView);
        mDrawablePadding = typedArray.getDimension(R.styleable.ThumbUpView_tuv_drawable_padding, TuvUtils.dip2px(context, DEFAULT_DRAWABLE_PADDING));
        mCount = typedArray.getInt(R.styleable.ThumbUpView_tuv_count, 0);
        mTextColor = typedArray.getColor(R.styleable.ThumbUpView_tuv_text_color, Color.parseColor(CountView.DEFAULT_TEXT_COLOR));
        mTextSize = typedArray.getDimension(R.styleable.ThumbUpView_tuv_drawable_padding, TuvUtils.sp2px(context, CountView.DEFAULT_TEXT_SIZE));
        mlsThumbUp = typedArray.getBoolean(R.styleable.ThumbUpView_tuv_isThumbUp, false);
        typedArray.recycle();
        init();
    }

    private void init() {
        removeAllViews();
        setClipChildren(false);
        setOrientation(LinearLayout.HORIZONTAL);

        addThumbView();
        addCountView();

        //把设置的padding分解到子view, 否则对超出view范围的动画显示不全
        setPadding(0, 0, 0, 0, false);
        setOnClickListener(this);
    }

    public ThumbUpView setCount(int count) {
        this.mCount = mCount;
        mCountView.setCount(mCount);
        return this;
    }

    public ThumbUpView setTextColor(int textColor) {
        mTextColor = textColor;
        mCountView.setTextColor(mCount);
        return this;
    }

    public ThumbUpView setTextSize(float mTextsize) {
        this.mTextSize = mTextsize;
        mCountView.setTextColor(mCount);
        return this;
    }

    public ThumbUpView setThumbUp(boolean isThumbUp) {
        this.mlsThumbUp = isThumbUp;
        mThumbView.setIsThumbUp(isThumbUp);
        return this;
    }


    public void setThumbUpClickListener(ThumbView.ThumbUpClickListener listener) {
        mThumbView.setThumbUpClickListener(listener);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        if (mNeedChangeChildView) {
            resetThumbParams();
            resetCountViewParams();
            mNeedChangeChildView = false;
        } else {
            super.setPadding(left, top, right, bottom);
        }
    }

    @SuppressWarnings("SameParameterValue")
    public void setPadding(int left, int top, int right, int bottom, boolean needChange) {
        this.mNeedChangeChildView = needChange;
        setPadding(left, top, right, bottom);
    }


    private void resetCountViewParams() {
        LayoutParams params = (LayoutParams) mCountView.getLayoutParams();
        if (mTopMargin > 0) {
            params.topMargin = mTopMargin;//设置这个距离是为了文字与拇指居中显示
        }
        params.leftMargin = (int) mDrawablePadding;
        params.topMargin = getPaddingTop();
        params.bottomMargin = getPaddingBottom();
        params.rightMargin = getPaddingRight();
        mCountView.setLayoutParams(params);
    }

    private void resetThumbParams() {
        LayoutParams params = (LayoutParams) mThumbView.getLayoutParams();
        if (mTopMargin < 0) {
            params.topMargin = mTopMargin;//为了文字和拇指居中显示
        }
        params.leftMargin = getPaddingLeft();
        params.topMargin += getPaddingTop();
        params.bottomMargin = getPaddingBottom();
        mThumbView.setLayoutParams(params);
    }

    private void addCountView() {
        mCountView = new CountView(getContext());
        mCountView.setTextColor(mTextColor);
        mCountView.setTextSize(mTextSize);
        mCountView.setCount(mCount);

        addView(mCountView, getCountParams());
    }

    private void addThumbView() {
        mThumbView = new ThumbView(getContext());
        mThumbView.setIsThumbUp(mlsThumbUp);
        TuvPoint circlePoint = mThumbView.getCirclePoint();
        mTopMargin = (int) (circlePoint.y - mTextSize / 2);
        addView(mThumbView, getThumbParams());
    }

    @Override
    public void onClick(View v) {
        mlsThumbUp = !mlsThumbUp;
        if (mlsThumbUp) {
            mCountView.calculateChangeNum(1);
        } else {
            mCountView.calculateChangeNum(-1);
        }
        mThumbView.startAnim();
    }

    public LayoutParams getThumbParams() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTopMargin < 0) {
            params.topMargin = mTopMargin;
        }
        params.leftMargin = getPaddingLeft();
        params.topMargin += getPaddingTop();
        params.bottomMargin = getPaddingBottom();
        return params;
    }

    private LayoutParams getCountParams() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTopMargin > 0) {
            params.topMargin = mTopMargin;
        }
        params.leftMargin = (int) mDrawablePadding;
        params.topMargin += getPaddingTop();
        params.bottomMargin = getPaddingBottom();
        params.rightMargin = getPaddingRight();
        return params;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle data = new Bundle();
        data.putParcelable("superData", super.onSaveInstanceState());
        data.putInt("count", mCount);
        data.putFloat("textSize", mTextSize);
        data.putInt("textColor", mTextColor);
        data.putBoolean("isThumbUp", mlsThumbUp);
        data.putFloat("drawablePadding", mDrawablePadding);
        return data;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Bundle data = (Bundle) state;
        Parcelable superData = data.getParcelable("superData");
        super.onRestoreInstanceState(superData);
        mCount = data.getInt("count");
        mTextSize = data.getFloat("textSize", TuvUtils.sp2px(getContext(), CountView.DEFAULT_TEXT_SIZE));
        mTextColor = data.getInt("textColor", Color.parseColor(mCountView.DEFAULT_TEXT_COLOR));
        mlsThumbUp = data.getBoolean("isThumbUp", false);
        mDrawablePadding = data.getFloat("drawablePadding", TuvUtils.sp2px(getContext(), DEFAULT_DRAWABLE_PADDING));
        init();
    }
}
