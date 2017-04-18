package com.miguan.yjy.module.product;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsk.chain.bijection.RequiresPresenter;
import com.dsk.chain.expansion.data.BaseDataActivity;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.miguan.yjy.R;
import com.miguan.yjy.adapter.EvaluateAdapter;
import com.miguan.yjy.adapter.ProductComponentAdapter;
import com.miguan.yjy.model.bean.Component;
import com.miguan.yjy.model.bean.Evaluate;
import com.miguan.yjy.model.bean.Product;
import com.miguan.yjy.module.common.WebViewActivity;
import com.miguan.yjy.widget.FlowTagLayout;
import com.miguan.yjy.widget.SharePopupWindow;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 cjh
 * @日期 2017/3/22 10:11
 * @描述 产品详情页
 */
@RequiresPresenter(ProductDetailPresenter.class)
public class ProductDetailActivity extends BaseDataActivity<ProductDetailPresenter, Product> implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.iv_product_detail)
    ImageView mIvThumb;

    @BindView(R.id.tv_product_detail_name)
    TextView mTvName;

    @BindView(R.id.tv_product_detail_spec)
    TextView mTvSpec;

    @BindView(R.id.tv_product_date_detail)
    TextView mTvQueryDate;

    @BindView(R.id.tv_product_suit_num)
    TextView tvSuitNum;

    @BindView(R.id.tv_product_unsuit_num)
    TextView tvUnsuitNum;

    @BindView(R.id.tv_product_detail_record_inf0)
    TextView tvRecordInf0;

    @BindView(R.id.tv_product_detail_provision_no)
    TextView mTvProvisionNo;

    @BindView(R.id.tv_product_detail_country)
    TextView mTvCountry;

    @BindView(R.id.tv_product_detail_company)
    TextView mTvCompany;

    @BindView(R.id.ratbar_product_detail_grade)
    RatingBar ratbarGrade;

    @BindView(R.id.flowtag_product_detail_grade)
    FlowTagLayout flowtagGrade;

    @BindView(R.id.tv_product_detail_effect_info)
    TextView tvEffectInfo;

    @BindView(R.id.flowtag_product_detail_effect_info)
    FlowTagLayout flowtagEffectInfo;

    @BindView(R.id.tv_product_detail_lock_all_component)
    TextView tvLockAllComponent;

    @BindView(R.id.tv_product_user_evaluate_num)
    TextView tvUserEvaluteNum;

    @BindView(R.id.rgrp_evaluate_list_rank)
    RadioGroup mRgrpEvaluateRank;

    @BindView(R.id.tv_product_detail_like)
    TextView mTvLike;

    @BindView(R.id.tv_product_detail_homework)
    TextView tvHomework;

    @BindView(R.id.tv_product_detail_remark)
    TextView tvRemark;

    @BindView(R.id.ll_product_detail_info)
    LinearLayout ll_info;

    @BindView(R.id.ll_product_include_remark_info)
    LinearLayout llRemarkInfo;

    @BindView(R.id.iv_product_down)
    ImageView mIvProductDown;
    @BindView(R.id.tv_product_taobao)
    TextView mTvTaobao;
    @BindView(R.id.tv_product_jingdong)
    TextView mTvJingdong;
    @BindView(R.id.tv_product_amazon)
    TextView mTvAmazon;
    @BindView(R.id.recy_product_evaluate)
    EasyRecyclerView mRecyEvalutate;
    @BindView(R.id.tv_product_skin_sort)
    TextView mTvSkinSort;

    private ProductComponentAdapter mComponentAdapter;
    private ProductComponentAdapter mEffectAdapter;
    private EvaluateAdapter mEvaluateAdapter;
    private List<Evaluate> mEvaluates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity_detail);
        setToolbarTitle("产品详情");
        ButterKnife.bind(this);
        mRecyEvalutate.setFocusable(false);
        mRgrpEvaluateRank.setOnCheckedChangeListener(this);
    }

    @Override
    public void setData(Product product) {
        Glide.with(this).load(product.getProduct_img()).placeholder(R.mipmap.def_image_loading).error(R.mipmap.def_image_loading).into(mIvThumb);
        mTvName.setText(product.getProduct_name());
        mTvSpec.setText(String.format(getString(R.string.text_product_spec), product.getPrice(), product.getForm()));
        mTvQueryDate.setOnClickListener(v -> QueryCodePresenter.start(this, product));

        tvSuitNum.setText(String.format(getString(R.string.tv_product_fit_skin), product.getRecommend().size()));
        tvUnsuitNum.setText(String.format(getString(R.string.tv_product_no_fit_skin), product.getNotRecommend().size()));

        // 备案
        mTvProvisionNo.setText(product.getStandard_number());
        mTvCountry.setText(product.getProduct_country());
        mTvCompany.setText(product.getProduct_company());
        ratbarGrade.setRating(product.getStar());

        mComponentAdapter = new ProductComponentAdapter(ProductDetailActivity.this, product.getSecurity());
        flowtagGrade.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        flowtagGrade.setAdapter(mComponentAdapter);
        mComponentAdapter.onlyAddAll(product.getSecurity());
        mComponentAdapter.setSetOnTagClickListener(new ProductComponentAdapter.SetOnTagClickListener() {
            @Override
            public void itemClick(View v) {
                ProductReadPresenter.start(ProductDetailActivity.this, product);
            }
        });

        mEffectAdapter = new ProductComponentAdapter(ProductDetailActivity.this, product.getEffect());
        flowtagEffectInfo.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        flowtagEffectInfo.setAdapter(mEffectAdapter);
        mEffectAdapter.onlyAddAll(product.getEffect());

        mEffectAdapter.setSetOnTagClickListener(new ProductComponentAdapter.SetOnTagClickListener() {
            @Override
            public void itemClick(View v) {
                ProductReadPresenter.start(ProductDetailActivity.this, product);
            }
        });

        //去比价
        mTvTaobao.setOnClickListener(v -> WebViewActivity.satr(ProductDetailActivity.this, product.getProduct_name(), product.getBuy().getTaobao()));
        mTvJingdong.setOnClickListener(v -> WebViewActivity.satr(ProductDetailActivity.this, product.getProduct_name(), product.getBuy().getJd()));
        mTvAmazon.setOnClickListener(v -> WebViewActivity.satr(ProductDetailActivity.this, product.getProduct_name(), product.getBuy().getAmazon()));


        // 下面按钮
        mTvLike.setOnClickListener(v -> getPresenter().addLike());

        tvRemark.setOnClickListener(v -> ProductRemarkPresenter.start(this, product));
        Component component = new Component();
        tvLockAllComponent.setOnClickListener(v -> ProductComponentListPresenter.start(this, product));
        ll_info.setOnClickListener(this);

        tvSuitNum.setOnClickListener(v -> ProductReadPresenter.start(ProductDetailActivity.this, product));
        tvUnsuitNum.setOnClickListener(v -> ProductReadPresenter.start(ProductDetailActivity.this, product));
        mTvSkinSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTvSkinSort.getText().equals("肤质排序")) {
                    mTvSkinSort.setText(R.string.tv_product_detail_sort_total);
                    getPresenter().getEvaluateData(getPresenter().getUserEvluate(),getPresenter().SORT_DEFAULT);
                } else {
                    mTvSkinSort.setText(R.string.tv_product_detail_sort_skin);
                    getPresenter().getEvaluateData(getPresenter().getUserEvluate(), getPresenter().SORT_SKIN);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharePopupWindow popupWindow = new SharePopupWindow(this);
        popupWindow.showAtLocation(getToolbar(), Gravity.BOTTOM, 0, 0);
        return super.onOptionsItemSelected(item);
    }

    private void showSharePopWindow() {

    }

    @Override
    public void onClick(View v) {
        if (llRemarkInfo.getVisibility() == View.VISIBLE) {
            llRemarkInfo.setVisibility(View.GONE);
            mIvProductDown.setBackgroundResource(R.mipmap.ic_product_detail_up);
        } else {
            llRemarkInfo.setVisibility(View.VISIBLE);
            mIvProductDown.setBackgroundResource(R.mipmap.ic_product_detail_record_down);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_product_high_evaluate:
                getPresenter().setOrder("praise");
                getPresenter().getEvaluateData(getPresenter().START_PRAISE, getPresenter().getSort());
                break;
            case R.id.rbtn_product_medium_evaluate:
                getPresenter().setOrder("middle");
                getPresenter().getEvaluateData(getPresenter().START_MIDDLE, getPresenter().getSort());
                break;
            case R.id.rbtn_product_bad_evaluate:
                getPresenter().setOrder("bad");
                getPresenter().getEvaluateData(getPresenter().START_BAD, getPresenter().getSort());
                break;
        }
    }

    public void setEvaluate(List<Evaluate> list) {
        mEvaluateAdapter = new EvaluateAdapter(ProductDetailActivity.this, list);
        mRecyEvalutate.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyEvalutate.setAdapter(mEvaluateAdapter);
        tvUserEvaluteNum.setText(String.format(getString(R.string.tv_product_detail_user_evaluate), list.size()));

    }


}
