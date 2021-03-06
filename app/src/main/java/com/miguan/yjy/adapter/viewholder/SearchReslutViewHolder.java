package com.miguan.yjy.adapter.viewholder;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.miguan.yjy.R;
import com.miguan.yjy.model.bean.Product;
import com.miguan.yjy.module.product.ProductDetailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 cjh
 * @日期 2017/3/23 15:11
 * @描述 搜索结果（产品列表）
 */

public class SearchReslutViewHolder extends BaseViewHolder<Product> {

    @BindView(R.id.iv_prouduct_thumb)
    ImageView mDvThumb;

    @BindView(R.id.tv_product_name)
    TextView mTvName;

    @BindView(R.id.ratbar_product)
    RatingBar mRatbar;

    @BindView(R.id.tv_product_money)
    TextView mTvSpec;

    @BindView(R.id.tv_product_sort)
    TextView mTvProductSort;
    @BindView(R.id.ll_no_read)
    LinearLayout mLlNoRead;
    @BindView(R.id.tv_product_read)
    TextView mTvProductRead;
    @BindView(R.id.rl_product_info)
    RelativeLayout mRlProductInfo;

    private boolean mShowRank;

    public SearchReslutViewHolder(ViewGroup parent, boolean showRank) {
        this(parent, R.layout.item_product_list, showRank);
    }

    public SearchReslutViewHolder(ViewGroup parent, @LayoutRes int res) {
        this(parent, res, false);
    }

    public SearchReslutViewHolder(ViewGroup parent, @LayoutRes int res, boolean showRank) {
        super(parent, res);
        this.mShowRank = showRank;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setData(Product data) {
        if (mShowRank) {
            mTvProductSort.setText("No." + (getAdapterPosition() + 1));
            mTvProductSort.setVisibility(View.VISIBLE);
        } else {
            mTvProductSort.setVisibility(View.GONE);
        }
        mDvThumb.setImageURI(Uri.parse(data.getProduct_img()));
        mTvName.setText(data.getProduct_name());
        mRatbar.setRating(data.getStar());
        mTvSpec.setText(data.getSpec(getContext()));
        itemView.setOnClickListener(v -> ProductDetailPresenter.start(getContext(), data.getId()));
        if (TextUtils.isEmpty(data.getProduct_explain())) {
            mLlNoRead.setVisibility(View.VISIBLE);
            mTvProductRead.setVisibility(View.GONE);
        } else {
            mLlNoRead.setVisibility(View.GONE);
            mTvProductRead.setVisibility(View.VISIBLE);
            mTvProductRead.setText(data.getProduct_explain());
        }
    }

}
