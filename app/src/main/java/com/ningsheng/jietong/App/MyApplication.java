package com.ningsheng.jietong.App;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.ningsheng.jietong.Activity.GesturePassWordActivity;
import com.ningsheng.jietong.Activity.LoginActivity;
import com.ningsheng.jietong.Activity.LoginOutActivit;
import com.ningsheng.jietong.Entity.User;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.ActUtil;
import com.ningsheng.jietong.Utils.ImageLoaderUtil;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Set;
import java.util.UUID;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by zhushunqing on 2016/1/28.
 */
public class MyApplication extends Application {
    private static MyApplication application;
    private static int screenWidth = -1, screenHeight = -1;
    private static int statusBarHeight = -1;
    public static double lat;
    public static double lng;
    public static String city = "合肥市";//当前城市
    public static boolean isActive = true;

    public static User user = new User();
    private LocationClient mLocClient;

    public MyApplication() {
    }

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        x.Ext.init(this);
//        ImageLoaderUtil.getInstance().setCacheDirName("/" + getPackageName());
//        ImageLoaderUtil.getInstance().initImageLoader(this);
//        ImageLoaderUtil.getInstance().setCacheOnDisk(true);
        application = this;
        //激光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        MobclickAgent.setSessionContinueMillis(12*60*60*1000);
        registerMessageReceiver();

        if (SharedPreferencesUtil.getInstance(MyApplication.getInstance()).getBeanfromSharedPreferences(SharedPreferencesUtil.USER, User.class) == null) {
            user = new User();
        } else {
            user = SharedPreferencesUtil.getInstance(MyApplication.getInstance()).getBeanfromSharedPreferences(SharedPreferencesUtil.USER, User.class);
        }

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                // map view 销毁后不在处理新接收的位置
                MyApplication.lat = bdLocation.getLatitude() == 0 ? 117.2365960000 : bdLocation.getLatitude();
                MyApplication.lng = bdLocation.getLongitude() == 0 ? 31.8262060000 : bdLocation.getLongitude();
                MyApplication.city = bdLocation.getCity() == null ? "上海市" : bdLocation.getCity();

//                map.put("lat", MyApplication.lat + "");//
//                map.put("lng", MyApplication.lng + "");//
                city = MyApplication.city;

//                getType();
//                initDate();
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



    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStateBarHeight() {
        if (statusBarHeight == -1) {
            int resourceId = Resources.getSystem().getIdentifier(
                    "status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = Resources.getSystem().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }

    public static int getScreenWidth() {
        if (screenWidth == -1) {
            DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
            screenHeight = dm.heightPixels;
            screenWidth = dm.widthPixels;
        }
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenWidth == -1) {
            DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
            screenHeight = dm.heightPixels;
            screenWidth = dm.widthPixels;
        }
        return screenHeight;
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();

            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);

            String version = info.versionCode + "";
            return version;

        } catch (Exception e) {
            return null;
        }
    }

    public String getVersionName() {
        try {
            PackageManager manager = this.getPackageManager();

            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);

            String version = info.versionName;
            return version;

        } catch (Exception e) {
            return null;
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference IpAddress", ex.toString());
        }


        return null;
    }

    public static String getOnlyId() {
        final TelephonyManager tm = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.ningsheng.jietong.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                String type = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
                String message = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
                Log.i("------intent------", type + "" + message);
                setCostomMsg(context, message, type);
            }
        }
    }

    private void setCostomMsg(final Context context, String msg, String type) {
//        if((type.substring(type.length()-1,type.length()).equals("0"))){
        if (gsonUtil.getInstance().getValue(type, "type").equals("0")) {
            Log.i("--type---", "000");
            Intent intet = new Intent(context, LoginOutActivit.class);
            intet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intet.putExtra("content", msg);
            context.startActivity(intet);
        } else {//tongzhi
            Log.i("--type---", "11111");
            String id=(String)gsonUtil.getInstance().getValue(type, "id");
            initNotification(msg,id);
        }
    }


    private void initNotification(String message,String id) {
        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // params
        int smallIconId = R.mipmap.icon_notification_smal;
        Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon_notification_big)).getBitmap();
        String info = message;//内容

        // action when clicked
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setData(Uri.parse("host://anotheractivity"));
        intent.setData(Uri.parse(id));
        intent.putExtra("id",id);
        Log.d("-----MyApplication-initNotification----",id);
        builder.setLargeIcon(largeIcon)
                .setSmallIcon(smallIconId)
                .setContentTitle("捷通钱包")//标题
                .setContentText(info)
                .setTicker(info)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0));


        boolean isAboveLollipop = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

        builder.setSmallIcon(isAboveLollipop ? smallIconId : R.mipmap.icon_notification_big);
        final Notification n = builder.getNotification();

        nm.notify(NOTIFICATION_ID, n);
    }

    public final static int NOTIFICATION_ID = "Notification_jtqb".hashCode();

}
