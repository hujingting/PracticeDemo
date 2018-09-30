package com.sweet.yicheng.scaleinviewpager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.sweet.yicheng.scaleinviewpager.transformer.ScaleInTransformer;
import com.tutao.common.utils.CircleImageView;
import com.tutao.common.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author jingting
 */
public class MainActivity extends AppCompatActivity {

    private static final int PERIOD_TIME = 3000;
//    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    int[] bannerRes = new int[] {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
    List<View> views;

    private boolean toLoop;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PERIOD_TIME && toLoop) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ButterKnife.bind(this);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setPageMargin(30);

        initViews();

        MyAdapter myAdapter = new MyAdapter(this);

        mViewPager.setPageTransformer(false, new ScaleInTransformer());
        mViewPager.setAdapter(myAdapter);

        mViewPager.setCurrentItem(bannerRes.length / 2);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    toLoop = true;
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    toLoop = false;
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

                }
            }
        });
    }

    private void initViews() {
        views = new ArrayList<>();
        CircleImageView leftView = new CircleImageView(this);
//        leftView.setTag("leftView");
        views.add(leftView);

        CircleImageView middleView = new CircleImageView(this);
//        middleView.setTag("middleView");
        views.add(middleView);

        CircleImageView rightView = new CircleImageView(this);
//        middleView.setTag("rightView");
        views.add(rightView);

        BannerTimeTask timeTask = new BannerTimeTask();
        Timer timer = new Timer();
        timer.schedule(timeTask, PERIOD_TIME, PERIOD_TIME);
    }


    class MyAdapter extends PagerAdapter {

        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            CircleImageView imageView = (CircleImageView) views.get(position);
            imageView.setRound(10, 10);
            ImageUtils.loadLocalImage(context, bannerRes[position], imageView);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class BannerTimeTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(PERIOD_TIME);
        }
    }
}
