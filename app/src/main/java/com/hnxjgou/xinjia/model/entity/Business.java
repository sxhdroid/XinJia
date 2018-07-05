package com.hnxjgou.xinjia.model.entity;

import java.util.List;

/**
 * 商家实体类
 */

public class Business {

    /**
     * 商家名称
     */
    public String BusinessName; //
    /**
     * 商家ID
     */
    public int BusinessId; //
    /**
     * 商家类型（1商铺、2个人）
     */
    public int BusinessType; //

    /**
     * 商家所属用户的ID
     */
    public int UserId; //
    /**
     * 4张广告图,是一个JSON{[],[],[]}
     */
    public List<String> Advertisings; //
    /**
     * 营业开始时间
     */
    public String StartTime; //
    /**
     * 营业结束时间
     */
    public String ClosingTime; //
    /**
     * 商店详细地址
     */
    public String DetailedAddress; //
    /**
     * 商店经度
     */
    public double StoreLon; //
    /**
     * 商店纬度
     */
    public double StoreLat; //
    /**
     * 商店所在省
     */
    public String Province; //
    /**
     * 商店所在市
     */
    public String City; //
    /**
     * 商店所在区/县
     */
    public String County; //
    /**
     * 商店所在镇
     */
    public String Town; //
    /**
     * 商家所在村乡
     */
    public String Village; //
    /**
     * 个人是否服务全国 1是 2 不是
     */
    public int IsServesCountry; //
    /**
     * 个人服务范围（米）
     */
    public int ServiceScope; //
    /**
     * 商家详情
     */
    public String Details; //
    /**
     * 所属类目（叶子类目）
     */
    public int CategoryId;
    /**
     * 商家所属类型（装修 早餐）可用来做搜索字段
     */
    public String OperateType; //
    /**
     * 入驻时间
     */
    public long LocateTime; //
    /**
     * 注销时间
     */
    public long ExitTime; //
    /**
     * 注销类型（1 自动退出、2违规退出、3封号）
     */
    public int ExitType; //
    /**
     * 营业执照编号
     */
    public String LicenseNumber; //
    /**
     * 营业执照
     */
    public String LicenseImage; //
    /**
     * 其他证明
     */
    public String OtherProof; //
    /**
     * 商家真实姓名
     */
    public String RealName;//
    /**
     * 身份证号码
     */
    public String IdNumber; //
    /**
     * 个人身份证正面
     */
    public String IdFacade; //
    /**
     * 个人身份证反面
     */
    public String IdReverse; //
    /**
     * 联系电话
     */
    public String Phone; //
    /**
     * 结算账户号（银行卡、支付宝账号）
     */
    public String SettlementNumber; //
    /**
     * 结算支付类型 1 微信 2 支付宝 3 银联
     */
    public int PaymentType;//
    /**
     * 银行名字
     */
    public String BankName; //
    /**
     * 开户行
     */
    public String OpeningName; //
    /**
     * 支付宝姓名
     */
    public String AlipayName; //
    public String Remarks; //
    public String Logo; //
    /**
     * 状态（1正常 2审核 3关闭）
     */
    public int State; //
    /**
     * 是否支付代理人提成 1支付 2不支付
     */
    public int IsPayAgent; //
    /**
     * 支付金额
     */
    public double PaymentMoney; //
    /**
     * 支付时间
     */
    public long PaymentTime;//
    /**
     * 记录ID（代理人商家入驻ID）
     */
    public long AgentId;//
    /**
     * 结算代理商家ID
     */
    public long SettlementAgentId;//
    /**
     * 店铺评价分，总分 5分
     */
    public double Score; //
    /**
     * 商家活动说明（优惠活动）
     */
    public String BusinessActivity; //
    /**
     * 今日 销售量
     */
    public int DayMarketing; //
    /**
     * 上月销售量
     */
    public int MonthMarketing; //

    /**
     * 订单数量
     */
    public int OrderNumber;

    /**
     * 营业额（分）
     */
    public long Turnover;

    /**
     * 销售总量（分）
     */
//    public int SalesVolume;

    /**
     * 年销售额（分）
     */
    public long YearTurnover;

    /**
     * 每月的销售数据
     */
    public List<Sales> BusinessSales;
}
