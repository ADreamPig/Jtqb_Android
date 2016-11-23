package com.ningsheng.jietong.Activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.Utils.PermissionsChecker;
import com.ningsheng.jietong.interdace.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 * 多选
 * Created by zhang on 2015/11/26.
 */
public class AibumGridsActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {
    private RecyclerView recyclerView;
    private AibumGridAdapter adapter;
    private AibumListActivity.Aibum aibum;
    private final int COLUMN_COUNT = 3;
    private int PADDING;
    private int count;
    private TextView left, middle, right;
    private TextView tvSelect;
    private boolean isNewly;
    private SparseArray<String> sparseArray;
    private ArrayList<String> listAibums;
    private ImageLoader imageLoader;

//    private PermissionsChecker mPermissionsChecker; // 权限检测器
//    private static final int REQUEST_CODE = 0; // 请求码
//    // 所需的全部权限
//    static final String[] PERMISSIONS = new String[]{
//            Manifest.permission.READ_EXTERNAL_STORAGE,
////            Manifest.permission.MODIFY_AUDIO_SETTINGS
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aibum);
        View m_title=findViewById(R.id.app_title_bg);
        m_title.setBackgroundColor(getResources().getColor(R.color.app_color));

//        mPermissionsChecker=new PermissionsChecker(this);
//        if(mPermissionsChecker.lacksPermissions(PERMISSIONS)){
//            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
//            return;
//        }else{
//            initView();
//        }


        initView();
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
        count = getIntent().getIntExtra("count", 10);
        sparseArray = new SparseArray<String>(count);
        listAibums = new ArrayList<String>(count);
        findViewById(R.id.aibum_relative_select).setVisibility(View.VISIBLE);
        tvSelect = (TextView) findViewById(R.id.aibum_select);
        middle = (TextView) findViewById(R.id.text_title);
        left = (TextView) findViewById(R.id.button_backward);
        right = (TextView) findViewById(R.id.button_forward);
        findViewById(R.id.button_backward).setOnClickListener(this);
        left.setOnClickListener(this);
        if (isNewly) {
            middle.setText("最近照片");
        } else {
            middle.setText(aibum.getName());
        }
        if (aibum != null && aibum.getLists() != null && !aibum.getLists().isEmpty()) {
            PADDING = AndroidUtil.dip2px(AibumGridsActivity.this, 5);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setPadding(0, 0, 0, AndroidUtil.dip2px(this, 40));
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
                            break;
                        case 1:
                            System.out.println("~~~~~ 依然在滑");
                            ImageLoader.getInstance().pause();
                            break;
                        case 2:
                            System.out.println("~~~~~ 惯性滑");
                            ImageLoader.getInstance().resume();
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_backward:
                if (isNewly) {
                    Intent intent = new Intent(this, AibumListActivity.class);
                    intent.putExtra("type", AibumListActivity.TYPE_MULTI_SELECTION);
                    startActivity(intent);
                    finish();
                } else
                    finish();
                break;
            case R.id.left:
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
        ImageLoader.getInstance().resume();
    }

    @Override
    public void onBackPressed() {
        if (isNewly) {
            Intent intent = new Intent(this, AibumListActivity.class);
            intent.putExtra("type", AibumListActivity.TYPE_MULTI_SELECTION);
            startActivity(intent);
            finish();
        } else
            finish();
    }

    @Override
    public void onItemClickListener(View v, int position) {

    }

    public void aibumSelectClick(View view) {
        setResult(RESULT_OK, getIntent().putExtra("images", listAibums));
        finish();
    }

    private class AibumGridAdapter extends RecyclerView.Adapter {
        private OnItemClickListener onItemClickListener;
        private SparseBooleanArray booleanArray;

        public AibumGridAdapter(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            booleanArray = new SparseBooleanArray(10);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(View.inflate(AibumGridsActivity.this, R.layout.layout_aibum_item_grids, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            MediaUtil.displayImageQqQuick("file://" + aibum.getLists().get(position).getPath(), viewHolder.iv);
            viewHolder.cb.setChecked(booleanArray.get(position));
        }

        @Override
        public int getItemCount() {
            return aibum.getLists().size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CheckBox.OnCheckedChangeListener {
            private ImageView iv;
            private CheckBox cb;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                iv = (ImageView) itemView.findViewById(R.id.aibum_iv);
                cb = (CheckBox) itemView.findViewById(R.id.aibum_gou);
                cb.setOnCheckedChangeListener(this);
                cb.setTag(iv);
                int width = (int) (MyApplication.getScreenWidth() * 1.0f / COLUMN_COUNT - AndroidUtil.dip2px(AibumGridsActivity.this, 20));
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(width, width);
                itemView.setLayoutParams(layoutParams);
            }

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, getPosition());
                }
                cb.setChecked(!cb.isChecked());
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImageView iv = (ImageView) buttonView.getTag();
                if (booleanArray.size() == count && isChecked && !booleanArray.get(getPosition())) {
                    buttonView.setChecked(false);
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.aibum_relative_select), "最多只能选" + count + "张哦!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("关闭", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                    return;
                } else if (booleanArray.size() == 10 && !booleanArray.get(getPosition())) {
                    iv.setAlpha(255);
                    return;
                }

                if (!isChecked) {
                    booleanArray.delete(getPosition());
                    iv.setAlpha(255);
                    listAibums.remove(aibum.getLists().get(getPosition()).getPath());
                } else {
                    booleanArray.put(getPosition(), true);
                    iv.setAlpha(125);
                    listAibums.add(aibum.getLists().get(getPosition()).getPath());
                }
                tvSelect.setText("选择(" + booleanArray.size() + ")");
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
//        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
//        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
//            setResult(13);
//            finish();
//        }else if(requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED){
//            initView();
//        }

        if (resultCode != RESULT_OK)
            return;
        if (requestCode == 0x101) {
            String filepath = data.getStringExtra("file");
            Intent intent = new Intent();
            intent.putExtra("file", filepath);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
