package com.ningsheng.jietong.Fragmen;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ningsheng.jietong.Adapter.CallFaceValueAdapter;
import com.ningsheng.jietong.Adapter.ItemDivider;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.View.GridLayoutManagerInScr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhushunqing on 2016/5/5.
 */
public class KeepCallFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<String> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_keep_call, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_keep_call_facevalue);
        GridLayoutManagerInScr gm = new GridLayoutManagerInScr(getContext(), 3);
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        CallFaceValueAdapter adapter = new CallFaceValueAdapter(getContext(), list);
        recyclerView.setLayoutManager(gm);
//        recyclerView.addItemDecoration(new ItemDivider(getContext(), R.drawable.recycler_line));
        recyclerView.setAdapter(adapter);
    }
}
