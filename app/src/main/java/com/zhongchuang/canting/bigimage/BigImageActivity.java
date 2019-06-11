package com.zhongchuang.canting.bigimage;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;


public class BigImageActivity extends BaseActivity {
    @BindView(R.id.viewpager_image)
    PhotoViewPager photoViewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.view_temp)
    View viewTemp;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_rtc)
    Button btnRtc;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.title)
    RelativeLayout title;
    private boolean isNetUrl = true;

    private TabLayout.Tab[] tabs;
    private String[] images;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigimage);
        ButterKnife.bind(this);
        getData();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        MyAdapter adapter = new MyAdapter(images);
        photoViewPager.setAdapter(adapter);
        photoViewPager.setCurrentItem(page);
        tabLayout.setupWithViewPager(photoViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabs = new TabLayout.Tab[images.length];
        for (int i = 0; i < images.length; i++) {
            tabs[i] = tabLayout.getTabAt(i);
            tabs[i].setCustomView(getBottomView(i, page, R.drawable.pointer_selector));
        }
    }

    private void initView() {


    }

    private void getData() {
        //图片路径
        images = getIntent().getStringArrayExtra("images");
        //当前索引
        page = getIntent().getIntExtra("page", 0);
        //是否是网络图片
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private View getBottomView(final int index, final int page, int drawableRes) {
        View view = getLayoutInflater().inflate(R.layout.layout_big_photo_point, null);
        ImageView button = view.findViewById(R.id.iv_foot_point);
        if (index == page) {
            tabs[index].select();
        }
        button.setBackground(getDrawable(drawableRes));
        if (isCompatible(Build.VERSION_CODES.LOLLIPOP)) {
            button.setStateListAnimator(null);
        }
        return view;
    }


    protected boolean isCompatible(int apiLevel) {
        return Build.VERSION.SDK_INT >= apiLevel;
    }

    private class MyAdapter extends PagerAdapter {
        private String[] images;

        public MyAdapter(String[] images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public PhotoView instantiateItem(ViewGroup container, int position) {
            String image = images[position];
            PhotoView photoView = new PhotoView(BigImageActivity.this);
            Glide.with(BigImageActivity.this).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.moren).error(R.drawable.moren).into(photoView);


            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((PhotoView) object);
        }
    }
}
