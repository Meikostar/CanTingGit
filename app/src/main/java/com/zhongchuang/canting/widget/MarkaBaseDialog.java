package com.zhongchuang.canting.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;


public class MarkaBaseDialog extends Dialog {
	private CharSequence message;
	@Override
	public void onBackPressed() {

	}

	public void setMessage(CharSequence message) {
		this.message = message;
	}

	public String getMessage(){
		return message.toString();
	}

	public MarkaBaseDialog(Context context, int theme) {
		super(context, theme);
	}

	public MarkaBaseDialog(Context context) {
		super(context);
	}

	public static class Builder {
		private Context context;
		private String title;
		private CharSequence content;
		private String leftButtonText;
		private String rightButtonText;
		private Button mButtonLeft;
		private Button mButtonRight;
		private LinearLayout mLayoutContent;
		private LinearLayout mLayoutMessage;
		private View mViewLine;
		protected OnClickListener onClickListener;
		private TextView mTexViewTitle;
		private View mView;
		private View messageView;
		private int leftColorId;
		private int rightColorId;
		private int layoutId;
		private int w=285;
		private int h=300;
		private boolean haveTitle;
		public static final int LEFT_BUTTON= DialogInterface.BUTTON_NEGATIVE;
		public static final int RIGHT_BUTTON= DialogInterface.BUTTON_POSITIVE;
		private DialogType type = DialogType.TYPE_NORMAL;

		public enum DialogType{
			TYPE_NORMAL,TYPE_DELETE
		}

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		public Builder setMessage(CharSequence content){
			this.content=content;
			return this;
		}

		public Builder setButtonType(DialogType type){
			this.type = type;
			return this;
		}

		public Builder haveTitle(boolean haveTitle){
			this.haveTitle = haveTitle;
			return this;
		}

		public Builder setLeftButtonText(String leftButtonText){
			this.leftButtonText=leftButtonText;
			return this;
		}
		public Builder setOnClickListener(OnClickListener listener) {
			this.onClickListener = listener;
			return this;
		}

		public Builder setRightButtonText(String rightButtonText){
			this.rightButtonText=rightButtonText;
			return this;
		}

		public Builder setLeftButtonColor(int color){
			this.leftColorId=color;
			return this;
		}
		public Builder setRightButtonColor(int color){
			this.rightColorId=color;
			return this;
		}

		public Builder setLayoutID(int id){
			this.layoutId=id;
			this.mView = null;
			return this;
		}

		public Builder setMessageView(View view){
			this.messageView = view;
			return this;
		}

		public Builder setLayoutWH(int wPx,int hPx){
			this.w=wPx;
			this.h=hPx;
			return this;
		}

		@SuppressLint("Override")
		public MarkaBaseDialog create() {
			if(context==null) return null;
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final MarkaBaseDialog dialog = new MarkaBaseDialog(context, R.style.Dialog);
			dialog.setMessage(content);
			if (mView==null && layoutId!=0) {
				mView = inflater.inflate(layoutId, null);
			}else if (mView==null){
				mView = inflater.inflate(R.layout.dialog_base_two_button,null);
			}
			dialog.setContentView(mView, new LayoutParams(w, h));
			mButtonLeft = mView.findViewById(R.id.but_left);
			mButtonRight= mView.findViewById(R.id.but_right);
			mTexViewTitle= mView.findViewById(R.id.tv_title);
			mLayoutContent = mView.findViewById(R.id.ll_content);
			mViewLine = mView.findViewById(R.id.view_line);
			mLayoutMessage = mView.findViewById(R.id.ll_message);


			setOnLeftListener(dialog);
			if(mButtonRight!=null){
				mButtonRight.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						rightClickListener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
						if(onClickListener!=null){
							onClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}

					}
				});
			}

			if (haveTitle){
			//	mLayoutContent.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_bg_common_dialog_content));
				mTexViewTitle.setVisibility(View.VISIBLE);
			}else {
				mTexViewTitle.setVisibility(View.GONE);
			//	mLayoutContent.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_bg_common_dialog_content_notitle));
			}

			if(!TextUtils.isEmpty(title)){
				mTexViewTitle.setText(title);
			}

			if (messageView!=null){
				mLayoutMessage.addView(messageView);
				TextView mTexViewContent = mView.findViewById(R.id.tv_content);
				mTexViewContent.setVisibility(View.GONE);
			}else {
				if (content != null) {
					TextView mTexViewContent = mView.findViewById(R.id.tv_content);
					mTexViewContent.setVisibility(View.VISIBLE);
					mTexViewContent.setText(content);
				}
			}

			if(!TextUtils.isEmpty(leftButtonText)&&mButtonLeft!=null){
				mButtonLeft.setVisibility(View.VISIBLE);
				if (TextUtils.isEmpty(rightButtonText)){
					mViewLine.setVisibility(View.GONE);
				}else {
					mViewLine.setVisibility(View.VISIBLE);
				}
				mButtonLeft.setText(leftButtonText);
			}else if (mButtonLeft!=null){
				mButtonLeft.setVisibility(View.GONE);
				mViewLine.setVisibility(View.GONE);
				if(rightButtonText!=null&&mButtonRight!=null){
					//mButtonRight.setBackgroundResource(R.drawable.selector_click_dialog);
				}
			}

			if (type == DialogType.TYPE_NORMAL){
			//	mButtonRight.setTextColor(ContextCompat.getColor(context,R.color.color_primary_33));
			}else if (type == DialogType.TYPE_DELETE){
			//	mButtonRight.setTextColor(ContextCompat.getColor(context,R.color.color_red_f83333));
			}

			if(rightButtonText!=null&&mButtonRight!=null){
				mButtonRight.setText(rightButtonText);
				mButtonRight.setVisibility(View.VISIBLE);
				if (TextUtils.isEmpty(leftButtonText)){
					mViewLine.setVisibility(View.GONE);
				}else {
					mViewLine.setVisibility(View.VISIBLE);
				}
			}else {
				mButtonRight.setVisibility(View.GONE);
				mViewLine.setVisibility(View.GONE);
				if(mButtonLeft!=null&&mButtonLeft!=null){
					//mButtonLeft.setBackgroundResource(R.drawable.selector_click_dialog);
				}
			}


			if(leftColorId!=0&&mButtonLeft!=null){
				mButtonLeft.setTextColor(context.getResources().getColor(leftColorId));
			}
			if(rightColorId!=0&&mButtonRight!=null){
				mButtonRight.setTextColor(context.getResources().getColor(rightColorId));
			}
			dialog.setContentView(mView);
			return dialog;
		}

		private void setOnLeftListener(final MarkaBaseDialog dialog) {
			if(mButtonLeft!=null){
				mButtonLeft.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(onClickListener!=null)
						onClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
					}
				});
			}
		}
	}

	@Override
	public void show() {
		super.show();
		setCanceledOnTouchOutside(true);
	}

	public void showCanceledOnTouchOutside(boolean cancel){
		show();
		setCanceledOnTouchOutside(cancel);
	}
}
