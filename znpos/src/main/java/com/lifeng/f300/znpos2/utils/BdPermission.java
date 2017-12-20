package com.lifeng.f300.znpos2.utils;

/**
 * Created by happen on 2017/10/12.
 */

/**
 * 各种保险权限控制，签到的时候下发（餐饮保险\商城保险）
 * @author wangkeze
 * 获取权限
 */

import android.text.TextUtils;

/**
 *  0、无权限,1、银行卡,2、手输卡号,3、消费,4、储值消费,5、现金消费,6、积分兑换,7、优惠券,8、订单,9、充值,
 *  10、银行卡充值,11、现金充值,12、预售权/预订,13、领卡激活,14、重置密码,15、卡解挂,16、卡补办,17、交易撤销,
 *  18、来电弹屏,19、商品零售,20、计次充值/消费,21、售卡,22、解锁,23、解冻,24、退卡,25、作废,26、解除卡与会员关联',
*/
public class BdPermission {

    private String auth="";
    private MerchantSignDataManager.SignData signData;
    private static BdPermission instance;
    private String auth1="";//2017060801 YR弃用原分支，开始使用主分支 begin tchl
    private String isPreauth="";//2017052201 tchl add 预授权
    public BdPermission(){
        loadData();
    }

    /**
     * 获取权限实列对象
     * @return
     */
    public static BdPermission getInstance() {
        instance = new BdPermission();
        return instance;
    }

    private void loadData(){
        signData = MerchantSignDataManager.getInstance().getSignData();
        if (null != signData) {
            auth=signData.INSAUTH;
        }else{
            auth="";
        }
    }

    /**2017060801 YR弃用原分支，开始使用主分支 begin tchl
     *
     * @return
     */
    public boolean isBankCard(){
        boolean i = TextUtils.isEmpty(auth1) ? false : (auth1.length() >= 1 ? (auth1.charAt(0) == '1') : false);
        return i;
    }

    public boolean demoG100(){
        return true;
    }
}
