package com.ningsheng.jietong.Activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.Utils.PermissionsChecker;
import com.ningsheng.jietong.interdace.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by zhang on 2015/11/26.
 */
public class AibumGridActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {
    private RecyclerView recyclerView;
    private AibumGridAdapter adapter;
    private AibumListActivity.Aibum aibum;
    private final int COLUMN_COUNT = 3;
    private int PADDING;
    private TextView left, middle, right;
    private boolean isNewly;
    private ImageLoader imageLoader;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aibum);
        mPermissionsChecker=new PermissionsChecker(this);
        if(mPermissionsChecker.lacksPermissions(PERMISSIONS)){
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
            return;
        }else{
            initView();
        }



    }

    private void initView() {
        imageLoader = ImageLoader.getInstance();
        MediaUtil.initImageLoaderConfiguration2ImageQqQuick(this);
        MediaUtil.initImageLoaderDisplayImageOptions2ImageQqQuick(true, true);
        aibum = (AibumListActivity.Aibum) getIntent().getSerializableExtra("aibum");
        if (aibum == null) {
            isNewly = true;
            Cursor mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                    MediaStore.MediaColumns.DATE_ADDED + " DESC limit 100");
            aibum = new AibumListActivity.Aibum();
            while (mCursor.moveToNext()) {
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                aibum.getLists().add(new AibumListActivity.Aibum.Image(path));
            }
        }

        middle = (TextView) findViewById(R.id.text_title);
        left = (TextView) findViewById(R.id.button_backward);
        right = (TextView) findViewById(R.id.button_forward);
        left.setOnClickListener(this);
        if (isNewly) {
            middle.setText("最近照片");
        } else {
            middle.setText(aibum.getName());
        }
        if (aibum != null && aibum.getLists() != null && !aibum.getLists().isEmpty()) {
            PADDING = AndroidUtil.dip2px(AibumGridActivity.this, 5);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            adapter = new AibumGridAdapter(this);
            GridLayoutManager manager = new GridLayoutManager(this, COLUMN_COUNT);
            manager.offsetChildrenHorizontal(10);
            manager.offsetChildrenVertical(10);
            recyclerView.setLayoutManager(manager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new GridItemDecoration());
            recyclerView.setAdapter(adapter);
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    switch (newState) {
                        case 0:
                            System.out.println("~~~~~ 停止滑动");
                            imageLoader.resume();
                            break;
                        case 1:
                            System.out.println("~~~~~ 依然在滑");
                            imageLoader.pause();
                            break;
                        case 2:
                            System.out.println("~~~~~ 惯性滑");
                            imageLoader.resume();
                            break;
                    }
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_backward:
                if (isNewly) {
                    Intent intent = new Intent(this, AibumListActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(imageLoader!=null){
            imageLoader.resume();
        }

    }

    @Override
    public void onBackPressed() {
        if (isNewly) {
            Intent intent = new Intent(this, AibumListActivity.class);
            startActivity(intent);
            finish();
        } else
            finish();
    }

    @Override
    public void onItemClickListener(View v, int position) {
        Intent intent = new Intent(this, TrimCopyQqActivity.class);
        intent.putExtra("path", aibum.getLists().get(position).getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, getString(R.string.ANDROID_LOLLIPOP_TRANSITIONNAME_FIRST));
            ActivityCompat.startActivityForResult(this, intent, 0x101, optionsCompat.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(v, v.getWidth() >> 1, v.getHeight() >> 1, 0, 0);
            ActivityCompat.startActivityForResult(this, intent, 0x101, optionsCompat.toBundle());
        }
    }


    private class AibumGridAdapter extends RecyclerView.Adapter {
        private OnItemClickListener onItemClickListener;

        public AibumGridAdapter(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(View.inflate(AibumGridActivity.this, R.layout.layout_aibum_item_grid, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
//            MediaUtil.displayImageQqQuick(AibumGridActivity.this, "file://" + aibum.getLists().get(position).getPath(), viewHolder.iv);
//            imageLoader.displayImage("file://" + aibum.getLists().get(position).getPath(), viewHolder.iv, DISPLAY_IMAGE_OPTIONS_AIBUM_QQ_QUICK);
            MediaUtil.displayImageQqQuick("file://" + aibum.getLists().get(position).getPath(),viewHolder.iv);
        }

        @Override
        public int getItemCount() {
            return aibum.getLists().size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView iv;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                iv = (ImageView) itemView.findViewById(R.id.aibum_iv);
                int width = (int) (MyApplication.getScreenWidth() * 1.0f / COLUMN_COUNT - AndroidUtil.dip2px(AibumGridActivity.this, 20));
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(width, width);
                iv.setLayoutParams(layoutParams);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, getPosition());
                }
            }
        }
    }

    private class GridItemDecoration extends RecyclerView.ItemDecoration {
        public GridItemDecoration() {
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildLayoutPosition(view);

            if ((position + 1) % COLUMN_COUNT == 0) {
                outRect.right = PADDING;
            } else if (position % COLUMN_COUNT == 0) {
                outRect.right = PADDING;
                outRect.left = PADDING;
            } else {
                outRect.right = PADDING;
            }
            if ((position + 1) > COLUMN_COUNT) {
                outRect.top = PADDING;
            } else {
                outRect.top = 5;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            setResult(13);
            finish();
        }else if(requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED){
            initView();
        }
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == 0x101) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
