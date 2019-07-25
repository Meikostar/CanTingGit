package com.zhongchuang.canting.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.easeui.EaseUI;
import com.zhongchuang.canting.easeui.EaseUI.EaseUserProfileProvider;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.utils.TextUtil;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).asBitmap().placeholder(R.drawable.ease_default_avatar).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            if(CanTingAppLication.easeDatas==null){
                Glide.with(context).load(username).asBitmap().placeholder(R.drawable.ease_default_avatar).into(imageView);
            }else {
                String nick = CanTingAppLication.easeDatas.get(user.getNickname());
                if(TextUtil.isNotEmpty(nick)){

                    String[] split = nick.split(",");
                     if(split.length==2){
                         nick=split[1];
                     }

                    Glide.with(context).load(nick).asBitmap().placeholder(R.drawable.ease_default_avatar).into(imageView);
                }else {
                    Glide.with(context).load(username).asBitmap().placeholder(R.drawable.ease_default_avatar).into(imageView);
                }

            }

        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNickname() != null){
        	    if(CanTingAppLication.easeDatas==null){
                    textView.setText(user.getNickname());
                }else {
                    String nick = CanTingAppLication.easeDatas.get(user.getNickname());
                    if(TextUtil.isNotEmpty(nick)){

                        String[] split = nick.split(",");

                        nick=split[0];
                        textView.setText(nick);
                    }else {
                        textView.setText(user.getNickname());
                    }

                }



        	}else{
        		textView.setText(username);
        	}

        }
    }

    /**
     * set user's nickname
     */
    public static String getUserNick(String username){

            EaseUser user = getUserInfo(username);
            if(user != null && user.getNickname() != null){
                return user.getNickname();
            }else{
               return username;
            }


    }
}
