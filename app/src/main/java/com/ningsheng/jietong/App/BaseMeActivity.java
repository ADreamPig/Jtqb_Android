package com.ningsheng.jietong.App;

import android.content.Intent;
import android.util.Log;

import com.ningsheng.jietong.SwipeBack.SwipeBackLayout;

/**
 * Created by zhushunqing on 2016/3/20.
 */
public class BaseMeActivity extends TitleActivity {
    private SwipeBackLayout m_back;

    @Override
    protected void initListener() {
        m_back=getSwipeBackLayout();
        m_back.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
                Log.d("onEdgeTouch","-----------onScrollStateChange------------"+state+"---"+scrollPercent);
                if(state==2){
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
//                    finish();
                }
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Log.d("onEdgeTouch","-----------onEdgeTouch------------");
            }

            @Override
            public void onScrollOverThreshold() {
                Log.d("onEdgeTouch","-----------onScrollOverThreshold------------");
            }
        });
    }

    @Override
    protected void initDate() {

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    protected void onBackward() {
//        super.onBackward();
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();

    }
}
