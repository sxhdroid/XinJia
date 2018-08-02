package com.hnxjgou.xinjia.model.entity;

/**
 * 商品属性
 */
public class GoodsAttr {

    /**属性id*/
    public int AttributeId;
    /**所属商品ID／配件ID*/
    public int BelongId;
    /**属性名*/
    public String AttributeName;
    /**属性价格*/
    public int AttributePrice;
    /**数量*/
    public int Number;
    /**标题，如果标题是一样就表示是一组的*/
    public String Title;
}
