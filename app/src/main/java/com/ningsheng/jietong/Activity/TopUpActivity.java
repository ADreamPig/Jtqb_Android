package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.AliPay.PayUtil;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.NormalPopuWindows;
import com.ningsheng.jietong.Dialog.PaymentDialog;
import com.ningsheng.jietong.Dialog.RealNameDialog;
import com.ningsheng.jietong.Dialog.SetPayPassDialog;
import com.ningsheng.jietong.Entity.AccountCard;
import com.ningsheng.jietong.Entity.OrderDetail;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.ClearWriteEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/23.
 */
public class TopUpActivity extends TitleActivity implements View.OnClickListener, ClearWriteEditText.OnEditChangeListener {
    private RadioGroup m_group;
    private int state;
    private View m_selectCard;
    private ClearWriteEditText m_money;
    private Map<String, String> map;
    private TextView m_pull;
    private View m_show;
    private Button m_commit;
    private List<String> list = new ArrayList<>();
    private NormalPopuWindows popu;

    private SetPayPassDialog dialog, dialog1;
    private String first_pwd;

    private RealNameDialog dialog_real;
    private  RippleView rippleView;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_top_up);
        setTitle("充值");

        m_group = (RadioGroup) findViewById(R.id.act_top_up_radiogroup);
        m_selectCard = findViewById(R.id.act_top_up_seleccardll);
        m_money = (ClearWriteEditText) findViewById(R.id.act_top_up_money);
        m_pull = (TextView) findViewById(R.id.act_top_up_pull);
        m_show = findViewById(R.id.act_top_up_xiala);
        rippleView=(RippleView)findViewById(R.id.act_login_RippleView);

        if(MyApplication.user.getIdentityCard()==null||"".equals(MyApplication.user.getIdentityCard())){
            dialog_real=new RealNameDialog(this);
            dialog_real.setCancelable(false);
            dialog_real.show();
        }

        map = new HashMap<String, String>();
        map.put("payType", "3");
        map.put("phoneOSType", "0");
        map.put("operationType", "4");
    }

    @Override
    protected void initListener() {
        m_pull.setOnClickListener(this);
        m_money.setListener(this);
        m_commit = (Button) findViewById(R.id.act_top_up_commit);
        m_commit.setOnClickListener(this);
        m_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.act_top_up_radiobutton1:
                        state = 0;
                        m_selectCard.setVisibility(View.VISIBLE);
                        break;
                    case R.id.act_top_up_radiobutton2:
                        state = 1;
                        m_selectCard.setVisibility(View.GONE);
                        break;
                }
            }
        });

        m_money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Double temp = Double.parseDouble(m_money.getText().toString());
                }
            }
        });
    }

    private String carNo;

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", MyApplication.user.getId());
        map.put("accountId", MyApplication.user.getId());
        HttpSender sender = new HttpSender(Url.myCardList, "我的卡", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                list.clear();
                final List<AccountCard> Mlist = (List<AccountCard>) gsonUtil.getInstance().json2List(data, new TypeToken<List<AccountCard>>() {
                }.getType());
                for (AccountCard ac : Mlist) {
                    if (ac.getCardType().equals("0")) {
                        if ("".equals(ac.getFaceValue())||"0".equals(ac.getFaceValue())) {
                            if("0".equals(ac.getStatus()))
                            list.add(ac.getCardNo()+"￥("+ac.getBalance()+")");
                        }
                    }
                }
                popu = new NormalPopuWindows(TopUpActivity.this, list, m_show, null);
                popu.setOnBack(new NormalPopuWindows.NormalPopuBack() {
                    @Override
                    public void popuBack(int position) {
                        m_pull.setText(list.get(position));
                        popu.dismiss();
                        carNo = list.get(position).split("￥")[0];
//                        TopUpActivity.this.map.put("cardNo", Mlist.get(position).getCardNo());
                    }
                });
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x11&&!"".equals(MyApplication.user.getIdentityCard())){
            dialog_real.dismiss();
        }
      else  if (StringUtil.isBlank(MyApplication.user.getTransactionPwd())) {
            dialog = new SetPayPassDialog(this);
            dialog.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                @Override
                public void callBaceTradePwd(String pwd) {
//                    dialog.setTitle("再次输入密码");
//                    dialog.setIsfirst(true);
                    first_pwd = pwd;
                    dialog.dismiss();
                    dialog1 = new SetPayPassDialog(TopUpActivity.this);
                    dialog1.setTitle("再次输入密码");
                    dialog1.setIsfirst(true);
                    dialog1.show();
                    dialog1.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                        @Override
                        public void callBaceTradePwd(String pwd) {
                            if (first_pwd.equals(pwd)) {

                                setTransPassWord(pwd);
                            }
                        }

                        @Override
                        public void callClose() {
                            dialog1.dismiss();
                            finish();
                        }
                    });
                }

                @Override
                public void callClose() {
                    dialog.dismiss();
                    finish();
                }
            });

            dialog.show();
        }
    }

    private void setTransPassWord(String num) {
        Map<String, String> map = new HashMap<>();
        map.put("id", MyApplication.user.getId());
        map.put("newPwd", num);
        HttpSender sender = new HttpSender(Url.assignTranPwd, "设置支付密码", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    dialog1.dismiss();
                    MyApplication.user.setTransactionPwd("true");
                    SharedPreferencesUtil.getInstance(TopUpActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER, MyApplication.user);
                    toast("设置成功！");
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    private void commmit() {
        if ("".equals(m_money.getText().toString())) {
            toast("金额不能为空！");
            m_money.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }
        if (Double.parseDouble(m_money.getText().toString()) > 50000) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("最大充值金额为50000");
            dialog.setPositiveButton("确定", null);
            dialog.show();
            return;
        };

        map.put("accountId", MyApplication.user.getId());
        map.put("money", m_money.getText().toString());
        map.put("cardNo", carNo);
        HttpSender sender = new HttpSender(Url.submitOrder, "充值", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    OrderDetail get = gsonUtil.getInstance().json2Bean(data, OrderDetail.class);
                    new PayUtil(TopUpActivity.this, "商品1", "描述1", get.getMoney(), get.getTradeNo(), get.getNotifyUrl(), new PayUtil.OnPayCallBack() {
                        @Override
                        public void onSucceed() {
                            toast("充值成功！");
                            Intent intent=new Intent(TopUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void OnPaying() {
                            toast("支付中。。。");
                        }

                        @Override
                        public void OnFailure() {
                            toast("失败！");
                        }
                    }).pay();
                } else {
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_top_up_commit:
                if (m_money.getText().toString().equals("")) {
                    toast("请填写充值金额！");
                    return;
                }
                if(state==0&&("".equals(carNo)||carNo==null)){
                    toast("请选择充值卡号");
                    return;
                }
                final PaymentDialog dialog = new PaymentDialog(this);
                dialog.setOnPaymentBack(new PaymentDialog.onPaymentBack() {
                    @Override
                    public void onBack() {
                        dialog.dismiss();
                        commmit();

                    }
                });
                dialog.show();
                break;
            case R.id.act_top_up_pull:
                if (popu != null) {
                    if (list.size() == 0) {
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                        dialog1.setMessage("还没有可充值的卡哦");
                        dialog1.setPositiveButton("确定", null);
                        dialog1.show();
                        return;
                    }
                    popu.show();
                }
                break;
        }
    }

    @Override
    public void change(View view, boolean ischange) {
        m_commit.setBackgroundResource(ischange ? R.drawable.shape_button : R.drawable.shape_messagecode_gary);
        rippleView.setEnabled(ischange);
    }
}
