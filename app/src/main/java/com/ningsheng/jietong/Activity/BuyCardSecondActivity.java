package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.FaceValueAdapter;
import com.ningsheng.jietong.Adapter.RecycleOnItemClickLisiener;
import com.ningsheng.jietong.Adapter.RecyclerFaceValueAdapre;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.FaceValue;
import com.ningsheng.jietong.Entity.SubmitOrder;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.IdcardUtils;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhushunqing on 2016/2/23.
 */
public class BuyCardSecondActivity extends TitleActivity implements View.OnClickListener {
    private RadioGroup radioGroup;
    private RecyclerView m_list;
    private String type = "0";//0不记名卡 1.记名充值卡 2.记名面值卡
    private View m_module1, m_module2, m_module3, m_module4;
    private EditText m_nickname, m_cardId, m_num, m_num1;
    private TextView m_paySum1, m_paySum;
    private String id;//收货地址id
    private SubmitOrder submitOrder = new SubmitOrder();
    private List<FaceValue> list = new ArrayList<FaceValue>();
    private RecyclerFaceValueAdapre adapter;
    private EditText m_topup;

    private int num = 1, num1 = 1;
    private String sum = "0", sum1 = "0";

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_buycard_second);
        setTitle("购卡申请");
        showBackwardView("", true);
        id = getIntent().getStringExtra("id");

        radioGroup = (RadioGroup) findViewById(R.id.act_buycard_second_group);
        m_list = (RecyclerView) findViewById(R.id.act_buycard_sencond_value);

        m_module1 = findViewById(R.id.act_buycard_sencond_module1);
        m_module2 = findViewById(R.id.act_buycard_sencond_module2);
        m_module3 = findViewById(R.id.act_buycard_sencond_module3);
        m_module4 = findViewById(R.id.act_buycard_sencond_module4);
        m_nickname = (EditText) findViewById(R.id.act_buycard_sencond_nickname);
        m_cardId = (EditText) findViewById(R.id.act_buycard_sencond_cardId);
        m_num = (EditText) findViewById(R.id.act_buycard_sencond_num);
        m_num1 = (EditText) findViewById(R.id.act_buycard_sencond_num1);
        m_paySum1 = (TextView) findViewById(R.id.act_buycard_sencond_paySum1);//type=2
        m_paySum = (TextView) findViewById(R.id.act_buycard_sencond_paySum);
        m_topup = (EditText) findViewById(R.id.act_buycard_sencond_TopUp);
        findViewById(R.id.act_buycard_sencond_numsub).setOnClickListener(this);
        findViewById(R.id.act_buycard_sencond_numadd).setOnClickListener(this);
        findViewById(R.id.act_buycard_sencond_numsub1).setOnClickListener(this);
        findViewById(R.id.act_buycard_sencond_numadd1).setOnClickListener(this);
        m_num.setText(num + "");
//        m_topup.setText(num1+"");
        m_paySum1.setText("￥" + sum1);
        m_paySum.setText("￥" + sum);
//        adapter = new FaceValueAdapter(this, list);
         adapter=new RecyclerFaceValueAdapre(this,list);
        m_list.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL));
        m_list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        setResult(99);
        finish();
    }

    @Override
    protected void onBackward() {
//        super.onBackward();
        setResult(99);
        finish();
    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_buycard_second_next).setOnClickListener(this);

        m_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                num += 1;
//                m_num.setText(num + "");
                if (m_num.getText().toString() == null || m_num.getText().toString().equals("")) {
                    num = 0;
                } else {
                    num = Integer.parseInt(s.toString());
                }
                Log.i("----num------", num +"");
                m_paySum.setText("￥" + (num) * Integer.parseInt(adapter.getSelect()));
                sum = (num) * Integer.parseInt(adapter.getSelect()) + "";

            }
        });

        m_num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if("".equals(m_num1.getText().toString())){
                    num1=0;
                }else{
                    num1=Integer.parseInt(s.toString());
                }

                if (!"".equals(m_topup.getText().toString())) {
                    m_paySum1.setText("￥" + (num1) * (int) Double.parseDouble(m_topup.getText().toString()));
                    sum1 = (num1) * (int) Double.parseDouble(m_topup.getText().toString()) + "";
                } else {
                    m_paySum1.setText("￥" + (num1) * (int) Double.parseDouble("0"));
                    sum1 = (num1) * (int) Double.parseDouble("0") + "";
                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.act_buycard_second_button1:
                        type = "0";
                        m_module1.setVisibility(View.GONE);
                        m_module2.setVisibility(View.VISIBLE);
                        m_module3.setVisibility(View.VISIBLE);
                        m_module4.setVisibility(View.GONE);
                        adapter.setSelect(0);
                        m_paySum.setText("￥" + Integer.parseInt(adapter.getSelect()) * num);
                        sum = adapter.getSelect();
                        break;
                    case R.id.act_buycard_second_button2:
                        type = "1";
                        m_module1.setVisibility(View.VISIBLE);
                        m_module2.setVisibility(View.GONE);
                        m_module3.setVisibility(View.GONE);
                        m_module4.setVisibility(View.VISIBLE);
                        break;
                    case R.id.act_buycard_second_button3:
                        type = "2";
                        m_module1.setVisibility(View.VISIBLE);
                        m_module2.setVisibility(View.VISIBLE);
                        m_module3.setVisibility(View.VISIBLE);
                        m_module4.setVisibility(View.GONE);
                        adapter.setSelect(0);
                        m_paySum.setText("￥" + Integer.parseInt(adapter.getSelect()) * num);
                        sum = adapter.getSelect();
//                        m_paySum1.setText("￥"+adapter.getSelect());
//                        sum1=adapter.getSelect();
                        break;
                }
            }
        });
//        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                adapter.setSelect(position);
//                m_paySum.setText("￥" + num * Integer.parseInt(adapter.getSelect()));
//                sum = num * Integer.parseInt(adapter.getSelect()) + "";
////                m_paySum1.setText("￥"+num1*Integer.parseInt(adapter.getSelect()));
//                adapter.notifyDataSetChanged();
//            }
//        });
        adapter.setOnItemClickListenet(new RecycleOnItemClickLisiener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                adapter.setSelect(position);
                m_paySum.setText("￥" + num * Integer.parseInt(adapter.getSelect()));
                sum = num * Integer.parseInt(adapter.getSelect()) + "";
//                m_paySum1.setText("￥"+num1*Integer.parseInt(adapter.getSelect()));
                adapter.notifyDataSetChanged();
            }
        });
        m_topup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //失去焦点
                    if (!m_topup.getText().toString().equals("")) {
                        int d = Integer.parseInt(m_topup.getText().toString());
                        if (d<100||d>5000) {
                            AlertDialog.Builder dialog=new AlertDialog.Builder(BuyCardSecondActivity.this);
                            dialog.setMessage("请输入大于100小于5000的整数");
                            dialog.setPositiveButton("确定",null);
                            dialog.show();
//                            toast("请输入100为单位的整数");
                            m_topup.setText("");
                            return;
                        }
                        m_paySum1.setText("￥" + num1 * Integer.parseInt(m_topup.getText().toString()));
                        sum1 = num1 * Integer.parseInt(m_topup.getText().toString()) + "";
                    }
                }
            }
        });

        m_topup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!m_topup.getText().toString().equals("")) {
                    int d = Integer.parseInt(m_topup.getText().toString());
//                    double d = Double.parseDouble(m_topup.getText().toString());
//                    if ((d / 100) - (int) (d / 100) != 0) {
//                        toast("请输入100为单位的整数");
//                        m_topup.setText("");
//                        return;
//                    }
                    m_paySum1.setText("￥" + num1 * Integer.parseInt(m_topup.getText().toString()));
                    sum1 = num1 * Integer.parseInt(m_topup.getText().toString()) + "";
                }
            }
        });

    }

    @Override
    protected void initDate() {
        HttpSender sender = new HttpSender(Url.faceValues, "卡面值", null, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                List<FaceValue> Mlist = (List<FaceValue>) gsonUtil.getInstance().json2List(data, new TypeToken<List<FaceValue>>() {
                }.getType());
                list.addAll(Mlist);
                adapter.notifyDataSetChanged();
                sum = list.get(0).getFaceValue();
//                sum1=list.get(0).getFaceValue();
//                m_paySum1.setText("￥"+sum1);
                m_paySum.setText("￥" + sum);
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }

    private void submit() {
        submitOrder.setAccountId(MyApplication.user.getId());
        submitOrder.setPhoneOSType("0");//安卓
        submitOrder.setOperationType("1");//业务类型
        submitOrder.setShippingAddressId(id);//地址id
        submitOrder.setPayType("3");//支付宝
        if (type.equals("0")) {
            submitOrder.setMoney(sum);//支付金额
            submitOrder.setCardType("0");
            submitOrder.setFaceValue(adapter.getSelect());//面值
            submitOrder.setCardNumber(num + "");
        } else if (type.equals("1")) {
            if (!IdcardUtils.validateCard(m_cardId.getText().toString())) {
                toast("身份证信息不合法！");
                return;
            }
            if (m_nickname.getText().toString().equals("")) {
                toast("请输入姓名！");
                return;
            }
            if (m_topup.getText().toString().equals("")) {
                toast("请输入面值！");
                return;
            }
            double d = Double.parseDouble(m_topup.getText().toString());
//            if ((d / 100) - (int) (d / 100) != 0) {
            if (d<100||d>5000) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(BuyCardSecondActivity.this);
                dialog.setMessage("请输入大于100小于5000的整数");
                dialog.setPositiveButton("确定",null);
                dialog.show();
//                            toast("请输入100为单位的整数");
                m_topup.setText("");
                return;
            }
            m_paySum1.setText("￥" + num1 * Integer.parseInt(m_topup.getText().toString()));
            sum1 = num1 * Integer.parseInt(m_topup.getText().toString()) + "";
            submitOrder.setMoney(sum1);//支付金额
            submitOrder.setCardType("1");
            submitOrder.setFaceValue(m_topup.getText().toString());//面值
            submitOrder.setCardNumber(num1 + "");
            submitOrder.setRealName(m_nickname.getText().toString());
            submitOrder.setIdentityCard(m_cardId.getText().toString());
        } else if (type.equals("2")) {
            submitOrder.setMoney(sum);//支付金额
            submitOrder.setCardType("2");
            submitOrder.setFaceValue(adapter.getSelect());//面值
            submitOrder.setCardNumber(num + "");
            submitOrder.setRealName(m_nickname.getText().toString());
            submitOrder.setIdentityCard(m_cardId.getText().toString());
        }
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("order", submitOrder);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_buycard_sencond_numsub:
                if (num - 1 > 0) {
                    num -= 1;
                    m_num.setText(num + "");
                    m_paySum.setText("￥" + (num) * Integer.parseInt(adapter.getSelect()));
                    sum = (num) * Integer.parseInt(adapter.getSelect()) + "";
                } else {
                    toast("最少购买一张！");
                }
                break;
            case R.id.act_buycard_sencond_numsub1:
                if (num1 - 1 > 0) {
                    num1 -= 1;
                    m_num1.setText(num1 + "");
                    if (m_topup.getText().toString().equals("")) {
                        m_paySum1.setText("￥" + (num1) * (int) Double.parseDouble("0"));
                        sum1 = (num1) * (int) Double.parseDouble("0") + "";
                    } else {
                        m_paySum1.setText("￥" + (num1) * (int) Double.parseDouble(m_topup.getText().toString()));
                        sum1 = (num1) * (int) Double.parseDouble(m_topup.getText().toString()) + "";
                    }
                } else {
                    toast("最少购买一张！");
                }
                break;
            case R.id.act_buycard_sencond_numadd:
                num += 1;
                m_num.setText(num + "");
                m_paySum.setText("￥" + (num) * Integer.parseInt(adapter.getSelect()));
                sum = (num) * Integer.parseInt(adapter.getSelect()) + "";
                break;
            case R.id.act_buycard_sencond_numadd1:
                num1 += 1;
                m_num1.setText(num1 + "");
                if (!"".equals(m_topup.getText().toString())) {
                    m_paySum1.setText("￥" + (num1) * (int) Double.parseDouble(m_topup.getText().toString()));
                    sum1 = (num1) * (int) Double.parseDouble(m_topup.getText().toString()) + "";
                } else {
                    m_paySum1.setText("￥" + (num1) * (int) Double.parseDouble("0"));
                    sum1 = (num1) * (int) Double.parseDouble("0") + "";
                }
                break;
            case R.id.act_buycard_second_next:
                submit();
                break;
        }
    }
}
