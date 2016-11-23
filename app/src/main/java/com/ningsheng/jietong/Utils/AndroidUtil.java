package com.ningsheng.jietong.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * 各类杂七杂八方法
 * <p> getScreenSize 获取屏幕尺寸
 * <p> 
 * 
 * @author M.c
 * 
 */
public class AndroidUtil {

	/**
	 * 获取屏幕尺寸
	 * 
	 * @param context
	 * @param type
	 *            1为宽度 2为高度
	 * @return
	 */
	public static int getScreenSize(Activity context, int type) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (type == 1) {
			return dm.widthPixels;
		} else {
			return dm.heightPixels;
		}

	}
	
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    /*
     * 获取屏幕分辨率
     */
    public static int getDisplay(Activity context , int type){//1 宽 2高
    	DisplayMetrics metrics=new DisplayMetrics();
    	 context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    	int widthPixels=metrics.widthPixels;
    	int heightPixels=metrics.heightPixels;
    	return type==1?widthPixels:heightPixels;
    }
    /** 
     * OnCreate中获得组件大小 
     * params2: 1.宽 2.高
     */  
    public static int getViewSize(View view, int type) {
    	 int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	      int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	      view.measure(w, h); 
	      int height =view.getMeasuredHeight();
	      int width =view.getMeasuredWidth();
	      if(type==1){
	    	  return width;
	      }else{
	    	  return height;
	      }
    }  


    /**
     * 发短信
     * @param context
     * @param phoneNumber  发送号码
     * @param smsBody      发送内容
     */
    public static void sendSms(Context context, String phoneNumber, String smsBody){
        if(StringUtil.isBlank(phoneNumber)){
            Log.e("","号码为空");
            return;
        }
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    /**
     *打电话
     * @param context
     * @param phoneNumber  发送号码
     */
    public static void sendPhone(Context context, String phoneNumber){
        if (!StringUtil.isBlank(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
//            context.startActivity(intent);
            context.startActivity(intent);
        }else{
            Log.e("","号码为空");
        }
    }

    @SuppressLint("SetJavaScriptEnabled") @SuppressWarnings("deprecation")
	public static WebView setWebView(WebView webView){
        WebSettings set = webView.getSettings();
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocus();
        set.setJavaScriptCanOpenWindowsAutomatically(true);
        set.setPluginState(WebSettings.PluginState.ON);
        set.setAllowFileAccess(true);
        set.setJavaScriptEnabled(true);
        set.setUseWideViewPort(true);
        set.setLoadWithOverviewMode(true); 
        set.setSupportZoom(true);
//        set.setBuiltInZoomControls(true);
        set.setDomStorageEnabled(true);// 自动开启窗口
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });
        return webView;
    }
    
    public static void setListViewHeightBasedOnChildren(ListView listView) {

		// 获取ListView对应的Adapter

		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {

			return;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0); // 计算子项View 的宽高

			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// listView.getDividerHeight()获取子项间分隔符占用的高度

		// params.height最后得到整个ListView完整显示需要的高度

		listView.setLayoutParams(params);

	}
    
    /**
     * 隐藏软件盘
     * @param activity
     */
    public static void hideMethod(Activity activity){
    	
    	 InputMethodManager imm = (InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE);
         imm. toggleSoftInput (0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 获取屏幕分辨率
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = { width, height };
        return result;
    }


    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
