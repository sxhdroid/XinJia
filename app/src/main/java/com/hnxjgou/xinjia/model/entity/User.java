package com.hnxjgou.xinjia.model.entity;

/**
 * Created by apple on 2018/5/25.
 */

public class User {

    /**用户id*/
    public int Userid;
    /**用户头像*/
    public String HeadImage;
    /**用户名*/
    public String Name;
    /**电话*/
    public String Phone;
    /**用户类型（1普通用户 2 商家 3个人服务商 4代理商 ）*/
    public int Type;
    /**入驻时间*/
    public long LocateTime;
    /**家号*/
    public String HomeNumber;
    /**性别 1 男  2女 3保密 */
    public int Gender;
    /**职业*/
    public String Occupation;
    /**详细地址*/
    public String DetailedAddress;
    /**生日*/
    public String Birthday;
    /**微信uuid*/
    public String wxuuid;
    /**微信openid*/
    public String Wxopenid;
    /**微信昵称*/
    public String WxName;
    /**微信头像*/
    public String WxHeadImage;
    /**经度*/
    public float Lon;
    /**纬度*/
    public float Lat;
    /**退出时间（商家）*/
    public long ExitTime;
    /**退出原因（商家）*/
    public String ExitReason;
    /**用户状态（1正常 2封号 3违规）*/
    public int State;
    /**备注*/
    public String Remarks;
    public int IsAgent;
    public long CreatTime;
    /**商家ID*/
    public int BusinessId;
}
