package com.hnxjgou.xinjia.model.entity;

import java.util.List;

/**
 * 商品实体类
 */
public class Goods {
    /**商品ID*/
    public int CommodityId;
    /**广告语*/
    public String Advertisement;
    /**所属商家id*/
    public int BusinessId;
    /**商品logo*/
    public String Commoditylogo;
    /**商品图片列表*/
    public List<String> CommodityImages;
    /**商品名称*/
    public String CommodityName;
    /**原价*/
    public long OriginalPrice;
    /**优惠价*/
    public long ConcessionalPrice;
    /**优惠介绍*/
    public String ConcessionalExplain;
    /**物流类型：1无（服务是没有的）、2自提、3送货上门、4快递*/
    public int LogisticsType;
    /**运费*/
    public long FreightPrice;
    /**类别ID*/
    public int CategoryId;
    /**状态：1上架、2下架、3删除 */
    public int State;
    /**上架时间*/
    public long ShelvesTime;
    /**下架时间*/
    public long OffShelfTime;
    /**详情*/
    public String Details;
    /**商家所在地地址*/
    public String CommodityAddress;
    /**服务地址类型（1全国、2区域）*/
    public int ServiceAddressType;
    public String ServiceAddress;
    /**服务范围*/
    public int ServerceRangeNumber;
    /**服务商地址经度*/
    public float ServerceLon;
    /**服务商地址纬度*/
    public float ServerceLat;
    /**商家备注*/
    public String ServerRemarks;
    /**商品配件id列表*/
    public String []PartsIds;
    /**所属商家类别id*/
    public long BusinessCategoryId;
    /**用户ID*/
    public int Userid;
    /**商品类型*/
    public int CommodityType;
    /**存货数量*/
    public int Number;
    /**商品属性数组*/
    public String AttributeIds;
    /**商品附件*/
    public List<GoodsParts> Parts;
    /**商品属性*/
    public List<GoodsAttr> Attributes;
}
