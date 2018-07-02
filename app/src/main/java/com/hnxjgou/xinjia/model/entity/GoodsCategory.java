package com.hnxjgou.xinjia.model.entity;

import java.util.List;

/**
 * 商品类别实体类
 */
public class GoodsCategory {
    /**商品类别*/
    public int BusinessCategoryId;
    /**所属商家的id*/
    public int BusinessId;
    /**类别名称*/
    public String Name;
    /**类别图标*/
    public String TitleImage;
    public int Number;
    /**该类别下的商品列表*/
    public List<Goods> Commoditys;
}
