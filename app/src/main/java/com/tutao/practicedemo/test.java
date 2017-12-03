package com.tutao.practicedemo;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jingting on 2017/11/30.
 */

public class test extends View{

    private Paint mPaint;

    public test(Context context) {
        super(context);
    }

    public test(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public test(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public test(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        
    }
}
