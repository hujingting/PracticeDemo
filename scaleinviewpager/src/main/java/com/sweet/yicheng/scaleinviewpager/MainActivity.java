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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author jingting
 */
public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    int[] bannerRes = new int[] {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
    List<View> views;

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
}
