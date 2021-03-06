package com.miguan.yjy.module.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.exgridview.ExGridView;
import com.miguan.yjy.R;
import com.miguan.yjy.adapter.BannerPagerAdapter;
import com.miguan.yjy.adapter.HomeHeaderAdapter;
import com.miguan.yjy.model.bean.Home;
import com.miguan.yjy.utils.LUtils;
import com.miguan.yjy.widget.CirclePageIndicator;
import com.miguan.yjy.widget.HeadViewPager;
import com.miguan.yjy.widget.LoadingImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Copyright (c) 2017/8/28. LiaoPeiKun Inc. All rights reserved.
 */

public class HomeHeader implements RecyclerArrayAdapter.ItemView {

    @BindView(R.id.hv_home_banner)
    HeadViewPager mHvBanner;

    @BindView(R.id.indicator_main_home)
    CirclePageIndicator mIndicator;

    @BindView(R.id.iv_home_evaluate_label)
    LoadingImageView mIvEvaluateLabel;

    @BindView(R.id.gv_home_category)
    ExGridView mGvCategory;

    private Context mContext;

    private Home mHome;

    private HomeHeaderAdapter mAdapter;

    public HomeHeader(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.header_main_home, parent, false);
        ButterKnife.bind(this, view);

        ViewGroup.LayoutParams lp = mHvBanner.getLayoutParams();
        lp.height = (int) (LUtils.getScreenWidth() / 1.7);
        mHvBanner.setLayoutParams(lp);

        mAdapter = new HomeHeaderAdapter(mContext);
        mGvCategory.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onBindView(View headerView) {
        if (mHome != null) {
            mAdapter.setLoading(false);
            mIvEvaluateLabel.setImageResource(R.mipmap.bg_commend_evaluate);

            if (mHome.getBanner() != null && mHome.getBanner().size() > 0) {
                mHvBanner.setAdapter(new BannerPagerAdapter(mContext, mHome.getBanner()));
                mIndicator.setViewPager(mHvBanner);
            }
            if (mHome.getBanner() != null && mHome.getBanner().size() <= 1)
                mIndicator.setVisibility(View.GONE);
        }
    }

    public void setHome(Home home) {
        mHome = home;
    }

}
