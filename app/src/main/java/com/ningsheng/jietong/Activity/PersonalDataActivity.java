package com.ningsheng.jietong.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.SwipeBack.SwipeBackLayout;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.FileUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.PermissionsChecker;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.UpLoadView;
import com.ningsheng.jietong.View.CircularUpLoadView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/22.
 * 个人资料
 */
public class PersonalDataActivity extends TitleActivity {
    private PersonalDialog dialog;
    private ImageView iv;
    private TextView tv;
    private CircularUpLoadView upLoadView;
    private String pathPhoto;
    private String pathFinish;
    private String name;
    private String name_click;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            float progress = (float) msg.obj;
            System.out.println("progress:" + progress);
            upLoadView.setProgress(progress);
        }
    };

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE = 0; // 请求码
    private PermissionsChecker mPermissionsChecker;

    @Override
    protected void initListener() {
        mPermissionsChecker = new PermissionsChecker(this);
    }

    @Override
    protected void initDate() {
//        tv.setText(getIntent().getStringExtra("name"));
//        MediaUtil.displayImageHead(this, getIntent().getStringExtra("image"), iv);

        tv.setText(MyApplication.user.getUserName());
        MediaUtil.displayImageHead(this, MyApplication.user.getImage(), iv);
    }

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_personaldata);
        setTitle("个人资料");
        iv = (ImageView) findViewById(R.id.personaldata_iv_head);
        tv = (TextView) findViewById(R.id.personaldata_tv_nickname);
        tv.setText(getIntent().getStringExtra("name"));
        upLoadView = (CircularUpLoadView) findViewById(R.id.upLoadView);
        upLoadView.setVisibility(View.GONE);
        MediaUtil.displayImageHead(this, getIntent().getStringExtra("image"), iv);


        if ("".equals(MyApplication.user.getId())) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 11);
        }
        m_swipe = getSwipeBackLayout();
        m_swipe.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
//                if(){}
                Log.i("----scrollPercent-----:", scrollPercent + ":--------:" + state);
                if (state == 2) {
                    Intent intent = getIntent();
                    intent.putExtra("path", pathFinish);
                    intent.putExtra("name", name);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {

                Log.i("onEdgeTouch--------", edgeFlag + "");
            }

            @Override
            public void onScrollOverThreshold() {
                Log.i("onScrollOverThreshold--------", "asas");
//                Intent intent = getIntent();
//                intent.putExtra("path", pathFinish);
//                intent.putExtra("name", name);
//                setResult(0x22, intent);
//                finish();
            }
        });
    }

    SwipeBackLayout m_swipe;

    @Override
    public void scrollToFinishActivity() {
        super.scrollToFinishActivity();
        Log.i("scrollToFinishActivity----", "");
//        Intent intent = getIntent();
//        intent.putExtra("path", pathFinish);
//        intent.putExtra("name", name);
//        setResult(0x22, intent);
//        finish();
    }

    @Override
    protected void onBackward() {
        Intent intent = getIntent();
        intent.putExtra("path", pathFinish);
        intent.putExtra("name", name);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("path", pathFinish);
        intent.putExtra("name", name);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    public void personalOnClick1(View view) {
        if (dialog == null) {
            dialog = new PersonalDialog(this);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

    public void personalOnClick2(View view) {
        Intent intent = new Intent(this, ChangeNicknameActivity.class);
        intent.putExtra("name", tv.getText().toString());
        startActivityForResult(intent, 0x103);
    }

    private class PersonalDialog extends Dialog implements View.OnClickListener {

        public PersonalDialog(Context context) {
            super(context, R.style.MaterialDialogStyle);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            initView();
        }

        private void initView() {
            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(0xFFFFFFFF);
            backgroundDrawable.setCornerRadius(AndroidUtil.dip2px(getContext(), 4));
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setBackgroundDrawable(backgroundDrawable);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtil.dip2px(getContext(), 50));
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtil.dip2px(getContext(), 0.5f));
            TextView textView = new TextView(getContext());
            textView.setOnClickListener(this);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.font_black));
            textView.setText("拍照");
            TextView textView1 = new TextView(getContext());
            textView1.setOnClickListener(this);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(getResources().getColor(R.color.font_black));
            textView1.setText("从手机相册中选取");
            View line = new View(getContext());
            line.setBackgroundColor(getResources().getColor(R.color.line_color));
            linearLayout.addView(textView, params);
            linearLayout.addView(line, params1);
            linearLayout.addView(textView1, params);
            setContentView(linearLayout);
            getWindow().getAttributes().width = (int) (AndroidUtil.getScreenSize(PersonalDataActivity.this, 1) * 0.9);


        }


        @Override
        public void onClick(View v) {
            dismiss();
            TextView tv = (TextView) v;
            name_click=tv.getText().toString();
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                PermissionsActivity.startActivityForResult(PersonalDataActivity.this, REQUEST_CODE, PERMISSIONS);
                return;
            }
            photo(name_click);

        }
    }

    private void photo(String name){
        Intent intent = new Intent();
        switch (name) {
            case "拍照":
                camera();
                break;
            case "从手机相册中选取":
                intent.setClass(PersonalDataActivity.this, AibumGridActivity.class);
                startActivityForResult(intent, 0x101);
                break;
            default:
                break;
        }

    }

    private void camera() {
        Intent intent = new Intent();
        if (pathPhoto == null || "".equals(pathPhoto))
            pathPhoto = FileUtil.getPerfectionPath(PersonalDataActivity.this, "head");
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        Uri uri = Uri.fromFile(new File(pathPhoto, "head.png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 0x100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //权限的授权
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
//            setResult(13);
//            finish();
//            toast("拒绝");
        } else if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            photo(name_click);
        }
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 0x100://拍照
                    Bitmap bitmap = BitmapFactory.decodeFile(pathPhoto + File.separator + "head.png");
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(new File(pathPhoto, "head.png")));
                    sendBroadcast(intent);
                    Intent intent1 = new Intent(this, TrimCopyQqActivity.class);
                    intent1.putExtra("path", pathPhoto + File.separator + "head.png");
                    startActivityForResult(intent1, 0x102);
                break;
            case 0x101://相册的剪裁
                pathFinish = data.getStringExtra("path");
                MediaUtil.displayImageHead(MyApplication.getInstance(), "file://" + data.getStringExtra("path"), iv);
                uploadImage(data.getStringExtra("path"));
                upLoadView.setVisibility(View.VISIBLE);
                break;
            case 0x102://拍照的裁剪
                pathFinish = data.getStringExtra("path");
                MediaUtil.displayImageHead(MyApplication.getInstance(), "file://" + pathFinish, iv);
                uploadImage(pathFinish);
                upLoadView.setVisibility(View.VISIBLE);
                break;
            case 0x103://修改昵称
                name = data.getStringExtra("name");
                tv.setText(name);
                break;
        }
    }

    private void uploadImage1(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                try {
                    URL url = new URL(Url.uploadImage);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");// 提交模式
                    conn.setConnectTimeout(10000);//连接超时 单位毫秒
                    conn.setReadTimeout(10000);//读取超时 单位毫秒
                    conn.setDoOutput(true);// 是否输入参数
                    conn.setDoInput(true);// 是否输入参数
                    //设置请求属性
                    conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
                    conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                    conn.setRequestProperty("Charset", "UTF-8");
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    byte[] bytes = FileUtil.bitmap2Bytes(bitmap);
                    String strBytes = Base64.encodeToString(bytes, Base64.NO_WRAP);
                    StringBuffer params = new StringBuffer();
                    // 表单参数与get形式一样
                    params.append("id").append("=").append(MyApplication.user.getId()).append("&")
                            .append("filename").append(" =").append("head.png").append("&").append("fileByte").append("=").append(strBytes);
                    final byte[] bypes = params.toString().getBytes();
                    ByteArrayInputStream in = new ByteArrayInputStream(bypes);
//                    DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
//                    outputStream.writeBytes(params.toString());
//                    outputStream.flush();
//                    outputStream.close();
                    int len = 0;
                    byte[] b = new byte[1024];
                    while ((len = in.read(b)) != -1) {
                        conn.getOutputStream().write(b, 0, len);// 输入参数
                        conn.getOutputStream().flush();
                        count += len;
                        Message msg = handler.obtainMessage();
                        System.out.println("count:" + count);
                        System.out.println("bypts.length:" + bypes.length);
                        msg.obj = count * 1.0f / bypes.length;
                        msg.sendToTarget();
                    }
                    conn.getOutputStream().close();
                    in.close();
                    InputStream inStream = conn.getInputStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    len = 0;
                    while ((len = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    byte[] data = outStream.toByteArray();//网页的二进制数据
                    System.out.print("上传:" + new String(data));
                    outStream.close();
                    inStream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void uploadImage(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap != null) {
            byte[] bytes = FileUtil.bitmap2Bytes(bitmap);
            String strBytes = Base64.encodeToString(bytes, Base64.NO_WRAP);
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", MyApplication.user.getId());
            map.put("accountId", MyApplication.user.getId());
            map.put("filename", "head.png");
            map.put("fileByte", strBytes);
            HttpSender sender = new HttpSender(Url.uploadImage, "上传用户头像", map, new OnHttpResultListener() {

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    super.onLoading(total, current, isDownloading);
                    System.out.println("total:" + total + " current" + current);
                    upLoadView.setProgress(current * 1.0f / total);
                }

                @Override
                public void onSuccess(String message, String code, String data) {
                    if ("0000".equals(code)) {
//                        upLoadView = (CircularUpLoadView) findViewById(R.id.upLoadView);
//                        upLoadView.setVisibility(View.GONE);
//                        MediaUtil.displayImageHead(PersonalDataActivity.this, getIntent().getStringExtra("image"), iv);
                        toast("修改成功！");
                        upLoadView.setProgress(1.0f);
                        String url = (String) gsonUtil.getInstance().getValue(data, "data");
                        MyApplication.user.setImage(url);
                        SharedPreferencesUtil.getInstance(PersonalDataActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER, MyApplication.user);
                    } else {
                        toast(message);
                    }


                }
            });
            sender.setContext(this);
            sender.send(Url.Post);

        }
    }
}
