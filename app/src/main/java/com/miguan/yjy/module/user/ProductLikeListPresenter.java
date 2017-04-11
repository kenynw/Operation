package com.miguan.yjy.module.user;

import com.dsk.chain.expansion.list.BaseListActivityPresenter;
import com.miguan.yjy.model.UserModel;
import com.miguan.yjy.model.bean.ProductList;
import com.miguan.yjy.model.bean.Product;

/**
 * Copyright (c) 2017/3/31. LiaoPeiKun Inc. All rights reserved.
 */

public class ProductLikeListPresenter extends BaseListActivityPresenter<ProductLikeListActivity, Product> {

    @Override
    protected void onCreateView(ProductLikeListActivity view) {
        super.onCreateView(view);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        UserModel.getInstance().getLikeProductList(1, "美白", 1)
                .map(ProductList::getData)
                .unsafeSubscribe(getRefreshSubscriber());
    }
}
