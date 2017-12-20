package com.lifeng.f300.common.entites;

import java.util.List;

/**
 * Decripe : 会员信息
 * Created by happen on 2017/9/4.
 */

public class QueryMember {
    public String EMAIL; // 邮件
    public String FAX; // 传真
    public String GENDER; // 性别 0
    public String IDENTITYCODE; // 身份证号
    public int MEMBERID; // 会员号 1803
    public String MEMBERNAME; // 会员姓名 冀忠玲(8折）
    public String MERCHANTNAME; // 机构名称
    public String PHONE; // 电话
    public String PHOTONAME; // 照片文件名称
    public String PHOTOPATH; // PHOTOPATH
    public String QQ; // QQ号
    public String STATUS; // 状态
    public String MESSAGE; // 消息s
    public String MOBILENO; //手机号码关联的手机号码
    public String MERCHANTID;//会员的id
    public String MERCHANTCODE;
    public String BIRTHDAY; //生日
    public String MEMBERADDR; //地址
    public String bankCard;//银行卡号...自己定义的

    public List<CARDLIST> CARDLIST; // 卡的列表
    public class CARDLIST {
        public String CARD; // 会员卡号：2310831513091817789
        public String BINDMOBILENO; //与卡绑定的手机号码  //匿名：15390395799",
        public String CARDSTATUS; // 4: 状态：1:已生成; 2:已分配; 3:已售出; 4:正常; 5:已挂失;// 6:已锁定;// 7:已冻结; 8:已作废;
        public String DESPOIT; // 储值余额 18.00
        public String EXPDATE;
        public String LASTDATE; // 上次使用时间
        public String POINT; // 产生积分:12345678901.00
        public String SELLDATE; // 售卡时间
        public String SOLDTIME;//售卡时间新
        public String ISVIRTUAL; //是否是虚拟卡,为1就是虚拟卡,0就是实体卡
        public String CARDRANK;//卡等级
        public String ISEXPIRE;// 0没有过期，1已经过期
        public String ACTIVETIME;//激活卡时间
    }

    public List<CouponList> COUPONLIST; // 优惠券列表
}
