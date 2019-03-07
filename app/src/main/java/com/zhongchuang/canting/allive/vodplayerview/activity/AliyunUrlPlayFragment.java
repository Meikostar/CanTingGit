package com.zhongchuang.canting.allive.vodplayerview.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.vodplayerview.constants.PlayParameter;

//import com.google.zxing.activity.CaptureActivity;


/**
 * vid设置界面
 * Created by Mulberry on 2018/4/4.
 */
public class AliyunUrlPlayFragment extends Fragment implements OnClickListener{
    private static final int REQ_CODE_PERMISSION = 0x1111;
    ImageView ivQRcode;
    EditText etPlayUrl;

    /**
     * 返回给上个activity的resultcode: 100为vid播放类型, 200为URL播放类型
     */
    private static final int CODE_RESULT_URL = 200;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_urlplay_layout, container, false);

        ivQRcode =(ImageView)v.findViewById(R.id.iv_qrcode);
        etPlayUrl = (EditText)v.findViewById(R.id.et_play_url);
        ivQRcode.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_qrcode){
            if (ContextCompat.checkSelfPermission(AliyunUrlPlayFragment.this.getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                // Do not have the permission of camera, request it.
                ActivityCompat.requestPermissions(AliyunUrlPlayFragment.this.getActivity(),
                    new String[] {Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
            } else {
                // Have gotten the permission

            }
        }
    }

    /**
     * start player by Url
     */
    public void startPlayerByUrl(){
        if (!TextUtils.isEmpty(etPlayUrl.getText().toString())){
            Intent intent = new Intent();
            intent.setClass(this.getActivity(), AliyunPlayerSkinActivity.class);

            PlayParameter.PLAY_PARAM_TYPE = "localSource";
            PlayParameter.PLAY_PARAM_URL = etPlayUrl.getText().toString();

            getActivity().setResult(CODE_RESULT_URL);
            getActivity().finish();
        } else {
            Toast.makeText(this.getActivity().getApplicationContext(), R.string.play_url_null_toast, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Jump to CaptureActivity and pass some parameters,  set requestCode to receive the returned data
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission

                } else {
                    // User disagree the permission
                    Toast.makeText(this.getActivity(), "You must agree the camera permission request before you use the code scan function", Toast.LENGTH_LONG).show();
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
