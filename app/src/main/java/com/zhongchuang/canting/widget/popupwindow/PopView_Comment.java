package com.zhongchuang.canting.widget.popupwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.widget.ClearEditText;


/**
 * 评论输入框
 *
 */
public class PopView_Comment extends BasePopView {

    private ClearEditText et_comment;
    private Button btn_send_comment;

    public PopView_Comment(Activity activity) {
        super(activity);
    }
    public void clear(){
        et_comment.setText("");
    }
    @Override
    protected View initPopView(LayoutInflater infalter) {
        View popView = infalter.inflate(R.layout.view_add_goods_new_comment, null);
        et_comment = popView.findViewById(R.id.et_comment);
        et_comment.setOnClearEditTextListener(new ClearEditText.ClearEditTextListener() {
            @Override
            public void afterTextChanged4ClearEdit(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    btn_send_comment.setEnabled(false);
                }else{
                    btn_send_comment.setEnabled(true);
                }
            }

            @Override
            public void changeText(CharSequence s) {

            }
        });
        btn_send_comment = popView.findViewById(R.id.btn_send_comment);
        btn_send_comment.setOnClickListener(this);
        return popView;
    }
    public void setTextHints(String text){
        et_comment.setHint(text);
    }
    public void initPopUpView(){
        pop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);
    }

    public void showPopView(String comment) {
        if(pop.isShowing()){
            return;
        }
        et_comment.setText(comment);
        //防止虚拟软键盘被弹出菜单遮住
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.showPopView();
        showKeyBoard();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_send_comment:
                dismiss();
                String commentStr = et_comment.getText().toString();

                if (lister != null) {
                    lister.chooseDeviceConfig(commentStr);
                }
                break;
        }
    }

    private OnPop_CommentListener lister;

    public interface OnPop_CommentListener {
        void chooseDeviceConfig(String commentStr);
    }

    public void setOnPop_CommentListenerr(OnPop_CommentListener lister) {
        this.lister = lister;
    }
}
