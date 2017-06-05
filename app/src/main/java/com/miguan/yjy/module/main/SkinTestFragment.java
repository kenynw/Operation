package com.miguan.yjy.module.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.dsk.chain.bijection.RequiresPresenter;
import com.dsk.chain.expansion.data.BaseDataFragment;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.miguan.yjy.R;
import com.miguan.yjy.adapter.TestSkinAdapter;
import com.miguan.yjy.adapter.viewholder.ArticleViewHolder;
import com.miguan.yjy.model.TestModel;
import com.miguan.yjy.model.UserModel;
import com.miguan.yjy.model.bean.Article;
import com.miguan.yjy.model.bean.Skin;
import com.miguan.yjy.model.bean.Test;
import com.miguan.yjy.model.bean.User;
import com.miguan.yjy.model.local.UserPreferences;
import com.miguan.yjy.model.services.ServicesResponse;
import com.miguan.yjy.module.account.LoginActivity;
import com.miguan.yjy.module.common.WebViewActivity;
import com.miguan.yjy.module.test.TestGuideActivity;
import com.miguan.yjy.module.test.TestRecomendPresenter;
import com.miguan.yjy.module.user.ProfilePresenter;
import com.miguan.yjy.utils.DateUtils;
import com.miguan.yjy.utils.LUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.functions.Func1;

/**
 * @作者 cjh
 * @日期 2017/6/5 17:29
 * @描述
 */
@RequiresPresenter(SkinTestFragmentPresenter.class)
public class SkinTestFragment extends BaseDataFragment<SkinTestFragmentPresenter, Test> implements View.OnClickListener {
    int type = 0;

    //未测试页面(测试主页)
    @BindView(R.id.iv_test_wrinkle)
    ImageView mIvTestWrinkle;
    @BindView(R.id.tv_test_wrinkle)
    TextView mTvTestWrinkle;
    @BindView(R.id.ll_test_wrinkle)
    LinearLayout mLlTestWrinkle;
    @BindView(R.id.iv_test_oily)
    ImageView mIvTestOily;
    @BindView(R.id.tv_test_oily)
    TextView mTvTestOily;
    @BindView(R.id.ll_test_oily)
    LinearLayout mLlTestOily;
    @BindView(R.id.iv_test_sensitive)
    ImageView mIvTestSensitive;
    @BindView(R.id.tv_test_sensitive)
    TextView mTvTestSensitive;
    @BindView(R.id.ll_test_sensitive)
    LinearLayout mLlTestSensitive;
    @BindView(R.id.iv_test_pigment)
    ImageView mIvTestPigment;
    @BindView(R.id.tv_test_pigment)
    TextView mTvTestPigment;
    @BindView(R.id.ll_test_pigment)
    LinearLayout mLlTestPigment;
    @BindView(R.id.tv_test_result)
    TextView mTvTestResult;
    @BindView(R.id.scr_main_test)
    ScrollView mScrMainTest;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private TextView mTvTestSelectBirthday;
    private TextView mTvTestMan;
    private TextView mTvTestWoman;
    private TextView mTvTestInto;
    private ImageView mTvTestClose;
    private PopupWindow popupWindow;
    String birthDay;
    private int sex = 0;
    private int tag = 0;
    private User userInfo;
    private List<Integer> nums = new ArrayList<>();


    //测试结果(我的肤质)
    public static final String H5_SCORE = "http://m.yjyapp.com/site/score-tip";
    private Unbinder mBind;

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

    @BindViews({R.id.tv_test_first_letter, R.id.tv_test_second_letter, R.id.tv_test_third_letter, R.id.tv_test_four_letter})
    List<TextView> mSkinLetters;

    @BindViews({R.id.tv_test_first_name, R.id.tv_test_second_name, R.id.tv_test_third_name, R.id.tv_test_four_name})
    List<TextView> mSkinNames;

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    @BindView(R.id.recy_test_article)
    RecyclerView mRecyArticle;


    @BindView(R.id.ll_test_no)
    LinearLayout mLlTestNo;
    @BindView(R.id.ll_test_ok)
    LinearLayout mLlTestOk;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skin_test_main_fragment, null);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMainData();
    }


    public void loadFirstData() {
        mLlTestOk.setVisibility(View.GONE);
        mLlTestNo.setVisibility(View.VISIBLE);
        initListener();
        if (UserPreferences.getUserID() > 0) {
            UserModel.getInstance().getUserInfo().subscribe(new ServicesResponse<User>() {
                @Override
                public void onNext(User user) {
                    nums.clear();
                    userInfo = user;
                    if (userInfo.getCompact() != 0) {
                        mLlTestWrinkle.setBackgroundResource(R.drawable.bg_shape_test_a3e);
                        mTvTestWrinkle.setText(Skin.getCompact(userInfo.getCompact()));
                        mIvTestWrinkle.setBackgroundResource(R.mipmap.ic_test_wrinkle_reslut);
                        nums.add(0);
                    }
                    if (userInfo.getDry() != 0) {
                        mLlTestOily.setBackgroundResource(R.drawable.bg_shape_test_a9d);
                        nums.add(1);
                        mTvTestOily.setText(Skin.getDryOil(userInfo.getDry()));
                        mIvTestOily.setBackgroundResource(R.mipmap.ic_test_oily_reslut);
                    }
                    if (userInfo.getTolerance() != 0) {
                        mLlTestSensitive.setBackgroundResource(R.drawable.bg_shape_test_a9d);
                        nums.add(2);
                        mTvTestSensitive.setText(Skin.getTolerance(userInfo.getTolerance()));
                        mIvTestSensitive.setBackgroundResource(R.mipmap.ic_test_sensitive_reslut);
                    }
                    if (userInfo.getPigment() != 0) {
                        mLlTestPigment.setBackgroundResource(R.drawable.bg_shape_test_a3e);
                        nums.add(3);
                        mTvTestPigment.setText(Skin.getPigment(userInfo.getPigment()));
                        mIvTestPigment.setBackgroundResource(R.mipmap.ic_test_pigment_reslut);
                    }
                    if (nums.size() == 4) {
                        mTvTestResult.setText("查看结果");
                        mTvTestResult.setClickable(true);
                    } else {
                        mTvTestResult.setText("完成度\n" + nums.size() + "/4");
                        mTvTestResult.setClickable(false);
                    }

                }
            });
        } else {
            Log.e("userId", UserPreferences.getUserID() + "--=0--");
            mLlTestWrinkle.setBackgroundResource(R.drawable.bg_shape_white);
            mTvTestWrinkle.setText(R.string.text_no_test);
            mIvTestWrinkle.setBackgroundResource(R.mipmap.ic_test_wrinkle);

            mLlTestOily.setBackgroundResource(R.drawable.bg_shape_white);
            mTvTestOily.setText(R.string.text_no_test);
            mIvTestOily.setBackgroundResource(R.mipmap.ic_test_oily);

            mLlTestSensitive.setBackgroundResource(R.drawable.bg_shape_white);
            mTvTestSensitive.setText(R.string.text_no_test);
            mIvTestSensitive.setBackgroundResource(R.mipmap.ic_test_sensitive);

            mLlTestPigment.setBackgroundResource(R.drawable.bg_shape_white);
            mTvTestPigment.setText(R.string.text_no_test);
            mIvTestPigment.setBackgroundResource(R.mipmap.ic_test_pigment);

            mTvTestResult.setText("完成度\n" + "0/4");
            mTvTestResult.setClickable(true);
        }

    }

    private void initListener() {
        mLlTestWrinkle.setOnClickListener(this);
        mLlTestOily.setOnClickListener(this);
        mLlTestSensitive.setOnClickListener(this);
        mLlTestPigment.setOnClickListener(this);
        mTvTestResult.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_test_wrinkle:
                if (UserPreferences.getUserID() > 0) {
                    tag = 0;
                    if (TextUtils.isEmpty(userInfo.getBirth_day())) {
                        showTestUserInfoPop();
                    } else {
                        TestGuideActivity.start(getActivity(), TestModel.getInstantce().setTestData().get(0), TestGuideActivity.EXTRA_TEST_FOUR_TYPE);
                    }
                } else {
                    startToLogin();
                }
                break;
            case R.id.ll_test_oily:
                if (UserPreferences.getUserID() > 0) {
                    tag = 1;
                    if (TextUtils.isEmpty(userInfo.getBirth_day())) {
                        showTestUserInfoPop();
                    } else {
                        TestGuideActivity.start(getActivity(), TestModel.getInstantce().setTestData().get(1), TestGuideActivity.EXTRA_TEST_FIRST_TYPE);
                    }
                } else {
                    startToLogin();
                }
                break;
            case R.id.ll_test_sensitive:
                if (UserPreferences.getUserID() > 0) {
                    tag = 2;
                    if (TextUtils.isEmpty(userInfo.getBirth_day())) {
                        showTestUserInfoPop();
                    } else {
                        TestGuideActivity.start(getActivity(), TestModel.getInstantce().setTestData().get(2), TestGuideActivity.EXTRA_TEST_SECOND_TYPE);
                    }
                } else {
                    startToLogin();
                }
                break;
            case R.id.ll_test_pigment:
                if (UserPreferences.getUserID() > 0) {
                    tag = 3;
                    if (TextUtils.isEmpty(userInfo.getBirth_day())) {
                        showTestUserInfoPop();
                    } else {
                        TestGuideActivity.start(getActivity(), TestModel.getInstantce().setTestData().get(3), TestGuideActivity.EXTRA_TEST_THIRD_TYPE);
                    }
                } else {
                    startToLogin();
                }
                break;
            case R.id.tv_test_man:
                sex = 1;
                mTvTestMan.setBackgroundResource(R.drawable.bg_shape_63c);
                mTvTestWoman.setBackgroundResource(R.drawable.bg_round_stroke_div);
                mTvTestMan.setTextColor(getResources().getColor(R.color.white));
                mTvTestWoman.setTextColor(getResources().getColor(R.color.textSecondary));
                break;
            case R.id.tv_test_woman:
                sex = 0;
                mTvTestMan.setBackgroundResource(R.drawable.bg_round_stroke_div);
                mTvTestWoman.setBackgroundResource(R.drawable.bg_shape_fb7);
                mTvTestMan.setTextColor(getResources().getColor(R.color.textSecondary));
                mTvTestWoman.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.tv_test_into:
                if (TextUtils.isEmpty(birthDay)) {
                    LUtils.toast("请输入完整信息喔~");
                } else {
                    UserModel.getInstance()
                            .modifyProfile(ProfilePresenter.KEY_PROFILE_BIRTHDAY, birthDay)
                            .flatMap(new Func1<String, Observable<String>>() {
                                @Override
                                public Observable<String> call(String s) {
                                    return UserModel.getInstance().modifyProfile("sex", sex + "");
                                }
                            })
                            .subscribe(new ServicesResponse<String>() {
                                @Override
                                public void onNext(String s) {
                                    switch (tag) {
                                        case 0:
                                            TestGuideActivity.start(getActivity(), TestModel.getInstantce().setTestData().get(0), TestGuideActivity.EXTRA_TEST_FIRST_TYPE);
                                            break;
                                        case 1:
                                            TestGuideActivity.start(getActivity(), TestModel.getInstantce().setTestData().get(1), TestGuideActivity.EXTRA_TEST_SECOND_TYPE);
                                            break;
                                        case 2:
                                            TestGuideActivity.start(getActivity(), TestModel.getInstantce().setTestData().get(2), TestGuideActivity.EXTRA_TEST_THIRD_TYPE);
                                            break;
                                        case 3:
                                            TestGuideActivity.start(getActivity(), TestModel.getInstantce().setTestData().get(3), TestGuideActivity.EXTRA_TEST_FOUR_TYPE);
                                            break;
                                    }

                                }
                            });
                    popupWindow.dismiss();
                }
                break;
            case R.id.iv_test_close:
                popupWindow.dismiss();
                break;
            case R.id.tv_test_result:
                if (UserPreferences.getUserID() > 0) {
//                  setData();显示我的肤质
                    setSencondData();
                    UserPreferences.setIsShowTest(false);
                } else {
                    startToLogin();
                }

                break;

        }
    }

    /**
     * 显示测试用户基础信息
     */

    private void showTestUserInfoPop() {
        View view = View.inflate(getActivity(), R.layout.popwindow_test_info, null);
        mTvTestSelectBirthday = ButterKnife.findById(view, R.id.tv_test_select_birthday);
        mTvTestMan = ButterKnife.findById(view, R.id.tv_test_man);
        mTvTestWoman = ButterKnife.findById(view, R.id.tv_test_woman);
        mTvTestInto = ButterKnife.findById(view, R.id.tv_test_into);
        mTvTestClose = ButterKnife.findById(view, R.id.iv_test_close);

        mTvTestSelectBirthday.setOnClickListener(v -> selectDate());
        mTvTestMan.setOnClickListener(this);
        mTvTestWoman.setOnClickListener(this);
        mTvTestInto.setOnClickListener(this);
        mTvTestClose.setOnClickListener(this);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        ColorDrawable bg = new ColorDrawable(0x55000000);
        popupWindow.setBackgroundDrawable(bg);
        popupWindow.showAtLocation(mToolbar, Gravity.BOTTOM, 0, 0);


    }

    private void selectDate() {

        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                birthDay = String.valueOf(date.getTime() / 1000);
                mTvTestSelectBirthday.setText(DateUtils.DateToStr(date));
            }
        }).setCancelColor(getResources().getColor(R.color.textSecondary)).setSubmitColor(getResources().getColor(R.color.colorPrimary)).isDialog(true).setType(TimePickerView.Type.YEAR_MONTH_DAY).build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    public void startToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    public void loadMainData() {
        if (UserPreferences.getUserID() > 0) {
            UserModel.getInstance().getUserInfo().subscribe(new ServicesResponse<User>() {
                @Override
                public void onNext(User user) {
                    nums.clear();
                    userInfo = user;
                    if (userInfo.getCompact() != 0) {
                        Log.e("userInfo.getCompact()", userInfo.getCompact() + "------");
                        nums.add(0);
                    }
                    if (userInfo.getDry() != 0) {
                        nums.add(1);
                        Log.e("userInfo.getDry()", userInfo.getDry() + "------");
                    }
                    if (userInfo.getTolerance() != 0) {
                        nums.add(2);
                        Log.e("userInfo.getTolerance()", userInfo.getTolerance() + "------");
                    }
                    if (userInfo.getPigment() != 0) {
                        nums.add(3);
                        Log.e("userInfo.getPigment()", userInfo.getPigment() + "------");
                    }
                    Log.e("nums", nums.size() + "--大小 boolean--" + UserPreferences.getIsShowTest());
                    if (nums.size() == 4 && !UserPreferences.getIsShowTest()) {
                        //显示测试结果
                        setSencondData();
                    } else {
                        //显示未测试页面
                        loadFirstData();

                    }
                }
            });
        } else {
            //显示未测试页面
            loadFirstData();
        }

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (getView() != null) {
            getView().setVisibility(menuVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }


    private void setSencondData() {
        mLlTestOk.setVisibility(View.VISIBLE);
        mLlTestNo.setVisibility(View.GONE);
        if (UserPreferences.getUserID() > 0) {
            TestModel.getInstantce().getSkinRecommend().subscribe(new ServicesResponse<Test>() {
                @Override
                public void onNext(Test test) {
                    super.onNext(test);

                    mToolbarTitle.setText("我的肤质");
                    mLlTestGrade.setOnClickListener(v -> WebViewActivity.start(getActivity(), getString(R.string.text_test_grade), H5_SCORE));
                    mLlTestAgain.setOnClickListener(v -> {
                        //显示肤质测试主页
                        loadFirstData();
                        UserPreferences.setIsShowTest(true);
                    });
                    mRectTestMySkin.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    mRecyArticle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


                    setSkinResult(test);
                    ArticleAdapter articleAdapter = new ArticleAdapter(getActivity(), test.getSkinArticle());
                    if (test.getSkinArticle().size() > 0) {

                        articleAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                            @Override
                            public View onCreateView(ViewGroup parent) {
                                View headView = View.inflate(getActivity(), R.layout.include_head_article, null);
                                return headView;
                            }

                            @Override
                            public void onBindView(View headerView) {

                            }
                        });
                    }
                    mRecyArticle.setAdapter(articleAdapter);

                    List<Skin> datas = test.getSkinProduct();
                    TestSkinAdapter testSkinAdapter = new TestSkinAdapter(getActivity(), datas);
                    testSkinAdapter.setOnItemClickListener(position -> TestRecomendPresenter.star(getActivity(), test.getCategoryList(), position, datas.get(position).getCategory_name()));
                    mRectTestMySkin.setAdapter(testSkinAdapter);
                }
            });
        }

    }

    // 设置肤质
    private void setSkinResult(Test test) {
        List<Skin> skinList = test.getDesc();
        for (int i = 0; i < skinList.size(); i++) {
            mSkinLetters.get(i).setText(skinList.get(i).getLetter());
            mSkinNames.get(i).setText(skinList.get(i).getName());
        }
        mTvResultDescirbe.setText(test.getFeatures());
        mTvResultDescirbeSecond.setText(test.getElements());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    class ArticleAdapter extends RecyclerArrayAdapter<Article> {


        public ArticleAdapter(Context context, List<Article> objects) {
            super(context, objects);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new ArticleViewHolder(parent);
        }
    }


}
