package com.tutao.rxdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutao.rxdemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jingting on 2017/9/21.
 */

public class ZipFragment extends BaseFragment {

    @Bind(R.id.rb1)
    AppCompatRadioButton mRb1;
    @Bind(R.id.rb2)
    AppCompatRadioButton mRb2;
    @Bind(R.id.rb3)
    AppCompatRadioButton mRb3;
    @Bind(R.id.rb4)
    AppCompatRadioButton mRb4;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_elementary_ui, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.rb1)
    public void onMRb1Clicked() {
    }

    @OnClick(R.id.rb2)
    public void onMRb2Clicked() {
    }

    @OnClick(R.id.rb3)
    public void onMRb3Clicked() {
    }

    @OnClick(R.id.rb4)
    public void onMRb4Clicked() {
    }
}
