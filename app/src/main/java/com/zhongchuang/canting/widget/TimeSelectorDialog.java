package com.zhongchuang.canting.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.utils.StringUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/***
 * 功能描述:时间选择器
 * 作者:J
 * 时间:2016/9/1
 * 版本:
 ***/
public class TimeSelectorDialog {
    private Context mContext;
    private View mView;
    private PopupWindow mPopupWindow;
    private CycleWheelView mCycleWheelViewDate;
    private CycleWheelView mCycleWheelViewHour;
    private CycleWheelView mCycleWheelViewMinute;
    private TextView mButtonCancel;
    private TextView mButtonConfirm;
    private FrameLayout mFrameLayout;
    private BindClickListener mBindClickListener;

    private String years;
    private String months;
    private String days;
    private  int type=0;
    private  boolean langue;
//    Jan/Feb/Mar/Apr/May/Jun/Jul/Aug/Sept/Oct/Nov/Dec.
    private String[] lists={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"};
    public TimeSelectorDialog(Context mContext) {
        this.mContext = mContext;
            langue=true;

    }

  public void setType(int type){
      this.type=type;
  }

    public TimeSelectorDialog setDate(String date){
        if (StringUtil.isEmpty(date)){
            return setDate(new Date(System.currentTimeMillis()));
        }
        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setDate(date1);
        return this;
    }

    public TimeSelectorDialog setDate(Date date){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dates = format.format(date);
        String[] split = dates.split("-");
        if(split!=null){
            for (int i=0;i<split.length;i++){
                if(i==0){
                    years=split[0];
                }else if(i==1){
                    months=split[1];
                }else {
                    days=split[2];
                }
            }
        }
        mBeforeCalendar.setTime(date);
        mAfterCalendar.setTime(date);
        time = mBeforeCalendar.getTimeInMillis();
        timeEnd = mBeforeCalendar.getTimeInMillis();
        return this;
    }
   private int status=0;
    public TimeSelectorDialog setDate(Date date, String dates){
        status=1;
        String[] split = dates.split("/");
        if(split!=null){
            if(split.length!=3){
                split = dates.split("-");
            }
            for (int i=0;i<split.length;i++){
                if(i==0){
                    years=split[0];
                }else if(i==1){
                    months=split[1];
                }else {
                    days=split[2];
                }
            }
        }
        mBeforeCalendar.setTime(date);
        mAfterCalendar.setTime(date);
        time = mBeforeCalendar.getTimeInMillis();
        timeEnd = mBeforeCalendar.getTimeInMillis();
        return this;
    }


    public TimeSelectorDialog setBindClickListener(BindClickListener mBindClickListener){
        this.mBindClickListener=mBindClickListener;
        return this;
    }

    public interface BindClickListener{
        void time(String time);
    }

    private void initView(){
        mView= View.inflate(mContext, R.layout.view_dialog_time_selector, null);
        mCycleWheelViewDate = mView.findViewById(R.id.cwv_date);
        mCycleWheelViewHour = mView.findViewById(R.id.cwv_hour);
        mCycleWheelViewMinute = mView.findViewById(R.id.cwv_minute);
        mCycleWheelViewDate.setCycleEnable(false);
        mCycleWheelViewHour.setCycleEnable(false);
        mCycleWheelViewMinute.setCycleEnable(false);
        mButtonCancel = mView.findViewById(R.id.but_cancel);
        mButtonConfirm = mView.findViewById(R.id.but_confirm);

        mFrameLayout = mView.findViewById(R.id.fl_time_select);
        setDate();
        setHour();
        setMinute();
        mFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                String hour=mCycleWheelViewHour.getSelectLabel();
                String minute=mCycleWheelViewMinute.getSelectLabel();
                year= mDataYearList.get(mCycleWheelViewDate.getSelection());

                String str=null;
                String date=null;
                str= mDataList.get(mCycleWheelViewDate.getSelection());
                date=str.substring(0,str.indexOf(mContext.getString(R.string.yea)))+"-"+hour.substring(0,hour.indexOf(mContext.getString(R.string.yeu)))+"-"+minute.substring(0,minute.indexOf(mContext.getString(R.string.riis)));

                mBindClickListener.time(date);

            }
        });
    }

    private int year,month,day;
    private long time = System.currentTimeMillis();
    private long timeEnd= System.currentTimeMillis();
    private List<String> mDataList;
    private List<Integer> mDataYearList = new ArrayList<>();
    private Calendar mBeforeCalendar = Calendar.getInstance();
    private Calendar mAfterCalendar = Calendar.getInstance();
    private Calendar mCurrentCalend= Calendar.getInstance();


    /**
     * 日期
     */
    private void setDate(){
        mDataList =new ArrayList<>();
        setBeforeDate();
        setAfterDate();
        mCycleWheelViewDate.setLabels(mDataList);
        try {
            mCycleWheelViewDate.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        mCycleWheelViewDate.setLabelSelectSize(14f);
        mCycleWheelViewDate.setLabelSize(12f);
        mCycleWheelViewDate.setAlphaGradual(0.7f);
        mCycleWheelViewDate.setCycleEnable(false);
        mCycleWheelViewDate.setSelection(9);
        mCycleWheelViewDate.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewDate.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewDate.setLabelSelectColor(Color.parseColor("#333333"));
        mCycleWheelViewDate.setSolid(Color.WHITE, Color.WHITE);
        mCycleWheelViewDate.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
                //LogUtils.e("mCycleWheelViewDate",position+"===="+mCycleWheelViewDate.getSelection());
                int count=mCycleWheelViewDate.getLabelCount();
                if (position==5){
                    setBeforeDate();
                    mCycleWheelViewDate.setLabels(mDataList);
                    mCycleWheelViewDate.setSelection(10+position);
                }
                if(position==count-5){
                    setAfterDate();
                    mCycleWheelViewDate.setLabels(mDataList);
                }
            }
        });

    }



    private void setAfterDate(){
             for(int i=0;i<10;i++){


            month= mAfterCalendar.get(Calendar.MONTH)+1;//月
            year= mAfterCalendar.get(Calendar.YEAR)+ i;//月
            day= mAfterCalendar.get(Calendar.DAY_OF_MONTH)+1;//日
            NumberFormat formatter = new DecimalFormat(mContext.getString(R.string.yues));
                 NumberFormat formatters=null;
                 if(langue){
                      formatters = new DecimalFormat(mContext.getString(R.string.years));
                 }else {
                      formatters = new DecimalFormat("0000");
                 }

            NumberFormat dayFormatter = new DecimalFormat(mContext.getString(R.string.ris));
            String xx = formatter.format(month);
            String yy = formatters.format(year);
            String dd=dayFormatter.format(day);

                mDataList.add(yy);

            mDataYearList.add(mAfterCalendar.get(Calendar.YEAR));

             }
    }

    private void setBeforeDate() {
        List<String> mList = new ArrayList<>();
        List<Integer> mList2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            timeEnd = timeEnd - (1000 * 60 * 60 * 24);
            mBeforeCalendar.setTimeInMillis(timeEnd);
            month = mBeforeCalendar.get(Calendar.MONTH) + 1;//月
            year = mBeforeCalendar.get(Calendar.YEAR) - i;//月

            day = mBeforeCalendar.get(Calendar.DAY_OF_MONTH) + 1;//日
            NumberFormat formatter = new DecimalFormat(mContext.getString(R.string.yues));

            NumberFormat dayFormatter = new DecimalFormat(mContext.getString(R.string.ris));
            NumberFormat formatters = null;
            if (langue) {
                formatters = new DecimalFormat(mContext.getString(R.string.years));
            } else {
                formatters = new DecimalFormat("0000");
            }
            String xx = formatter.format(month);
            String dd = dayFormatter.format(day);
            String yy = formatters.format(year);
            if (i != 0) {
                mList.add(yy);

                mList2.add(mBeforeCalendar.get(Calendar.YEAR));
            }

        }


        List<String> mslist = new ArrayList<>();
        List<Integer> mslist2 = new ArrayList<>();
        for (int i = mList.size() - 1; i >= 0; i--) {
            mslist.add(mList.get(i));
            mslist2.add(mList2.get(i));
        }
        mDataList.addAll(0, mslist);
        mDataYearList.addAll(0, mslist2);
    }




    /**
     * 小时
     */
    private void setHour(){
        List<String> list =new ArrayList<>();
        for(int i=0;i<12;i++){
            NumberFormat formatters=null;
            String xx=null;
            if(langue){
                 xx = (i+1)+mContext.getString(R.string.yeu);
            }else {
                 xx = lists[i];
            }


            list.add(i,xx);
        }
        mCycleWheelViewHour.setLabels(list);
        try {
            mCycleWheelViewHour.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH");
        String date = sDateFormat.format(new Date());
        mCycleWheelViewHour.setLabelSelectSize(14f);
        mCycleWheelViewHour.setLabelSize(12f);
        mCycleWheelViewHour.setAlphaGradual(0.7f);
        mCycleWheelViewHour.setCycleEnable(false);
        mCycleWheelViewHour.selection(Integer.valueOf(months)-1);
        mCycleWheelViewHour.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewHour.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewHour.setLabelSelectColor(Color.parseColor("#333333"));
        mCycleWheelViewHour.setSolid(Color.WHITE, Color.WHITE);
        mCycleWheelViewHour.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int positions, String label) {
                //LogUtils.e("mCycleWheelViewDate",position+"===="+mCycleWheelViewDate.getSelection());
                int count=mCycleWheelViewHour.getLabelCount();
                List<String> list =new ArrayList<>();
                int position=positions+1;
                if (position==1||position==3||position==5
                        ||position==7||position==8||position==10||position==12){


                    for(int i=0;i<31;i++){

                        month= mAfterCalendar.get(Calendar.MONTH)+1;//年
                        String xx="";
                        if(langue){
                            xx=(i+1)+mContext.getString(R.string.riis);
                        }else {
                            xx=(i+1)+"";
                        }
                        list.add( i,xx);
                    }
                    mCycleWheelViewMinute.setLabels(list);
                }   if (position==4||position==6||position==9
                        ||position==11){
                    for(int i=0;i<30;i++){
                        month= mAfterCalendar.get(Calendar.MONTH)+1;//年
                        String xx="";
                        if(langue){
                            xx=(i+1)+mContext.getString(R.string.riis);
                        }else {
                            xx=(i+1)+"";
                        }
                        list.add(i,xx);
                    }
                    mCycleWheelViewMinute.setLabels(list);
                } if (position==2){
                    for(int i=0;i<28;i++){
                        month= mAfterCalendar.get(Calendar.MONTH)+1;//年
                        String xx="";
                        if(langue){
                            xx=(i+1)+mContext.getString(R.string.riis);
                        }else {
                            xx=(i+1)+"";
                        }
                        list.add(i,xx);
                    }
                    mCycleWheelViewMinute.setLabels(list);
                }

            }
        });
    }

    /**
     * 分钟
     */
    private void setMinute(){
        List<String> list =new ArrayList<>();
        for(int i=0;i<31;i++){
            month= mAfterCalendar.get(Calendar.MONTH)+1;//年
            String xx="";
            if(langue){
                xx=(i+1)+mContext.getString(R.string.riis);
            }else {
                xx=(i+1)+"";
            }

            list.add(i,xx);
        }
        mCycleWheelViewMinute.setLabels(list);
        try {
            mCycleWheelViewMinute.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("mm");
        String date = sDateFormat.format(new Date());
        mCycleWheelViewMinute.setLabelSelectSize(14f);
        mCycleWheelViewMinute.setLabelSize(12f);
        mCycleWheelViewMinute.setAlphaGradual(0.7f);
        mCycleWheelViewMinute.setCycleEnable(false);
        int day = Integer.valueOf(days);

        mCycleWheelViewMinute.setSelection(day-1);
        mCycleWheelViewMinute.setLabelColor(Color.parseColor("#b3b3b3"));
        mCycleWheelViewMinute.setDivider(Color.parseColor("#e3e3e3"), 1);
        mCycleWheelViewMinute.setLabelSelectColor(Color.parseColor("#333333"));
        mCycleWheelViewMinute.setSolid(Color.WHITE, Color.WHITE);
    }

    public void show(View parentView){
        initView();
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        if(mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }else {
            mPopupWindow.showAtLocation(parentView , Gravity.BOTTOM, 0, 0);
        }
    }



    public void dismiss(){
        if (mPopupWindow!=null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

}
