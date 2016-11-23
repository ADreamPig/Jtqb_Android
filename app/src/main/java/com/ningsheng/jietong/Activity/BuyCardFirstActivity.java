package com.ningsheng.jietong.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.RealNameDialog;
import com.ningsheng.jietong.Entity.AddressEntity;
import com.ningsheng.jietong.Entity.AddressInfo;
import com.ningsheng.jietong.Entity.MyAddressInfo;
import com.ningsheng.jietong.Entity.SubmitOrder;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.SwipeBack.SwipeBackLayout;
import com.ningsheng.jietong.Utils.ActUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.StringUtil;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhushunqing on 2016/2/23.
 */
public class BuyCardFirstActivity extends TitleActivity implements View.OnClickListener{
    private int limitStart=0;
    private int limitEnd=10;
    private View m_checkin,m_nocheck;
    private TextView m_name,m_address,m_change;
    private EditText m_addName,m_addMobile,m_addDetail;
    private boolean falg;//是否有默认地址AlertDialog
    private SubmitOrder submitOrder;
    private String id;//收货地址id
    private  TextView m_PCA;
    private static final int VIBRATE_DURATION = 20;
    private String province,city,area;
    int edgeFlag = SwipeBackLayout.EDGE_LEFT;
    private SwipeBackLayout mSwipeBackLayout;
    private RealNameDialog dialog;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_buycard_first);
        setTitle("购卡申请");
        showBackwardView("",true);

        m_checkin=findViewById(R.id.act_buycard_first_checkin);
        m_nocheck=findViewById(R.id.act_buycard_first_nocheck);
         m_name=(TextView) findViewById(R.id.act_buycard_first_name);
         m_address=(TextView)findViewById(R.id.act_buycard_first_address);
         m_change=(TextView)findViewById(R.id.act_buycard_first_change);
        m_addName=(EditText)findViewById(R.id.act_buycard_first_addname);
        m_addMobile=(EditText)findViewById(R.id.act_buycard_first_addmobile);
        m_addDetail=(EditText)findViewById(R.id.act_buycard_first_adddetail);
         m_PCA=(TextView) findViewById(R.id.act_buycard_first_provCityAddr);
        m_checkin.setVisibility(View.GONE);
        m_nocheck.setVisibility(View.VISIBLE);

//        if(MyApplication.user.getIdentityCard()==null||"".equals(MyApplication.user.getIdentityCard())){



//        mSwipeBackLayout.setEdgeTrackingEnabled(edgeFlag);
//        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
//            @Override
//            public void onScrollStateChange(int state, float scrollPercent) {
//
//            }
//
//            @Override
//            public void onEdgeTouch(int edgeFlag) {
//                vibrate(VIBRATE_DURATION);
//            }
//
//            @Override
//            public void onScrollOverThreshold() {
//                vibrate(VIBRATE_DURATION);
//            }
//        });
//
//
//    }
//
//    private void vibrate(long duration) {
//        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {
//                0, duration
//        };
//        vibrator.vibrate(pattern, -1);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_buycard_first_next).setOnClickListener(this);
        findViewById(R.id.act_buycard_first_city).setOnClickListener(this);
        m_change.setOnClickListener(this);
    }

    @Override
    protected void initDate() {
        if("".equals(MyApplication.user.getId())||MyApplication.user==null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 11);
            return;
        } else   if (TextUtils.isEmpty(MyApplication.user.getName()) || TextUtils.isEmpty(MyApplication.user.getIdentityCard())) {
            dialog = new RealNameDialog(this);
            dialog.setCancelable(false);
            dialog.show();
            return;
        }
        Map<String,String> map=new HashMap<String,String>();
        map.put("accountId", MyApplication.user.getId());//9a6178c81c9d42cb8e3927826faf48b0
        map.put("limitStart",limitStart+"");
        map.put("limitEnd",limitEnd+"");
        HttpSender sender=new HttpSender(Url.shippingaddressListInfo, "获取收货地址", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                    limitStart += 1;
                if(data==null){
                    return;
                }
//                    AddressInfo addressinfo = gsonUtil.getInstance().json2Bean(data, AddressInfo.class);
//                    List<AddressEntity> MList = addressinfo.getData();
                List<AddressEntity> MList=( List<AddressEntity>)gsonUtil.getInstance().json2List(data,new TypeToken<List<AddressEntity>>(){}.getType());
                    int i = 0;
//                    for (i = 0; i < MList.size(); i++) {
//                        if (MList.get(i).getCommonaddr().equals("1")) {
                if(MList!=null&&MList.size()!=0){
                            falg = true;
                            m_checkin.setVisibility(View.VISIBLE);
                            m_nocheck.setVisibility(View.GONE);
                            m_name.setText(MList.get(0).getConsignee() + "  " + MList.get(0).getMobile());
                            m_address.setText(MList.get(0).getProv()+MList.get(0).getCity()+MList.get(0).getDistrict()+MList.get(0).getAddress());
                            id = MList.get(0).getId();
//                            break;

//                        }
                    }
//                if(!falg){
//                    m_checkin.setVisibility(View.GONE);
//                    m_nocheck.setVisibility(View.VISIBLE);
//
//                }

            }
        });
        sender.setContext(this);
        sender.send(Url.Post);



    }

    private void SubmitAddress(){
        if(province==null){
            toast("请选择省市");
            return;
        }
        if(m_addName.getText().toString().equals("")){
            toast("请填写收货人名字");
            return;
        }
        Log.i("=======",""+m_addMobile.getText().toString());
        if(!StringUtil.isMobileNo(m_addMobile.getText().toString())){
            toast("请填写正确电话号码");
            return;
        }
        if(m_addDetail.getText().toString().equals("")){
            toast("请填写详细地址");
            return;
        }
        Map<String,String> map=new HashMap<String,String>();
        map.put("accountId",MyApplication.user.getId());
        map.put("provCityAddr",province+","+city+","+area);
        map.put("consignee",m_addName.getText().toString());
        map.put("mobile",m_addMobile.getText().toString());
        map.put("address",m_addDetail.getText().toString());
        map.put("address",m_addDetail.getText().toString());
        map.put("commonaddr","0");
        HttpSender sender=new HttpSender(Url.addShoppingAddress, "增加收货地址", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if ("新增收货地址成功".equals(message)) {
                    Intent intent = new Intent(BuyCardFirstActivity.this, BuyCardSecondActivity.class);
                    intent.putExtra("id", data);
                    startActivityForResult(intent, 88);
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        province= intent.getStringExtra("province");
         city=intent.getStringExtra("city");
         area=intent.getStringExtra("area");
        if(province!=null&&city!=null&&area!=null){
            m_PCA.setText(province+city+area);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x11&&!"".equals(MyApplication.user.getIdentityCard())){
            dialog.dismiss();
            initDate();
        }else if(requestCode==4&&resultCode==0x33){
            MyAddressInfo orderInfo=(MyAddressInfo)data.getSerializableExtra("address");
            m_name.setText(orderInfo.getConsignee() + "  " + orderInfo.getMobile());
            m_address.setText(orderInfo.getProv()+orderInfo.getCity()+orderInfo.getDistrict()+orderInfo.getAddress());
            id = orderInfo.getId();
        }else if(requestCode==88&&resultCode==99){
            initDate();
        }else if(requestCode==0x11&&"".equals(MyApplication.user.getIdentityCard())){
            if (TextUtils.isEmpty(MyApplication.user.getName()) || TextUtils.isEmpty(MyApplication.user.getIdentityCard())) {
                dialog = new RealNameDialog(this);
                dialog.setCancelable(false);
                dialog.show();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_buycard_first_next:
                if(falg){
                    Intent intent=new Intent(BuyCardFirstActivity.this,BuyCardSecondActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else {
                    SubmitAddress();
                }
                break;
            case R.id.act_buycard_first_city:
                 Intent intent=new Intent(this,ProvinceActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.act_buycard_first_change:
                intent=new Intent(this,ChangeAddressAct.class);
                startActivityForResult(intent,4);
                break;
        }
//        if(intent!=null){
//            startActivity(intent);
//        }
    }
}
