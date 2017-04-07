package com.miguan.yjy.model;

import com.dsk.chain.model.AbsModel;
import com.miguan.yjy.R;
import com.miguan.yjy.model.bean.Test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @作者 cjh
 * @日期 2017/3/31 16:59
 * @描述
 */

public class TestModel extends AbsModel {

    public static TestModel getInstantce() {
        return getInstance(TestModel.class);
    }

    public ArrayList<Test> setTestData() {
        ArrayList<Test> tests = new ArrayList<>();
        int[] resImg = {R.mipmap.ic_test_guide_wrinkle, R.mipmap.ic_test_guide_oily, R.mipmap.ic_test_guide_sensitive, R.mipmap.ic_test_guide_pigment};
        int[] strTitle = {R.string.tv_test_wrinkele, R.string.tv_test_oily, R.string.tv_test_sensitive, R.string.tv_test_pigment};
        int[] strDescribe = {R.string.tv_test_wrinkele_describe, R.string.tv_test_oily_describe, R.string.tv_test_sensitive_describe, R.string.tv_test_pigment_describe};
        for (int i = 0; i < 4; i++) {
            Test test = new Test();
            test.setImg(resImg[i]);
            test.setTitle(strTitle[i]);
            test.setDescribe(strDescribe[i]);
            tests.add(test);
        }
        return tests;
    }


    public Observable<List<Test>> getTestList() {
        List<Test> tests = new ArrayList<>();
        int[] resImg = {R.mipmap.ic_test_guide_wrinkle, R.mipmap.ic_test_guide_oily, R.mipmap.ic_test_guide_sensitive, R.mipmap.ic_test_guide_pigment};
        int[] strTitle = {R.string.tv_test_wrinkele, R.string.tv_test_oily, R.string.tv_test_sensitive, R.string.tv_test_pigment};
        int[] strDescribe = {R.string.tv_test_wrinkele_describe, R.string.tv_test_oily_describe, R.string.tv_test_sensitive_describe, R.string.tv_test_pigment_describe};
        String[] strings = {"洗面奶", "测试2", "测试3", "测试4"};
        for (int i = 0; i < 4; i++) {
            Test test = new Test();
            test.setImg(resImg[i]);
            test.setTitle(strTitle[i]);
            test.setTestName(strings[i]);
            tests.add(test);
        }
        return Observable.just(tests);
    }


}
