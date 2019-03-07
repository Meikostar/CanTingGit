package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.PREFIX;
import com.zhongchuang.canting.utils.PinYinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author zaaach on 2016/1/26.
 */
public class CityListAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 3;

    private Context mContext;
    private LayoutInflater inflater;
    private List<PREFIX> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    private String type = "location";
    private int status;

    private String locatedCity;


    public CityListAdapter(Context mContext, List<PREFIX> mCities, int type) {
        status = type;
        this.mContext = mContext;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
        if (mCities == null) {
            mCities = new ArrayList<>();
        }
//        if(!TextUtils.equals(type,"city")){
//            CONTURY CONTURY = new CONTURY();
//            CONTURY.name="定位";
//            CONTURY.first_letter="0";
//            CONTURY.type=0;
//            CONTURY citys = new CONTURY();
//            citys.name="热门";
//            citys.first_letter="1";
//            citys.type=1;
//            mCities.add(0, CONTURY);
//            mCities.add(1, citys);
//        }

        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = PinYinUtils.getFirstLetter(mCities.get(index).countryCode);
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinYinUtils.getFirstLetter(mCities.get(index - 1).countryCode) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {

        this.locatedCity = city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }


    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public PREFIX getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;

        if (view == null) {
            view = View.inflate(mContext, R.layout.choose_contry_items, null);
            holder = new CityViewHolder();
            holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
            holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
            holder.names = (TextView) view.findViewById(R.id.tv_item_city_listview_names);
            holder.ll_bg = (LinearLayout) view.findViewById(R.id.ll_bg);
            view.setTag(holder);
        } else {
            holder = (CityViewHolder) view.getTag();
        }
        String city = null;

            city = mCities.get(position).countryName;

        if (position >= 1) {

            holder.name.setText(city);
            holder.names.setText(mCities.get(position).phoneCode);
            String filter ;
            String lastfilter = "";

                filter = mCities.get(position).countryCode;
                if(position>0){
                    lastfilter = mCities.get(position-1).countryCode;
                }


            String currentLetter = PinYinUtils.getFirstLetter(filter);
            String previousLetter = position >= 1 ? PinYinUtils.getFirstLetter(lastfilter) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                holder.letter.setVisibility(View.VISIBLE);
                holder.letter.setText(currentLetter);
            } else {
                holder.letter.setVisibility(View.GONE);
            }
            holder.ll_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCityClickListener != null) {
                        onCityClickListener.onCityClick(mCities.get(position).phoneCode);
                    }
                }
            });
        } else {
            holder.name.setText(city);
            holder.names.setText(mCities.get(position).phoneCode);
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText("A");
            holder.ll_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCityClickListener != null) {
                        onCityClickListener.onCityClick(mCities.get(position).phoneCode);
                    }
                }
            });

        }
        return view;
    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
        TextView names;
        LinearLayout ll_bg;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(String name);

        void onLocateClick();
    }
}
