package com.ningsheng.jietong.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.MyCardAdapter;
import com.ningsheng.jietong.App.BaseMeActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.MyCardInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.Typefaces;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.Titanic;
import com.ningsheng.jietong.View.TitanicTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/22.
 * 我的卡|更多
 */
public class MyCardActivity extends BaseMeActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private View linearTitle;
    private ListView listView;
    private MyCardAdapter adapter;
    private List<MyCardInfo> datas;
    private List<MyCardInfo> datas_;
    private ValueAnimator valueAnimatorOpen;
    private ValueAnimator valueAnimatorClose;
    private boolean isOpenMore;
    private FrameLayout m_fl;

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.mycard_tv_more).setOnClickListener(this);
        listView.setOnItemClickListener(this);
        findViewById(R.id.mycard_main).setVisibility(View.GONE);
        findViewById(R.id.nodata).setVisibility(View.VISIBLE);
        m_fl=(FrameLayout) findViewById(R.id.act_mycard__fl);
    }

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("id", MyApplication.user.getId());
        HttpSender sender = new HttpSender(Url.myCardList, "我的卡", map, new OnHttpResultListener() {
            Titanic titanic;
            TitanicTextView tv;
            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                List<MyCardInfo> myCardInfos = (List<MyCardInfo>) gsonUtil.getInstance().json2List(data, new TypeToken<ArrayList<MyCardInfo>>() {
                }.getType());
                if (myCardInfos != null && !myCardInfos.isEmpty()) {
                    findViewById(R.id.mycard_main).setVisibility(View.VISIBLE);
                    findViewById(R.id.nodata).setVisibility(View.GONE);
                    datas_.clear();
                    datas.clear();
                    datas_.addAll(myCardInfos);
                    datas.add(datas_.get(0));
                    if (datas_.size() > 1)
                        datas.add(datas_.get(1));
                    adapter.notifyDataSetChanged();
                    if (valueAnimatorOpen == null)
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                valueAnimatorOpen = ValueAnimator.ofFloat(listView.getHeight(), (AndroidUtil.getScreenSize(MyCardActivity.this, 2)));
                                valueAnimatorOpen.setDuration(1000);
                                valueAnimatorOpen.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        listView.getLayoutParams().height = (int) ((float) animation.getAnimatedValue());
                                        listView.requestLayout();
                                    }
                                });
                                valueAnimatorOpen.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        listView.getLayoutParams().height = AbsListView.LayoutParams.MATCH_PARENT;
                                        listView.requestLayout();
                                    }
                                });
                                valueAnimatorClose = ValueAnimator.ofFloat(AndroidUtil.getScreenSize(MyCardActivity.this, 2), listView.getHeight());
                                valueAnimatorClose.setDuration(400);
                                valueAnimatorClose.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        listView.getLayoutParams().height = (int) ((float) animation.getAnimatedValue());
                                        listView.requestLayout();
                                    }
                                });
                            }
                        });
                    if("wall".equals(type)){
                        MoreClick();
                    }
                }
                }else{
                    toast(message);
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                super.onLoading(total, current, isDownloading);
                findViewById(R.id.mycard_main).setVisibility(View.GONE);
                findViewById(R.id.nodata).setVisibility(View.GONE);
                 tv=new TitanicTextView(MyCardActivity.this);
                tv.setText("Loading");
                tv.setTypeface(Typefaces.get(MyCardActivity.this, "Satisfy-Regular.ttf"));
                 titanic = new Titanic();
                titanic.start(tv);
                m_fl.addView(tv);
            }

            @Override
            public void onFinished() {
                super.onFinished();
                titanic.cancel();
                m_fl.removeView(tv);
            }
        });
//        sender.setContext(this);
        sender.send(Url.Post);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        type=intent.getStringExtra("type");
        initDate();

    }

    private String type;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_mycard);
        setTitle("我的卡");
        showBackwardView("返回", true);
         type=getIntent().getStringExtra("type");

        findViewById(R.id.mycard_tv_add).setOnClickListener(this);
        linearTitle = findViewById(R.id.mycard_linear_title);
        TextView content = (TextView) findViewById(R.id.nodata_content);
        content.setText("你还没有绑定任何卡，绑定后可在这里查看。");
        TextView next = (TextView) findViewById(R.id.nodata_next);
        next.setText("绑定新卡");
        listView = (ListView) findViewById(R.id.listView);
        datas = new ArrayList<>();
        datas_ = new ArrayList<>();
        adapter = new MyCardAdapter(this, datas);
        listView.setAdapter(adapter);
    }

    private void MoreClick(){
        isOpenMore = true;
        linearTitle.setVisibility(View.GONE);
//        valueAnimatorOpen.start();
        datas.clear();
        datas.addAll(datas_);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mycard_tv_more:
                MoreClick();
                break;
            case R.id.mycard_tv_add:
                Intent intent=new Intent(this,BindCardActivity.class);
                startActivityForResult(intent,0x41);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x41){
            datas.clear();
            initDate();
        }
    }

    public void nodataClick(View v) {
        Intent intent=new Intent(this,BindCardActivity.class);
        startActivityForResult(intent,0x41);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra("cardInfo",datas_.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MyCardActivity.this, view, view.getTransitionName());
            ActivityCompat.startActivityForResult(MyCardActivity.this, intent, 0x101, optionsCompat.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() >> 1, view.getHeight() >> 1, 0, 0);
            ActivityCompat.startActivityForResult(MyCardActivity.this, intent, 0x101, optionsCompat.toBundle());
        }
    }

    @Override
    public void onBackPressed() {
        if (isOpenMore) {
            isOpenMore = false;
            linearTitle.setVisibility(View.VISIBLE);
            valueAnimatorClose.start();
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    datas.clear();
                    datas.add(datas_.get(0));
                    if (datas_.size() > 1)
                        datas.add(datas_.get(1));
                    adapter.notifyDataSetChanged();
                }
            }, 400);

        } else {
            super.onBackPressed();
        }
    }


}
