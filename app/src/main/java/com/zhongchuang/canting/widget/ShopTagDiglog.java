package com.zhongchuang.canting.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShopTagDiglog extends Dialog {

    private TagContainerLayout.ViewColor mBanViewColor=new TagContainerLayout.ViewColor();
    private TagContainerLayout.ViewColor mDefaultViewColor=new TagContainerLayout.ViewColor();
    private TagContainerLayout.ViewColor mClickViewColor=new TagContainerLayout.ViewColor();
    private List<TagBean> mTagBean=null;

    private TagFactory tagFactory;
    private TextView priceTextView;
    private TextView amountTextView;
    private TextView chooseTextView;
    private AddEditText add;
    private ImageView image;
    private TagContainerLayout colorTagContainer;
    private TagContainerLayout sizeTagContainer;
    private int colorPosition=-1;
    private int sizePosition=-1;
    private TextView sizeLabel;
    private TextView sureButton;
    private onClickListener clickListener;
   private Context context;
    public interface onClickListener {
        void  clickListener(String sku,String cout);
    }
    private ShopTagDiglog(Context context) {

        super(context, R.style.ShopTabDialog);
        this.context=context;
    }

    protected ShopTagDiglog(Context context, int themeResId) {
        super(context, themeResId);
        this.context=context;
    }

    protected ShopTagDiglog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
    }

     private void init(){
         initDialog();
         if (mTagBean==null){
             throw new RuntimeException("NullPointer exception!");
         }

         if (mTagBean.get(0).getTagBean()==null) {
             initOneTag();
         }else {
             sizeLabel.setVisibility(View.VISIBLE);
             sizeTagContainer.setVisibility(View.VISIBLE);
             initTwoTag();
         }


     }
    private String cor;
    private String siz;
    private void initOneTag() {
        chooseTextView.setText(R.string.qxzysfl);
        List<String> titles = new ArrayList<String>();
        for (TagBean tagBean :mTagBean) {
            titles.add(tagBean.getTitle());
        }
        colorTagContainer.setTitles(titles);
        tagFactory=new OneTagLabel(mTagBean,colorTagContainer.getAllChildViews(),mBanViewColor,mDefaultViewColor,mClickViewColor);
        colorTagContainer.setOnTagClickListener(new TagView.OnTagClickListener(){
            @Override
            public void onTagClick(TagView view, int position, String text) {
                TagFactory.ClickStatus clickStatus =tagFactory.onColorTagClick(position);
                if (clickStatus== TagFactory.ClickStatus.CLICK){
                    cor=mTagBean.get(position).getTitle();
                    priceTextView.setText(mTagBean.get(position).getPrice()+"");
                    amountTextView.setText(context.getString(R.string.kc)+mTagBean.get(position).getAmount()+context.getString(R.string.jian));
                    chooseTextView.setText(context.getString(R.string.yxzs)+mTagBean.get(position).getTitle());
                }else if(clickStatus== TagFactory.ClickStatus.UNCLICK) {
                    cor="";
                    chooseTextView.setText(R.string.qxzysfl);
                }
//                EventBus.getDefault().post(chooseTextView.getText());
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }

    private void initTwoTag() {
        chooseTextView.setText(R.string.qxzyscm);
        List<String> colorTitles = new ArrayList<String>();
        List<String> sizeTitles=new ArrayList<String>();
        for (TagBean colorTagBean :mTagBean) {
            colorTitles.add(colorTagBean.getTitle());
        }
        for (TagBean sizeTagBean :mTagBean.get(0).getTagBean()) {
            sizeTitles.add(sizeTagBean.getTitle());
        }

        colorTagContainer.setTitles(colorTitles);
        sizeTagContainer.setTitles(sizeTitles);
        tagFactory=new TwoTagLabel(mTagBean, colorTagContainer.getAllChildViews(), sizeTagContainer.getAllChildViews(),mBanViewColor,mDefaultViewColor,mClickViewColor);
        colorTagContainer.setOnTagClickListener(new TagView.OnTagClickListener(){
            @Override
            public void onTagClick(TagView view, int position, String text) {
                TagFactory.ClickStatus clickStatus =tagFactory.onColorTagClick(position);
                if (clickStatus==TagFactory.ClickStatus.CLICK){
                    cor=mTagBean.get(position).getTitle();
                    colorPosition=position;
                    if (sizePosition==-1){
                        chooseTextView.setText(context.getString(R.string.yxzs)+mTagBean.get(position).getTitle()+context.getString(R.string.qxzcm));
                    }else{
                        Glide.with(context).load(StringUtil.changeUrl(mTagBean.get(position).getUrl())).asBitmap().placeholder(R.drawable.moren1).into(image);
                        priceTextView.setText(mTagBean.get(position).getTagBean().get(sizePosition).getPrice()+"");
                        amountTextView.setText(context.getString(R.string.kc)+mTagBean.get(position).getTagBean().get(sizePosition).getAmount()+context.getString(R.string.jian));
                        chooseTextView.setText(context.getString(R.string.yxzs)+mTagBean.get(position).getTitle()+" "+mTagBean.get(position).getTagBean().get(sizePosition).getTitle());
                    }
                }else if(clickStatus==TagFactory.ClickStatus.UNCLICK){
                    cor="";
                    colorPosition=-1;
                    if (sizePosition==-1){
                        chooseTextView.setText(R.string.qxzyscm);
                    }else{
                        chooseTextView.setText(context.getString(R.string.yxzs)+mTagBean.get(position).getTagBean().get(sizePosition).getTitle()+ context.getString(R.string.qxzysfl));
                    }
                }
//                EventBus.getDefault().post(chooseTextView.getText());
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });


        sizeTagContainer.setOnTagClickListener(new TagView.OnTagClickListener(){
            @Override
            public void onTagClick(TagView view, int position, String text) {
                TagFactory.ClickStatus clickStatus =tagFactory.onSizeTagClick(position);
                if (clickStatus==TagFactory.ClickStatus.CLICK){
                    sizePosition=position;
                    siz=mTagBean.get(0).getTagBean().get(position).getTitle();
                    if (colorPosition==-1){
                        chooseTextView.setText(context.getString(R.string.yxzs)+mTagBean.get(0).getTagBean().get(position).getTitle()+context.getString(R.string.qxzysfl));

                    }else{
                        priceTextView.setText(mTagBean.get(colorPosition).getTagBean().get(position).getPrice()+"");
                        amountTextView.setText(context.getString(R.string.kc)+mTagBean.get(colorPosition).getTagBean().get(position).getAmount()+context.getString(R.string.jian));
                        chooseTextView.setText(context.getString(R.string.yxzs)+mTagBean.get(colorPosition).getTitle()+" "+mTagBean.get(colorPosition).getTagBean().get(position).getTitle());
                    }
                }else if(clickStatus==TagFactory.ClickStatus.UNCLICK){
                    sizePosition=-1;
                    siz="";
                    if (colorPosition==-1){
                        chooseTextView.setText(context.getString(R.string.kc));
                    }else{
                        chooseTextView.setText(context.getString(R.string.yxzs)+mTagBean.get(colorPosition).getTitle()+ context.getString(R.string.qxzcm));
                    }
                }
//                EventBus.getDefault().post(chooseTextView.getText());
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

    }


    private void initDialog() {
        setContentView(R.layout.shop_tab_dialog);
        //设置返回键可撤销
        setCancelable(true);
        //设置点击非Dialog区可撤销
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        priceTextView = findViewById(R.id.price);
        amountTextView = findViewById(R.id.amount);
        chooseTextView = findViewById(R.id.choose);
        add = findViewById(R.id.add);
        image = findViewById(R.id.image);
//        colorTagContainer = (TagContainerLayout) findViewById(R.id.color_tag_container);
//        sizeTagContainer = (TagContainerLayout) findViewById(R.id.size_tag_container);
//        sizeLabel = (TextView) findViewById(R.id.size_label);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.sure_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (clickListener!=null){
                     if(TextUtil.isEmpty(cor)||TextUtil.isEmpty(siz)){
                         return;
                     }else {
                         clickListener.clickListener(map.get(cor+"#"+siz),add.getCout());
                     }
                 }
            }
        });

    }
    private Map<String,String> map;
    public void setData(Map<String,String> map){
        this.map=map;
    }
    public void setOnClickListener(onClickListener listener){
         this.clickListener = listener;
    }


    public static class Builder{
        private ShopTagDiglog shopTagDiglog;
        public Builder(Context context){
            shopTagDiglog=new ShopTagDiglog(context);
        }
        public Builder setBanViewColor(TagContainerLayout.ViewColor viewColor){
            shopTagDiglog.mBanViewColor=viewColor;
            return this;
        }
        public Builder setDefaultViewColor(TagContainerLayout.ViewColor viewColor){
            shopTagDiglog.mDefaultViewColor=viewColor;
            return this;
        }
        public Builder setClickViewColor(TagContainerLayout.ViewColor viewColor){
            shopTagDiglog.mClickViewColor=viewColor;
            return this;
        }
        public Builder setTagBean(List<TagBean> tagBean){
            shopTagDiglog.mTagBean=tagBean;
            return this;
        }



        public ShopTagDiglog create(){
            shopTagDiglog.init();
            return shopTagDiglog;
        }

    }
}
