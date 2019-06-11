package com.zhongchuang.canting.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhongchuang.canting.R;

import java.util.Timer;
import java.util.TimerTask;

/***
 * 功能描述:搜索框
 * 作者:xiongning
 * 时间:2017/01/09
 * 版本:1.0
 ***/
public class GoodsSearchView extends LinearLayout {

    private Context mContext;

    private YunShlEditText mYunShlEditText;

    private ImageView iv_clean;
    private ImageView back;

    private TextView tv_cancel;

    private OnSearchBoxClickListener mOnSearchBoxClickListener;

    public void registerListener(OnSearchBoxClickListener mOnSearchBoxClickListener){

        this.mOnSearchBoxClickListener=mOnSearchBoxClickListener;

    }

    public interface OnSearchBoxClickListener{
        void onClean();
        void onCancle();
        void finishs();
        void onKeyWordsChange(String keyWords);
    }

    public GoodsSearchView(Context context) {
        super(context);
        init(context);
    }

    public GoodsSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoodsSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public String getSearchKeyWords(){

        return mYunShlEditText.getTextString();
    }
   public void setYunEditHint(String hint){
       mYunShlEditText.setHint(hint);
   }
    public void setKeyWords(String keyWords){

        mYunShlEditText.setText(keyWords);

    }
    public void requestFocuse(){

        mYunShlEditText.setFocusable(true);

        mYunShlEditText.setFocusableInTouchMode(true);

        mYunShlEditText.requestFocus();

        Timer timer = new Timer();

        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity

            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager)mYunShlEditText.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(mYunShlEditText, 0);
            }

        }, 800);
    }

    private void init(final Context context){

        this.mContext=context;

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.view_goods_search_box,this);

        mYunShlEditText= view.findViewById(R.id.edt_search_box);

        iv_clean= view.findViewById(R.id.iv_clean);
        back= view.findViewById(R.id.iv_back);

        tv_cancel= view.findViewById(R.id.tv_cancel);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {



                if(mOnSearchBoxClickListener!=null)
                    mOnSearchBoxClickListener.finishs();
            }
        });
        iv_clean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                mYunShlEditText.setText("");

                if(mOnSearchBoxClickListener!=null)
                    mOnSearchBoxClickListener.onClean();
            }
        });

        tv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnSearchBoxClickListener!=null)
                    mOnSearchBoxClickListener.onCancle();
            }
        });

        mYunShlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>0){

                    iv_clean.setVisibility(View.VISIBLE);

                }else {

                    iv_clean.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(mOnSearchBoxClickListener!=null)
                    mOnSearchBoxClickListener.onKeyWordsChange(editable.toString());

            }
        });

    }
}
