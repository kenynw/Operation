package com.miguan.yjy.module.template;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.miguan.yjy.model.bean.Product;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2017/4/5. LiaoPeiKun Inc. All rights reserved.
 */

public class TemplateViewHolder extends BaseViewHolder<Product> implements View.OnClickListener {

    public TemplateViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        ButterKnife.bind(this, itemView);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onImageClick(getAdapterPosition());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setFilter(FilterEvent event) {
        if (event.mApplyAll || (getAdapterPosition() == event.mPosition)) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setGpuImage(UriEvent uri) {
        if (uri.position == getAdapterPosition()) {
//            mDvImage.setImage(uri.mUri);
        }
    }

    public static class FilterEvent {

        final PipelineDraweeController mController;

        final boolean mApplyAll;

        final int mPosition;

        public FilterEvent(PipelineDraweeController controller, boolean applyAll, int position) {
            mController = controller;
            mApplyAll = applyAll;
            mPosition = position;
        }
    }

    public static class UriEvent {

        final Uri mUri;

        final int position;

        UriEvent(Uri uri, int position) {
            mUri = uri;
            this.position = position;
        }

    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    private OnImageClickListener mListener;

    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

}
