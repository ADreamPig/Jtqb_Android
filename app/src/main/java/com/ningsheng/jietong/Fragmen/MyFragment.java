package com.ningsheng.jietong.Fragmen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.Activity.BalanceActivity;
import com.ningsheng.jietong.Activity.InventedCertificateActivity;
import com.ningsheng.jietong.Activity.MoreActivity;
import com.ningsheng.jietong.Activity.MyAddressActivity;
import com.ningsheng.jietong.Activity.MyCardActivity;
import com.ningsheng.jietong.Activity.PayCardRecordActivity;
import com.ningsheng.jietong.Activity.PersonalDataActivity;
import com.ningsheng.jietong.Activity.SafeSetupActivity;
import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.PersonalInfo;
import com.ningsheng.jietong.Entity.User;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.ListViewInScrollView;
import com.ningsheng.jietong.View.WhiteEdgeCircularImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/22.
 */
public class MyFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private WhiteEdgeCircularImageView ivHead;
    private TextView tvLoginStatus;
    private TextView tvBalance;
    private SwipeRefreshLayout m_pull;
    private ListViewInScrollView listView;
    private LayoutInflater inflater;
    private String[] datas = {"我的卡", "电子券", "购卡记录", "我的收货地址", "安全设置", "更多"};
    private int[] images = {R.mipmap.wode_wodeka, R.mipmap.wode_dianziquan, R.mipmap.wode_goukajilv, R.mipmap.wode_dizhi, R.mipmap.wode_anquanshezhi, R.mipmap.wode_gengduo};
    private User personalInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragment_my, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        inflater = getActivity().getLayoutInflater();
        ivHead = (WhiteEdgeCircularImageView) v.findViewById(R.id.my_iv_head);
        ivHead.setOnClickListener(this);
        m_pull = (SwipeRefreshLayout) v.findViewById(R.id.fragment_my_pull);
        m_pull.setOnRefreshListener(this);
        tvLoginStatus = (TextView) v.findViewById(R.id.my_tv_loginStatus);
        tvBalance = (TextView) v.findViewById(R.id.my_tv_balance);
        tvLoginStatus.setOnClickListener(this);
        v.findViewById(R.id.my_linear_balance).setOnClickListener(this);
        listView = (ListViewInScrollView) v.findViewById(R.id.listView);
        listView.setAdapter(new MyFragmentAdapter());
        listView.setOnItemClickListener(this);
        if (MyApplication.user.getId() == null || MyApplication.user.getId().equals("")) {
            tvLoginStatus.setText("立即登录");
            tvLoginStatus.setOnClickListener(this);
        } else {
            tvLoginStatus.setText(MyApplication.user.getUserName());
            MediaUtil.displayImageHead(MyApplication.getInstance(), MyApplication.user.getImage(), ivHead);
            tvBalance.setText(MyApplication.user.getAccountBalence());
        }

//        tvLoginStatus.setText(MyApplication.user.getUserName());
//                tvBalance.setText(MyApplication.user.getAccountBalence());
//                MediaUtil.displayImageHead(MyApplication.getInstance(), MyApplication.user.getImage(), ivHead);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("-------MyFragment----", "onViewCreated");
//        getInfo(activity);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0://我的卡
                intent.setClass(getActivity(), MyCardActivity.class);
                break;
            case 1://电子券
                intent.setClass(getActivity(), InventedCertificateActivity.class);
                break;
            case 2://购卡记录
                intent.setClass(getActivity(), PayCardRecordActivity.class);
                break;
            case 3://我的收货地址
                intent.setClass(getActivity(), MyAddressActivity.class);
                break;
            case 4://安全设置
                intent.setClass(getActivity(), SafeSetupActivity.class);
                if (!MyApplication.user.getId().equals("")) {
                    intent.putExtra("name", MyApplication.user.getName());
                    intent.putExtra("mobile", MyApplication.user.getMobile());
                    intent.putExtra("identityCard", MyApplication.user.getIdentityCard());
                }
                break;
            case 5://更多
                intent.setClass(getActivity(), MoreActivity.class);
                break;
            default:
                return;
        }
        startActivityForResult(intent, 0x33);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.my_tv_loginStatus:
//                intent.setClass(getActivity(), LoginActivity.class);
//                break;
            case R.id.my_iv_head:
                intent.setClass(getActivity(), PersonalDataActivity.class);
                if (MyApplication.user != null) {
                    intent.putExtra("name", MyApplication.user.getUserName());
                    intent.putExtra("image", MyApplication.user.getImage());
                }
                startActivityForResult(intent, 0x21);
                break;

            case R.id.my_linear_balance:
                intent.setClass(getActivity(), BalanceActivity.class);
                intent.putExtra("balance", tvBalance.getText().toString());
                startActivity(intent);
                break;

        }
    }

    private HttpSender sender;

    public void getInfo(final BaseActivity activity) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("id", MyApplication.user.getId());
        sender = new HttpSender(Url.accountInfo, "个人资料", map, new OnHttpResultListener() {
            @Override
            public void onFinished() {
                super.onFinished();
                m_pull.setRefreshing(false);
            }

            @Override
            public void onSuccess(String message, String code, String data) {
                personalInfo = gsonUtil.getInstance().json2Bean(data, User.class);
                MyApplication.user = personalInfo;
                SharedPreferencesUtil.getInstance(activity).setBeantoSharedPreferences(SharedPreferencesUtil.USER, personalInfo);
                tvLoginStatus.setText(personalInfo.getUserName());
                tvBalance.setText(personalInfo.getAccountBalence());
                MediaUtil.displayImageHead(MyApplication.getInstance(), personalInfo.getImage(), ivHead);
                ((TextView) listView.getChildAt(0).getTag()).setText(personalInfo.getCardNum());
                ((TextView) listView.getChildAt(1).getTag()).setText(personalInfo.getElecVoucherCount());
            }
        });
        sender.setContext(activity);
        sender.send(Url.Post);

    }

    @Override
    public void onRefresh() {
        Log.d("========myFragment=========","onRefresh");
        if (m_pull != null) {
            m_pull.setRefreshing(false);
        }

        if (tvLoginStatus != null) {
            if (MyApplication.user.getId() == null || MyApplication.user.getId().equals("")) {
                tvLoginStatus.setText("立即登录");
                ivHead.setImageResource(R.mipmap.wode_photo);
                tvLoginStatus.setOnClickListener(this);
                tvBalance.setText("0.00");
                ((TextView) listView.getChildAt(0).getTag()).setText("0");
                ((TextView) listView.getChildAt(1).getTag()).setText("0");

            } else {
                tvLoginStatus.setText(MyApplication.user.getUserName());
//            Log.i("------head----",MyApplication.user.getImage());
                MediaUtil.displayImageHead(MyApplication.getInstance(), MyApplication.user.getImage(), ivHead);
//            tvBalance.setText(MyApplication.user.getAccountBalence());
                getInfo(activity);
            }
//        getInfo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sender != null) {
            sender.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("-------MyFragment----", "onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("-------MyFragment----", "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("-------MyFragment----", "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("-------MyFragment----", "omStart");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("-------MyFragment----", "onAttach");
    }

    private class MyFragmentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public String getItem(int position) {
            return datas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.layout_item_my, null);
            TextView tv = (TextView) convertView.findViewById(R.id.item_my_tv);
            TextView tv1 = (TextView) convertView.findViewById(R.id.item_my_tv1);
            ImageView iv = (ImageView) convertView.findViewById(R.id.item_my_iv);
            tv.setText(getItem(position));
            iv.setImageResource(images[position]);
            convertView.setTag(tv1);
            if (MyApplication.user != null) {
                if (position == 0) {
                    tv1.setText(MyApplication.user.getCardNum());
                } else if (position == 1) {
                    tv1.setText(MyApplication.user.getElecVoucherCount());
                }
            }
            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case 0x21:
                onRefresh();
//                String path = data.getStringExtra("path");
//                if (!TextUtils.isEmpty(path))
//                    MediaUtil.displayImageHead(MyApplication.getInstance(), "file://" + data.getStringExtra("path"), ivHead);
//                String name = data.getStringExtra("name");
//                if (!TextUtils.isEmpty(name))
//                    tvLoginStatus.setText(name);
                break;
            case 0x33:
                onRefresh();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
