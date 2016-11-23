package com.ningsheng.jietong.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.MaterialDialog;
import com.ningsheng.jietong.Dialog.SetPayPassDialog;
import com.ningsheng.jietong.Entity.MyCardInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.MateriaDialogUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/23.
 */
public class
CardDetailsActivity extends BaseActivity implements View.OnClickListener, MaterialDialog.MaterialButtonListener {
    private MyPopupwindow popupwindow;
    private MaterialDialog materialDialog;
    private AnimatorSet showAnimatorSet;
    private AnimatorSet hideAnimatorSet;
    private int lineWidth;
    private int linearHeight;
    private boolean bool = true;
    private TextView tvType;
    private TextView tvRealName;
    private TextView tvDenomination;
    private TextView tvCardNo;
    private TextView cardDetailsTvType;
    private TextView cardDetailsTvBanance;
    private ImageView ivGua;
    private View m_toup;
    private int error = 5;
    MyCardInfo info;

    protected void initView() {
        setContentView(R.layout.activity_carddetails);
        tvType = (TextView) findViewById(R.id.mycard_item_cardType);
        tvRealName = (TextView) findViewById(R.id.mycard_item_realName);
        ivGua = (ImageView) findViewById(R.id.adapter_mycard_guashi);

        tvDenomination = (TextView) findViewById(R.id.mycard_item_denomination);
        tvCardNo = (TextView) findViewById(R.id.mycard_item_cardNo);
        cardDetailsTvType = (TextView) findViewById(R.id.carddetails_tv_type);
        cardDetailsTvBanance = (TextView) findViewById(R.id.carddetails_tv_balance);
         m_toup=findViewById(R.id.act_carddetails_toup);

         info = (MyCardInfo) getIntent().getSerializableExtra("cardInfo");
        if (info == null) {
            finish();
            return;
        }

        if ("0".equals(info.getCardType())) {
            if("1".equals(info.getStatus())){
                ivGua.setVisibility(View.VISIBLE);
                tvRealName.setVisibility(View.GONE);
            }else{
                ivGua.setVisibility(View.GONE);
                tvRealName.setVisibility(View.VISIBLE);
            }
        } else {
            tvRealName.setVisibility(View.GONE);
        }


//        if ("实体卡".equals(info.getHandlerCardType())) {
//            tvRealName.setVisibility(View.VISIBLE);
//        } else {
//            tvRealName.setVisibility(View.INVISIBLE);
//        }
        if (null == info.getFaceValue()||"0".equals(info.getFaceValue())) {
            tvDenomination.setVisibility(View.INVISIBLE);
        } else {
            tvDenomination.setText(info.getFaceValue());
        }
        if (null != info.getCardNo()) {
            String cardNo = info.getCardNo();
            if (cardNo.length() > 8) {
                StringBuilder sb = new StringBuilder(cardNo.length() - 8);
                for (int i = 0; i < cardNo.length() - 8; i++) {
                    if ((i + 1) % 4 == 0)
                        sb.append(" ");
                    sb.append("*");
                }
                tvCardNo.setText(cardNo.substring(0, 4) + " " + sb.toString() + " " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
            } else {
                tvCardNo.setText(cardNo);
            }
        } else {
            tvCardNo.setVisibility(View.INVISIBLE);
        }

        if(info.getFaceValue()==null||info.getFaceValue().equals("0")){
            cardDetailsTvType.setText("充值卡");
//            m_toup.setVisibility(View.VISIBLE);
        }else{
            cardDetailsTvType.setText("面值卡");
        }

        if(info.getCardType().equals("0")&&(info.getFaceValue()==null||info.getFaceValue().equals("0"))){//虚拟卡 0.实体卡
            m_toup.setVisibility(View.VISIBLE);
        }else{
            m_toup.setVisibility(View.GONE);
        }

        cardDetailsTvBanance.setText(info.getBalance());

    }

    @Override
    protected void initListener() {
        findViewById(R.id.button_forward).setOnClickListener(this);
        findViewById(R.id.button_backward).setOnClickListener(this);
        m_toup.setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_forward:
                if (popupwindow == null) {
                    popupwindow = new MyPopupwindow(this);
                }
                if(info.getCardType().equals("1")){
                    toast("虚拟卡暂时不能解绑！");
                    return;
                }
                if(info.getStatus().equals("1")){
                    toast("该卡已挂失！");
                    return;
                }
                popupwindow.showAsDropDown(v, (-v.getWidth() - AndroidUtil.dip2px(CardDetailsActivity.this, 60)) >> 1, -v.getHeight() >> 1);
                break;
            case R.id.button_backward:
                finish();
                break;
            case R.id.act_carddetails_toup:
                Intent intent=new Intent(this,TopUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 消费明细
     * @param view
     */
    public void cardDetailClick(View view) {
        Intent intent = new Intent(this, ConsumeDetailActivity.class);
        intent.putExtra("cardNo",info.getCardNo());
        startActivity(intent);
    }

    private void valiTranPwd(String pwd) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", MyApplication.user.getId());
        map.put("transactionPwd", pwd);
        HttpSender sender = new HttpSender(Url.valiTranPwd, "验证支付密码", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                if ("支付密码验证错误".equals(message)) {
                    error--;
                    if (error <= 0) {
                    }
                    materialDialog.setTitle("密码错误");
                    materialDialog.setContentEdit1("(还可输入" + error + "次，5次错误后账户将冻结24小时)");
                    showAnimatorSet.start();
                    bool = false;
                } else {
                    Intent intent = new Intent(CardDetailsActivity.this, RemoveBindActivity.class);
                    intent.putExtra("cardNo",info.getCardNo());
                    startActivity(intent);
                }
            }

            @Override
            public void onFinished() {
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }

    @Override
    public void onCancel(TextView v) {
    }

    @Override
    public void onConfirm(final TextView v) {
        final MaterialDialog.MaterialDialogEdit materialDialogEdit = (MaterialDialog.MaterialDialogEdit) v.getTag();
        if (TextUtils.isEmpty(materialDialogEdit.et.getText().toString())) {
            toast("亲，忘记输支付密码了");
            materialDialogEdit.et.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }
        if (showAnimatorSet == null) {
            lineWidth = materialDialogEdit.line.getWidth();
            linearHeight = materialDialogEdit.linear.getHeight();
            ValueAnimator showValueAnimator1 = ValueAnimator.ofInt(materialDialogEdit.line.getWidth(), 0);
            showValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    materialDialogEdit.line.getLayoutParams().width = (int) animation.getAnimatedValue();
                    materialDialogEdit.line.requestLayout();
                }
            });
            ValueAnimator showValueAnimator2 = ValueAnimator.ofInt(materialDialogEdit.linear.getHeight(), 0);
            showValueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    materialDialogEdit.linear.getLayoutParams().height = (int) animation.getAnimatedValue();
                    materialDialogEdit.linear.requestLayout();
                }
            });
            ValueAnimator showValueAnimator3 = ValueAnimator.ofInt(0, AndroidUtil.dip2px(CardDetailsActivity.this, 40));
            showValueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    materialDialogEdit.linear1.getLayoutParams().height = (int) animation.getAnimatedValue();
                    materialDialogEdit.linear1.requestLayout();
                }
            });
            ValueAnimator showValueAnimator4 = ValueAnimator.ofInt(AndroidUtil.dip2px(CardDetailsActivity.this, 55), AndroidUtil.dip2px(CardDetailsActivity.this, 70));
            showValueAnimator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    materialDialogEdit.confirmButton.getLayoutParams().width = (int) animation.getAnimatedValue();
                    materialDialogEdit.confirmButton.requestLayout();
                }
            });
            ObjectAnimator showObjectAnimator1 = ObjectAnimator.ofFloat(materialDialogEdit.linear, "alpha", 1.0f, 0f);
            showAnimatorSet = new AnimatorSet();
            showAnimatorSet.setDuration(500);
            showAnimatorSet.play(showValueAnimator1).before(showObjectAnimator1);
            showAnimatorSet.play(showObjectAnimator1).with(showValueAnimator2);
            showAnimatorSet.play(showValueAnimator2).with(showValueAnimator3).with(showValueAnimator4);
            showAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    materialDialog.setConfirmButton("重新输入");
                    v.setEnabled(true);

                }
            });

        } else if (hideAnimatorSet == null) {
            ValueAnimator hideValueAnimator1 = ValueAnimator.ofInt(0, lineWidth);
            hideValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    materialDialogEdit.line.getLayoutParams().width = (int) animation.getAnimatedValue();
                    materialDialogEdit.line.requestLayout();
                }
            });
            ValueAnimator hideValueAnimator2 = ValueAnimator.ofInt(0, linearHeight);
            hideValueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    materialDialogEdit.linear.getLayoutParams().height = (int) animation.getAnimatedValue();
                    materialDialogEdit.linear.requestLayout();
                }
            });
            ValueAnimator hideValueAnimator3 = ValueAnimator.ofInt(AndroidUtil.dip2px(CardDetailsActivity.this, 40), 0);
            hideValueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    materialDialogEdit.linear1.getLayoutParams().height = (int) animation.getAnimatedValue();
                    materialDialogEdit.linear1.requestLayout();
                }
            });
            ValueAnimator hideValueAnimator4 = ValueAnimator.ofInt(AndroidUtil.dip2px(CardDetailsActivity.this, 70), AndroidUtil.dip2px(CardDetailsActivity.this, 55));
            hideValueAnimator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    materialDialogEdit.confirmButton.getLayoutParams().width = (int) animation.getAnimatedValue();
                    materialDialogEdit.confirmButton.requestLayout();
                }
            });
            ObjectAnimator hideObjectAnimator1 = ObjectAnimator.ofFloat(materialDialogEdit.linear, "alpha", 0f, 1.0f);
            hideAnimatorSet = new AnimatorSet();
            hideAnimatorSet.setDuration(500);
            hideAnimatorSet.play(hideValueAnimator2).with(hideValueAnimator3).with(hideValueAnimator4).with(hideObjectAnimator1).before(hideValueAnimator1);
            hideAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    v.setEnabled(true);
                }
            });

        }
        if (bool) {
            valiTranPwd(materialDialogEdit.et.getText().toString());
        } else {
            materialDialog.setTitle("输入支付密码");
            materialDialog.setConfirmButton("确定");
            hideAnimatorSet.start();
            bool = true;
        }
    }

    private class MyPopupwindow extends PopupWindow implements View.OnClickListener {
        private Context context;

        public MyPopupwindow(Context context) {
            super(context);
            this.context = context;
            initView();
        }

        private void initView() {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(6);
            drawable.setColor(0xFFFFFFFF);
            RelativeLayout relativeLayout = new RelativeLayout(context);
            int padding = AndroidUtil.dip2px(context, 35);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(padding << 1, padding);
            TextView tv = new TextView(context);
            tv.setOnClickListener(this);
            tv.setGravity(Gravity.CENTER);
            relativeLayout.addView(tv, layoutParams);
            tv.setTextColor(getResources().getColor(R.color.font_black));
            tv.setText("解绑");
            tv.setTextSize(12);
            setContentView(relativeLayout);
            setFocusable(true);
            setBackgroundDrawable(drawable);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setWidth(AndroidUtil.dip2px(CardDetailsActivity.this, 60));
        }

        private SetPayPassDialog dialog, dialog1;
        private String first_pwd;

        @Override
        public void onClick(View v) {
            dismiss();
            if(info.getCardType().equals("1")){
                toast("虚拟卡暂时不能解绑！");
                return;
            }
            if(info.getStatus().equals("1")){
                toast("改卡暂时为挂失状态不能解绑！");
                return;
            }
            if (MyApplication.user.getId() == null || MyApplication.user.getId().equals("")){
                Intent intent=new Intent(CardDetailsActivity.this,LoginActivity.class);
                startActivityForResult(intent,11);
            }else{
                isTranPw();
            }



//            Intent intent = new Intent(CardDetailsActivity.this, RemoveBindActivity.class);
//            startActivity(intent);
        }


        private void isTranPw(){
            if (StringUtil.isBlank(MyApplication.user.getTransactionPwd())) {
                dialog = new SetPayPassDialog(CardDetailsActivity.this);
                dialog.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                    @Override
                    public void callBaceTradePwd(String pwd) {
//                    dialog.setTitle("再次输入密码");
//                    dialog.setIsfirst(true);
                        first_pwd = pwd;
                        dialog.dismiss();
                        dialog1 = new SetPayPassDialog(CardDetailsActivity.this);
                        dialog1.setTitle("再次输入密码");
                        dialog1.setIsfirst(true);
                        dialog1.show();
                        dialog1.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                            @Override
                            public void callBaceTradePwd(String pwd) {
                                if (first_pwd.equals(pwd)) {
                                    setTransPassWord(pwd);
                                }else{
                                    toast("两次输入不一致");
                                    dialog.show();
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
            }else{
                if (materialDialog == null) {
                    materialDialog = MateriaDialogUtil.showEditDialog(CardDetailsActivity.this, "输入密码", "", "取消", "确定", CardDetailsActivity.this);
                } else {
                    materialDialog.show();
                }
            }
        }

        private void setTransPassWord(String num) {
            Map<String, String> map = new HashMap<>();
            map.put("accountId", MyApplication.user.getId());
            map.put("id", MyApplication.user.getId());
            map.put("newPwd", num);
            HttpSender sender = new HttpSender(Url.assignTranPwd, "设置支付密码", map, new OnHttpResultListener() {
                @Override
                public void onSuccess(String message, String code, String data) {
                    if (code.equals("0000")) {
                        dialog1.dismiss();
                        MyApplication.user.setTransactionPwd("true");
                        SharedPreferencesUtil.getInstance(CardDetailsActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER, MyApplication.user);
                        toast("设置成功！");
                        if (materialDialog == null) {
                            materialDialog = MateriaDialogUtil.showEditDialog(CardDetailsActivity.this, "输入密码", "", "取消", "确定", CardDetailsActivity.this);
                        } else {
                            materialDialog.show();
                        }
                    }else{
                        toast(message);
                    }
                }
            });
            sender.setContext(CardDetailsActivity.this);
            sender.send(Url.Post);
        }





        private void showPassWordDialog() {
            if(info.getCardType().equals("1")){
                toast("虚拟卡暂时不能解绑！");
                return;
            }
            if(info.getStatus().equals("1")){
                toast("改卡暂时为挂失状态不能解绑！");
                return;
            }
            materialDialog = MateriaDialogUtil.showEditDialog(CardDetailsActivity.this, "输入密码", "", "取消", "确定", new MaterialDialog.MaterialButtonListener() {
                @Override
                public void onCancel(TextView v) {

                }

                @Override
                public void onConfirm(final TextView v) {
                    v.setEnabled(false);
                    final MaterialDialog.MaterialDialogEdit materialDialogEdit = (MaterialDialog.MaterialDialogEdit) v.getTag();

                }
            });
            materialDialog.setCancalButtonColor(R.color.font_gray, R.color.color_while);

        }
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//    }

}


