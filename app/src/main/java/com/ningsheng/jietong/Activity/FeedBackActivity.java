package com.ningsheng.jietong.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.BitmapUtil;
import com.ningsheng.jietong.Utils.FileUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.PermissionsChecker;
import com.ningsheng.jietong.View.UpLoadView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/25.
 */
public class FeedBackActivity extends TitleActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    private EditText etInput;
    private TextView tvCount;
    private GridView gridView;
    private TextView tvNext;
    private List<String> datas;
    private FeedBackAdapter adapter;
    private FeedBackDialog dialog;
    private ImageLoader imageLoader;
    private String path;
    private int i;
    private String result;
    private LayoutInflater inflater;
    private SparseArray<String> sparseArray;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x100:
                    float progress = (float) msg.obj;
                    System.out.println("progress:" + progress);
                    UpLoadView view = (UpLoadView) gridView.getChildAt(msg.arg1).getTag();
                    view.setProgress(progress);
                    break;
                case 0x101:
                    Toast.makeText(FeedBackActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }

        }
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_feedback);
        setTitle("意见反馈");
        mPermissionsChecker=new PermissionsChecker(this);
        imageLoader = ImageLoader.getInstance();
        MediaUtil.initImageLoaderConfiguration2ImageQqQuick(this);
        etInput = (EditText) findViewById(R.id.feedBack_et_input);
        tvCount = (TextView) findViewById(R.id.feedBack_tv_count);
        gridView = (GridView) findViewById(R.id.gridView);
        tvNext = (TextView) findViewById(R.id.feedBack_tv_next);
        tvNext.setOnClickListener(this);
        etInput.addTextChangedListener(this);
        datas = new ArrayList<>();
        adapter = new FeedBackAdapter();
        gridView.setAdapter(adapter);
        inflater = getLayoutInflater();
        sparseArray = new SparseArray<>(10);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaUtil.initImageLoaderDisplayImageOptions2ImageQqQuick(true, false);
    }

    @Override
    protected void initListener() {
        gridView.setOnItemClickListener(this);

    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(etInput.getText().toString())) {
            toast("亲，不想吐槽两句么?");
            etInput.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }
        upLoadFeedBack();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvCount.setText(s.length() + "/100");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (datas.size() == position) {
            if (dialog == null) {
                dialog = new FeedBackDialog(this);
            }
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

    }

    private class FeedBackAdapter extends BaseAdapter {
        private AbsListView.LayoutParams layoutParams;

        public FeedBackAdapter() {
            super();
            int width = (MyApplication.getScreenWidth() - AndroidUtil.dip2px(FeedBackActivity.this, 15) * (gridView.getNumColumns() - 1)) / gridView.getNumColumns();
            layoutParams = new AbsListView.LayoutParams(width, width);
        }

        @Override
        public int getCount() {
            if (datas.size() < 5)
                return datas.size() + 1;
            return datas.size();
        }

        @Override
        public String getItem(int position) {
            if (position == datas.size())
                return null;
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv;
            UpLoadView upLoadView;
            convertView = inflater.inflate(R.layout.layout_item_feedback, null);
            iv = (ImageView) convertView.findViewById(R.id.iv);
            upLoadView = (UpLoadView) convertView.findViewById(R.id.upLoadView);
            convertView.setTag(upLoadView);
            int width = (MyApplication.getScreenWidth() - AndroidUtil.dip2px(FeedBackActivity.this, 15) * (gridView.getNumColumns() - 1) - AndroidUtil.dip2px(FeedBackActivity.this, 20)) / gridView.getNumColumns();
            layoutParams = new AbsListView.LayoutParams(width, width);
            convertView.setLayoutParams(layoutParams);
            String path = getItem(position);
            if (null == path) {
                iv.setImageResource(R.mipmap.tianjiatupian);
                upLoadView.setVisibility(View.GONE);
            } else {
                MediaUtil.displayImageQqQuick("file://" + path, iv);
                upLoadView.setVisibility(View.VISIBLE);
                if (position < index) {
                    upLoadView.setProgress(1);
                } else {
                }
            }
            return convertView;
        }
    }

    private class FeedBackInfo {
        private String path;
        private int type;//1照片,2+

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    private class FeedBackDialog extends Dialog implements View.OnClickListener {

        public FeedBackDialog(Context context) {
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
            getWindow().getAttributes().width = (int) (AndroidUtil.getScreenSize(FeedBackActivity.this, 1) * 0.9);


        }

        @Override
        public void onClick(View v) {
            dismiss();
            TextView tv = (TextView) v;
            Intent intent = new Intent();
            name_click=tv.getText().toString();
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                PermissionsActivity.startActivityForResult(FeedBackActivity.this, REQUEST_CODE, PERMISSIONS);
                return;
            }
            photo(tv.getText().toString());


        }
    }
    private String name_click;
    private void photo(String name){
        Intent intent = new Intent();
        switch (name) {
            case "拍照":
                if (path == null || "".equals(path))
                    path = FileUtil.getPerfectionPath(FeedBackActivity.this, "photo");
                intent.setAction("android.media.action.IMAGE_CAPTURE");
                Uri uri = Uri.fromFile(new File(path, i + "image.png"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 0x100);
                break;
            case "从手机相册中选取":
                intent.setClass(FeedBackActivity.this, AibumGridsActivity.class);
                intent.putExtra("count", 5 - datas.size());
                startActivityForResult(intent, 0x101);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            case 0x100:
                String imagePath = path + File.separator + i + "image.png";
                datas.add(imagePath);
                adapter.notifyDataSetChanged();
                Bitmap bitmap1 = BitmapUtil.getBitmapFromPath(imagePath, MyApplication.getScreenWidth() >> 1, MyApplication.getStateBarHeight() >> 1);
                byte[] bytes1 = FileUtil.bitmap2Bytes(bitmap1);
                String strBytes1 = Base64.encodeToString(bytes1, Base64.NO_WRAP);
                sparseArray.put(sparseArray.size(), strBytes1);

//                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//                MediaStore.Images .Media.insertImage(getContentResolver(), bitmap, "", "");
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                intent.setData(Uri.fromFile(new File(path, i + "image.png")));
//                sendBroadcast(intent);
                i++;
                break;
            case 0x101://相册
                List<String> lists = (List<String>) data.getSerializableExtra("images");
                if (lists != null && !lists.isEmpty()) {
                    datas.addAll(lists);
                    adapter.notifyDataSetChanged();
                }
                for (String path : lists) {
                    Bitmap bitmap = BitmapUtil.getBitmapFromPath(path, MyApplication.getScreenWidth() >> 1, MyApplication.getScreenHeight() >> 1);
                    byte[] bytes = FileUtil.bitmap2Bytes(bitmap);
                    String strBytes = Base64.encodeToString(bytes, Base64.NO_WRAP);
                    sparseArray.put(sparseArray.size(), strBytes);                }
                break;
        }
    }

    private void upLoadFeedBack() {
        StringBuffer sb=new StringBuffer();
        for (int i=0;i<sparseArray.size();i++){
            sb.append(sparseArray.get(i)+",");
        }
        if(sb.length()!=0){
        sb.deleteCharAt(sb.length()-1);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accountId", MyApplication.user.getId());
        map.put("content", etInput.getText().toString());
        map.put("arrayImgByte", sb.toString());
        HttpSender sender = new HttpSender(Url.addFeedbackInfo, "意见反馈", map, new OnHttpResultListener() {

            @Override
            public void onStar() {
                super.onStar();
            }

            @Override
            public void onSuccess(String message, String code, String data) {
                for(int i=0;i<gridView.getCount();i++){
                    ((UpLoadView) gridView.getChildAt(i).getTag()).setProgress( 1.0f );
                        }
                Toast.makeText(FeedBackActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }



            @Override
            public void onFinished() {
                super.onFinished();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                super.onLoading(total, current, isDownloading);
//                gridView.getCount()
                Log.i("----loading----",total+"----"+current);
//                        for(int i=0;i<gridView.getCount();i++){
                            ((UpLoadView) gridView.getChildAt(i).getTag()).setProgress(current * 1.0f / total);
//                        }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }

    private int index;

    private void upLoadFeedBack1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                try {
                    URL url = new URL(Url.addFeedbackInfo);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");// 提交模式
                    conn.setConnectTimeout(5000);//连接超时 单位毫秒
                    conn.setReadTimeout(5000);//读取超时 单位毫秒
                    conn.setDoOutput(true);// 是否输入参数
                    StringBuffer params = new StringBuffer();
                    // 表单参数与get形式一样
                    params.append("accountId").append("=").append(MyApplication.user.getId()).append("&")
                            .append("content").append("=").append(etInput.getText().toString()).append("&").append("arrayImgByte").append("=").append(sparseArray.get(index));
                    final byte[] bypes = params.toString().getBytes();
                    ByteArrayInputStream in = new ByteArrayInputStream(bypes);
                    int len = 0;
                    byte[] b = new byte[1024];
                    while ((len = in.read(b)) != -1) {
                        conn.getOutputStream().write(b, 0, len);// 输入参数
                        count += len;
                        if (sparseArray.get(index) != null) {
                            Message msg = handler.obtainMessage();
                            System.out.println("count:" + count);
                            System.out.println("bypts.length:" + bypes.length);
                            msg.obj = count * 1.0f / bypes.length;
                            msg.arg1 = index;
                            msg.what = 0x100;
                            msg.sendToTarget();
                        }
                    }
                    if (sparseArray.get(index) != null)
                        index++;
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
                    outStream.close();
                    inStream.close();
                    result = new String(data, "UTF-8");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (index >= sparseArray.size()) {
                        Message msg = handler.obtainMessage();
                        msg.what = 0x101;
                        try {

                            JSONObject obj = new JSONObject(result);
                            msg.obj = obj.getString("CZMS");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            msg.obj = "哎呀，上传出了点小问题";
                        }
                        msg.sendToTarget();
                    } else {
                        upLoadFeedBack1();
                    }
                }
            }
        }).start();

    }
}
