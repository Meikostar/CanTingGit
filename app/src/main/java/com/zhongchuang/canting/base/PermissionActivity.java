package com.zhongchuang.canting.base;

import android.support.annotation.NonNull;


import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.permission.AfterPermissionGranted;
import com.zhongchuang.canting.permission.EasyPermissions;

import java.util.List;

/**
 * Created by fl on 2017/4/28.
 */

public abstract class PermissionActivity extends BaseTitle_Activity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_CODE_PERMISSION_OTHER = 101;



    protected void requestPermission(  String... permissions){

        if (EasyPermissions.hasPermissions(this,permissions)){
            permissionSuccess();
        }else {
            EasyPermissions.requestPermissions(this,getString(R.string.permission_message_permission_rationale), R.string.permission_resume, R.string.permission_cancel,REQUEST_CODE_PERMISSION_OTHER,permissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_OTHER)
    public abstract void permissionSuccess();

    public abstract void permissionFail();


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        permissionFail();
    }


}
