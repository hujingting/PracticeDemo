package com.tutao.rxdemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tutao.rxdemo.R;
import com.tutao.rxdemo.adapter.ItemListAdapter;
import com.tutao.rxdemo.model.Item;
import com.tutao.rxdemo.network.Network;
import com.tutao.rxdemo.utils.GankBeautyResultToItemsMapper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jingting on 2017/9/21.
 */

public class MapFragment extends BaseFragment {


    @BindView(R.id.pageTv)
    TextView mPageTv;
    @BindView(R.id.previousPageBt)
    AppCompatButton mPreviousPageBt;
    @BindView(R.id.nextPageBt)
    AppCompatButton mNextPageBt;
    @BindView(R.id.tipBt)
    Button mTipBt;
    @BindView(R.id.gridRv)
    RecyclerView mGridRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int page = 1;

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ItemListAdapter mAdapter = new ItemListAdapter();
    Observer<List<Item>> mObserver = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<Item> items) {
            mSwipeRefreshLayout.setRefreshing(false);
            mPageTv.setText(getString(R.string.page_with_number, page));
            mAdapter.setItems(items);

        }
    };

    @OnClick(R.id.previousPageBt)
    void previousPage() {
        loadPage(--page);
        if (page == 1) {
            mPreviousPageBt.setEnabled(false);
        }
    }

    @OnClick(R.id.nextPageBt)
    void nextPage() {
        loadPage(++page);
        if (page == 2) {
            mPreviousPageBt.setEnabled(true);
        }
    }

    private void loadPage(int page) {
        mSwipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        mSubscription = Network.getGankApi()
                .getBeauties(10, page)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);

        mGridRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mGridRv.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);
        return rootView;
    }

}
