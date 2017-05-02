package com.miguan.yjy.model;

import com.dsk.chain.model.AbsModel;
import com.miguan.yjy.model.bean.BrandList;
import com.miguan.yjy.model.bean.Component;
import com.miguan.yjy.model.bean.Evaluate;
import com.miguan.yjy.model.bean.Product;
import com.miguan.yjy.model.bean.ProductList;
import com.miguan.yjy.model.bean.UserProduct;
import com.miguan.yjy.model.local.UserPreferences;
import com.miguan.yjy.model.services.DefaultTransform;
import com.miguan.yjy.model.services.ServicesClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @作者 cjh
 * @日期 2017/3/21 14:04
 * @描述
 */

public class ProductModel extends AbsModel {

    public static final int TYPE_PRODUCT = 1;

    public static ProductModel getInstance() {
        return getInstance(ProductModel.class);
    }

    /**
     * 大家都在搜
     */
    public Observable<List<Product>> searchHot() {
        return ServicesClient.getServices().searchHot().compose(new DefaultTransform<>());
    }

    /**
     * 搜索联想
     */
    public Observable<List<Product>> searchAssociate(String keywords) {
        return ServicesClient.getServices().searchAssociate(keywords).compose(new DefaultTransform<>());
    }

    /**
     * 搜索结果接口
     */
    public Observable<ProductList> searchQuery(String keywords,int type, int cate_id, String effect, int page) {
        return ServicesClient.getServices().searchQuery(keywords, type, cate_id, effect, page).compose(new DefaultTransform<>());
    }

    public Observable<List<Product>> getSearchList() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setProduct_name("sss");
            list.add(product);
        }
        return Observable.just(list);
    }

    public Observable<Product> getProductDetail(int productId) {
//        Product mProduct = new Product();
//        mProduct.setProduct_name("hahaha");
//        mProduct.setBrand("理肤泉");
//        return Observable.just(mProduct).compose(new DefaultTransform<>());
        return ServicesClient.getServices().productDetail(productId, UserPreferences.getUserID()).compose(new DefaultTransform<>());
    }

    public Observable<UserProduct> queryCode(int brandId, String number) {
        return ServicesClient.getServices().queryCode(brandId, number).compose(new DefaultTransform<>());
    }

    public Observable<List<Component>> getReadList() {
        List<Component> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Component component = new Component();
            component.setName("成分名");
            list.add(component);
        }
        return Observable.just(list);
    }

    public List<Component> getReadListData() {
        List<Component> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Component component = new Component();
            component.setName("成分名");
            list.add(component);
        }
        return list;
    }

    public Observable<BrandList> getBrandList() {
//        List<Brand> hot = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            Brand brand = new Brand();
//            brand.setName("Biotherm 碧欧泉");
//            brand.setLetter("B");
//            hot.add(brand);
//        }
//        String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
//                "T", "U", "V", "W", "X", "Y", "Z"};
//        List<Brand> other = new ArrayList<>();
//        for (String letter : letters) {
//            for (int i = 0; i < 5; i++) {
//                Brand brand = new Brand();
//                brand.setName("安娜苏" + letter);
//                brand.setLetter(letter);
//                other.add(brand);
//            }
//        }
//        Brand brand = new Brand();
//        brand.setHotCosmetics(hot);
//        brand.setOtherCosmetics(other);
//        return Observable.just(brand).compose(new DefaultTransform<>());
        return ServicesClient.getServices().brandList().compose(new DefaultTransform<>());
    }

    /**
     * 添加收藏（长草）
     *
     * @param productId
     * @return
     */
    public Observable<String> addLike(int productId) {
        return ServicesClient.getServices().addStar(productId, UserPreferences.getUserID(), 1).compose(new DefaultTransform<>());
    }

    /**
     * 添加到我的产品库
     *
     * @return
     */
    public Observable<String> addRepository(int brandId, String brandName, String product, int isSeal, String sealTime, int qualityTime, String overdueTime) {
        return ServicesClient.getServices().addRepository(
                UserPreferences.getUserID(), brandId, brandName, product, isSeal, sealTime, qualityTime, overdueTime
        ).compose(new DefaultTransform<>());
    }

    /**
     * 写点评
     */
    public Observable<String> addEvaluate(int productId, int satr, String content) {
        return ServicesClient.getServices()
                .addEvaluate(productId, UserPreferences.getUserID(), TYPE_PRODUCT, satr, content)
                .compose(new DefaultTransform<>());
    }

    /**
     * 获取用户点评列表
     * id(int) － 对应ID
     * user_id(int) － 用户ID，未登录可为空
     * type(int) － 类型 1-产品，2-文章
     * orderBy(string) － 排序方式-默认default综合排序，skin 肤质排序
     * condition(string) － 筛选星级,目前有'Praise'好评,'middle'中评,'bad'差评
     * page(int) － 当前页数
     * pageSize(int) － 每页多少条
     */
    public Observable<List<Evaluate>> getEvaluate(int productId, int page, String orderBy, String condition) {
        return ServicesClient.getServices()
                .evaluateList(productId, page, 10, UserPreferences.getUserID(), TYPE_PRODUCT, orderBy, condition)
                .compose(new DefaultTransform<>());
    }

    /**
     * 产品评论列表点赞
     */
    public Observable<String> addEvaluateLike(int evaluateId) {
        return ServicesClient.getServices().addEvaluateLike(evaluateId, UserPreferences.getUserID()).compose(new DefaultTransform<>());
    }


}
