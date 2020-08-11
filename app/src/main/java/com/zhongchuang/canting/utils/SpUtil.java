package com.zhongchuang.canting.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhongchuang.canting.app.CanTingAppLication;

/**
 * Created by Administrator on 2017/11/14.
 */

public class SpUtil {

    private static SharedPreferences sp;
    public static String xml_name = "chusheng";



    public static String getToken(Context context){
        return getString(context,"token","");
    }
    public static String getRoomId(Context context){
        return getString(context,"room_id","");
    }
    public static String getChatRoomId(Context context){
        return getString(context,"chatroomsId","");
    }
    public static String getLa(Context context){
        return getString(context,"la","");
    }
    public static String getLg(Context context){
        return getString(context,"lg","");
    }
    public static String getAddress(Context context){
        return getString(context,"adress","");
    }

    public static String getUrl(Context context){
        return getString(context,"idCardFrontImg","");
    }

    public static String getUserInfoId(Context context){
        return getString(context,"userInfoId","");
    }
    public static String getAvar(Context context){
        return getString(context,"ava","");
    }
    public static String getLiveUrl(Context context){
        return getString(context,"live_url","");
    }
    public static String getName(Context context){
        return getString(context,"name","");
    }
    public static String getNick(Context context){
        return getString(context,"nick","");
    }
    public static String getSign(Context context){
        return getString(context,"sign","");
    }
    public static String isAnchor(Context context){
        return getString(context,"isAnchor","");
    }

    public static String getMobileNumber(Context context){
        return getString(context,"mobileNumber","");
    }

    public static String getRingLetterName(Context context){
        return getString(context,"ringLetterName","");
    }

    public static String getRingLetterPwd(Context context){
        return getString(context,"ringLetterPwd","");
    }

/*


1.1  更新
    SpUtil.putString(getActivity(),"mobileNumber",mobileNumber);//手机号
    SpUtil.putString(getActivity(),"userInfoId",userInfoId);//密码
    SpUtil.putString(getActivity(),"token",token值);//token值
    SpUtil.putString(getActivity(),"ringLetterName",ringLetterName);;//环信登录名
    SpUtil.putString(getActivity(),"ringLetterPwd",ringLetterPwd);//环信登录密码
     SpUtil.putString(getActivity(),"code",checkNum);//短信验证码


*
* */



    /*
    1.0  使用  废弃
    mSpUtil.putString(getActivity(),"mobileNumber", mobileNumber);//手机号
        mSpUtil.putString(getActivity(),"password", passWord);//密码
        mSpUtil.putString(getActivity(),"token", userToken);//token值
        mSpUtil.putString(getActivity(),"userId", userId);//userId值    &&&&&&废弃
        SpUtil.putString(getActivity(), "ringLetterName", hunXinName);//环信登录名
        SpUtil.putString(getActivity(), "ringLetterPwd", hunXinPawd);//环信登录密码



    */


    /**
     * 写入String变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public static void putString(Context ctx, String key, String value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }


    public static void putInt(Context ctx, String key, int value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    public static boolean isAdmin( String key) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = CanTingAppLication.getInstance().getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        int groupId = sp.getInt(key, 0);

        return  groupId==1 ;
    }
    public static int getInt(Context ctx, String key, int value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, value);
    }
   public static String getLangueType(Context context){
        return getString(context,"LangueType","zh-rCN");
   }
    /**
     * 读取String标示从sp中
     *
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @return 默认值或者此节点读取到的结果
     */
    public static String getString(Context ctx, String key, String d) {
        String name="";
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        if(TextUtil.isNotEmpty(sp.getString(key, d))){
            name=sp.getString(key, d);
        }
        return name;
    }

    /**
     * 从sp中移除指定节点
     *
     * @param ctx 上下文环境
     * @param key 需要移除节点的名称
     */
    public static void remove(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }






}
