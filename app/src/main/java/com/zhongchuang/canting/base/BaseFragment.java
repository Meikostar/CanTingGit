package com.zhongchuang.canting.base;

import android.content.Context;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.zhongchuang.canting.hud.HudHelper;


/**
 *   设置状态栏颜色的基类
 * Created by Administrator on 2016/12/17 0017.
 */

public class BaseFragment extends Fragment {

    private HudHelper helper;



    public void showPress(){
        if (helper==null){
            helper = HudHelper.getInstance();
        }

        helper.showPress(getActivity());
    }

    public void showPress(String label){
        if (helper==null){
            helper = HudHelper.getInstance();
        }

        helper.showPress(getActivity(),label);
    }

    public void hidePress(){
        if (helper!=null){
            helper.hide();
        }
    }


}
