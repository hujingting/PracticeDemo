package com.sweet.yicheng.scaleinviewpager.transformer;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class ScaleInTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_MIN_SCALE = 0.85f;
    public static final float DEFAULT_CENTER = 0.5f;

    private float mMinScale = DEFAULT_MIN_SCALE;

    @Override
    public void transformPage(View view, float position) {
        int pageWith = view.getWidth();
        int pageHeight = view.getHeight();

        Log.d("jt_" + view.getId(), position + "");
        //设置锚点
        view.setPivotX(pageWith / 2);
        view.setPivotY(pageHeight / 2);

        if (position < -1) {
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
//            view.setPivotX(pageWith);

        } else if (position <= 1) {

            if (position < 0) {
                float scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                view.setPivotX(pageWith * (DEFAULT_CENTER + (DEFAULT_CENTER * -position)));
            } else {
                float scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setPivotX(pageWith * (1 - position) * DEFAULT_CENTER);
            }

        } else {

            view.setPivotX(0);
            view.setScaleY(mMinScale);
            view.setScaleX(mMinScale);
        }



    }
}
