package com.miguan.yjy.module.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsk.chain.bijection.RequiresPresenter;
import com.dsk.chain.expansion.list.BaseListActivity;
import com.dsk.chain.expansion.list.ListConfig;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.miguan.yjy.R;
import com.miguan.yjy.adapter.TestSkinAdapter;
import com.miguan.yjy.adapter.viewholder.ArticleViewHolder;
import com.miguan.yjy.model.bean.Skin;
import com.miguan.yjy.module.common.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 cjh
 * @日期 2017/4/1 16:43
 * @描述 测试结果(我的肤质)
 */
@RequiresPresenter(TestResultPresenter.class)
public class TestResultActivity extends BaseListActivity<TestResultPresenter> {

    public static final String H5_SCORE = "http://m.yjyapp.com/site/score-tip";

    @BindView(R.id.tv_test_result_descirbe)
    TextView mTvResultDescirbe;
    @BindView(R.id.tv_test_result_descirbe_second)
    TextView mTvResultDescirbeSecond;
    @BindView(R.id.rect_test_my_skin)
    RecyclerView mRectTestMySkin;
    @BindView(R.id.ll_test_grade)
    LinearLayout mLlTestGrade;
    @BindView(R.id.ll_test_again)
    LinearLayout mLlTestAgain;
    @BindView(R.id.recycle)
    EasyRecyclerView mRecycle;
    @BindView(R.id.tv_test_first_letter)
    TextView mTvFirstLetter;
    @BindView(R.id.tv_test_first_name)
    TextView mTvFirstName;
    @BindView(R.id.tv_test_second_letter)
    TextView mTvSecondLetter;
    @BindView(R.id.tv_test_second_name)
    TextView mTvSecondName;
    @BindView(R.id.tv_test_third_letter)
    TextView mTvThirdLetter;
    @BindView(R.id.tv_test_third_name)
    TextView mTvThirdName;
    @BindView(R.id.tv_test_four_letter)
    TextView mTvFourLetter;
    @BindView(R.id.tv_test_four_name)
    TextView mTvFourName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle("我的肤质");
        ButterKnife.bind(this);
        mLlTestGrade.setOnClickListener(v -> WebViewActivity.satr(this, getString(R.string.text_test_grade), H5_SCORE));
    }

    @Override
    protected BaseViewHolder createViewHolder(ViewGroup prent, int viewType) {
        return new ArticleViewHolder(prent);
    }

    @Override
    protected int getLayout() {
        return R.layout.test_activity_result;
    }

    public static void star(Context context) {
        Intent intent = new Intent(context, TestResultActivity.class);
        context.startActivity(intent);
    }

    public void setData(List<Skin> datas,ArrayList<Skin> categoryList) {
        mRectTestMySkin.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        TestSkinAdapter testSkinAdapter=new TestSkinAdapter(TestResultActivity.this, datas);
        testSkinAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                TestRecomendPresenter.star(TestResultActivity.this, categoryList);
            }
        });
        mRectTestMySkin.setAdapter(testSkinAdapter);
    }

    @Override
    public ListConfig getListConfig() {
        return super.getListConfig().setLoadMoreAble(false);
    }
}
