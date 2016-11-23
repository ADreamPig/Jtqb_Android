package com.ningsheng.jietong.Utils;

import java.util.List;
import java.util.Stack;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;

import com.ningsheng.jietong.App.BaseActivity;


public class ActUtil {
	private static Stack<BaseActivity> mActivityStack;
	private static ActUtil mActUtil;

	private ActUtil() {
	}

	/**
	 * 单一实例
	 */
	public static ActUtil getInstance() {
		if (mActUtil == null) {
			mActUtil = new ActUtil();
		}
		return mActUtil;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(BaseActivity activity) {
		if (mActivityStack == null) {
			mActivityStack = new Stack<BaseActivity>();
		}
		mActivityStack.add(activity);
	}

	/**
	 * 获取栈顶Activity（堆栈中最后一个压入的）
	 */
	public BaseActivity getTopActivity() {
		if(mActivityStack!=null){
		BaseActivity activity = mActivityStack.lastElement();
		return activity;
		}else{
			return null;
		}
	}

	/**
	 * 结束栈顶Activity（堆栈中最后一个压入的）
	 */
	public void killTopActivity() {
		BaseActivity activity = mActivityStack.lastElement();
		killActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void killActivity(BaseActivity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void killActivity(Class<?> cls) {
		for (BaseActivity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				killActivity(activity);
			}
		}
	}
	
	 public String getTopActivity(Activity context)
	 {
	      ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE) ;
	      List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
	          
	      if(runningTaskInfos != null)
	        return (runningTaskInfos.get(0).topActivity).toString() ;
	           else
	        return null ;
	 }
	

	/**
	 * 结束所有Activity
	 */
	public void killAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			killAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}

	// public void exitBytokentoLogin()
	// {
	// BaseActivity activity = getTopActivity();
	// activity.getSharedPreferences().edit().clear().commit();
	// Intent intent=new Intent(activity,LoginActivity.class);
	// activity.startActivity(intent);
	// for (int i = 0; i < mActivityStack.size()-1; i++) {
	// mActivityStack.get(i).finish();
	// }
	// }
	public void exitBytokentoClass(Class<BaseActivity> c) {
		Activity activity = getTopActivity();
		Intent intent = new Intent(activity, c);
		activity.startActivity(intent);
		for (int i = 0; i < mActivityStack.size() - 1; i++) {
			mActivityStack.get(i).finish();
		}
	}

}
