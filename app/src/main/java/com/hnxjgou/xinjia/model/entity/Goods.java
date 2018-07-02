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
    /**物流类型*/
    public int LogisticsType;
    /**运费*/
    public long FreightPrice;
    /**类别ID*/
    public int CategoryId;
    /**状态*/
    public int State;
    /**上架事件*/
    public long ShelvesTime;
    public long OffShelfTime;
    /**详情*/
    public String Details;
    public String CommodityAddress;
    /**用户ID*/
    public int Userid;
}
