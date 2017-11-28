package com.allelink.wzyx.fragment.main.near;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allelink.wzyx.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author yangc
 * @version 1.0
 * @filename NearFragment
 * @date 2017/11/22
 * @description 附近 fragment
 * @email 1048027353@qq.com
 */

public class NearFragment extends SupportFragment{

    public static NearFragment newInstance() {
        Bundle args = new Bundle();
        NearFragment fragment = new NearFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near, container, false);
        return view;
    }
}
