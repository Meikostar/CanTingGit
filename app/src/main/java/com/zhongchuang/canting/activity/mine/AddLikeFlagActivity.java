package com.zhongchuang.canting.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mine.clip.ClipActivity;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.BaseType;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.PhotoUtils;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.Custom_TagBtn;
import com.zhongchuang.canting.widget.FlexboxLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.valuesfeng.picker.ImageSelectActivity;

public class AddLikeFlagActivity extends BaseActivity1 {


    @BindView(R.id.fbl_tag7)
    FlexboxLayout fblTag7;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.but_save)
    Button butSave;
    @BindView(R.id.fbl_tag1)
    FlexboxLayout fblTag1;
    @BindView(R.id.fbl_tag2)
    FlexboxLayout fblTag2;
    private int type = 1;//1电影，2音乐
    private String data;
    private String title;
    private List<BaseType> tags = new ArrayList<>();//标签数据
    private List<BaseType> chooses = new ArrayList<>();//标签数据
    private List<BaseType> datas = new ArrayList<>();//标签数据
    private Map<String, String> map = new HashMap<>();
    private Map<String, String> maps = new HashMap<>();

    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_like);
        ButterKnife.bind(this);

        data = getIntent().getStringExtra("data");
        title = getIntent().getStringExtra("title");
        type=getIntent().getIntExtra("type",1);
        tvTitle.setText(title);



    }

    private String contents;
    private String conts;

    @Override
    public void bindEvents() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooses.size() > 0) {
                    int a = 0;
                    for (BaseType baseType : chooses) {
                        if (a == 0) {
                            contents = baseType.name;
                        } else {
                            contents = contents + "," + baseType.name;
                        }
                        a++;
                    }
                } else {
                    contents = "";
                }


                Intent intent = new Intent();
                if(TextUtil.isNotEmpty(contents)){
                    if(contents.equals("")){
                        intent.putExtra("data", "+ 自定义标签");
                    }else {
                        intent.putExtra("data", contents);
                    }
                }else {
                    intent.putExtra("data", contents);
                }

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    public void initData1() {
        BaseType baseType = new BaseType();
        baseType.name = "恐怖片";
        maps.put(baseType.name,baseType.name);
        BaseType baseType1 = new BaseType();
        baseType1.name = "喜剧片";
        maps.put(baseType1.name,baseType1.name);
        BaseType baseType2 = new BaseType();
        baseType2.name = "侦探片";
        maps.put(baseType2.name,baseType2.name);
        BaseType baseType3 = new BaseType();
        baseType3.name = "动作片";
        maps.put(baseType3.name,baseType3.name);
        BaseType baseType4 = new BaseType();
        baseType4.name = "冒险片";
        maps.put(baseType4.name,baseType4.name);
        BaseType baseType5 = new BaseType();
        baseType5.name = "科幻片";
        maps.put(baseType5.name,baseType5.name);
        BaseType baseType6 = new BaseType();
        baseType6.name = "爱情片";
        maps.put(baseType6.name,baseType6.name);
        BaseType baseType7 = new BaseType();
        baseType7.name = "悬疑片";
        maps.put(baseType7.name,baseType7.name);
        BaseType baseType8 = new BaseType();
        baseType8.name = "惊悚片";
        maps.put(baseType8.name,baseType8.name);
        BaseType baseType9 = new BaseType();
        baseType9.name = "记录片";
        maps.put(baseType9.name,baseType9.name);
        BaseType baseType10 = new BaseType();
        baseType10.name = "三维动画片";
        maps.put(baseType10.name,baseType10.name);
        BaseType baseType11 = new BaseType();
        baseType11.name = "犯罪黑帮片";
        maps.put(baseType11.name,baseType11.name);
        BaseType baseType12 = new BaseType();
        baseType12.name = "公路电影";
        maps.put(baseType12.name,baseType12.name);
        BaseType baseType13 = new BaseType();
        baseType13.name = "音乐/歌舞片";
        maps.put(baseType13.name,baseType13.name);
        BaseType baseType14 = new BaseType();
        baseType14.name = "战争/反战片";
        maps.put(baseType14.name,baseType14.name);
        BaseType baseType15 = new BaseType();
        baseType15.name = "西部片";
        maps.put(baseType15.name,baseType15.name);
        BaseType baseType16 = new BaseType();
        baseType16.name = "灾难片";
        maps.put(baseType16.name,baseType16.name);

        BaseType baseType17 = new BaseType();
        baseType17.name = "律政片 ";
        maps.put(baseType17.name,baseType17.name);
        BaseType baseType18 = new BaseType();
        baseType18.name = "奇幻电影";
        maps.put(baseType18.name,baseType18.name);
        BaseType baseType19 = new BaseType();
        baseType19.name = "黑色喜剧";
        maps.put(baseType19.name,baseType19.name);
        BaseType baseType20 = new BaseType();
        baseType20.name = "鬼片 ";
        maps.put(baseType20.name,baseType20.name);

        tags.add(baseType);
        tags.add(baseType1);
        tags.add(baseType2);
        tags.add(baseType3);
        tags.add(baseType4);
        tags.add(baseType5);
        tags.add(baseType6);
        tags.add(baseType7);
        tags.add(baseType8);
        tags.add(baseType9);
        tags.add(baseType10);
        tags.add(baseType11);
        tags.add(baseType12);
        tags.add(baseType13);
        tags.add(baseType14);
        tags.add(baseType15);
        tags.add(baseType16);
        tags.add(baseType17);
        tags.add(baseType18);
        tags.add(baseType19);
        tags.add(baseType20);



    }
    public void initData2() {
        BaseType baseType = new BaseType();
        baseType.name = "民歌";
        maps.put(baseType.name,baseType.name);
        BaseType baseType1 = new BaseType();
        baseType1.name = "经典流行曲";
        maps.put(baseType1.name,baseType1.name);
        BaseType baseType2 = new BaseType();
        baseType2.name = "RAP";
        maps.put(baseType2.name,baseType2.name);
        BaseType baseType3 = new BaseType();
        baseType3.name = "POP";
        maps.put(baseType3.name,baseType3.name);
        BaseType baseType4 = new BaseType();
        baseType4.name = "乡村音乐";
        maps.put(baseType4.name,baseType4.name);
        BaseType baseType5 = new BaseType();
        baseType5.name = "爵士音乐";
        maps.put(baseType5.name,baseType5.name);
        BaseType baseType6 = new BaseType();
        baseType6.name = "MC";
        maps.put(baseType6.name,baseType6.name);
        BaseType baseType7 = new BaseType();
        baseType7.name = "DJ";
        maps.put(baseType7.name,baseType7.name);
        BaseType baseType8 = new BaseType();
        baseType8.name = "古典音乐";
        maps.put(baseType8.name,baseType8.name);
        BaseType baseType9 = new BaseType();
        baseType9.name = "蓝调";
        maps.put(baseType9.name,baseType9.name);
        BaseType baseType10 = new BaseType();
        baseType10.name = "轻音乐";
        maps.put(baseType10.name,baseType10.name);
        BaseType baseType11 = new BaseType();
        baseType11.name = "dream-pop";
        maps.put(baseType11.name,baseType11.name);
        BaseType baseType12 = new BaseType();
        baseType12.name = "Synth Pop";
        maps.put(baseType12.name,baseType12.name);
        BaseType baseType13 = new BaseType();
        baseType13.name = "金属乐";
        maps.put(baseType13.name,baseType13.name);
        BaseType baseType14 = new BaseType();
        baseType14.name = "朋克";
        maps.put(baseType14.name,baseType14.name);
        BaseType baseType15 = new BaseType();
        baseType15.name = "摇滚乐";
        maps.put(baseType15.name,baseType15.name);
        BaseType baseType16 = new BaseType();
        baseType16.name = "巴西音乐";
        maps.put(baseType16.name,baseType16.name);

        BaseType baseType17 = new BaseType();
        baseType17.name = "reggae雷鬼 ";
        maps.put(baseType17.name,baseType17.name);
        BaseType baseType18 = new BaseType();
        baseType18.name = "合成乐";
        maps.put(baseType18.name,baseType18.name);
        BaseType baseType19 = new BaseType();
        baseType19.name = "bossa nova";
        maps.put(baseType19.name,baseType19.name);
        BaseType baseType20 = new BaseType();
        baseType20.name = "breakbeat ";
        maps.put(baseType20.name,baseType20.name);

        tags.add(baseType);
        tags.add(baseType1);
        tags.add(baseType2);
        tags.add(baseType3);
        tags.add(baseType4);
        tags.add(baseType5);
        tags.add(baseType6);
        tags.add(baseType7);
        tags.add(baseType8);
        tags.add(baseType9);
        tags.add(baseType10);
        tags.add(baseType11);
        tags.add(baseType12);
        tags.add(baseType13);
        tags.add(baseType14);
        tags.add(baseType15);
        tags.add(baseType16);
        tags.add(baseType17);
        tags.add(baseType18);
        tags.add(baseType19);
        tags.add(baseType20);



    }
    public void initData3() {
        BaseType baseType = new BaseType();
        baseType.name = "神话小说";
        maps.put(baseType.name,baseType.name);
        BaseType baseType1 = new BaseType();
        baseType1.name = "武侠小说";
        maps.put(baseType1.name,baseType1.name);
        BaseType baseType2 = new BaseType();
        baseType2.name = "仙侠小说";
        maps.put(baseType2.name,baseType2.name);
        BaseType baseType3 = new BaseType();
        baseType3.name = "侦探小说";
        maps.put(baseType3.name,baseType3.name);
        BaseType baseType4 = new BaseType();
        baseType4.name = "探险小说";
        maps.put(baseType4.name,baseType4.name);
        BaseType baseType5 = new BaseType();
        baseType5.name = "历史小说";
        maps.put(baseType5.name,baseType5.name);
        BaseType baseType6 = new BaseType();
        baseType6.name = "言情小说";
        maps.put(baseType6.name,baseType6.name);
        BaseType baseType7 = new BaseType();
        baseType7.name = "科幻小说";
        maps.put(baseType7.name,baseType7.name);
        BaseType baseType8 = new BaseType();
        baseType8.name = "恐怖小说";
        maps.put(baseType8.name,baseType8.name);
        BaseType baseType9 = new BaseType();
        baseType9.name = "玄幻小说";
        maps.put(baseType9.name,baseType9.name);
        BaseType baseType10 = new BaseType();
        baseType10.name = "古典小说";
        maps.put(baseType10.name,baseType10.name);
        BaseType baseType11 = new BaseType();
        baseType11.name = "现代小说";
        maps.put(baseType11.name,baseType11.name);
        BaseType baseType12 = new BaseType();
        baseType12.name = "微型小说";
        maps.put(baseType12.name,baseType12.name);
        BaseType baseType13 = new BaseType();
        baseType13.name = "短篇小说";
        maps.put(baseType13.name,baseType13.name);
        BaseType baseType14 = new BaseType();
        baseType14.name = "中篇小说";
        maps.put(baseType14.name,baseType14.name);
        BaseType baseType15 = new BaseType();
        baseType15.name = "长篇小说";
        maps.put(baseType15.name,baseType15.name);


        tags.add(baseType);
        tags.add(baseType1);
        tags.add(baseType2);
        tags.add(baseType3);
        tags.add(baseType4);
        tags.add(baseType5);
        tags.add(baseType6);
        tags.add(baseType7);
        tags.add(baseType8);
        tags.add(baseType9);
        tags.add(baseType10);
        tags.add(baseType11);
        tags.add(baseType12);
        tags.add(baseType13);
        tags.add(baseType14);
        tags.add(baseType15);



        setTagAdapter();
    }
    public void initData4() {
        BaseType baseType = new BaseType();
        baseType.name = "文艺青年";
        BaseType baseType1 = new BaseType();
        baseType1.name = "有为青年";
        BaseType baseType2 = new BaseType();
        baseType2.name = "白领";
        BaseType baseType3 = new BaseType();
        baseType3.name = "学生";
        BaseType baseType4 = new BaseType();
        baseType4.name = "IT民工";
        BaseType baseType5 = new BaseType();
        baseType5.name = "自由职业";
        BaseType baseType6 = new BaseType();
        baseType6.name = "上班族";
        BaseType baseType7 = new BaseType();
        baseType7.name = "潜力股";
        BaseType baseType8 = new BaseType();
        baseType8.name = "创业者";
        BaseType baseType9 = new BaseType();
        baseType9.name = "技术宅";
        BaseType baseType10 = new BaseType();
        baseType10.name = "小清新";
        BaseType baseType11 = new BaseType();
        baseType11.name = "月光族";
        BaseType baseType12 = new BaseType();
        baseType12.name = "乐活族";
        BaseType baseType13 = new BaseType();
        baseType13.name = "愤青";
        BaseType baseType14 = new BaseType();
        baseType14.name = "正太";
        BaseType baseType15 = new BaseType();
        baseType15.name = "萝莉";


        tags.add(baseType);
        tags.add(baseType1);
        tags.add(baseType2);
        tags.add(baseType3);
        tags.add(baseType4);
        tags.add(baseType5);
        tags.add(baseType6);
        tags.add(baseType7);
        tags.add(baseType8);
        tags.add(baseType9);
        tags.add(baseType10);
        tags.add(baseType11);
        tags.add(baseType12);
        tags.add(baseType13);
        tags.add(baseType14);
        tags.add(baseType15);

        for(BaseType type:tags){
            maps.put(type.name,type.name);
        }

    }
    public void initData5() {
        BaseType baseType = new BaseType();
        baseType.name = "成熟";
        BaseType baseType1 = new BaseType();
        baseType1.name = "各种宅";
        BaseType baseType2 = new BaseType();
        baseType2.name = "幽默";
        BaseType baseType3 = new BaseType();
        baseType3.name = "爱时尚";
        BaseType baseType4 = new BaseType();
        baseType4.name = "执着";
        BaseType baseType5 = new BaseType();
        baseType5.name = "温柔";
        BaseType baseType6 = new BaseType();
        baseType6.name = "直率";
        BaseType baseType7 = new BaseType();
        baseType7.name = "闷骚";
        BaseType baseType8 = new BaseType();
        baseType8.name = "善良";
        BaseType baseType9 = new BaseType();
        baseType9.name = "低调";
        BaseType baseType10 = new BaseType();
        baseType10.name = "自由";
        BaseType baseType11 = new BaseType();
        baseType11.name = "阳光";
        BaseType baseType12 = new BaseType();
        baseType12.name = "乐观";
        BaseType baseType13 = new BaseType();
        baseType13.name = "完美主义";
        BaseType baseType14 = new BaseType();
        baseType14.name = "自信";
        BaseType baseType15 = new BaseType();
        baseType15.name = "萌";


        tags.add(baseType);
        tags.add(baseType1);
        tags.add(baseType2);
        tags.add(baseType3);
        tags.add(baseType4);
        tags.add(baseType5);
        tags.add(baseType6);
        tags.add(baseType7);
        tags.add(baseType8);
        tags.add(baseType9);
        tags.add(baseType10);
        tags.add(baseType11);
        tags.add(baseType12);
        tags.add(baseType13);
        tags.add(baseType14);
        tags.add(baseType15);

        for(BaseType type:tags){
            maps.put(type.name,type.name);
        }


    }
    public void initData6() {
        BaseType baseType = new BaseType();
        baseType.name = "单身待解救";
        BaseType baseType1 = new BaseType();
        baseType1.name = "静待缘分";
        BaseType baseType2 = new BaseType();
        baseType2.name = "起床困难户";
        BaseType baseType3 = new BaseType();
        baseType3.name = "奋斗ing";
        BaseType baseType4 = new BaseType();
        baseType4.name = "努力加班";
        BaseType baseType5 = new BaseType();
        baseType5.name = "幸福ing";
        BaseType baseType6 = new BaseType();
        baseType6.name = "学习ing";
        BaseType baseType7 = new BaseType();
        baseType7.name = "减肥ing";
        BaseType baseType8 = new BaseType();
        baseType8.name = "失恋ing";
        BaseType baseType9 = new BaseType();
        baseType9.name = "热恋ing";
        BaseType baseType10 = new BaseType();
        baseType10.name = "纠结ing";
        BaseType baseType11 = new BaseType();
        baseType11.name = "寂寞ing";
        BaseType baseType12 = new BaseType();
        baseType12.name = "缺爱ing";
        BaseType baseType13 = new BaseType();
        baseType13.name = "成长ing";



        tags.add(baseType);
        tags.add(baseType1);
        tags.add(baseType2);
        tags.add(baseType3);
        tags.add(baseType4);
        tags.add(baseType5);
        tags.add(baseType6);
        tags.add(baseType7);
        tags.add(baseType8);
        tags.add(baseType9);
        tags.add(baseType10);
        tags.add(baseType11);
        tags.add(baseType12);
        tags.add(baseType13);



        for(BaseType type:tags){
            maps.put(type.name,type.name);
        }
    }
    public void initData7() {
        BaseType baseType = new BaseType();
        baseType.name = "舌头打桃结";
        BaseType baseType1 = new BaseType();
        baseType1.name = "记忆力超常";
        BaseType baseType2 = new BaseType();
        baseType2.name = "过目不忘";
        BaseType baseType3 = new BaseType();
        baseType3.name = "力大无穷";
        BaseType baseType4 = new BaseType();
        baseType4.name = "晒不黑";
        BaseType baseType5 = new BaseType();
        baseType5.name = "狂吃不胖";
        BaseType baseType6 = new BaseType();
        baseType6.name = "没生过病";
        BaseType baseType7 = new BaseType();
        baseType7.name = "睁眼睡觉";
        BaseType baseType8 = new BaseType();
        baseType8.name = "耳朵能动";
        BaseType baseType9 = new BaseType();
        baseType9.name = "倒立睡觉";
        BaseType baseType10 = new BaseType();
        baseType10.name = "一字马";
        BaseType baseType11 = new BaseType();
        baseType11.name = "单个眉毛动";
        BaseType baseType12 = new BaseType();
        baseType12.name = "舌头舔鼻子";
        BaseType baseType13 = new BaseType();
        baseType13.name = "对眼";
        BaseType baseType14 = new BaseType();
        baseType14.name = "口吞拳头";
        BaseType baseType15 = new BaseType();
        baseType15.name = "长时憋气";


        tags.add(baseType);
        tags.add(baseType1);
        tags.add(baseType2);
        tags.add(baseType3);
        tags.add(baseType4);
        tags.add(baseType5);
        tags.add(baseType6);
        tags.add(baseType7);
        tags.add(baseType8);
        tags.add(baseType9);
        tags.add(baseType10);
        tags.add(baseType11);
        tags.add(baseType12);
        tags.add(baseType13);
        tags.add(baseType14);
        tags.add(baseType15);


        for(BaseType type:tags){
            maps.put(type.name,type.name);
        }

    }
    public void initData8() {
        BaseType baseType = new BaseType();
        baseType.name = "文艺青年";
        BaseType baseType1 = new BaseType();
        baseType1.name = "有为青年";
        BaseType baseType2 = new BaseType();
        baseType2.name = "白领";
        BaseType baseType3 = new BaseType();
        baseType3.name = "学生";
        BaseType baseType4 = new BaseType();
        baseType4.name = "IT民工";
        BaseType baseType5 = new BaseType();
        baseType5.name = "自由职业";
        BaseType baseType6 = new BaseType();
        baseType6.name = "上班族";
        BaseType baseType7 = new BaseType();
        baseType7.name = "潜力股";
        BaseType baseType8 = new BaseType();
        baseType8.name = "创业者";
        BaseType baseType9 = new BaseType();
        baseType9.name = "技术宅";
        BaseType baseType10 = new BaseType();
        baseType10.name = "小清新";
        BaseType baseType11 = new BaseType();
        baseType11.name = "月光族";
        BaseType baseType12 = new BaseType();
        baseType12.name = "乐活族";
        BaseType baseType13 = new BaseType();
        baseType13.name = "愤青";
        BaseType baseType14 = new BaseType();
        baseType14.name = "正太";
        BaseType baseType15 = new BaseType();
        baseType15.name = "萝莉";


        tags.add(baseType);
        tags.add(baseType1);
        tags.add(baseType2);
        tags.add(baseType3);
        tags.add(baseType4);
        tags.add(baseType5);
        tags.add(baseType6);
        tags.add(baseType7);
        tags.add(baseType8);
        tags.add(baseType9);
        tags.add(baseType10);
        tags.add(baseType11);
        tags.add(baseType12);
        tags.add(baseType13);
        tags.add(baseType14);
        tags.add(baseType15);

        for(BaseType type:tags){
            maps.put(type.name,type.name);
        }


    }
    @Override
    public void initData() {
        if(type==1){
            initData1();
        }else if(type==2){
            initData2();
        }else if(type==3){
            initData3();
        }else if(type==4){
            initData4();
        }else if(type==5){
            initData5();
        }else if(type==6){
            initData6();
        }else if(type==7){
            initData7();
        }else if(type==8){
            initData8();
        }




        if (TextUtil.isNotEmpty(data)) {

            String[] splits = data.split("&&");
            if(splits.length==2){
                String[] split = splits[0].split(",");
                for (String name : split) {
                    map.put(name, name);
                    BaseType baseType = new BaseType();
                    baseType.name = name;
                    baseType.isChoos = true;
                    chooses.add(baseType);
                }
                BaseType baseType = new BaseType();
                baseType.name = "+ 自定义标签";
                baseType.isChoos = false;
                baseType.iscreate = true;
                datas.add(baseType);
                String[] splt = splits[1].split(",");
                for (String name : splt) {
                    map.put(name, name);
                    BaseType baseTypes = new BaseType();
                    baseTypes.name = name;
                    baseTypes.isChoos = true;
                    chooses.add(baseType);
                    datas.add(baseType);
                }
            }else {
                BaseType baseType = new BaseType();
                baseType.name = "+ 自定义标签";
                baseType.isChoos = false;
                baseType.iscreate = true;
                datas.add(baseType);
                String[] split = data.split(",");
                for (String name : split) {
                    String keys = maps.get(name);
                    if(TextUtil.isEmpty(keys)){
                        BaseType base = new BaseType();
                        base.name =name;
                        base.isChoos = true;
                        datas.add(base);
                    }
                    map.put(name, name);
                    BaseType baseTypes = new BaseType();
                    baseTypes.name = name;
                    baseTypes.isChoos = true;
                    chooses.add(baseTypes);
                }

            }

        }else {
            BaseType baseType = new BaseType();
            baseType.name = "+ 自定义标签";
            baseType.isChoos = false;
            baseType.iscreate = true;
            datas.add(baseType);
        }
        if (chooses.size() > 0) {
            for (BaseType type : tags) {
                String content = map.get(type.name);
                if (TextUtil.isNotEmpty(content)) {
                    type.isChoos = true;
                }
            }
        }
        setTagAdapter();
    }

    private List<BaseType> list = new ArrayList<>();


    public void selectChoose() {
        chooses.clear();
        for (BaseType type : tags) {
            if (type.isChoos) {
                BaseType baseType = new BaseType();
                baseType.name = type.name;
                baseType.isChoos = type.isChoos;
                chooses.add(baseType);
            }
        }
        for (BaseType type : datas) {
            if(type.isChoos){
                BaseType baseType = new BaseType();
                baseType.name = type.name;
                baseType.isChoos = type.isChoos;
                chooses.add(baseType);
            }


        }
    }

    public int getChooseSize() {
        return chooses.size();
    }
    public int getDataSize() {
        return chooses.size();
    }
    public void selectRemove() {
        if (TextUtil.isNotEmpty(names)) {
            for (BaseType type : tags) {
                if (type.name.equals(names)) {
                    type.isChoos = false;
                }
            }
        }

    }

    /**
     * 初始化标签适配器
     */
    private void setTagAdapter() {
        fblTag1.removeAllViews();
        fblTag2.removeAllViews();
        fblTag7.removeAllViews();


        if (tags.size() > 0) {
            for (int i = 0; i <tags.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(tags.get(i));
                final int position = i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {

                        if (type == 1) {
                            for (int j = 0; j < tags.size(); j++) {
                                if (position == j) {

                                    if (tags.get(j).isChoos) {
                                        tags.get(j).isChoos = false;
                                    } else {
                                        if (getChooseSize() < 6) {
                                            tags.get(j).isChoos = true;
                                        } else {
                                            showToasts("最多可以选5个标签");
                                            return;
                                        }

                                    }

                                }
                            }
                        } else if (type == 2) {
                            for (int j = 0; j < tags.size(); j++) {
                                if (position == j) {
                                    tags.remove(j);
                                }
                            }

                        } else if (type == 3) {
                            for (int j = 0; j < tags.size(); j++) {
                                if (position == j) {
                                    tags.remove(j);
                                }
                            }

                        }
                        selectChoose();
                        setTagAdapter();
                    }
                });
                fblTag2.addView(tagBtn, i);
            }


            if (chooses.size() > 0) {

                for (int i = 0; i < chooses.size(); i++) {
                    final Custom_TagBtn tagBtn = createBaseFlexItemTextView(chooses.get(i));
                    final int position = i;
                    tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                        @Override
                        public void clickDelete(int type) {
                            for (int j = 0; j < chooses.size(); j++) {
                                if (position == j) {
                                    names = chooses.get(j).name;
                                    chooses.remove(j);
                                }
                            }
                            selectRemove();
                            setTagAdapter();
                        }
                    });
                    fblTag1.addView(tagBtn, i);
                }
            }

            if (datas.size() > 0) {

                for (int i = 0; i < datas.size(); i++) {
                    final Custom_TagBtn tagBtn = createBaseFlexItemTextView(datas.get(i));
                    final int position = i;
                    tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                        @Override
                        public void clickDelete(int type) {

                            for (int j = 0; j < datas.size(); j++) {
                                if(j==0){
                                    if (getDataSize() < 6) {
                                        Intent intent = new Intent(AddLikeFlagActivity.this, EditorSecondActivity.class);

                                        intent.putExtra("data", title);
                                        startActivityForResult(intent, 666);
                                    } else {
                                        showToasts("最多可以选5个标签");
                                        return;
                                    }


                                }else {
                                    if (datas.get(j).isChoos) {
                                        datas.get(j).isChoos = false;
                                    } else {
                                        if (getDataSize() < 6) {
                                            datas.get(j).isChoos = true;
                                        } else {
                                            showToasts("最多可以选5个标签");
                                            return;
                                        }

                                    }
                                }

                            }
                            selectRemove();
                            selectChoose();
                            setTagAdapter();
                        }
                    });
                    fblTag7.addView(tagBtn, i);
                }
            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 666:
                    String content = data.getStringExtra("data");
                    BaseType baseType = new BaseType();
                    baseType.name = content;
                    baseType.isChoos = true;
                    datas.add(baseType);
                    selectChoose();
                    setTagAdapter();

            }
        }
    }
    private String names;

    /**
     * 创建流式布局item
     *
     * @param content
     * @return
     */
    public Custom_TagBtn createBaseFlexItemTextView(BaseType content) {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = DensityUtil.dip2px(this, 10);
        lp.leftMargin = DensityUtil.dip2px(this, 7);
        lp.rightMargin = DensityUtil.dip2px(this, 7);


        Custom_TagBtn view = (Custom_TagBtn) LayoutInflater.from(this).inflate(R.layout.dish_item, null);
        if (content.isChoos) {
            view.setBg(R.drawable.blue_regle);
            view.setColors(R.color.white);
        } else {
            if(content.iscreate){
                view.setBg(R.drawable.black_line_rectangle_dash);
                view.setColors(R.color.color6);
            }else {
                view.setBg(R.drawable.black_rectangle_flag);
                view.setColors(R.color.color6);
            }

        }
        int width = (int) DensityUtil.getWidth(this) / 3;
        String name = content.name;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(15);
        int with = (int) textPaint.measureText(name);
        view.setSize(with + 34, 40, 15, 1);
        view.setLayoutParams(lp);
        view.setCustomText(content.name);

        return view;
    }



}

