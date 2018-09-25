package com.sweet.yicheng.scaleinviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.sweet.yicheng.scaleinviewpager.transformer.ScaleInTransformer;
import com.tutao.common.utils.CircleImageView;
import com.tutao.common.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author jingting
 */
public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    int[] bannerRes = new int[] {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ButterKnife.bind(this);
        mViewPager = findViewById(R.id.view_pager);

        MyAdapter myAdapter = new MyAdapter(this);

        mViewPager.setPageTransformer(false, new ScaleInTransformer());
        mViewPager.setAdapter(myAdapter);
    }


    class MyAdapter extends PagerAdapter {

        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }


        @Override
        public int getCount() {
            return bannerRes.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            CircleImageView circleImageView = new CircleImageView(context);
            circleImageView.setRound(10, 10);
            ImageUtils.loadLocalImage(context, bannerRes[position], circleImageView);
            container.addView(circleImageView);

            return circleImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
