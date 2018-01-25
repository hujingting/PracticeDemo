package com.tutao.rxdemo.fragment;

import android.support.v4.app.Fragment;

import rx.Subscription;

/**
 * Created by jingting on 2017/9/21.
 */

public class BaseFragment extends Fragment {
    Subscription mSubscription;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }


}
