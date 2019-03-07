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

import com.zhongchuang.canting.R;;

import java.util.Timer;
import java.util.TimerTask;

/***
 * 功能描述:搜索框
 * 作者:xiongning
 * 时间:2017/01/09
 * 版本:1.0
 ***/
public class GoodsSearchViews extends LinearLayout {

    private Context mContext;

    private YunShlEditText mYunShlEditText;

    private ImageView iv_clean;
    private ImageView back;
    private ImageView iv_change;


    private OnSearchBoxClickListener mOnSearchBoxClickListener;

    public void registerListener(OnSearchBoxClickListener mOnSearchBoxClickListener){

        this.mOnSearchBoxClickListener=mOnSearchBoxClickListener;

    }

    public interface OnSearchBoxClickListener{
        void onClean();
        void onCancle();
        void finishs(int type);
        void onKeyWordsChange(String keyWords);
    }

    public GoodsSearchViews(Context context) {
        super(context);
        init(context);
    }

    public GoodsSearchViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoodsSearchViews(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public void cancelFocuse(){

        mYunShlEditText.setFocusable(false);

        mYunShlEditText.clearFocus();

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

        View view = inflater.inflate(R.layout.view_goods_searchs,this);

        mYunShlEditText=(YunShlEditText)view.findViewById(R.id.edt_search_box);

        iv_clean=(ImageView)view.findViewById(R.id.iv_clean);
        back=(ImageView)view.findViewById(R.id.back);
        iv_change=(ImageView)view.findViewById(R.id.iv_change);

        iv_change.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mOnSearchBoxClickListener!=null)
                    mOnSearchBoxClickListener.onCancle();
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mOnSearchBoxClickListener!=null)
                    mOnSearchBoxClickListener.finishs(1);
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

        mYunShlEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSearchBoxClickListener!=null)
                    mOnSearchBoxClickListener.finishs(2);
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

//        requestFocuse();
    }
}
