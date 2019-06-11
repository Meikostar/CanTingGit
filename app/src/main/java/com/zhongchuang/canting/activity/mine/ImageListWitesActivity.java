package com.zhongchuang.canting.activity.mine;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.base.StatusBarUtil;
import com.zhongchuang.canting.been.IMG;
import com.zhongchuang.canting.utils.StringUtil;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by honghouyang on 16/9/12.
 */
public class ImageListWitesActivity extends BaseAllActivity {
    private ViewPager viewPager;
    private TextView pageText;
    private ImageView iv_back;
    private IMG img;
    private ViewPagerAdapter adapter;
    private int position;

    public void setStatus() {
        StatusBarUtil.setTranslucentStatus(this,false);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.black),0);
    }
    public void initViews() {

        setContentView(R.layout.activity_image_list);



        img = (IMG) getIntent().getSerializableExtra("img");
        position = getIntent().getIntExtra("position", 0);
        initMainView();
    }

    @Override
    public void bindEvents() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }


    private void initMainView() {
        pageText = (TextView) findViewById(R.id.page_text);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        viewPager = (ViewPager) findViewById(R.id.image_pager);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        pageText.setText(position+1  + "/" + img.child_list.size());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int new_position) {
                position = new_position;
                pageText.setText(position + 1 + "/" + img.child_list.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int new_position) {
            View view = LayoutInflater.from(ImageListWitesActivity.this).inflate(
                    R.layout.lp_pager_img_list, null);
            PhotoView photoView = view.findViewById(R.id.img_content);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageListWitesActivity.this.finish();
                }
            });
//            int screenWidth = Win.getScreenWidth(ImageListWitesActivity.this); // 获取屏幕宽度
//            ViewGroup.LayoutParams lp = photoView.getLayoutParams();
//            lp.width = screenWidth;
//            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            photoView.setLayoutParams(lp);
//            photoView.setMaxWidth(screenWidth);
//            photoView.setMaxHeight(screenWidth * 5); //这里其实可以根据需求而定，我这里测试为最大宽度的5倍
            Glide.with(ImageListWitesActivity.this).load(StringUtil.changeUrl( img.child_list.get(new_position).img_url)).asBitmap()
                    .placeholder(R.drawable.moren).into(photoView);

//            photoView.setImageWithURL(ImageListWitesActivity.this, img.child_list.get(new_position).img_url);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return img.child_list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
