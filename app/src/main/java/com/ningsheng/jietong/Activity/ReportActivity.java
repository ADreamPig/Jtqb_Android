package com.ningsheng.jietong.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.NormalPopuWindows;
import com.ningsheng.jietong.Dialog.SetPayPassDialog;
import com.ningsheng.jietong.Entity.AccountCard;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.StringUtil;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class ReportActivity extends TitleActivity implements View.OnClickListener{
    private TextView m_edit;
    private String carNo;
    private   NormalPopuWindows popu;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_report);
        setTitle("挂失");
        showBackwardView("",true);

        findViewById(R.id.act_report_commit).setOnClickListener(this);

         m_edit=(TextView) findViewById(R.id.act_report_pull);
        m_edit.setOnClickListener(this);
    }

    @Override
    protected void initListener() {

    }
    private List<String> list = new ArrayList<>();
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
                    if (ac.getCardType().equals("0")&&ac.getStatus().equals("0")) {
//                        if ("".equals(ac.getFaceValue())) {
                            list.add(ac.getCardNo() + "（￥" + ac.getBalance() + "）");
//                        }
                    }
                }
                  popu = new NormalPopuWindows(ReportActivity.this, list, m_edit, null);
                popu.setOnBack(new NormalPopuWindows.NormalPopuBack() {
                    @Override
                    public void popuBack(int position) {
                        m_edit.setText(list.get(position));
                        popu.dismiss();
                        carNo =  list.get(position).split("（")[0];
//                        TopUpActivity.this.map.put("cardNo", Mlist.get(position).getCardNo());
                    }
                });
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

//        if (StringUtil.isBlank(MyApplication.user.getTransactionPwd())) {
//            dialog = new SetPayPassDialog(this);
//            dialog.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
//                @Override
//                public void callBaceTradePwd(String pwd) {
////                    dialog.setTitle("再次输入密码");
////                    dialog.setIsfirst(true);
//                    first_pwd = pwd;
//                    dialog.dismiss();
//                    dialog1 = new SetPayPassDialog(TopUpActivity.this);
//                    dialog1.setTitle("再次输入密码");
//                    dialog1.setIsfirst(true);
//                    dialog1.show();
//                    dialog1.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
//                        @Override
//                        public void callBaceTradePwd(String pwd) {
//                            if (first_pwd.equals(pwd)) {
//
//                                setTransPassWord(pwd);
//                            }
//                        }
//
//                        @Override
//                        public void callClose() {
//                            dialog1.dismiss();
//                            finish();
//                        }
//                    });
//                }
//
//                @Override
//                public void callClose() {
//                    dialog.dismiss();
//                    finish();
//                }
//            });
//
//            dialog.show();
//        }
    }

    public void tel(View v){
        Intent intent1 = new Intent(Intent.ACTION_CALL);
        intent1.setData(Uri.parse("tel:4008871380"));
        startActivity(intent1);
    }

    private void commit(String carNo){
        Map<String ,String > map=new HashMap<>();
        map.put("cardNum",carNo);
        HttpSender sender=new HttpSender(Url.lossCard, "挂失", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                String msg=null;
                if(code.equals("0000")){
                    msg="挂失成功，如需解挂请拨打人工客服解挂！";
//                Intent intent=new Intent(ReportActivity.this,RepordFinishedAct.class);
//                startActivity(intent);
                }else{
                     msg="挂失失败，请拨打人工客服申请挂失！";
                }
                AlertDialog.Builder dialog=new AlertDialog.Builder(ReportActivity.this);
                dialog.setMessage(msg);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();


            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_report_commit:
                if(carNo==null){
                    toast("请选择要挂失的卡！");
                    return;
                }
                commit(carNo);

                break;
            case R.id.act_report_pull:
                if(popu!=null){
                    if(list.size()==0){
                        toast("还没有可挂失的卡哦！");
                        return;
                    }
                    popu.show();
                }
                break;
        }

    }
}
