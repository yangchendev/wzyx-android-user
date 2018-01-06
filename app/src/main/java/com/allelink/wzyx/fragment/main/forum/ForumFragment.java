package com.allelink.wzyx.fragment.main.forum;

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
 * @filename ForumFragment
 * @date 2017/11/22
 * @description 论坛 fragment
 * @email 1048027353@qq.com
 */

public class ForumFragment extends SupportFragment{

    public static ForumFragment newInstance() {
        Bundle args = new Bundle();
        ForumFragment fragment = new ForumFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_forum, container, false);
        return view;
    }
}
