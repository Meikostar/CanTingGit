package com.zhongchuang.canting.easeui.utils;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;
import android.text.TextUtils;

import com.zhongchuang.canting.easeui.bean.GROUP_USER;
import com.zhongchuang.canting.easeui.bean.USER;
import com.zhongchuang.canting.easeui.ui.ImageGridActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.valuesfeng.picker.universalimageloader.utils.L;

public class Utils {

    private Utils() {
    }

    @SuppressLint("NewApi")
    public static void enableStrictMode() {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(ImageGridActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }


    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;

    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static List<Size> getResolutionList(Camera camera) {
        Parameters parameters = camera.getParameters();
        return parameters.getSupportedPreviewSizes();
    }

    public static class ResolutionComparator implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            if (lhs.height != rhs.height)
                return lhs.height - rhs.height;
            else
                return lhs.width - rhs.width;
        }

    }

    public static int InfoFunction(USER user) {
        int function = 0;
        if (user.is_friend == 1) {
            return 3;
        }
        if (user.is_request == 0) {
            switch (user.requests_status) {
                case 0:
                    function = 1;
                    break;
                case 1:
                    function = 2;
                    break;
                case 2:
                    function = 3;
                    break;
            }
        } else {
            switch (user.requests_status) {
                case 0:
                    function = 1;
                    break;
                case 1:
                    function = 4;
                    break;
                case 2:
                    function = 3;
                    break;
            }
        }
        return function;
    }

    public static String GetTypeName(int Type) {
        String name = "";
        switch (Type) {
            case 1:
                name = "砼车司机";
                break;
            case 2:
                name = "泵车司机";
                break;
            case 3:
                name = "工地负责人";
                break;
            case 4:
                name = "搅拌站调度员";
                break;
            case 5:
                name = "其他";
                break;
            case 6:
                name = "搅拌站";
                break;
            case 7:
                name = "工地";
                break;
            case 8:
                name = "物流";
                break;
            case 9:
                name = "其他";
                break;
        }
        return name;
    }

    public static String userGroups(List<USER> userList) {
        String userGroups = null;
        for (int i = 0; i < userList.size(); i++) {
            if (i == 0)
                userGroups = userList.get(i).easemob;
            else
                userGroups += "," + userList.get(i).easemob;
        }
        return userGroups;
    }

    public static String userGroupsUser(List<GROUP_USER> userList) {
        String userGroups = null;
        for (int i = 0; i < userList.size(); i++) {
            if (i == 0)
                userGroups = userList.get(i).getUserinfo().easemob;
            else
                userGroups += "," + userList.get(i).getUserinfo().easemob;
        }
        return userGroups;
    }

    public static String userGroupsId(List<GROUP_USER> userList) {
        String userGroups = null;
        for (int i = 0; i < userList.size(); i++) {
            if (i == 0)
                userGroups = userList.get(i).getUserinfo().user_id + "";
            else
                userGroups += "," + userList.get(i).getUserinfo().user_id;
        }
        return userGroups;
    }

    public static String getGroupImgStr(ArrayList<String> strList) {
        String userGroups = null;
        if (strList == null) {
            return "";
        }
        for (int i = 0; i < strList.size(); i++) {
            L.e("strList" + strList.get(i));
            if (i == 0)
                userGroups = strList.get(i);
            else{
                if (TextUtils.isEmpty(strList.get(i))){
                    userGroups+=",,,";
                }else {
                    userGroups += ",,," + strList.get(i);
                }
            }

        }
        return userGroups;
    }

    /**
     * @return 处理好的数值，每个增加string
     * @author 沈翊君
     * @createtime 2016-6-12
     * @List<String> 待处理的数值
     * @String 待添加的内容
     */
    public static String stringAdd(List<String> strings, String string) {
        String s = "";
        if (strings != null && strings.size() > 0) {
            for (int i = 0; i < strings.size(); i++) {
                if (i == 0) {
                    s = strings.get(i);
                } else {
                    s = s + string + strings.get(i);
                }
            }
            return s;
        }
        return null;
    }
}
