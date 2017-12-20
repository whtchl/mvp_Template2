package com.lifeng.f300.znpos2.utils;

import android.text.TextUtils;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.utils.DesUtils;
import com.lifeng.f300.common.utils.FileUtils;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.config.ConnectURL;

import org.json.JSONObject;

/**
 * Created by happen on 2017/8/21.
 */

public class MerchantSignDataManager {
    private static MerchantSignDataManager instance;
    public static final String SIGN_DATA=ConnectURL.ENVIRONMENT+ConnectURL.PATH_SPECIAL+ConnectURL.FIlE_SIGN;
    private SignData signData;
    /**
     * 获取实例
     * @return
     */
    public static MerchantSignDataManager getInstance() {
        instance = new MerchantSignDataManager();
        return instance;
    }

    /**
     * 获取签到的基本信息
     */
    public SignData getSignData() {
        return signData;
    }

    //	权限：0、无权限,
//	1、银行卡,2、手输卡号,3、消费,4、储值消费,5、现金消费,6、积分兑换,7、优惠券,8、订单,9、充值,10、银行卡充值,11、现金充值,12、预售权/预订,13、领卡激活,14、重置密码,15、卡解挂,16、卡补办,17、交易撤销,18、来电弹屏,19、商品零售,20、计次充值/消费,21、售卡,22、解锁,23、解冻,24、退卡,25、作废,26、解除卡与会员关联,27、支持磁条卡,28、支持非接卡

    public static class SignData {
        //签到参数
        public String PINKEY;
        public String SECONDARYKEY;	//  二级秘钥  855c552d3c43a6b406f69842e72d52e0840120477cb7ac7ae72d52e0840120477cb7ac7a
        public String AUTH;		//	1111111111111111111111111011   权限	AUTH	32位（不足左补空格）
        public String BATCHNUM;	//	000001  终端批次号
        public String CARDPREFIX;	//66666716 卡前缀
        public String CHECKPASSPORD;  //充值是否验证密码 0
        public String MERCHANTCODE;//	商户编号 100000000000032
        public String MERCHANTID;//	商户ID 173
        public String MIFAREKEY;	//0475010b0f74   会员卡非接秘钥
        public String MERCHANTNAME;//商户名称
        public String POINTRATE;	//积分与人民币兑换率 1
        public String TELNO;	//服务电话 010-67137577
        public String UPDATEADDR;	//升级地址
        public String UPDATESTATUS;	//0  是否需要升级
        public String WEIADDRESS;	//微信网址
        public String MESSAGE;	//消息
        public String STATUS;	//00
        public String ISSUECARD; //0无卡、1为实体卡、2为虚拟卡、3为虚拟/实体卡
        public String RESPONSE_TIME;
        public String AREA_DESC; //商户所属地区
        public String VERSION_DESC;//商户系统版本
        public String ISINS;//是否是保险业务,返回1就是保险业务,其他的不是
        public boolean FORCE_TO_UP;//如果是true，就是强制升级，如果是false就不强制
        public String ORDER_SERVER_URL;//订单地址
        public String ISOVERDRAW;//允许透支下发的金额
        public String POSNAME;//POS终端的名称
        public String WEIXIN_ZS;//微信主扫  1是，0否
        public String ZHIFUBAO_ZS;//支付宝主扫
        public String WEIXIN_BS;//微信被扫
        public String ZHIFUBAO_BS;//支付宝被扫
        public String POSPUSH_SERVER_URL;//推送或者第三方认证的service系统基础地址
        public String INSURANCE_SERVER_URL;//保险系统基础地址
        public String INSAUTH;//各种保险系统权限0没有开通，1是开通    1010010
        public boolean signDataIsOk;//数据是否正常
        public String SPLIT_RATE;//分账比例
        public String ISNO_CARD_PAY;//2017051101 tchl add 38封顶无卡支付。1支持无卡支付，0不支持不卡支付
        public String BM2;  //YR 控制超市充值和商家充值   //2017060801 YR弃用原分支，开始使用主分支  add tchl
        public String BM;   //是否设置银行通道
    }

    private MerchantSignDataManager() {
        loadData();
    }

    private String content="";

    /**
     * 加载数据
     */
    public void loadData() {
        // 读取商户数据存储文件
        /*if (BuildModle.isF300OrS600()) {
            content = FileUtils.readFile(SIGN_DATA_F300, "UTF-8");
        }else if(BuildModle.isWpos()){
            content = FileUtils.readFile(SIGN_DATA_Wpos, "UTF-8");
        }else{
            content = FileUtils.readFile(SIGN_DATA_S300, "UTF-8");
        }*/
        /*if (BuildModle.isHI98()) {
            content = FileUtils.readFile(Hi98.REGISTER_DATA_HI98, "UTF-8");
        }*/
        content = FileUtils.readFile(SIGN_DATA, "UTF-8");
        if (TextUtils.isEmpty(content)) {
            LogUtils.d("wang", SIGN_DATA+"内容是空.");
            SignData data = new SignData();
            data.signDataIsOk = false;
            signData = data;
        }else{
            try {
                content= DesUtils.getDesString(content, ConnectURL.DESUTILS_KEY.getBytes());//解密
                JSONObject json = new JSONObject(content);
                JUtils.Log("json:"+json.toString());
                SignData data = new SignData();
                data.PINKEY = StringUtil.getJsonInfo(json,"PINKEY");
                data.SECONDARYKEY = StringUtil.getJsonInfo(json,"SECONDARYKEY");
                data.AUTH = StringUtil.getJsonInfo(json,"AUTH");
                data.BATCHNUM = StringUtil.getJsonInfo(json,"BATCHNUM");
                data.CARDPREFIX = StringUtil.getJsonInfo(json,"CARDPREFIX");
                data.CHECKPASSPORD = StringUtil.getJsonInfo(json,"CHECKPASSPORD");
                data.MERCHANTCODE = StringUtil.getJsonInfo(json,"MERCHANTCODE");
                data.MERCHANTID = StringUtil.getJsonInfo(json,"MERCHANTID");
                data.MIFAREKEY = StringUtil.getJsonInfo(json,"MIFAREKEY");
                data.POINTRATE = StringUtil.getJsonInfo(json,"POINTRATE");
                data.TELNO = StringUtil.getJsonInfo(json,"TELNO");
                data.UPDATEADDR = StringUtil.getJsonInfo(json,"UPDATEADDR");
                data.UPDATESTATUS = StringUtil.getJsonInfo(json,"UPDATESTATUS");
                data.WEIADDRESS = StringUtil.getJsonInfo(json,"WEIADDRESS");
                data.MESSAGE = StringUtil.getJsonInfo(json,"MESSAGE");
                data.STATUS = StringUtil.getJsonInfo(json,"STATUS");
                data.ISSUECARD = StringUtil.getJsonInfo(json,"ISSUECARD");
                data.MERCHANTNAME = StringUtil.getJsonInfo(json,"MERCHANTNAME");
                data.RESPONSE_TIME = StringUtil.getJsonInfo(json,"RESPONSE_TIME");
                data.AREA_DESC = StringUtil.getJsonInfo(json,"AREA_DESC");
                data.VERSION_DESC = StringUtil.getJsonInfo(json,"VERSION_DESC");
                data.ISINS = StringUtil.getJsonInfo(json,"ISINS");
                data.FORCE_TO_UP = json.isNull("FORCE_TO_UP") ? false : json.getBoolean("FORCE_TO_UP");
                data.ORDER_SERVER_URL = StringUtil.getJsonInfo(json,"ORDER_SERVER_URL");
                data.ISOVERDRAW = StringUtil.getJsonInfo(json,"ISOVERDRAW");
                data.POSNAME = StringUtil.getJsonInfo(json,"POSNAME");
                data.WEIXIN_ZS = StringUtil.getJsonInfo(json,"WEIXIN_ZS");
                data.ZHIFUBAO_ZS = StringUtil.getJsonInfo(json,"ZHIFUBAO_ZS");
                data.WEIXIN_BS = StringUtil.getJsonInfo(json,"WEIXIN_BS");
                data.ZHIFUBAO_BS = StringUtil.getJsonInfo(json,"ZHIFUBAO_BS");
                data.POSPUSH_SERVER_URL = StringUtil.getJsonInfo(json,"POSPUSH_SERVER_URL");
                data.INSURANCE_SERVER_URL = StringUtil.getJsonInfo(json,"INSURANCE_SERVER_URL");
                data.INSAUTH = StringUtil.getJsonInfo(json,"INSAUTH");
                data.signDataIsOk = true;
                data.SPLIT_RATE = StringUtil.getJsonInfo(json,"SPLIT_RATE");
                data.ISNO_CARD_PAY="0";//2017051101 tchl add 38封顶无卡支付. 1支持无卡支付，0不支持不卡支付
                JUtils.Log("posname:"+data.POSNAME+ "merchant name:"+data.MERCHANTNAME+" SECONDARYKEY:"+data.SECONDARYKEY+" 批次号："+data.BATCHNUM);
                signData = data;
            } catch (Exception e) {
                LogUtils.d("wang", "sign parser json data error.", e);
                SignData data = new SignData();
                data.signDataIsOk = false;
                signData = data;
            }
        }
    }
}
