package com.ningsheng.jietong.App;

/**
 * Created by zhushunqing on 2016/2/25.
 */
public class Url {
    public final static String Post = "Post";
    public final static String Get = "Get";
    public final static String url = "http://hyjf.shizitegong.com:8080/jtqb-app-api";
//public final static String url = "http://www.52jtk.com:8080/jtqb-app-api";
//    public final static String url = "http://192.168.2.188:8080/jtqb-app-api";
//    public final static String url = "http://192.168.0.157:8088/jtqb-app-api";
//    public final static String url="http://192.168.0.194:8080/jtqb-app-api";

    public final static String login = url + "/account/login";//登录
    public final static String register = url + "/account/register";//注册
    public final static String indext = url + "/account/appIndexInit";//首页s
    public final static String merchantInfo = url + "/merchant/merchantInfo";//;商户详情
    public final static String merchantSalesList = url + "/merchant/merchantSalesList";//;活动列表
    public final static String appMerchantList = url + "/merchant/appMerchantList";//;商户列表
    public final static String getDataDictInfo = url + "/datadict/getDataDictInfo";//;省市/

    public final static String identityAuth = url + "/account/identityAuth";//身份证验证
    public final static String unBind = url + "/account/unBind";//解绑
    public final static String addFeedbackInfo = url + "/more/addFeedbackInfo";//意见反馈
    public final static String valiTranPwd = url + "/account/valiTranPwd";//验证支付密码
    public final static String sendSms = url + "/sms/smsSend";//发送短信验证码
    public final static String payCardList = url + "/pay/list";//购卡列表
    public final static String ticketList = url + "/ticket/list";//电子券列表
    public final static String authentication = url + "/account/authentication";//姓名身份证号验证
    public final static String shippingaddressListInfo = url + "/shoppingaddress/shippingaddressListInfo";//;获取收货地址
    public final static String addShoppingAddress = url + "/shoppingaddress/addShoppingAddress";//;增加收货地址
    public final static String updateShoppingAddress = url + "/shoppingaddress/updateShoppingAddress";//;修改收货地址
    public final static String deleteShoppingAddress = url + "/shoppingaddress/deleteShoppingAddress";//;删除收货地址
    public final static String accountInfo = url + "/account/accountInfo";//个人资料
    public final static String uploadImage = url + "/account/upLoadUserImg";//上传用户头像
    public final static String updateNickName = url + "/account/updateNickName";//修改昵称
    public final static String oldMobileValidation = url + "/account/oldMobileValidation";//验证原手机
    public final static String updateNewMobile = url + "/account/updateNewMobile";//重置手机号
    public final static String updateUserPwd = url + "/account/updateUserPwd";//修改登录密码
    public final static String updateTranPwd = url + "/account/updateTranPwd";//修改支付密码
    public final static String assignTranPwd = url + "/account/assignTranPwd";//重置支付密码
    public final static String myCardList = url + "/account/cardList";//我的卡
    public final static String submitOrder = url + "/pay/submitOrder";//购卡
    public final static String faceValues = url + "/faceValue/faceValues";//卡面值list
    public final static String getAreaData = url + "/datadict/getAreaData";//卡面值list
    public final static String bindCard = url + "/account/bindCard";//绑卡
    public final static String query = url + "/account/balanceQuery";//绑卡
    public final static String buyTicket = url + "/ticket/buyTicket";//兑换电子券
    public final static String queryMerchantByLngLat = url + "/merchant/queryMerchantByLngLat";//商户删选
    public final static String initMerchantDatas = url + "/merchant/initMerchantDatas";//商户删选
    public final static String queryTransRecord = url + "/account/queryTransRecord";//交易记录列表
    public final static String HotKey = url + "/merchant/hotKeyword";//热门关键字
    public final static String validateSmsCode = url + "/sms/validateSmsCode";//忘记密码短信码校验
    public final static String assignUserPwd = url + "/account/assignUserPwd";//重置登录密码
    public final static String logout = url + "/account/logout";//重置登录密码
    public final static String consumerDetails=url + "/account/consumerDetails";//消费明细列表
    public final static String lossCard=url + "/card/lossCard";//挂失
    public final static String merchantSalesInfo=url + "/merchant/merchantSalesInfo";//挂失
    public final static String getContent=url + "/content/getContent";//挂失
    public final static String serach=url + "/merchant/serach";//搜索
    public final static String transRecordInfo=url + "/account/transRecordInfo";//搜索



}
