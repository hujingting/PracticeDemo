package com.tutao.rxdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tutao.rxdemo.fragment.ElementaryFragment;
import com.tutao.rxdemo.fragment.MapFragment;
import com.tutao.rxdemo.fragment.ZipFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @Bind(android.R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    String[] tabString = {"基本", "转换", "压合"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(ElementaryFragment.newInstance());
        fragments.add(MapFragment.newInstance());
        fragments.add(ZipFragment.newInstance());

        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return tabString.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
               return tabString[position];
            }
        });

        mTabs.setupWithViewPager(mViewPager);
    }
}
