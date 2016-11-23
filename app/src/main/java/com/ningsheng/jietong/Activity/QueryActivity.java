package com.ningsheng.jietong.Activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.ClearWriteEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/23.
 */
public class QueryActivity extends TitleActivity implements View.OnClickListener,ClearWriteEditText.OnEditChangeListener{
    private RadioGroup radioGroup;
    private EditText m_password;
    private TextView m_hiti;
    private ClearWriteEditText m_cardNo;
    private Button button;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_query);
        setTitle("余额查询");
        showBackwardView("",true);

        radioGroup=(RadioGroup) findViewById(R.id.act_query_radiogroup);
        m_password=(EditText) findViewById(R.id.act_query_password);
        m_hiti=(TextView)findViewById(R.id.act_query_hint);
        m_cardNo=(ClearWriteEditText) findViewById(R.id.act_query_cardNo);

    }

    @Override
    protected void initListener() {
         button=(Button) findViewById(R.id.act_query_commit);
        button.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.act_query_radiobutton1:
                        m_password.setVisibility(View.GONE);
                        m_hiti.setVisibility(View.GONE);
                        break;
                    case R.id.act_query_radiobutton2:
                        m_password.setVisibility(View.VISIBLE);
                        m_hiti.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        m_cardNo.setListener(this);
    }

    @Override
    protected void initDate() {

    }

    private void query(){
        if(m_cardNo.getText().toString().length()!=19){
            toast("卡号有误！");
            m_cardNo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }
        Map<String,String> map=new HashMap<String,String>();
        map.put("cardNo",m_cardNo.getText().toString());
        map.put("accountId", MyApplication.user.getId());

        HttpSender sender=new HttpSender(Url.query, "查询", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                String balance=(String)gsonUtil.getInstance().getValue(data,"balance");
                final AlertDialog.Builder dailog=new AlertDialog.Builder(QueryActivity.this);
                dailog.setMessage("当前余额为：￥"+balance);
//                dailog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dailog.create().dismiss();
//                    }
//                });
                dailog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dailog.create().dismiss();
                    }
                });
                dailog.show();
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_query_commit:
                query();
                break;
        }
    }

    @Override
    public void change(View view, boolean ischange) {
        button.setEnabled(ischange);
        if(ischange){
            button.setBackgroundResource(R.drawable.shape_button);

        }else{
            button.setBackgroundResource(R.drawable.shape_messagecode_gary);

        }
    }
}
