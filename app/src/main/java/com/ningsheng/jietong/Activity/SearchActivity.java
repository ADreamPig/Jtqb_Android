package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Fragmen.BusinessFragment;
import com.ningsheng.jietong.Fragmen.SearchAfterFragment;
import com.ningsheng.jietong.Fragmen.SearchBeforeFragment;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class SearchActivity extends BaseActivity {
    private EditText m_search;
   private  SearchAfterFragment searchAfterFragment;
    private SearchBeforeFragment beforeFragment;
    public static  String city;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);

        city=getIntent().getStringExtra("city");
        m_search = (EditText) findViewById(R.id.act_search_edit);
        findViewById(R.id.act_search_title).setBackgroundColor(getResources().getColor(R.color.app_color));

        beforeFragment = new SearchBeforeFragment();
//        BusinessFragment f=new BusinessFragment();

        searchAfterFragment = new SearchAfterFragment();
        displayFragment(beforeFragment);
    }

    public void setText(String text){
        m_search.setText(text);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_search_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_search.getText().toString().equals("")){
                    toast("请输入搜索内容");
                    return;
                }
                searchAfterFragment.clean(m_search.getText().toString());
//                searchAfterFragment.getData(m_search.getText().toString());
            }
        });
        findViewById(R.id.act_search_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        m_search.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View paramView, int paramInt,
                                 KeyEvent paramKeyEvent) {

                if (paramInt == KeyEvent.KEYCODE_ENTER
                        && paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    // InputMethodManager
                    // g=(InputMethodManager)getSystemService("");
                    // 先隐藏键盘service
                    ((InputMethodManager)
                            getSystemService(InputMethodService.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    if(m_search.getText().toString().equals("")){
                        toast("请输入搜索内容");
                        return false;
                    }
                    searchAfterFragment.clean(m_search.getText().toString());
//                    searchAfterFragment.getData(m_search.getText().toString());
                }

                return false;
            }

        });
        m_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {//改变之钱
                if(!s.toString().trim().equals("")){

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//

            }

            @Override
            public void afterTextChanged(Editable s) {//改变之后
                if(s.length()==0){
                    displayFragment(beforeFragment);
                }else{
                    searchAfterFragment.clean(m_search.getText().toString());
//                    searchAfterFragment.getData(s.toString());
                    displayFragment(searchAfterFragment);
                }
            }
        });
    }

    @Override
    protected void initDate() {
//        String
//        getData(null);


    }


    private void displayFragment(Fragment f) {
        // TODO Auto-generated method stub
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.act_search_frame, f);
        ft.commit();
    }
}
