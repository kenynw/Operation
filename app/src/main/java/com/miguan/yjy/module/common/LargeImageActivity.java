package com.miguan.yjy.module.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.dsk.chain.bijection.ChainBaseActivity;
import com.dsk.chain.bijection.RequiresPresenter;
import com.github.chrisbanes.photoview.PhotoView;
import com.miguan.yjy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 cjh
 * @日期 2017/6/14 14:11
 * @描述
 */
@RequiresPresenter(LargeImagePresenter.class)
public class LargeImageActivity extends ChainBaseActivity {

    public static final String EXTRA_IMGURI = "imgUri";

    @BindView(R.id.img_common_large)
    PhotoView mImgCommonLarge;

    String imgUri;

    public static void start(Context context, String imgUri) {
        Intent intent = new Intent(context, LargeImageActivity.class);
        intent.putExtra(EXTRA_IMGURI, imgUri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_large_image);
        ButterKnife.bind(this);

        imgUri = getIntent().getStringExtra(EXTRA_IMGURI);

        Glide.with(this).load(imgUri).into(mImgCommonLarge);

        mImgCommonLarge.setImageURI(Uri.parse(imgUri));
        mImgCommonLarge.setOnPhotoTapListener((view1, x, y) -> finish());
        mImgCommonLarge.setOnOutsidePhotoTapListener(imageView -> finish());
    }

}