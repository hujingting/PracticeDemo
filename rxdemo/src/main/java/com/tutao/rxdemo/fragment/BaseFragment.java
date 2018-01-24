package com.tutao.rxdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import rx.Subscription;

/**
 * Created by jingting on 2017/9/21.
 */

public class BaseFragment extends Fragment {
    Subscription mSubscription;


    public static BaseFragment newInstance() {

        Bundle args = new Bundle();

        BaseFragment fragment = new BaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
