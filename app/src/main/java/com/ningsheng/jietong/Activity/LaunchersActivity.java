package com.ningsheng.jietong.Activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ningsheng.jietong.Adapter.ViewPagerAdapter;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;

import cn.jpush.android.api.JPushInterface;


public class LaunchersActivity extends Activity {

	private ViewPager viewpager;
	private List<View> list=new ArrayList<View>();
	private List<Integer> images=new ArrayList<Integer>();
	private List<ImageView> views;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadContentView();
		initView();
		setListener();
		initDate();


	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	protected void loadContentView() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_launcher);
	}

	protected void initView() {
		String count= SharedPreferencesUtil.getInstance(this).getSharedPreferences().getString(SharedPreferencesUtil.ISFIRST,"");
		if(!"".equals(count)){
			Intent intent=new Intent(this,WelcomeActivity.class);
			startActivity(intent);
			finish();
			return;
//			SaveData.Onther.saveIsFrist();
		}
		SharedPreferencesUtil.getInstance(this).cleanBeantoSharedPreferences(SharedPreferencesUtil.ISFIRST, "true");
		viewpager=(ViewPager)findViewById(R.id.launcher_activity_viewpager);
		images.add(R.mipmap.yd1);
		images.add(R.mipmap.yd2);
		images.add(R.mipmap.yd3);
		images.add(R.mipmap.yd4);

		for(int i=0;i<4;i++){
			View view=getLayoutInflater().inflate(R.layout.launcher_image, null, false);
			ImageView iv1=(ImageView)view.findViewById(R.id.launcher_image_iv);
//			ImageView iv=new ImageView(this);
//			ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			iv.setLayoutParams(params);
//			iv.setPadding(0, 0, 0, 0);
//			iv.setScaleType(ScaleType.FIT_XY);
			iv1.setImageResource(images.get(i));
			if(i==3){
				View view1=view.findViewById(R.id.launcher_image_tv);
				view1.setVisibility(View.VISIBLE);
				view1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(LaunchersActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
			list.add(view);
		}
		views = new ArrayList<ImageView>();
		LinearLayout ll=(LinearLayout)findViewById(R.id.activity_launcher_ll);
		for(int i=0;i<ll.getChildCount();i++){
			views.add((ImageView)ll.getChildAt(i));
		}
		ViewPagerAdapter adapter=new ViewPagerAdapter(list);
		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(0);
//		setCurrentPoint(0);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int paramInt) {
				// TODO Auto-generated method stub
				System.out.println(paramInt);
				setCurrentPoint(paramInt);
			}

			@Override
			public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int paramInt) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void initDate() {

	}

	protected void setListener() {

	}

	private void setCurrentPoint(int position){
		for(int i=0;i<views.size();i++){
			if(position==i){
				views.get(i).setImageResource(R.mipmap.redpoint);
			}else{
				views.get(i).setImageResource(R.mipmap.huipoint);
			}
		}


	}
}
