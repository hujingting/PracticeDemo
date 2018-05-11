package com.example.ruler;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ShellRuler extends ViewGroup{

    private Context context;
    //内部的尺子
    private InnerRuler innerRuler;
    //最小最大刻度值(以0.1克为单位)
    private int minScale = 464, maxScale = 2000;

    private Paint outLinePaint;

    private int cursorWidth, cursorHeight;

    private int smallScaleLength = 30, bigScaleLength = 60;

    private int smallScaleWidth, bigScaleWidth;

    private int textSize = 30;

    private int textMarginTop = 120;

    //刻度间隔
    private int interval = 18;



    public ShellRuler(Context context) {
        super(context);
    }

    public ShellRuler(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShellRuler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
