package com.ningsheng.jietong.AliPay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.Url;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by zhushunqing on 2016/2/26.
 */
public class PayUtil {

    // 商户PID
    public static final String PARTNER = "2088221196645671";
    // 商户收款账号
    public static final String SELLER = "ruixiangzhifu@chinajtk.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJWJItLNgic+9AW+QD4NdRWqGW8R5YygQD+YbGq+01BoPktA+vkMTyUL9pLTD/3z+9GlUPuM6ChVvG3gZkm7OeGqWVJv4Ha1pt1FpPh4nbU8sNjD7HGJj74pSeEJy4em6MPLiv+PgUtXB06eh7nVW8mpbbmXRbgl1KstFfsTomDBAgMBAAECgYBheDgIrAfwSOqKa844JFFzQ6K8cS/tD++RBGt20Newi6I/LyXn/AwCm9+nKIg+AjDcICO/M8OHtwrRm/lQigdZf7ZKrA6f3Q18M1n9trj4/0loSiNQ72/SweDoW7ypc8jfKg08Wp/jjA8RyXs3EGLl/wynUxV4QTxDWcsIFZSr+QJBAMaB2IvLSov0ZJzYpSUdKNxtSN/M7/z23gQRPwH97fbovsiA7eCpylZ4/I85MgLpKvs224/XkcYOAMOSm4zC0xcCQQDA2FY4QvtcCl219Qi8UuvBduw+Ixt2qLUVPxd8PKbPHNnn/EiBf7hyoYjLbSmU8y/Wk+7ezm8cQhb0BzotFbHnAkARd8Wdb5Msw6HC55GItYst8nPyNa98nCCnTPk1bKnvC0YWkLneBjLm7xiSZd8PwILmvkoN21NZkUvh6dnERjSbAkBFKhIaUaO3Shx8EysT6CWYGJ/4VT0XXMC9owg4TcSCCdX5hxk2IOm1a0wVscw1GNdyfVIx5RYiq21yDlmGK/7HAkBfYsy9Vv5hIEbi8H3AwCpXlpmFMq55FP20GQuCrg2j7Mbngkj99Z+SvUEmvrL5syxlxu2W1l++j0koZs+nHR1P";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private BaseActivity activity;
    private OnPayCallBack callBack;
    private String notice;
    private String title;
    private String price;
    private String order_id;
    private String callUrl;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                        callBack.onSucceed();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            callBack.OnPaying();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                            callBack.OnFailure();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(activity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

public PayUtil(BaseActivity activity,String title,String notice,String price,String order_id,String callUrl,OnPayCallBack OnPayCallBack){
    this.activity=activity;
    this.title=title;
    this.notice=notice;
    this.price=price;
    this.callBack=OnPayCallBack;
    this.order_id=order_id;
    this.callUrl=callUrl;

}

    public interface OnPayCallBack{
        public void onSucceed();
        public void OnPaying();
        public void OnFailure();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     */
    public void h5Pay() {
        Intent intent = new Intent(activity, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        activity.startActivity(intent);

    }

    public void pay(){
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
//                            finish();
                        }
                    }).show();
            return;
        }
//        // 构造PayTask 对象
//        PayTask payTask = new PayTask(activity);
//        // 调用查询接口，获取查询结果
//        boolean isExist = payTask.checkAccountIfExist();
//        if(!isExist){
//            h5Pay();
//            return;
//        }

        String orderInfo = getOrderInfo(title, notice, price,order_id);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price,String order_id) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order_id + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" +callUrl + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
