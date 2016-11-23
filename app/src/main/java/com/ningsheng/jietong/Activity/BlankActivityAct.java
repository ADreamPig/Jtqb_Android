package com.ningsheng.jietong.Activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.View.ReboundScrollView;

import java.io.IOException;

/**
 * Created by zhushunqing on 2016/3/7.
 */
public class BlankActivityAct extends TitleActivity implements SurfaceHolder.Callback, Handler.Callback{
    SurfaceHolder surfaceHolder;
    MediaPlayer player;


    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.blank_activity);
        showBackwardView("",true);
        setTitle("kongbai");
    }

//    private ImageView Mbanner;
//    private LinearLayout Mtitle1;
//    private LinearLayout Mtitle2;
//    private TextView Mcontent;
//    private ReboundScrollView Mscr;
//    private int height;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if(hasFocus){
//            height=Mbanner.getBottom(); //获取pirce_show的顶部位置，即rlayout的底部位置
//        }
    }

    @Override
    protected void initListener() {

//         Mbanner=(ImageView) findViewById(R.id.banner);
//         Mtitle1=(LinearLayout)findViewById(R.id.title_1);
//         Mtitle2=(LinearLayout)findViewById(R.id.title_2);
//         Mcontent=(TextView)findViewById(R.id.title_content);
//        Mscr=(ReboundScrollView)findViewById(R.id.scroll);
//
//
//        Log.i("-----height------",height+"");
//
//        Mscr.setScrollViewListener(new ReboundScrollView.ScrollViewListener() {
//            @Override
//            public void onScrollChanged(int x, int y, int oldx, int oldy) {
//                if(oldy>height){
//                    if(Mcontent.getParent()==Mtitle1){
//                        Mtitle1.removeView(Mcontent);
//                        Mtitle2.addView(Mcontent);
//                    }
//                }else{
//                    if(Mcontent.getParent()==Mtitle2){
//
//                        Mtitle2.removeView(Mcontent);
//                        Mtitle1.addView(Mcontent);
//                    }
//                }
//            }
//        });

    }

    ImageView view;
    private VideoView videoview;
    private Uri mUri;
    @Override
    protected void initDate() {
        videoview=(VideoView)findViewById(R.id.videoview);
        view=(ImageView) findViewById(R.id.surface);
         mUri = Uri.parse(Environment.getExternalStorageDirectory() + "/zhushunqing.mp4");

        Handler mHandler = new Handler(this);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(BlankActivityAct.this, R.anim.translate_anim);
                view.startAnimation(animation);
            }
        });


//        view.getHolder().setKeepScreenOn(true);

//        surfaceHolder=view.getHolder();//SurfaceHolder是SurfaceView的控制接口
//        surfaceHolder.addCallback(this);//因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
////        surfaceHolder.setFixedSize(320, 220);//显示的分辨率,不设置为视频默认
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//Surface类型

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {

        toast("dad");
//        videoview.setVideoURI(mUri);
//        videoview.start();
//        player.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player=new MediaPlayer();
//        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        player.setDisplay(surfaceHolder);
//        //设置显示视频显示在SurfaceView上
        try {
//            player.reset();
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//            player.setDataSource(Environment.getExternalStorageDirectory()
//                    +"/zhushunqing.mp4");
            // 把视频输出到SurfaceView上
//            player.setDisplay(view.getHolder());
//            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
