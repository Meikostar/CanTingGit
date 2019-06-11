package com.zhongchuang.canting.allive.editor.effects.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.editor.effects.control.EffectInfo;
import com.zhongchuang.canting.allive.editor.effects.control.OnItemClickListener;
import com.zhongchuang.canting.allive.editor.effects.control.OnItemLongClickListener;
import com.zhongchuang.canting.allive.editor.effects.control.OnItemTouchListener;
import com.zhongchuang.canting.allive.editor.effects.control.SpaceItemDecoration;
import com.zhongchuang.canting.allive.editor.msg.Dispatcher;
import com.zhongchuang.canting.allive.editor.msg.body.ClearAnimationFilter;
import com.zhongchuang.canting.allive.editor.msg.body.LongClickAnimationFilter;
import com.zhongchuang.canting.allive.editor.msg.body.LongClickUpAnimationFilter;
import com.zhongchuang.canting.allive.editor.util.Common;


public class AnimationFilterChooserItem extends Fragment
        implements OnItemLongClickListener, OnItemClickListener, OnItemTouchListener {
    private RecyclerView mListView;
    private FilterAdapter mFilterAdapter;

    @Override
    public boolean onItemClick(EffectInfo effectInfo, int index) {
        if(index == 0) {
            Dispatcher.getInstance().postMsg(new ClearAnimationFilter());
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aliyun_svideo_filter_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = view.findViewById(R.id.effect_list_filter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mListView.setLayoutManager(layoutManager);
        mFilterAdapter = new FilterAdapter(getContext());
        mFilterAdapter.setOnItemClickListener(this);
        mFilterAdapter.setOnItemLongClickListener(this);
        mFilterAdapter.setOnItemTouchListener(this);
        mFilterAdapter.setDataList(Common.getAnimationFilterList());
//        mFilterAdapter.setSelectedPos(mEditorService.getEffectIndex(UIEditorPage.FILTER_EFFECT));
        mListView.setAdapter(mFilterAdapter);
        mListView.addItemDecoration(new SpaceItemDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.list_item_space)));
//        mListView.scrollToPosition(mEditorService.getEffectIndex(UIEditorPage.FILTER_EFFECT));
    }

    @Override
    public boolean onItemLongClick(EffectInfo effectInfo, int index) {
        if(index > 0) {
            Dispatcher.getInstance().postMsg(new LongClickAnimationFilter.Builder()
                    .effectInfo(effectInfo)
                    .index(index)
                    .build());
        }
        return false;
    }

    @Override
    public void onTouchEvent(int motionEvent, int index, EffectInfo info) {
        Dispatcher.getInstance().postMsg(new LongClickUpAnimationFilter.Builder()
                .effectInfo(info)
                .index(index)
                .build());
    }
}
