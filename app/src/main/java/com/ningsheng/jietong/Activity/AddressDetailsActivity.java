package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.MyAddressInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/24.
 */
public class AddressDetailsActivity extends TitleActivity {
    public static final int TYPE_ADD = 0;//新增
    public static final int TYPE_DETAIL = 1;//详情
    private EditText etName;
    private EditText etPhone;
    private TextView tvAddress;
    private EditText etAddress;
    private CheckBox cbDefault;
    private MyAddressInfo addressEntity;
    private int position;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_address_details);
        setTitle("地址详情");
        etName = (EditText) findViewById(R.id.addressDetail_et_name);
        etPhone = (EditText) findViewById(R.id.addressDetail_et_phone);
        tvAddress = (TextView) findViewById(R.id.addressDetail_tv_address);
        etAddress = (EditText) findViewById(R.id.addressDetail_et_address);
        cbDefault = (CheckBox) findViewById(R.id.addressDetail_cb_default);


        switch (getIntent().getIntExtra("type", TYPE_ADD)) {
            case TYPE_ADD:
                break;
            case TYPE_DETAIL:
                showForwardView("删除", true);
                position = getIntent().getIntExtra("position", 0);
                addressEntity = (MyAddressInfo) getIntent().getSerializableExtra("address");
                if (addressEntity != null) {
//                    if ("1".equals(addressEntity.getCommonaddr())) {
//                        cbDefault.setVisibility(View.GONE);
//                    }
                    etName.setText(addressEntity.getConsignee());
                    etPhone.setText(addressEntity.getMobile());
                    tvAddress.setText(addressEntity.getProv()+","+addressEntity.getCity()+","+addressEntity.getDistrict());
                    etAddress.setText(addressEntity.getAddress());
                    cbDefault.setChecked(addressEntity.getCommonaddr().equals("1"));
                }


                break;
        }
    }

    private String ischeck;
    @Override
    protected void initListener() {
        cbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ischeck=isChecked?"1":"0";
            }
        });
    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void onForward() {
        deleteAddress();

    }


    public void addressDetailOnClick1(View view) {
        Intent intent = new Intent(this, ProvinceActivity.class);
        intent.putExtra("source",1);
        startActivity(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tvAddress.setText( intent.getStringExtra("province")+","+ intent.getStringExtra("city")+","+ intent.getStringExtra("area"));
    }

    public void addressDetailOnClick2(View view) {
        switch (getIntent().getIntExtra("type", TYPE_ADD)) {
            case TYPE_ADD:
                addAddress();
                break;
            case TYPE_DETAIL:
                updateAddress();
                break;
        }

    }

    private void addAddress() {
        if(tvAddress.getText().toString().equals("")){
            toast("请选择省市区");
            tvAddress.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        if(etAddress.getText().toString().equals("")){
            toast("请填写详细地址");
            etAddress.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        if(etName.getText().toString().equals("")){
            toast("请填写收货人");
            tvAddress.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        if( !StringUtil.isMobileNo(etPhone.getText().toString())){
            toast("请填写正确电话号码");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("provCityAddr", tvAddress.getText().toString());
        map.put("consignee", etName.getText().toString());
        map.put("mobile", etPhone.getText().toString());
        map.put("address", etAddress.getText().toString());
        map.put("commonaddr",ischeck);
        HttpSender sender = new HttpSender(Url.addShoppingAddress, "增加收货地址", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if ("新增收货地址成功".equals(message)) {
                    toast("添加地址成功");
//                    MyAddressInfo info = new MyAddressInfo();
//                    info.setProvCityAddr(tvAddress.getText().toString());
//                    info.setConsignee(etName.getText().toString());
//                    info.setMobile(etPhone.getText().toString());
//                    info.setAddress(etAddress.getText().toString());
//                    Intent intent=new Intent();
//                    setResult(RESULT_OK, intent);
//                    finish();
                    Intent intent=new Intent(AddressDetailsActivity.this,MyAddressActivity.class);
                    startActivity(intent);
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    private void updateAddress() {
        if(tvAddress.getText().toString().equals("")){
            toast("请选择省市区");
            return;
        }
        if(etAddress.getText().toString().equals("")){
            toast("请填写详细地址");
            return;
        }
        if(etName.getText().toString().equals("")){
            toast("请填写收货人");
            return;
        }
        if(! StringUtil.isMobileNo(etPhone.getText().toString())){
            toast("请填写正确电话号码");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", addressEntity.getId());
        map.put("consignee", etName.getText().toString());
        map.put("provCityAddr", tvAddress.getText().toString());
        map.put("mobile", etPhone.getText().toString());
        map.put("address", etAddress.getText().toString());
        map.put("commonaddr", cbDefault.isChecked() ? "1" : "0");
        HttpSender sender = new HttpSender(Url.updateShoppingAddress, "修改收货地址", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if ("修改收货地址成功".equals(message)) {
                    toast("修改地址成功");
//                    MyAddressInfo info = new MyAddressInfo();
//                    info.setProvCityAddr(tvAddress.getText().toString());
//                    info.setConsignee(etName.getText().toString());
//                    info.setMobile(etPhone.getText().toString());
//                    info.setAddress(etAddress.getText().toString());
//                    info.setAccountId(addressEntity.getAccountId());
//                    Intent intent=getIntent();
//                    intent.putExtra("address", info);
//                    intent.putExtra("position", position);
//                    setResult(RESULT_OK, intent);
//                    finish();
                    Intent intent=new Intent(AddressDetailsActivity.this,MyAddressActivity.class);
                    startActivity(intent);
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    private void deleteAddress() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", addressEntity.getId());
        HttpSender sender = new HttpSender(Url.deleteShoppingAddress, "删除收货地址", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if ("操作成功".equals(message)) {
                    toast("删除地址成功");
                    Intent intent=new Intent(AddressDetailsActivity.this,MyAddressActivity.class);
                    startActivity(intent);
//                    Intent intent=new Intent();
//                    intent.putExtra("position", position);
//                    setResult(RESULT_OK,intent );
//                    finish();
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }
}
