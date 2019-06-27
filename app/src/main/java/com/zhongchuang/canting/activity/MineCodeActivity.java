package com.zhongchuang.canting.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.easeui.widget.NineGridImageView;
import com.zhongchuang.canting.easeui.widget.NineGridImageViewAdapter;
import com.zhongchuang.canting.easeui.widget.WebImageView;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.ZXingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineCodeActivity extends BaseTitle_Activity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_imgs)
    ImageView iv_imgs;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.group_avatar)
    NineGridImageView groupAvatar;


    public View addContentView() {
        return getLayoutInflater().inflate(R.layout.activity_mine_code, null);
    }

    private String state;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        state = getIntent().getStringExtra("state");
        name = getIntent().getStringExtra("name");
        initViews();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (TextUtil.isNotEmpty(state)) {
            tvSearch.setText(R.string.qewm);
            tv_content.setText(R.string.syssmdqew);
            content = name + "," + state + ",@@!!##$$%%qwertyuioplkjhgfdsazxcvbnm";
        } else {
            content = SpUtil.getName(MineCodeActivity.this) + "," + SpUtil.getUserInfoId(MineCodeActivity.this) + "," + SpUtil.getName(MineCodeActivity.this);
        }
         if(CanTingAppLication.headimage!=null&&CanTingAppLication.headimage.size()>-1){
             groupAvatar.setAdapter(new NineGridImageViewAdapter() {
                 @Override
                 protected void onDisplayImage(Context context, WebImageView imageView, Object o) {
                     if (TextUtils.isEmpty((String) o)) {
                         imageView.setImageResource(R.drawable.dingdantouxiang);
                         return;
                     }
                     Glide.with(context).load((String)o).asBitmap().placeholder(R.drawable.dingdantouxiang).into(imageView);
//                imageView.setImageWithURL(context,(String)o,R.drawable.dingdantouxiang);
                 }
             });
             groupAvatar.setImagesData(CanTingAppLication.headimage);
         }else {
             groupAvatar.setVisibility(View.GONE);
             Glide.with(this).load(SpUtil.getAvar(this)).asBitmap().placeholder(R.drawable.dingdantouxiang).into(iv_imgs);

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
