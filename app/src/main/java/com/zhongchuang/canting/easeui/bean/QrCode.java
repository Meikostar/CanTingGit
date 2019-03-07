package com.zhongchuang.canting.easeui.bean;


import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

//import com.yshstudio.eqc.chat.hyphenate.ui.AddFriend.UserInfoActivity;

/**
 * Created by syj on 2016/12/20.
 */
public class QrCode implements Serializable {

    private String easemob;
    private String easemob_group_id;
    private int type;

    public String getEasemob() {
        return easemob;
    }

    public void setEasemob(String easemob) {
        this.easemob = easemob;
    }

    public String getEasemob_group_id() {
        return easemob_group_id;
    }

    public void setEasemob_group_id(String easemob_group_id) {
        this.easemob_group_id = easemob_group_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static String setQrCode(String easemob, String easemob_group_id, int type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            if (type == 2) {
                jsonObject.put("easemob", easemob);
            } else {
                jsonObject.put("easemob", easemob_group_id);
            }
//            KLog.e("qrcode" + jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static QrCode getQrCode(String scanResult) {
        QrCode qrCode = null;
        if (!TextUtils.isEmpty(scanResult)) {
            try {
                JSONObject jsonObject = new JSONObject(scanResult);
                Gson gson = new Gson();
                qrCode = gson.fromJson(jsonObject.toString(), QrCode.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return qrCode;
    }

//    public static void successJump(QrCode qrCode, Context context, IChatModelDelegate delegate) {
//        Intent intent;
//        if (qrCode.getType() == 1) {
//            new ChatModel().getGroupInfo(delegate, qrCode.getEasemob());
//        } else if (qrCode.getType() == 2) {
//            if (qrCode.getEasemob().equals(EMClient.getInstance().getCurrentUser())) {
//                ToastView toastView = new ToastView(context, "扫描的是自己的二维码");
//                toastView.setGravity(Gravity.CENTER, 0, 0);
//                toastView.show();
//                return;
//            }
//            ((BaseActivity)context).dimessProgress();
////            intent = new Intent(context, UserInfoActivity.class).setAction(qrCode.getEasemob());
////            ((Activity) context).startActivity(intent);
//        }
//    }

}
