package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.BitmapUtil;
import com.ningsheng.jietong.Utils.CheckUtil;
import com.ningsheng.jietong.Utils.FileUtil;
import com.ningsheng.jietong.View.TrimCopyQq.TrimCopyQqImageView;
import com.ningsheng.jietong.View.TrimCopyQq.TrimCopyQqSrcView;

import java.io.File;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;


/**
 * Created by zhangheng on 2015/12/1.
 */
public class TrimCopyQqActivity extends TitleActivity implements View.OnClickListener {
    private TrimCopyQqSrcView srcView;
    private TrimCopyQqImageView imageView;
    private String path;
    private Bitmap bitmap;
    public static final int TRIMCOPYQQPADDINGWIDTH = MyApplication.getScreenWidth() / 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getIntent().getStringExtra("path");
        if (CheckUtil.isNull(path)) {
            finish();
            return;
        }
        setTitle("移动和缩放");
        showBackwardView("照片", true);
        showForwardView("使用", true);
        setContentView(R.layout.activity_trimcopyqq);
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
        srcView = (TrimCopyQqSrcView) findViewById(R.id.trimCopyQq_Src);
        imageView = (TrimCopyQqImageView) findViewById(R.id.trimCopyQq_image);
        Bitmap bitmap_ = null;
        try {
            bitmap_ =   BitmapUtil.getBitmapFromPath(path, MyApplication.getScreenWidth() >> 2, MyApplication.getScreenHeight() >> 2);
        } catch (OutOfMemoryError e) {
            Log.e("OutOfMemoryError","Idiot,Out of memory.,I think it's a problem for you.");
            e.printStackTrace();
        }
//        System.out.println(Formatter.formatFileSize(this, bitmap.getByteCount()));
        if (bitmap_ == null) {
            toast("无效图片");
            finish();
            return;
        }
        System.out.println(Formatter.formatFileSize(this, bitmap_.getByteCount()));
        BitmapUtil.destroy();
        imageView.setImageBitmap(bitmap_);
        srcView.postDelayed(new Runnable() {
            @Override
            public void run() {
                SupportAnimator animator = ViewAnimationUtils.createCircularReveal(srcView, srcView.getWidth() >> 1, srcView.getHeight() >> 1, 0, (float) Math.sqrt(Math.pow(srcView.getWidth(), 2) + Math.pow(srcView.getHeight(), 2)));
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(1000);
                srcView.setVisibility(View.VISIBLE);
                animator.start();
            }
        }, 250);
        final Snackbar snackbar = Snackbar.make(imageView, "双击可放大图像", Snackbar.LENGTH_LONG);
        snackbar.setAction("关闭", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.next:
//                Toast.makeText(MyApplication.getInstance(), "图片剪裁成功", Toast.LENGTH_SHORT).show();
//                Long time = System.currentTimeMillis();
//                FileUtil.writeFile(FileUtil.getPerfectionPath(this) + File.separator + "HuiYingJinFu" + File.separator + "Image", "clipping_" + time + "_head.jpg", imageView.trim());
//                String filepath = FileUtil.getPerfectionPath(this) + File.separator + "HuiYingJinFu" + File.separator + "Image" + File.separator + "clipping_" + time + "_head.jpg";
//                Log.v("FILE", filepath);
//                Intent intent = new Intent();
//                intent.putExtra("file", filepath);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
        }
    }

    @Override
    protected void onForward() {
        Bitmap bitmap = imageView.trim();
        String fileName = System.currentTimeMillis() + "head_.png";
        FileUtil.writeFile(FileUtil.getPerfectionPath(this), fileName, bitmap);
        Intent intent = new Intent();
        intent.putExtra("path", FileUtil.getPerfectionPath(this) + File.separator + fileName);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageView.setImageBitmap(null);
    }
}
