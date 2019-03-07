package com.zhongchuang.canting.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2016-09-27
 * 版本:1.0
 ***/
public class YunShlEditText extends EditText {
    public static final int CATEGORY_PHONE = 1;
    public static final int CATEGORY_EMAIL = 2;
    public static final int CATEGORY_CHARACTER = 3;// 只允许字母、数字和汉字
    public static final int TYPE_NO_COMMA = 4;//禁止输入特殊字符
    public static final int TYPE_NUMBER = 5;//输入数字
    public static final int TYPE_NUMBER_DECIMAL = 6;//输入有小数点数字
    public static final int CATEGORY_NUMBER_ = 7;//输入字母，下划线，数字
    public static final int NO_FILTER = 8;//输入字母，下划线，数字

    private Editable mEditable;
    private boolean interGet = false;

    private Context mContext;
    private String regularStr;
    private int inputType;
    private List<OnFocusChangeListener> mFocusChangeListeners;


    public void setRegularExcept(String regularStr){
        this.regularStr = regularStr;
    }

    public void resetRegular(){
        this.regularStr = null;
    }

    public void setType(int type){
        inputType = type;
        switch (type){
            case CATEGORY_PHONE:
                regularStr = "[^0-9-]";
                break;
            case CATEGORY_EMAIL:
                regularStr = "[^0-9a-zA-Z-_\\.@]";
                break;
            case CATEGORY_CHARACTER:
                regularStr = "[^\\.a-zA-Z0-9\u4E00-\u9FA5]";// 只允许字母、数字和汉字
                break;
            case CATEGORY_NUMBER_:
                regularStr = "[^\\w]";
                break;
            case TYPE_NO_COMMA:
                regularStr = "[,，']";
                break;
            case TYPE_NUMBER:
                regularStr = "[^0-9\\+-]";
                break;
            case TYPE_NUMBER_DECIMAL:
                regularStr = "[^0-9\\.\\+-]";
                break;
            default:
                regularStr = null;
                break;
        }
    }

    public YunShlEditText(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public YunShlEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public YunShlEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void addFocusChangeListener(OnFocusChangeListener listener){
        if (mFocusChangeListeners == null) {
            mFocusChangeListeners = new ArrayList<>();
        }
        if (listener!=null){
            mFocusChangeListeners.add(listener);
        }
    }

    public void removeFocusChangeListener(OnFocusChangeListener listener){
        if (listener!=null && mFocusChangeListeners!=null){
            mFocusChangeListeners.remove(listener);
        }
    }

    private void init() {
        if (inputType == TYPE_NUMBER){
            setInputType(InputType.TYPE_CLASS_NUMBER);
        }else if (inputType == TYPE_NUMBER_DECIMAL){
            setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_CLASS_NUMBER);
        }
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.e("beforeTextChanged", "s:"+s+"start:"+start+"count:"+count);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //cou = before + count;
                interGet = true;
                int cursorStart = getSelectionStart();
                String editable = YunShlEditText.this.getText().toString();
                if (editable!=null && editable.length()>0){
                    cursorStart = editable.length() - cursorStart;
                }
                String str = stringFilter(editable); //过滤特殊字符
                str = specialFilter(str);
                if(!editable.equals(str)) {
                    YunShlEditText.this.setText(str);
//                    YunShlEditText.this.setSelection(YunShlEditText.this.length());
                    if (cursorStart>=0){
                        YunShlEditText.this.setSelection(YunShlEditText.this.length()-cursorStart>=0?YunShlEditText.this.length()-cursorStart:0);
                    }else {
                        YunShlEditText.this.setSelection(YunShlEditText.this.length());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEditable = s;
            }
        });
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFocusChangeListeners!=null){
                    for (OnFocusChangeListener listener : mFocusChangeListeners){
                        listener.onFocusChange(v,hasFocus);
                    }
                }
            }
        });
    }


    public String getTextString(){
        return super.getText().toString().trim();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }


    public static String stringFilter(String str)throws PatternSyntaxException {
        try{
            Pattern emoji = Pattern.compile(
                    "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher m = emoji.matcher(str);
            return m.replaceAll("").replaceAll("'","");
        }catch (Exception e){
            e.printStackTrace();
            return str;
        }
    }

    public String specialFilter(String str) {
        if (regularStr!=null && !("".equals(regularStr))){
            Pattern p = Pattern.compile(regularStr);
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

}
