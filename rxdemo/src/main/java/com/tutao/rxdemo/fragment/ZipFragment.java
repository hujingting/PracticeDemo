package com.tutao.rxdemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tutao.rxdemo.R;
import com.tutao.rxdemo.adapter.ItemListAdapter;
import com.tutao.rxdemo.model.Item;
import com.tutao.rxdemo.model.ZhuangbiImage;
import com.tutao.rxdemo.network.Network;
import com.tutao.rxdemo.utils.GankBeautyResultToItemsMapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by jingting on 2017/9/21.
 */

public class ZipFragment extends BaseFragment {


    @BindView(R.id.zipLoadBt)
    Button mZipLoadBt;
    @BindView(R.id.tipBt)
    Button mTipBt;
    @BindView(R.id.gridRv)
    RecyclerView mGridRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static ZipFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ZipFragment fragment = new ZipFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_zip, container, false);
        ButterKnife.bind(this, rootView);

        mGridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mGridRv.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setEnabled(false);
        return rootView;
    }

    ItemListAdapter mAdapter = new ItemListAdapter();
    Observer<List<Item>> mObserver = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(List<Item> items) {
            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.setItems(items);
        }
    };

    @OnClick(R.id.zipLoadBt)
    void load() {
        mSwipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        mSubscription = Observable.zip(Network.getGankApi().getBeauties(200, 1).map(GankBeautyResultToItemsMapper.getInstance()),
                Network.getZhuangbiApi().search("装逼"),
                new Func2<List<Item>, List<ZhuangbiImage>, List<Item>>() {
                    @Override
                    public List<Item> call(List<Item> beautyItems, List<ZhuangbiImage> zhuangbiImages) {
                        List<Item> items = new ArrayList<>();
                        for (int i = 0; i < beautyItems.size() / 2 && i < zhuangbiImages.size(); i++) {
                            items.add(beautyItems.get(i * 2));
                            items.add(beautyItems.get(i * 2 + 1));
                            Item zhuangbiItem = new Item();
                            ZhuangbiImage zhuangbiImage = zhuangbiImages.get(i);
                            zhuangbiItem.description = zhuangbiImage.description;
                            zhuangbiItem.imageUrl = zhuangbiImage.image_url;
                            items.add(zhuangbiItem);
                        }
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
