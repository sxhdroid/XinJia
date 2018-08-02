package com.hnxjgou.xinjia.model.entity;

/**
 * 商品配件实体类
 */
public class GoodsParts {

    /**配件ID*/
    public int PartsId;
    /**配件图标*/
    public String PartsImage;
    /**配件名字*/
    public String PartsName;
    /**配件价格*/
    public long PartsPrice;
    /**所属商家id*/
    public int BusinessId;
    /**是否删除 是否删除 1 否  2 删除*/
    public int IsDelete;
    /**是否上架 1 上架 2  下架*/
    public int IsShelf;
    /**配件的属性*/
    public GoodsAttr []Attributes;

}
