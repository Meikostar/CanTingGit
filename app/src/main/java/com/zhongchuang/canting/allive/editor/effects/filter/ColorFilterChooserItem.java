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
import com.zhongchuang.canting.allive.editor.effects.control.SpaceItemDecoration;
import com.zhongchuang.canting.allive.editor.msg.Dispatcher;
import com.zhongchuang.canting.allive.editor.msg.body.SelectColorFilter;
import com.zhongchuang.canting.allive.editor.util.Common;

public class ColorFilterChooserItem extends Fragment implements OnItemClickListener {
    private RecyclerView mListView;
    private FilterAdapter mFilterAdapter;

    @Override
    public boolean onItemClick(EffectInfo effectInfo, int index) {
        Dispatcher.getInstance().postMsg(new SelectColorFilter.Builder()
                .effectInfo(effectInfo)
                .index(index).build());
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aliyun_svideo_filter_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (RecyclerView) view.findViewById(R.id.effect_list_filter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mListView.setLayoutManager(layoutManager);
        mFilterAdapter = new FilterAdapter(getContext());
        mFilterAdapter.setOnItemClickListener(this);
        mFilterAdapter.setDataList(Common.getColorFilterList());
//        mFilterAdapter.setSelectedPos(mEditorService.getEffectIndex(UIEditorPage.FILTER_EFFECT));
        mListView.setAdapter(mFilterAdapter);
        mListView.addItemDecoration(new SpaceItemDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.list_item_space)));
//        mListView.scrollToPosition(mEditorService.getEffectIndex(UIEditorPage.FILTER_EFFECT));
    }
}
