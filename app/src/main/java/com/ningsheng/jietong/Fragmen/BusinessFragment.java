package com.ningsheng.jietong.Fragmen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Activity.BusinessDetailAct;
import com.ningsheng.jietong.Activity.SearchActivity;
import com.ningsheng.jietong.Activity.SelectCityActivity;
import com.ningsheng.jietong.Adapter.BusinessAdapter;
import com.ningsheng.jietong.Adapter.MerchantAdapter;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.NormalPopuWindows;
import com.ningsheng.jietong.Entity.Industry;
import com.ningsheng.jietong.Entity.Merchant;
import com.ningsheng.jietong.Entity.MerchantDatas;
import com.ningsheng.jietong.Entity.PagingEntity;
import com.ningsheng.jietong.Entity.ShopArea;
import com.ningsheng.jietong.Entity.ShopAreaAll;
import com.ningsheng.jietong.Pull.MaterialRefreshLayout;
import com.ningsheng.jietong.Pull.MaterialRefreshListener;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/18.
 */
@ContentView(R.layout.fragment_business)
public class BusinessFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.fragment_business_current)
    private TextView m_current;
    @ViewInject(R.id.fragment_business_all)
    private TextView m_business;
    @ViewInject(R.id.fragment_business_city)
    private TextView m_city;
    @ViewInject(R.id.fragment_business_order)
    private TextView m_order;
    @ViewInject(R.id.fragment_business_line)
    private View m_line;
    @ViewInject(R.id.fragment_business_list)
    private ListView m_list;
    @ViewInject(R.id.fragmen_business_bg)
    private LinearLayout ll_bg;
    @ViewInject(R.id.fragment_business_search)
    private View m_search;
    @ViewInject(R.id.fragment_business_list_pull)
    private MaterialRefreshLayout m_pull;

    private NormalPopuWindows popu1, popu2, popu3;
    private BusinessAdapter adapter;
    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private List<String> list4 = new ArrayList<>();
    private List<Merchant> list3 = new ArrayList<>();
    private List<String> key_sortType = new ArrayList<>();
    private LocationClient mLocClient;

    private String city;
    private int limitStart = 0, limitEnd = 15;
    private Map<String, String> map;
    private List<Industry> industriesList;
    private List<ShopArea> areaList;
    private String industryId;//行业
    private String area;//商圈
    private String sortField;//排序字段 discount折扣度 distance 距离

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_bg.setBackgroundColor(getResources().getColor(R.color.app_color));
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) ll_bg.getLayoutParams();


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            params.topMargin=MyApplication.getStateBarHeight();
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            params.topMargin=MyApplication.getStateBarHeight();
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            params.topMargin=0;
        }
//        ll_bg.setLayoutParams(params);
        init();
    }

    private void init() {
        mLocClient = new LocationClient(activity);
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // map view 销毁后不在处理新接收的位置
                MyApplication.lat = bdLocation.getLatitude() == 0 ? 117.2365960000 : bdLocation.getLatitude();
                MyApplication.lng = bdLocation.getLongitude() == 0 ? 31.8262060000 : bdLocation.getLongitude();
                MyApplication.city = bdLocation.getCity() == null ? "上海市" : bdLocation.getCity();
                m_current.setText(bdLocation.getCity());

                map.put("lat", MyApplication.lat + "");//
                map.put("lng", MyApplication.lng + "");//
                city = MyApplication.city;

                getType();
                initDate();
                mLocClient.stop();

            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();

        map = new HashMap<String, String>();

        adapter = new BusinessAdapter(activity, list3);
        m_list.setAdapter(adapter);

        m_business.setOnClickListener(this);
        m_city.setOnClickListener(this);
        m_order.setOnClickListener(this);
        m_search.setOnClickListener(this);
        m_current.setOnClickListener(this);
        m_pull.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                limitStart = 0;
                limitEnd=15;
                list3.clear();
                initDate();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                initDate();
            }
        });
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, BusinessDetailAct.class);
                intent.putExtra("id", list3.get(position).getId());
                startActivity(intent);
            }
        });
//        initDate();
//        getType();
    }

    private String istop="2";
    private void initDate() {
        map.put("limitStart", limitStart + "");//
        map.put("limitEnd", limitEnd + "");//
        map.put("city", city);//
        map.put("area", area);//商圈 非必须
        map.put("industryId", industryId);//行业  非必须
        map.put("sortType", sortField);//排序类型
        map.put("isTop",istop);
        HttpSender sender = new HttpSender(Url.queryMerchantByLngLat, "商户删选", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                limitStart+=15;
                limitEnd+=15;
                PagingEntity get = gsonUtil.getInstance().json2Bean(data, PagingEntity.class);
                List<Merchant> Mlist = get.getData();
                list3.addAll(Mlist);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                m_pull.finishRefresh();
                m_pull.finishRefreshLoadMore();
            }
        });
        sender.setContext(limitStart == 0 ? activity : null);
        sender.send(Url.Post);
    }


    public void getType() {

        key_sortType.clear();
        list1.clear();
        list2.clear();
        list4.clear();

        Map<String, String> map = new HashMap<>();
        map.put("city", city);
        HttpSender sender = new HttpSender(Url.initMerchantDatas, "获取分类", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    MerchantDatas get = gsonUtil.getInstance().json2Bean(data, MerchantDatas.class);
                    Log.i("----", get.toString());
                    areaList = get.getShopArea().getAreas() == null ? new ArrayList<ShopArea>() : get.getShopArea().getAreas();
                    industriesList = get.getIndustry().getIndustry() == null ? new ArrayList<Industry>() : get.getIndustry().getIndustry();
                    Map<String, String> sortType = get.getSortType();

                    areaList.add(0, new ShopArea("all", get.getShopArea().getAll()));
                    industriesList.add(0, new Industry("all", get.getIndustry().getAll()));

                    for (String key : sortType.keySet()) {
                        key_sortType.add(key);
                        list4.add(sortType.get(key));
                    }
                    for (Industry in : industriesList) {
                        list1.add(in.getName());
                    }
                    for (ShopArea sa : areaList) {
                        list2.add(sa.getAreaName());
                    }
                    if(popu1==null){
                    popu1 = new NormalPopuWindows(activity, list1, m_line, m_list);
                    popu2 = new NormalPopuWindows(activity, list2, m_line, m_list);
                    popu3 = new NormalPopuWindows(activity, list4, m_line, m_list);
                    }else{
                    popu1.notifyDataSetChanged(list1);
                        popu2.notifyDataSetChanged(list2);
                        popu3.notifyDataSetChanged(list4);
                    }
Log.d("------第二次-------",list2.toString());
                    popu3.setOnBack(new NormalPopuWindows.NormalPopuBack() {
                        @Override
                        public void popuBack(int position) {
                            m_order.setText(list4.get(position));
                            sortField = key_sortType.get(position);
                            limitStart = 0;
                            list3.clear();
                            initDate();
                            popu3.dismiss();
                        }
                    });
                    popu3.setShowDisListener(new NormalPopuWindows.OnPopuDisShowListener() {
                        @Override
                        public void onShow() {
                            m_order.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_xiangshang), null);
                        }

                        @Override
                        public void onDismiss() {
                            m_order.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_xiangxia), null);
                        }
                    });
                    popu1.setOnBack(new NormalPopuWindows.NormalPopuBack() {
                        @Override
                        public void popuBack(int position) {
                            m_business.setText(list1.get(position));
                            industryId = industriesList.get(position).getId();
                            limitStart = 0;
                            list3.clear();
                            initDate();
                            popu1.dismiss();
                        }
                    });
                    popu1.setShowDisListener(new NormalPopuWindows.OnPopuDisShowListener() {
                        @Override
                        public void onShow() {
                            m_business.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_xiangshang), null);
                        }

                        @Override
                        public void onDismiss() {
                            m_business.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_xiangxia), null);
                        }
                    });
                    popu2.setOnBack(new NormalPopuWindows.NormalPopuBack() {
                        @Override
                        public void popuBack(int position) {
                            m_city.setText(list2.get(position));
                            area = areaList.get(position).getId();
                            limitStart = 0;
                            list3.clear();
                            initDate();
                            popu2.dismiss();
                        }
                    });
                    popu2.setShowDisListener(new NormalPopuWindows.OnPopuDisShowListener() {
                        @Override
                        public void onShow() {
                            m_city.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_xiangshang), null);
                        }

                        @Override
                        public void onDismiss() {
                            m_city.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_xiangxia), null);
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);

            }
        });
        sender.setContext(activity);
        sender.send(Url.Post);
    }

    private boolean ispic;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_business_all:
                if (popu1 != null) {
                    popu1.show();
//                    if(ispic){
//                        m_business.setCompoundDrawablesWithIntrinsicBounds (null, null, getResources().getDrawable(R.mipmap.icon_xiangshang), null);
//                    }else{
//                        m_business.setCompoundDrawablesWithIntrinsicBounds (null, null, getResources().getDrawable(R.mipmap.icon_xiangshang), null);
//                    }
                }

//                button1.setCompoundDrawablesWithIntrinsicBounds (null, null, getResources().getDrawable(R.drawable.icon_downon), null);
                break;
            case R.id.fragment_business_city:
                if (popu2 != null) {
                    popu2.show();
                }
                break;
            case R.id.fragment_business_order:
                if (popu3 != null) {
                    popu3.show();
                }
                break;
            case R.id.fragment_business_search:
                Intent intent = new Intent(activity, SearchActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
                break;
            case R.id.fragment_business_current:
                Intent intent1 = new Intent(activity, SelectCityActivity.class);
                startActivityForResult(intent1, 44);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 44 && resultCode == 33 && data != null) {
            city = data.getStringExtra("name");
            istop="2";
            m_current.setText(city);
            limitStart = 0;
            list3.clear();
            if (areaList != null) {
                areaList.clear();
            }
            if (industriesList != null) {
                industriesList.clear();
            }
            if (areaList != null) {
                areaList.clear();
            }
            getType();
            initDate();
        }
    }

    //    @Event(type = View.OnClickListener.class)
//    @Event(parentId = R.id.fragment_business_all ,
//            type = View.OnClickListener.class)
//    private void onBusiness(View view){}


}
