package com.ningsheng.jietong.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
	public static final String USER="user";
	public static final String RRID_REGISTER="rrid_register";
	public static final String ISFIRST="isfirst";
	public static final String SEARCH="search";
	public static final String RRID_FORGETPWD="rrid_forget";
	public static final String GESTURE="gesture";
	public static final String PROVINCE="PROVINCE";


	private static SharedPreferencesUtil sharedPreferencesUtil;

	public static SharedPreferencesUtil getInstance(Context context) {
		if (sharedPreferencesUtil == null) {
			sharedPreferencesUtil = new SharedPreferencesUtil(context);
		}
		return sharedPreferencesUtil;
	}
//
	private Context context;

	private SharedPreferencesUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences("info", Context.MODE_PRIVATE);
	}

	public void cleanBeantoSharedPreferences(String key,String str) {
		SharedPreferences sp = getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, str);
		editor.commit();
	}

	public String getString(String key,String default_value){
		return getSharedPreferences().getString(key,default_value);

	}
	public void cleanBeantoSharedPreferences(String key,int str) {
		SharedPreferences sp = getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, str);
		editor.commit();
	}

	//存对象
	public void setBeantoSharedPreferences(String key, Object obj) {
		SharedPreferences sp = getSharedPreferences();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, gsonUtil.getInstance().toJson(obj));
		editor.commit();
	}

	//去对象
	public <T> T getBeanfromSharedPreferences(String key, Class<T> clazz) {
		return gsonUtil.getInstance().json2Bean(getSharedPreferences().getString(key, ""), clazz);
	}
}
