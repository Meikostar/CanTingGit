package com.zhongchuang.canting.widget;
import android.view.SoundEffectConstants;

import java.util.List;



public class OneTagLabel extends   TagFactory<TagView>  {
    private final List<TagBean> mTagBean;
    private final List<TagView> mChildViews;
    private final TagContainerLayout.ViewColor mBanViewColor;
    private final TagContainerLayout.ViewColor mDefaultViewColor;
    private final TagContainerLayout.ViewColor mClickViewColor;


    public  OneTagLabel(List<TagBean> tagBean, List<TagView> allChildViews, TagContainerLayout.ViewColor banViewColor, TagContainerLayout.ViewColor defaultViewColor, TagContainerLayout.ViewColor clickViewColor){

        this.mChildViews=allChildViews;
        this.mBanViewColor=banViewColor;
        this.mDefaultViewColor=defaultViewColor;
        this.mClickViewColor=clickViewColor;
        this.mTagBean =tagBean;
        initTags();
    }

    private void initTags() {
        for (int i=0;i<mChildViews.size();i++){
            if (mTagBean.get(i).getAmount()==0){
                mChildViews.get(i).setTagViewColor(mBanViewColor);
                mChildViews.get(i).setEnabled(false);
            }else{
                mChildViews.get(i).setTagViewColor(mDefaultViewColor);
            }
            mChildViews.get(i).setText(mTagBean.get(i).getTitle());
            mChildViews.get(i).postInvalidate();
        }

    }

    @Override
    public  ClickStatus onColorTagClick(int position) {
        TagView view= mChildViews.get(position);
        if (view.getEnabled()==true){
            view.playSoundEffect(SoundEffectConstants.CLICK);
            if (!view.getIsClick()){
                for (TagView tagView : mChildViews) {
                    if (tagView.getEnabled()==true) {
                        tagView.setTagViewColor(mDefaultViewColor);
                        tagView.setIsClick(false);

                    }
                }
                view.setTagViewColor(mClickViewColor);
                view.setIsClick(true);

                return ClickStatus.CLICK;
            }else{
                view.setTagViewColor(mDefaultViewColor);
                view.setIsClick(false);

                return ClickStatus.UNCLICK;
            }
        }
        return ClickStatus.BAN;
    }

    @Override
    public ClickStatus onSizeTagClick(int position) {
       return null;
    }

    @Override
    public  TagView getClickObject() {
        return  null;
    }
}
