package com.zhongchuang.canting.widget;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;

/**
 * Created by Administrator on 2018/6/22.
 */


public class AddEditText extends LinearLayout {

    private ClearEditText mEditText;
    private TextView bAdd;
    private TextView bReduce;

    //这里的构造一定是两个参数。
    public AddEditText(final Context ctxt, AttributeSet attrs) {
        super(ctxt,attrs);
    }


    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.myedittext, this);
        init_widget();
        addListener();

    }

    public void init_widget(){


        mEditText = findViewById(R.id.edit_cout);
        bAdd = findViewById(R.id.tv_add);
        bReduce = findViewById(R.id.tv_clear);
        mEditText.setShow(false);
        mEditText.setText("1");
        mEditText.setOnClearEditTextListener(new ClearEditText.ClearEditTextListener() {
            @Override
            public void afterTextChanged4ClearEdit(Editable s) {

            }

            @Override
            public void changeText(CharSequence s) {
               listener.change(s.toString());
            }
        });
    }
    public interface  ChangeListener{
        void change(String name);
    }
    public void setListener(ChangeListener listener){
        this.listener=listener;
    }
    public ChangeListener listener;
    public void addListener(){
        bAdd.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                int num = Integer.valueOf(mEditText.getText().toString());
                num++;
                mEditText.setText(Integer.toString(num));
                listener.change(Integer.toString(num));
            }
        });

        bReduce.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int num = Integer.valueOf(mEditText.getText().toString());
                if(num>1){
                    num--;
                }
                mEditText.setText(Integer.toString(num));
                listener.change(Integer.toString(num));
            }
        });
    }
    public String getCout(){
        return mEditText.getText().toString().trim();
    }
    public void setTexts(String content){
        mEditText.setText(content);
    }
}
