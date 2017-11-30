package com.tutao.rxdemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutao.rxdemo.R;
import com.tutao.rxdemo.adapter.ZhuangbiListAdapter;
import com.tutao.rxdemo.model.ZhuangbiImage;
import com.tutao.rxdemo.network.Network;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jingting on 2017/9/21.
 */

public class ElementaryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

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

    Subscription mSubscription;

    public static ElementaryFragment newInstance() {
        Bundle args = new Bundle();
        ElementaryFragment fragment = new ElementaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();
    Observer<List<ZhuangbiImage>> mObserver = new Observer<List<ZhuangbiImage>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(List<ZhuangbiImage> zhuangbiImages) {
            mSwipeRefreshLayout.setRefreshing(false);
            adapter.setImages(zhuangbiImages);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_elementary_ui, container, false);
        ButterKnife.bind(this, rootView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);
        return rootView;
    }



    @Override
    public void onRefresh() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.rb1)
    public void onMRb1Clicked() {
        adapter.setImages(null);
        mSwipeRefreshLayout.setRefreshing(true);
        mSubscription = Network.getZhuangbiApi()
                .search(mRb1.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @OnClick(R.id.rb2)
    public void onMRb2Clicked() {
        adapter.setImages(null);
        mSwipeRefreshLayout.setRefreshing(true);
        mSubscription = Network.getZhuangbiApi()
                .search(mRb2.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @OnClick(R.id.rb3)
    public void onMRb3Clicked() {
        adapter.setImages(null);
        mSwipeRefreshLayout.setRefreshing(true);
        mSubscription = Network.getZhuangbiApi()
                .search(mRb2.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @OnClick(R.id.rb4)
    public void onMRb4Clicked() {
        adapter.setImages(null);
        mSwipeRefreshLayout.setRefreshing(true);
        mSubscription = Network.getZhuangbiApi()
                .search(mRb2.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }
}
