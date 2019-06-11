package com.zhongchuang.canting.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.BaseType;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.Custom_TagBtn;
import com.zhongchuang.canting.widget.FlexboxLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFlagActivity extends BaseActivity1 {


    @BindView(R.id.line)
    View line;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.but_save)
    Button butSave;
    @BindView(R.id.fbl_tag1)
    FlexboxLayout fblTag1;
    @BindView(R.id.fbl_tag2)
    FlexboxLayout fblTag2;
    @BindView(R.id.fbl_tag3)
    FlexboxLayout fblTag3;
    @BindView(R.id.fbl_tag4)
    FlexboxLayout fblTag4;
    @BindView(R.id.fbl_tag5)
    FlexboxLayout fblTag5;
    @BindView(R.id.fbl_tag6)
    FlexboxLayout fblTag6;
    @BindView(R.id.fbl_tag7)
    FlexboxLayout fblTag7;
    private int type = 1;//1代表做法分类2代表菜品
    private String data;
    private List<BaseType> tags = new ArrayList<>();//标签数据
    private List<BaseType> chooses = new ArrayList<>();//标签数据
    private Map<String, String> map = new HashMap<>();

    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_dish_category);
        ButterKnife.bind(this);

        data = getIntent().getStringExtra("data");




    }

   private String contents;
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
                if(chooses.size()>0){
                    int a=0;
                    for(BaseType baseType:chooses){
                        if(a==0){
                            contents=baseType.name;
                        }else {
                            contents=contents+","+baseType.name;
                        }
                        a++;
                    }
                }else {
                    contents="";
                }
                Intent intent = new Intent();
                intent.putExtra("data",contents);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        BaseType baseType = new BaseType();
        baseType.name = "专一";
        BaseType baseType1 = new BaseType();
        baseType1.name = "浪漫";
        BaseType baseType2 = new BaseType();
        baseType2.name = "工作狂";
        BaseType baseType3 = new BaseType();
        baseType3.name = "偏执狂";
        BaseType baseType4 = new BaseType();
        baseType4.name = "内向";
        BaseType baseType5 = new BaseType();
        baseType5.name = "控制狂";
        BaseType baseType6 = new BaseType();
        baseType6.name = "爱冒险";
        BaseType baseType7 = new BaseType();
        baseType7.name = "善变";
        BaseType baseType8 = new BaseType();
        baseType8.name = "中二";
        BaseType baseType9 = new BaseType();
        baseType9.name = "理性";
        BaseType baseType10 = new BaseType();
        baseType10.name = "居家";
        BaseType baseType11 = new BaseType();
        baseType11.name = "感性";
        BaseType baseType12 = new BaseType();
        baseType12.name = "热情";
        BaseType baseType13 = new BaseType();
        baseType13.name = "乐观";
        BaseType baseType14 = new BaseType();
        baseType14.name = "理想主义";
        BaseType baseType15= new BaseType();
        baseType15.name = "完美主义";
        BaseType baseType16 = new BaseType();
        baseType16.name = "讨好型";

        BaseType baseType17 = new BaseType();
        baseType17.name = "前卫";
        BaseType baseType18 = new BaseType();
        baseType18.name = "文艺";
        BaseType baseType19 = new BaseType();
        baseType19.name = "眼镜男";
        BaseType baseType20 = new BaseType();
        baseType20.name = "理工男";
        BaseType baseType21 = new BaseType();
        baseType21.name = "小鲜肉";
        BaseType baseType23 = new BaseType();
        baseType23.name = "纹身";
        BaseType baseType24 = new BaseType();
        baseType24.name = "肌肉男";
        BaseType baseType25 = new BaseType();
        baseType25.name = "大叔";
        BaseType baseType26 = new BaseType();
        baseType26.name = "微胖";
        BaseType baseType27 = new BaseType();
        baseType27.name = "潮男";


        BaseType baseType28 = new BaseType();
        baseType28.name = "选择恐惧症";
        BaseType baseType29 = new BaseType();
        baseType29.name = "数码控";
        BaseType baseType30 = new BaseType();
        baseType30.name = "暖男";
        BaseType baseType31 = new BaseType();
        baseType31.name = "夜猫子";
        BaseType baseType32 = new BaseType();
        baseType32.name = "好奇心";
        BaseType baseType33 = new BaseType();
        baseType33.name = "果粉";
        BaseType baseType34 = new BaseType();
        baseType34.name = "铲屎官";
        BaseType baseType35 = new BaseType();
        baseType35.name = "宅";
        BaseType baseType36 = new BaseType();
        baseType36.name = "逗比";
        BaseType baseType37 = new BaseType();
        baseType37.name = "话唠";
        BaseType baseType38 = new BaseType();
        baseType38.name = "拖延症";
        BaseType baseType39 = new BaseType();
        baseType39.name = "腹黑";
        BaseType baseType40= new BaseType();
        baseType40.name = "吃货";
        BaseType baseType41 = new BaseType();
        baseType41.name = "霸气";
        BaseType baseType42 = new BaseType();
        baseType42.name = "聪明";


        BaseType baseType43 = new BaseType();
        baseType43.name = "自驾";
        BaseType baseType44 = new BaseType();
        baseType44.name = "游戏";
        BaseType baseType45 = new BaseType();
        baseType45.name = "逛街";
        BaseType baseType46 = new BaseType();
        baseType46.name = "LOL";
        BaseType baseType47 = new BaseType();
        baseType47.name = "烘焙";
        BaseType baseType48 = new BaseType();
        baseType48.name = "摄影";
        BaseType baseType49 = new BaseType();
        baseType49.name = "读书";
        BaseType baseType50 = new BaseType();
        baseType50.name = "运动";
        BaseType baseType51 = new BaseType();
        baseType51.name = "看电影";
        BaseType baseType52 = new BaseType();
        baseType52.name = "手工";
        BaseType baseType53 = new BaseType();
        baseType53.name = "收藏";
        BaseType baseType54 = new BaseType();
        baseType54.name = "绿植";
        BaseType baseType55 = new BaseType();
        baseType55.name = "旅行";
        BaseType baseType56 = new BaseType();
        baseType56.name = "酒";
        BaseType baseType57 = new BaseType();
        baseType57.name = "麻将";
        BaseType baseType58 = new BaseType();
        baseType58.name = "写作";
        BaseType baseType59 = new BaseType();
        baseType59.name = "唱歌";
        BaseType baseType60 = new BaseType();
        baseType60.name = "绘画";
        BaseType baseType61 = new BaseType();
        baseType61.name = "泡吧";
        BaseType baseType62 = new BaseType();
        baseType62.name = "动漫";
        BaseType baseType63 = new BaseType();
        baseType63.name = "二次元";
        BaseType baseType64 = new BaseType();
        baseType64.name = "NBA";
        BaseType baseType65 = new BaseType();
        baseType65.name = "桌游";
        BaseType baseType66 = new BaseType();
        baseType66.name = "看剧";


        BaseType baseType67 = new BaseType();
        baseType67.name = "滑板";
        BaseType baseType68 = new BaseType();
        baseType68.name = "滑雪";
        BaseType baseType69 = new BaseType();
        baseType69.name = "兵乓球";
        BaseType baseType70 = new BaseType();
        baseType70.name = "舞蹈";
        BaseType baseType71 = new BaseType();
        baseType71.name = "跆拳道";
        BaseType baseType72 = new BaseType();
        baseType72.name = "垂钓";
        BaseType baseType73 = new BaseType();
        baseType73.name = "骑行";
        BaseType baseType74 = new BaseType();
        baseType74.name = "篮球";
        BaseType baseType75 = new BaseType();
        baseType75.name = "健身";
        BaseType baseType76 = new BaseType();
        baseType76.name = "跑步";
        BaseType baseType77 = new BaseType();
        baseType77.name = "瑜伽";
        BaseType baseType78 = new BaseType();
        baseType78.name = "爬山";
        BaseType baseType79 = new BaseType();
        baseType79.name = "射箭";
        BaseType baseType80 = new BaseType();
        baseType80.name = "高尔夫";
        BaseType baseType81 = new BaseType();
        baseType81.name = "游泳";
        BaseType baseType82 = new BaseType();
        baseType82.name = "台球";
        BaseType baseType83 = new BaseType();
        baseType83.name = "击剑";
        BaseType baseType84 = new BaseType();
        baseType84.name = "露营";
        BaseType baseType85 = new BaseType();
        baseType85.name = "足球";
        BaseType baseType86 = new BaseType();
        baseType86.name = "徒步";
        BaseType baseType87 = new BaseType();
        baseType87.name = "羽毛球";
        BaseType baseType88 = new BaseType();
        baseType88.name = "街舞";
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
        tags.add(baseType21);
        tags.add(baseType23);
        tags.add(baseType24);
        tags.add(baseType25);
        tags.add(baseType26);
        tags.add(baseType27);
        tags.add(baseType28);
        tags.add(baseType29);
        tags.add(baseType30);
        tags.add(baseType31);
        tags.add(baseType32);
        tags.add(baseType33);
        tags.add(baseType34);
        tags.add(baseType35);
        tags.add(baseType36);
        tags.add(baseType37);
        tags.add(baseType38);
        tags.add(baseType39);
        tags.add(baseType40);
        tags.add(baseType41);
        tags.add(baseType42);
        tags.add(baseType43);
        tags.add(baseType44);
        tags.add(baseType45);
        tags.add(baseType46);
        tags.add(baseType47);
        tags.add(baseType48);
        tags.add(baseType49);
        tags.add(baseType50);
        tags.add(baseType51);
        tags.add(baseType52);
        tags.add(baseType53);
        tags.add(baseType54);
        tags.add(baseType55);
        tags.add(baseType56);
        tags.add(baseType57);
        tags.add(baseType58);
        tags.add(baseType59);
        tags.add(baseType60);
        tags.add(baseType61);
        tags.add(baseType62);
        tags.add(baseType63);
        tags.add(baseType64);
        tags.add(baseType65);
        tags.add(baseType66);
        tags.add(baseType67);
        tags.add(baseType68);
        tags.add(baseType69);
        tags.add(baseType70);
        tags.add(baseType71);
        tags.add(baseType72);
        tags.add(baseType73);
        tags.add(baseType74);
        tags.add(baseType75);
        tags.add(baseType76);
        tags.add(baseType77);
        tags.add(baseType78);
        tags.add(baseType79);
        tags.add(baseType80);
        tags.add(baseType81);
        tags.add(baseType82);
        tags.add(baseType83);
        tags.add(baseType84);
        tags.add(baseType85);
        tags.add(baseType86);
        tags.add(baseType87);
        tags.add(baseType88);

        for(BaseType base:tags){
            maps.put(base.name,base.name);
        }
        if (TextUtil.isNotEmpty(data)) {

            String[] splits = data.split("&&");
            if(splits.length==2){
                String[] split = splits[0].split(",");
                for (String name : split) {
                    map.put(name, name);
                    BaseType baseTe = new BaseType();
                    baseTe.name = name;
                    baseTe.isChoos = true;
                    chooses.add(baseTe);
                }
                BaseType bType = new BaseType();
                bType.name = "+ 自定义标签";
                bType.isChoos = false;
                bType.iscreate = true;
                datas.add(bType);
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
                BaseType baseT = new BaseType();
                baseT.name = "+ 自定义标签";
                baseT.isChoos = false;
                baseT.iscreate = true;
                datas.add(baseT);
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
            BaseType baseTyp = new BaseType();
            baseTyp.name = "+ 自定义标签";
            baseTyp.isChoos = false;
            baseTyp.iscreate = true;
            datas.add(baseTyp);
        }

        if(chooses.size()>0){
            for(BaseType type:tags){
                String content = map.get(type.name);
                if(TextUtil.isNotEmpty(content)){
                 type.isChoos=true;
                }
            }
        }
        setTagAdapter();
    }

    private List<BaseType> list = new ArrayList<>();

    private Map<String, String> maps = new HashMap<>();
   public void selectChoose(){
       chooses.clear();
       for(BaseType type:tags){
           if(type.isChoos){
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
   public int getChooseSize(){
       return chooses.size();
   }
    public void selectRemove(){
        if(TextUtil.isNotEmpty(names)){
            for(BaseType type:tags){
                if(type.name.equals(names)){
                    type.isChoos=false;
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
        fblTag3.removeAllViews();
        fblTag4.removeAllViews();
        fblTag5.removeAllViews();
        fblTag6.removeAllViews();
        fblTag7.removeAllViews();
        if (datas.size() > 0) {

            for (int i = 0; i < datas.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(datas.get(i));
                final int position = i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {

                        for (int j = 0; j < datas.size(); j++) {
                            if(j==0){
                                if (getChooseSize() < 11) {
                                    Intent intent = new Intent(AddFlagActivity.this, EditorSecondActivity.class);

                                    intent.putExtra("data", "自定义标签");
                                    startActivityForResult(intent, 666);
                                } else {
                                    showToasts("最多可以选10个标签");
                                    return;
                                }


                            }else {
                                if (datas.get(j).isChoos) {
                                    datas.get(j).isChoos = false;
                                } else {
                                    if (getChooseSize() < 10) {
                                        datas.get(j).isChoos = true;
                                    } else {
                                        showToasts("最多可以选11个标签");
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
        if (tags.size() > 0) {
            for (int i = 0; i < 17; i++) {
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
                                        if(getChooseSize()<11){
                                            tags.get(j).isChoos = true;
                                        }else {
                                            showToasts("最多可以选10个标签");
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
            for (int i = 17; i < 27; i++) {
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
                                        if(getChooseSize()<11){
                                            tags.get(j).isChoos = true;
                                        }else {
                                            showToasts("最多可以选10个标签");
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
                fblTag3.addView(tagBtn, i-17);
            }
            for (int i = 27; i < 42; i++) {
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
                                           if(getChooseSize()<11){
                                            tags.get(j).isChoos = true;
                                        }else {
                                            showToasts("最多可以选10个标签");
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
                fblTag4.addView(tagBtn, i-27);
            }

            for (int i = 42; i < 66; i++) {
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
                                        if(getChooseSize()<11){
                                            tags.get(j).isChoos = true;
                                        }else {
                                            showToasts("最多可以选10个标签");
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
                fblTag5.addView(tagBtn, i-42);
            }

            for (int i = 66; i < 87; i++) {
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
                                        if(getChooseSize()<11){
                                            tags.get(j).isChoos = true;
                                        }else {
                                            showToasts("最多可以选10个标签");
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
                fblTag6.addView(tagBtn, i-66);
            }

            if(chooses.size()>0){

                for (int i = 0; i < chooses.size(); i++) {
                    final Custom_TagBtn tagBtn = createBaseFlexItemTextView(chooses.get(i));
                    final int position = i;
                    tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                        @Override
                        public void clickDelete(int type) {
                            for (int j = 0; j < chooses.size(); j++) {
                                if (position == j) {
                                    names=chooses.get(j).name;
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
        }

    }
    private List<BaseType> datas = new ArrayList<>();//标签数据

    private String names;
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
        view.setSize(with+34 , 40, 15, 1);
        view.setLayoutParams(lp);
        view.setCustomText(content.name);

        return view;
    }



}

