package com.zhongchuang.canting.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.ZXingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineCodeActivity extends BaseTitle_Activity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.tv_content)
    TextView tv_content;

    public View addContentView() {
        return getLayoutInflater().inflate(R.layout.activity_mine_code, null);
    }
    private String state;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        state=getIntent().getStringExtra("state");
        name=getIntent().getStringExtra("name");
        initViews();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(TextUtil.isNotEmpty(state)){
            tvSearch.setText(R.string.qewm);
            tv_content.setText(R.string.syssmdqew);
            content=name+ ","+ state+",@@!!##$$%%qwertyuioplkjhgfdsazxcvbnm";
        }else {
            content=SpUtil.getName(MineCodeActivity.this) + ","+ SpUtil.getUserInfoId(MineCodeActivity.this)+ ","+ SpUtil.getName(MineCodeActivity.this);
        }
    }

    @Override
    public boolean isTitleShow() {
        return false;
    }
    private String content;
    public void initViews() {



        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    Bitmap myBitmap = Glide.with(MineCodeActivity.this)
                            .load(SpUtil.getAvar(MineCodeActivity.this))
                            .asBitmap() //必须
                            .centerCrop()
                            .into(500, 500)
                            .get();
                    bitmap = ZXingUtils.createImage(content, 400, 400, myBitmap);
                    final Bitmap finalBitmap = bitmap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivCode.setImageBitmap(finalBitmap);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = ZXingUtils.createImage(content, 400, 400, null);
                            ivCode.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        }).start();


    }


}
