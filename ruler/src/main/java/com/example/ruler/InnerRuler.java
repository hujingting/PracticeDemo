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

    private Context context;
    //画笔
    private Paint smallScalePaint, bigScalePaint, textPaint;
    //当前刻度值
    private float currentScale = 0;
    //最大刻度数
    private int maxLength = 0;
    //长度，最小可滑动值, 最大可滑动值
    private int length, minPositionX = 0, maxPositionX = 0;
    //控制滑动
    private OverScroller overScroller;
    //记录落点
    private float lastX = 0;
    //惯性最大最小速度
    private int maximumVelocity, minimumVelocity;
    //速度获取
    private VelocityTracker velocityTracker;
    //一半宽度
    private int halfWidth = 0;
    //一个大刻度10格小刻度
    private int count = 10;
    //提前刻画量


    public InnerRuler(Context context) {
        super(context);
        init(context);
    }

    public InnerRuler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerRuler(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(Context context) {
        this.context = context;

//        maxLength =
    }

}
