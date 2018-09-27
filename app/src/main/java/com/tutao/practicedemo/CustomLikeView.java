package com.tutao.practicedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by jingting on 2018/3/28.
 */

public class CustomLikeView extends android.support.v7.widget.AppCompatImageView {

    Paint mPaint;
    int radius = 0;
    int alpha = 0;

    public CustomLikeView(Context context) {
        super(context);
    }

    public CustomLikeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomLikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.red_EE353A));
        mPaint.setAlpha(alpha);

//      ObjectAnimator objectAnimator = new ObjectAnimator()

    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2, mPaint);
    }
}
