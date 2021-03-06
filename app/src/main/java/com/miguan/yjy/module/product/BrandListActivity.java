package com.miguan.yjy.module.product;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.dsk.chain.bijection.RequiresPresenter;
import com.dsk.chain.expansion.list.BaseListActivity;
import com.dsk.chain.expansion.list.ListConfig;
import com.github.promeg.pinyinhelper.Pinyin;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.miguan.yjy.R;
import com.miguan.yjy.adapter.viewholder.BrandLetterViewHolder;
import com.miguan.yjy.adapter.viewholder.BrandViewHolder;
import com.miguan.yjy.model.bean.Brand;
import com.miguan.yjy.module.user.FeedbackActivity;
import com.miguan.yjy.widget.ScrollIndexer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Copyright (c) 2017/3/28. LiaoPeiKun Inc. All rights reserved.
 */
@RequiresPresenter(BrandListPresenter.class)
public class BrandListActivity extends BaseListActivity<BrandListPresenter> implements TextWatcher {

    @BindView(R.id.indexer_brand_list)
    ScrollIndexer mIndexer;

    @BindView(R.id.tv_brand_list_index)
    TextView mTvIndex;

    @BindView(R.id.et_brand_list_search)
    EditText mEtSearch;

    @BindView(R.id.tv_brand_list_clear)
    TextView mTvClear;

    private SearchListTask mSearchListTask;

    private boolean mCanAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(R.string.text_brand_list);
        ButterKnife.bind(this);

        mTvClear.setOnClickListener(v -> finish());
        mEtSearch.addTextChangedListener(this);
        mIndexer.setOnTextSelectedListener(new ScrollIndexer.OnTextSelectedListener() {
            @Override
            public void onTextSelected(int position, String text) {
                mTvIndex.setVisibility(View.VISIBLE);
                mTvIndex.setText(text);

                for (int i = 0; i < getPresenter().getAdapter().getCount(); i++) {
                    if (text.equals(getPresenter().getAdapter().getItem(i).getLetter())) {
                        scrollPosition(i);
                        break;
                    }
                }
            }

            @Override
            public void onTouchUp() {
                mTvIndex.setVisibility(View.GONE);
            }

        });
    }

    @Override
    protected BaseViewHolder createViewHolder(ViewGroup parent, int viewType) {
        BrandViewHolder viewHolder = viewType == 1 ? new BrandViewHolder(parent) : new BrandLetterViewHolder(parent);
        viewHolder.setOnBrandDeleteListener(getPresenter());
        return viewHolder;
    }

    @Override
    public ListConfig getListConfig() {
        View emptyView;

        mCanAdd = getIntent().getBooleanExtra(BrandListPresenter.EXTRA_CAN_ADD, false);
        if (mCanAdd) {
            emptyView = View.inflate(this, R.layout.empty_brand_list, null);
        } else {
            emptyView = View.inflate(this, R.layout.empty_brand_list_reback, null);
            emptyView.findViewById(R.id.tv_empty_brand_reback)
                    .setOnClickListener(v -> startActivity(new Intent(BrandListActivity.this, FeedbackActivity.class)));
        }

        return super.getListConfig().setRefreshAble(false)
                .setContainerEmptyView(emptyView)
                .setLoadMoreAble(false)
                .setFooterNoMoreRes(R.layout.include_default_footer)
                .setContainerLayoutRes(R.layout.product_brand_list_activity);
    }

    @Override
    public int getViewType(int position) {
        if (position == 0) return 2;
        String curLetter = getPresenter().getAdapter().getItem(position).getLetter();
        String lastLetter = getPresenter().getAdapter().getItem(position - 1).getLetter();
        return TextUtils.equals(curLetter, lastLetter) ? 1 : 2;
    }

    private void scrollPosition(int index) {
        int firstPosition = getLayoutManager().findFirstVisibleItemPosition();
        int lastPosition = getLayoutManager().findLastVisibleItemPosition();
        if (index <= firstPosition) {
            getListView().scrollToPosition(index);
        } else if (index <= lastPosition) {
            int top = getListView().getRecyclerView().getChildAt(index - firstPosition).getTop();
            getListView().getRecyclerView().scrollBy(0, top);
        } else {
            getListView().scrollToPosition(index);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String keywords = s.toString().trim().toUpperCase();
        if (mSearchListTask != null && mSearchListTask.getStatus() == AsyncTask.Status.FINISHED) {
            try {
                mSearchListTask.cancel(true);
            } catch (Exception e) {

            }
        }
        mSearchListTask = new SearchListTask();
        mSearchListTask.execute(keywords);
    }

    public void selectedFinish(Brand brand) {
        Intent intent = new Intent();
        intent.putExtra("brand", brand);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private class SearchListTask extends AsyncTask<String, Void, String> {

        private List<Brand> mFilterList = new ArrayList<>();

        private boolean mInSearchMode;

        @Override
        protected String doInBackground(String... params) {
            mFilterList.clear();

            String keyword = params[0];

            mInSearchMode = (keyword.length() > 0);

            if (mInSearchMode && getPresenter().getBrandList() != null) {
                for (Brand brand : getPresenter().getBrandList()) {
                    boolean isPinyin = brand.getLetter().contains(keyword);
                    boolean isChinese = brand.getName().contains(keyword);
                    if (isChinese || isPinyin) mFilterList.add(brand);
                }
            }

            return keyword;
        }

        @Override
        protected void onPostExecute(String result) {
            getPresenter().getAdapter().clear();
            getPresenter().getAdapter().addAll(mInSearchMode ? mFilterList : getPresenter().getBrandList());
            mIndexer.setVisibility(mInSearchMode ? View.GONE : View.VISIBLE);

            if (mCanAdd && mInSearchMode && mFilterList.size() == 0) {
                mTvClear.setText("添加");
                mTvClear.setOnClickListener(v -> {
                    Brand brand = new Brand();
                    brand.setName(result);
                    brand.setLetter(getLetter(result));
                    brand.setLocal(true);
                    getPresenter().insertBrand(brand);
                    selectedFinish(brand);
                });
            } else {
                mTvClear.setText("取消");
                mTvClear.setOnClickListener(v -> finish());
            }

        }

        private String getLetter(String string) {
            char charLetter = Pinyin.toPinyin(string.charAt(0)).charAt(0);
            return Character.isLetter(charLetter) ? String.valueOf(charLetter) : "#";
        }

    }

    @Override
    public int[] getHideSoftViewIds() {
        return new int[]{R.id.et_brand_list_search};
    }

}
