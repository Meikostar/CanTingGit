package com.zhongchuang.canting.allive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.adapter.HomeViewPagerAdapter;
import com.zhongchuang.canting.allive.adapter.MultilayerGridAdapter;
import com.zhongchuang.canting.allive.model.ScenesModel;
import com.zhongchuang.canting.allive.vodupload_demo.activity.PlayVideoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mulberry
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 小圆点指示器
     */
    private ViewGroup points;
    /**
     * 小圆点图片集合
     */
    private ImageView[] ivPoints;
    private ViewPager viewPager;

    /**
     * 用户设置页面按钮
     */
    private ImageView mIvPersonPage;
    /**
     * 当前页数
     */
    private int currentPage;
    /**
     * 总的页数
     */
    private int totalPage;
    /**
     * 每页显示的最大数量
     */
    private int mPageSize = 6;
    /**
     * 总的数据源
     */
    private List<ScenesModel> listDatas;
    /**
     * GridView作为一个View对象添加到ViewPager集合中
     */
    private List<View> viewPagerList;
    /**
     * module数据，播放器模块暂时只有一个
     */
    /**
     * module数据，播放器模块暂时只有一个
     */
    private int[] modules = new int[]{
            R.string.solution_recorder, R.string.solution_edit,
            R.string.solution_crop, R.string.solution_camera,
            R.string.solution_live_room, R.string.solution_push,
            R.string.solution_upload, R.string.solution_player,
            R.string.solution_apsarav
    };
    private int[] homeicon = {
            R.mipmap.icon_home_svideo, R.mipmap.icon_home_edit,
            R.mipmap.icon_home_svideo, R.mipmap.icon_home_svideo,
            R.mipmap.icon_home_live, R.mipmap.icon_home_live,
            R.mipmap.icon_home_upload, R.mipmap.icon_home_player,
            R.mipmap.icon_home_apsarav
    };
    private Spinner changeNetBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_main_spinner);

        iniViews();
        initData();
        setDatas();
        buildHomeItem();
    }

    private void initData() {

    }

    private void iniViews() {
        viewPager = (ViewPager) findViewById(R.id.home_viewPager);
        points = (ViewGroup) findViewById(R.id.points);
        changeNetBt = (Spinner) findViewById(R.id.spinner);
        changeNetBt.setVisibility(View.GONE);
        changeNetBt.setTag(false);

        mIvPersonPage = (ImageView) findViewById(R.id.person_page);
        mIvPersonPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setDatas() {
        listDatas = new ArrayList<>();
        for (int i = 0; i < modules.length; i++) {
            listDatas.add(new ScenesModel(getResources().getString(modules[i]), homeicon[i]));
        }

    }

    private void buildHomeItem() {
        LayoutInflater inflater = LayoutInflater.from(this);
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();


        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.alivc_home_girdview, viewPager, false);
            gridView.setAdapter(new MultilayerGridAdapter(this, listDatas, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent;
                    if (currentPage == 0) {
                        switch (position) {
                            case 0:
                                // 视频拍摄
                                Intent recorder = new Intent("com.duanqu.qupai.action.recorder.setting");
                                startActivity(recorder);

                                break;
                            case 1:
                                // 视频编辑
                                Intent edit = new Intent("com.duanqu.qupai.action.import.setting");
                                startActivity(edit);

                                break;
                            case 2:
                                // 视频裁剪
                                Intent crop = new Intent("com.duanqu.qupai.action.crop.setting");
                                startActivity(crop);

                                break;
                            case 3:
                                // 魔法相机
                                Intent camera = new Intent("com.duanqu.qupai.action.camera");
                                startActivity(camera);

                                break;

                            case 5:
                                // 直播推流
                                intent = new Intent(MainActivity.this, com.zhongchuang.canting.allive.pusher.ui.activity.MainActivity.class);
                                startActivity(intent);


                                break;

                            default:
                                break;
                        }
                    }else if (currentPage == 1) {
                        switch (position) {
                            case 0:
                                // 视频上传
                                //                                Toast.makeText(PlayVideoActivity.this,
                                //                                    getResources().getString(R.string.solution_unable_merge_remind), Toast.LENGTH_LONG)
                                //                                    .show();
                                intent = new Intent(MainActivity.this, PlayVideoActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                // 视频播放
                                intent = new Intent("com.aliyun.vodplayerview.activity.player");
                                startActivity(intent);
                                break;

                            case 2:
                                // ApsaraV
                                Toast.makeText(MainActivity.this,
                                        getResources().getString(R.string.solution_wait_remind), Toast.LENGTH_LONG).show();

                                break;

                            case 3:

                                break;

                            default:
                                break;
                        }
                    }
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }

        //设置ViewPager适配器
        viewPager.setAdapter(new HomeViewPagerAdapter(viewPagerList));

        //小圆点指示器
        if (totalPage > 1) {
            ivPoints = new ImageView[totalPage];
            for (int i = 0; i < ivPoints.length; i++) {
                ImageView imageView = new ImageView(this);
                //设置图片的宽高
                imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
                if (i == 0) {
                    imageView.setBackgroundResource(R.mipmap.page_selected_indicator);
                } else {
                    imageView.setBackgroundResource(R.mipmap.page_normal_indicator);
                }
                ivPoints[i] = imageView;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layoutParams.leftMargin = (int) getResources().getDimension(R.dimen.alivc_home_points_item_margin);//设置点点点view的左边距
                layoutParams.rightMargin = (int) getResources().getDimension(R.dimen.alivc_home_points_item_margin);
                //设置点点点view的右边距
                points.addView(imageView, layoutParams);
            }
            points.setVisibility(View.VISIBLE);
        } else {
            points.setVisibility(View.GONE);
        }


        //设置ViewPager滑动监听
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //改变小圆圈指示器的切换效果
                setImageBackground(position);
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < ivPoints.length; i++) {
            if (i == selectItems) {
                ivPoints[i].setBackgroundResource(R.mipmap.page_selected_indicator);
            } else {
                ivPoints[i].setBackgroundResource(R.mipmap.page_normal_indicator);
            }
        }
    }

}
