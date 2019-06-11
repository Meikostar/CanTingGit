package com.zhongchuang.canting.activity.mall;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionFail;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.ImageUploadView;
import com.zhongchuang.canting.widget.NavigationBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.valuesfeng.picker.ImageSelectActivity;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.widget.ImageLoaderEngine;

/***
 * 记录编辑
 */
public class AfterSaleActivity extends BaseActivity1 {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.et_room)
    ClearEditText etRoom;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_room_der)
    ClearEditText etRoomDer;
    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.tv_save)
    TextView tvSave;
    private List<File> files = new ArrayList<>();

    private ArrayList<UploadFileBean> img_path = new ArrayList<>();


    @Override
    public void initViews() {
        setContentView(R.layout.activity_sale_after);
        ButterKnife.bind(this);


    }

    @Override
    public void bindEvents() {


        ivAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                PermissionGen.with(AfterSaleActivity.this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .request();

            }
        });
//        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
//            @Override
//            public void navigationLeft() {
//                finish();
//            }
//
//            @Override
//            public void navigationRight() {
//                if (img_path.size() == 0) {
//                    showToasts("请选择图片 ");
//                    return;
//                }
//                if (TextUtil.isEmpty(etContent.getText().toString())) {
//                    showToasts("说点什么吧");
//                    return;
//                }
//                showProgress("上传中...");
//                int a = 0;
//
//                for (UploadFileBean bean : img_path) {
//                    a++;
//                    if (TextUtil.isEmpty(bean.getForderPath())) {
//
//                        if (a == 1) {
//                            url = bean.getUrl_();
//                        } else {
//                            url = url + "," + bean.getUrl_();
//                        }
//                        if(a==img_path.size()){
//                            presenter.growRecordUpdate(url, content, wipi.id);
//                        }
//                    } else {
//                        i = a ;
//                        updateAvator(new File(bean.getForderPath()));
//                        return;
//                    }
//                }
//            }
//
//            @Override
//            public void navigationimg() {
//
//            }
//        });
    }

    private int poition;

    @Override
    public void initData() {

    }

    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardSuccess() {
        Picker.from(this)
                .count(8 - img_path.size())
                .enableCamera(true)
                .setEngine(new ImageLoaderEngine())
                .setAdd_watermark(false)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @PermissionFail(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardFailed() {
//        showToast(getString(R.string.getqxdefault));
    }

    private int REQUEST_CODE_CHOOSE = 3;
    private int upload_position;

    /**
     * 删除照片和修改相册的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public String path;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                List<String> imgs = data.getStringArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION);
                path = imgs.get(0);
                for (String imgPath : imgs) {
                    if (imgPath != null) {
                        UploadFileBean bean = new UploadFileBean(1);
                        bean.setForderPath(imgPath);
                        img_path.add(bean);
                    }
                }

                upFlile(path);
                notifyImageDataChange();
                upload_position = 0;
            }

        }
    }

    public void upFlile(final String path) {


        QiniuUtils.getInstance().upFile(path, "tOw86noHVJUdmqcx66cgyEio6_52dKnb-qQYib8D:C5GPWJz8cyNtH6k8IUYlbHo81fs=:eyJzY29wZSI6ImltYWdlcyIsImRlYWRsaW5lIjoxNTMyNjc4MTU4fQ==", new QiniuUtils.CompleteListener() {
            @Override
            public void completeListener(String url) {
                showToasts(getString(R.string.sccg));
                dimessProgress();
                String urls = url;
            }
        });

    }

    private Map<String, String> map = new HashMap<>();
    private Map<String, File> maps = new HashMap<>();


    private String url = "";
    private int i = 1;
    private Gson mGson;

    private void notifyImageDataChange() {

    }


}
