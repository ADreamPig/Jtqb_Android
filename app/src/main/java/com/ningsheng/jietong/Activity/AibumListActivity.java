package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.interdace.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhang on 2015/11/25.
 */
public class AibumListActivity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {
    private RecyclerView recyclerView;
    //单选
    public static final int TYPE_SINGLE_SELECTION = 0;
    //多选
    public static final int TYPE_MULTI_SELECTION = 1;
    private int type;
    private ImageLoader imageLoader;
    @Override
    public void onItemClickListener(View v, int position) {
        Intent intent = new Intent();
        switch (type) {
            case TYPE_SINGLE_SELECTION:
                intent.setClass(this, AibumGridActivity.class);
                break;
            case TYPE_MULTI_SELECTION:
                intent.setClass(this, AibumGridsActivity.class);
                break;
        }
        intent.putExtra("aibum", aibums.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, getString(R.string.ANDROID_LOLLIPOP_TRANSITIONNAME_FIRST));
            ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(v, v.getWidth() >> 1, v.getHeight() >> 1, 0, 0);
            ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
        }

    }

    private AibumAdapter adapter;
    private List<Aibum> aibums = new ArrayList<Aibum>();
    private HashMap<String, Integer> subAibums = new HashMap<String, Integer>();
    private TextView left, middle, right;
    private Aibum newly = new Aibum();
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aibum);
        initView();
    }

    private void initView() {
        imageLoader = ImageLoader.getInstance();
        MediaUtil.initImageLoaderConfiguration2ImageQqQuick(this);
        MediaUtil.initImageLoaderDisplayImageOptions2ImageQqQuick(true,true);
        type = getIntent().getIntExtra("type", TYPE_SINGLE_SELECTION);
        middle = (TextView) findViewById(R.id.text_title);
        left = (TextView) findViewById(R.id.button_backward);
        right = (TextView) findViewById(R.id.button_forward);
        middle.setText("选择相册");
        left.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        newly.setName("最近照片");
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                getDatas();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (!newly.getLists().isEmpty()) {
                    newly.setPath(newly.getLists().get(0).getPath());
                    aibums.add(0, newly);
                }
                adapter = new AibumAdapter(AibumListActivity.this);
                LinearLayoutManager manager = new LinearLayoutManager(AibumListActivity.this);
                recyclerView.setLayoutManager(manager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        };
        asyncTask.execute();
    }

    private void getDatas() {
        Cursor mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        while (mCursor.moveToNext()) {
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            if (count < 100) {
                newly.getLists().add(new Aibum.Image(path));
            }
            count++;
            File file = new File(path).getParentFile();
            Aibum aibum = null;
            if (!subAibums.containsKey(file.getAbsolutePath())) {
                aibum = new Aibum();
                aibum.setPath(path);
                aibum.setName(file.getName());
                aibums.add(aibum);
                subAibums.put(file.getAbsolutePath(), aibums.indexOf(aibum));
            } else {
                aibum = aibums.get(subAibums.get(file.getAbsolutePath()));
            }
            aibum.getLists().add(new Aibum.Image(path));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_backward:
                finish();
                break;
        }
    }


    class AibumAdapter extends RecyclerView.Adapter {
        private OnItemClickListener onItemClickListener;

        public AibumAdapter(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(AibumListActivity.this, R.layout.layout_aibum_item_list, null);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv.setText(aibums.get(position).getName() + " (" + aibums.get(position).getLists().size() + ")");
            MediaUtil.displayImageQqQuick("file://" + aibums.get(position).getPath(), viewHolder.iv);
        }


        @Override
        public int getItemCount() {
            return aibums.size();
        }


        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView iv;
            private TextView tv;

            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                iv = (ImageView) v.findViewById(R.id.aibum_iv);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv.getLayoutParams();
                int width = (int) (((MyApplication.getScreenHeight() - MyApplication.getStateBarHeight()) / 9.0f - AndroidUtil.dip2px(AibumListActivity.this, 0.5f)));
                layoutParams.width = width;
                layoutParams.height = width;
                iv.setLayoutParams(layoutParams);
                tv = (TextView) v.findViewById(R.id.aibum_tv);
            }

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, getPosition());
                }
            }
        }


    }


    public static class Aibum implements Serializable {
        private String path;
        private String name;
        private List<Image> lists = new ArrayList<Image>();

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Image> getLists() {
            return lists;
        }


        public static class Image implements Serializable {
            private String path;

            public Image(String path) {
                this.path = path;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }
    }


}
