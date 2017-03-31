package com.miguan.yjy.module.user;

import android.view.ViewGroup;

import com.dsk.chain.bijection.RequiresPresenter;
import com.dsk.chain.expansion.list.BaseListActivity;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.miguan.yjy.R;
import com.miguan.yjy.adapter.viewholder.ProductLikeViewHolder;

/**
 * Copyright (c) 2017/3/30. LiaoPeiKun Inc. All rights reserved.
 */
@RequiresPresenter(UsedListPresenter.class)
public class UsedListActivity extends BaseListActivity<UsedListPresenter> {

    @Override
    protected BaseViewHolder createViewHolder(ViewGroup parent, int viewType) {
        return new ProductLikeViewHolder(parent);
    }

    @Override
    protected int getLayout() {
        return R.layout.common_activity_list;
    }

}