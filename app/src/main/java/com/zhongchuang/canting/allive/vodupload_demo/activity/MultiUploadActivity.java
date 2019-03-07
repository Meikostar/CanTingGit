package com.zhongchuang.canting.allive.vodupload_demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.vodupload_demo.adapter.VODUploadAdapter;
import com.zhongchuang.canting.allive.vodupload_demo.data.ItemInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 多文件上传示例：可支持OSS上传和点播上传.
 * 列表上传SDK支持多种上传模式.不同模式对应不同的产品，不同的产品对应不同的参数.\n
 // 点播上传。每次上传都是独立的鉴权，所以初始化时，不需要设置鉴权，主要需要两个参数UploadAuthAndAddress,这两个参数由开发者的Appserver提供.参考：https://help.aliyun.com/document_detail/55407.html\n
 // OSS直接上传:STS方式，安全但是较为复杂，建议生产环境下使用。参考STS介绍：\nhttps://help.aliyun.com/document_detail/28756.html
 // OSS直接上传:AK方式，简单但是不够安全，建议测试环境下使用。
 * Created by Mulberry on 2017/11/24.
 */
public class MultiUploadActivity extends AppCompatActivity {

    private String filePathPrefix = "/sdcard/";

    private String uploadAuth = "eyJTZWN1cml0eVRva2VuIjoiQ0FJU3pBUjFxNkZ0NUIyeWZTaklxN1ROUC9qY3VZNXc4cVBUYVdPRTBHbzBUc2gvaS9ISzJ6ejJJSHBLZVhkdUFlQVhzL28wbW1oWjcvWVlsclVxRU1jZUZCeVlNNUFodnN3SnFWdjVKcGZadjh1ODRZQURpNUNqUWJBR3k5OGZtSjI4V2Y3d2FmK0FVQm5HQ1RtZDVNY1lvOWJUY1RHbFFDWnVXLy90b0pWN2I5TVJjeENsWkQ1ZGZybC9MUmRqcjhsbzF4R3pVUEcyS1V6U24zYjNCa2hsc1JZZTcyUms4dmFIeGRhQXpSRGNnVmJtcUpjU3ZKK2pDNEM4WXM5Z0c1MTlYdHlwdm9weGJiR1Q4Q05aNXo5QTlxcDlrTTQ5L2l6YzdQNlFIMzViNFJpTkw4L1o3dFFOWHdoaWZmb2JIYTlZcmZIZ21OaGx2dkRTajQzdDF5dFZPZVpjWDBha1E1dTdrdTdaSFArb0x0OGphWXZqUDNQRTNyTHBNWUx1NFQ0OFpYVVNPRHREWWNaRFVIaHJFazRSVWpYZEk2T2Y4VXJXU1FDN1dzcjIxN290ZzdGeXlrM3M4TWFIQWtXTFg3U0IyRHdFQjRjNGFFb2tWVzRSeG5lelc2VUJhUkJwYmxkN0JxNmNWNWxPZEJSWm9LK0t6UXJKVFg5RXoycExtdUQ2ZS9MT3M3b0RWSjM3V1p0S3l1aDRZNDlkNFU4clZFalBRcWl5a1QwdEZncGZUSzFSemJQbU5MS205YmFCMjUvelcrUGREZTBkc1Znb0xGS0twaUdXRzNSTE5uK3p0Sjl4YUZ6ZG9aeUlrL1NYcWNzNVRGZHp2NHdBVTEvQWNjcGc4RXhtK3FqcjgxT044ZVB1VlRmbzNCSmhxb2FEb2RZZnRCTTZKNjM0MjdMTmhGT0U0aXpNTzV0ZXNkek1SV2hpVFM2d2YzRkUyLzJJamhvRjNVdGJ6VHpxWlU1UHVnblBqampvTFpSTGlPYjM3M2RGRTdwVnArUFVjRDZwNVY1OEV1aU81N3NicUUyVnVoU2xrSjBhZ0FHWXdidWg1OU01c3dhVEh1dE02TkwvL1RNdWdTNnZYcWN3Z1dGQnU3MjZUblduN2Y1L1AvRHo4dVhpVHVnZ3hQNGY3b2dsZE9oSXFwRmtQN2NraVF0TjNaNExDNnBwdHU4ZFdYNWZwZ0RYNXhDN2FNQ3dHMWZRek1ZaVBTR3ltSGlpVzI5NVgwa3ZlbWpjUURlenRWSWphYmZRSWo5aW9Wb0xHb29iRWRvK2JBPT0iLCJBY2Nlc3NLZXlJZCI6IlNUUy5Kd3h0QmhUUVFFYThrRTU0amFCR1NkNGE5IiwiQWNjZXNzS2V5U2VjcmV0IjoiR0N5R05QNkVKVnl6MVhnVVpMWHNWVUJqbW1lODI0NG13eTMxbjMxWjV1UjkiLCJFeHBpcmF0aW9uIjoiMzYwMCJ9";
    private String uploadAddress = "eyJFbmRwb2ludCI6Imh0dHBzOi8vb3NzLWNuLXNoYW5naGFpLmFsaXl1bmNzLmNvbSIsIkJ1Y2tldCI6ImluLTIwMTcwMzIwMTQ0NTE0NzY2LWh0ZTd4aHFrMXQiLCJGaWxlTmFtZSI6InZpZGVvL2U2MGUxM2ItMTYwYzRmMzUwMGEtMDAwNC1hMWY3LTk1My1mOGEzMC5tcDQifQ==";
    private String accessKeyId = "";
    private String accessKeySecret = "";
    private String secretToken = null;
    private String expireTime = null;

    private String endpoint = "";
    private String bucket = "";
    // 工作流的输入路径。
    private String vodPath = "";

    private int index = 0;
    private Random random=new Random();

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_upload);

        getIntentExtra();

        List<ItemInfo> list = new ArrayList<>();
        final VODUploadAdapter vodUploadAdapter = new VODUploadAdapter(MultiUploadActivity.this,
                R.layout.listitem, list);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(vodUploadAdapter);

        // 打开日志。
        OSSLog.enableLog();

        // UI只允许在主线程更新。
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                vodUploadAdapter.notifyDataSetChanged();
            }
        };


        final VODUploadClient uploader = new VODUploadClientImpl(getApplicationContext());

        VODUploadCallback callback = new VODUploadCallback() {

            public void onUploadSucceed(UploadFileInfo info) {
                OSSLog.logDebug("onsucceed ------------------" + info.getFilePath());
                for(int i=0; i<vodUploadAdapter.getCount(); i++) {
                    if (vodUploadAdapter.getItem(i).getFile().equals(info.getFilePath())) {
                        vodUploadAdapter.getItem(i).setStatus(info.getStatus().toString());
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }
            }

            public void onUploadFailed(UploadFileInfo info, String code, String message) {
                OSSLog.logError("onfailed ------------------ " + info.getFilePath() + " " + code + " " + message);
                for(int i=0; i<vodUploadAdapter.getCount(); i++) {
                    if (vodUploadAdapter.getItem(i).getFile().equals(info.getFilePath())) {
                        vodUploadAdapter.getItem(i).setStatus(info.getStatus().toString());
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }
            }

            public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
                OSSLog.logDebug("onProgress ------------------ " + info.getFilePath() + " " + uploadedSize + " " + totalSize);
                for(int i=0; i< vodUploadAdapter.getCount(); i++) {
                    if (vodUploadAdapter.getItem(i).getFile().equals(info.getFilePath())) {
                        vodUploadAdapter.getItem(i).setProgress(uploadedSize * 100 / totalSize);
                        vodUploadAdapter.getItem(i).setStatus(info.getStatus().toString());
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }
            }

            public void onUploadTokenExpired() {
                OSSLog.logError("onExpired ------------- ");
                if (isVodMode()) {
                    // 实现时，重新获取上传凭证:UploadAuth
                    uploadAuth = "eyJTZWN1cml0eVRva2VuIjoiQ0FJU3pBUjFxNkZ0NUIyeWZTaklxYWozSHVMSHJyWkcwSXFkU0YvZWh6Z2JSc2xVMjdQT2lqejJJSHBLZVhkdUFlQVhzL28wbW1oWjcvWVlsclVxRU1jZUZCeVlNNUFodnN3SnFWdjVKcGZadjh1ODRZQURpNUNqUWNzaTB2QWZtSjI4V2Y3d2FmK0FVQm5HQ1RtZDVNY1lvOWJUY1RHbFFDWnVXLy90b0pWN2I5TVJjeENsWkQ1ZGZybC9MUmRqcjhsbzF4R3pVUEcyS1V6U24zYjNCa2hsc1JZZTcyUms4dmFIeGRhQXpSRGNnVmJtcUpjU3ZKK2pDNEM4WXM5Z0c1MTlYdHlwdm9weGJiR1Q4Q05aNXo5QTlxcDlrTTQ5L2l6YzdQNlFIMzViNFJpTkw4L1o3dFFOWHdoaWZmb2JIYTlZcmZIZ21OaGx2dkRTajQzdDF5dFZPZVpjWDBha1E1dTdrdTdaSFArb0x0OGphWXZqUDNQRTNyTHBNWUx1NFQ0OFpYVVNPRHREWWNaRFVIaHJFazRSVWpYZEk2T2Y4VXJXU1FDN1dzcjIxN290ZzdGeXlrM3M4TWFIQWtXTFg3U0IyRHdFQjRjNGFFb2tWVzRSeG5lelc2VUJhUkJwYmxkN0JxNmNWNWxPZEJSWm9LK0t6UXJKVFg5RXoycExtdUQ2ZS9MT3M3b0RWSjM3V1p0S3l1aDRZNDlkNFU4clZFalBRcWl5a1QwdEZncGZUSzFSemJQbU5MS205YmFCMjUvelcrUGREZTBkc1Znb0xGS0twaUdXRzNSTE5uK3p0Sjl4YUZ6ZG9aeUlrL1NYcWNzNVRGZHp2NHdBVTEvQWNjcGc4RXhtK3FqcjgxT044ZVB1VlRmbzNCSmhxb2FEb2RZZnRCTTZKNjM0MjdMTmhGT0U0aXpNTzV0ZXNkek1SV2hpVFM2d2YzRkUyLzJJamhvRjNVdGJ6VHpxWlU1UHVnblBqampvTFpSTGlPYjM3M2RGRTdwVnArUFVjRDZwNVY1OEV1aU81N3NicUUyVnVoU2xrSjBhZ0FFMGNOSk1tbmNiNk5iOVlHWU5mVXZJNEpOQ1pkME9hQ2lTRHBNSFFKdXQ4SkVmeWpBRXkvQ1lnUVVSN3FTVTlYdk9zTHo1S2RXZlowTDdWZHcwem9TOHVMYzZvMkFIdm5UR29QNUpEd1J6TUVPKzVyZWdqdmZZREt3bk5GNVZyaW5LbG1XaWl4UHN0YjFSc21JWVNsQmNiQzBtU1hSeXREU0REdHVJMFBXcEpBPT0iLCJBY2Nlc3NLZXlJZCI6IlNUUy5Ia0JVWHNDaWdnSHZKeW9jOE5KRng0dmVoIiwiQWNjZXNzS2V5U2VjcmV0IjoidmlvOUdSOXhmVjVxS1p6MmdmNVJzYjRicDlwNFRCSGZ5MXBCaTNQZ2FzViIsIkV4cGlyYXRpb24iOiIzNjAwIn0=";
                    uploader.resumeWithAuth(uploadAuth);
                } else if (isSTSMode()) {
                    // 实现时，重新获取STS临时账号用于恢复上传
                    //uploader.resumeWithToken(accessKeyId, accessKeySecret, secretToken, expireTime);
                }
            }

            public void onUploadRetry(String code, String message) {
                OSSLog.logError("onUploadRetry ------------- ");
            }

            public void onUploadRetryResume() {
                OSSLog.logError("onUploadRetryResume ------------- ");
            }

            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
                OSSLog.logError("onUploadStarted ------------- ");
                if (isVodMode()) {
                    uploader.setUploadAuthAndAddress(uploadFileInfo, uploadAuth, uploadAddress);
                }
                OSSLog.logError("file path:" + uploadFileInfo.getFilePath() +
                        ", endpoint: " + uploadFileInfo.getEndpoint() +
                        ", bucket:" + uploadFileInfo.getBucket() +
                        ", object:" + uploadFileInfo.getObject() +
                        ", status:" + uploadFileInfo.getStatus());
            }
        };

        if (isVodMode()) {
            // 点播上传。每次上传都是独立的鉴权，所以初始化时，不需要设置鉴权
            uploader.init(callback);
        } else if (isSTSMode()) {
            // OSS直接上传:STS方式，安全但是较为复杂，建议生产环境下使用。
            // 临时账号过期时，在onUploadTokenExpired事件中，用resumeWithToken更新临时账号，上传会续传。
            uploader.init(accessKeyId, accessKeySecret, secretToken, expireTime, callback);
        } else {
            // OSS直接上传:AK方式，简单但是不够安全，建议测试环境下使用。
            uploader.init(accessKeyId, accessKeySecret, callback);
        }
        uploader.setPartSize(1024 * 1024);

        Button btnAdd = (Button) findViewById(R.id.btn_addFile);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (vodUploadAdapter.getCount() >= 4) {
//                    Context context = getApplicationContext();
//                    Toast.makeText(context, "列表放不下啦!", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                String fineName = filePathPrefix + "aliyunmedia"+ index +".mp4";
                String ossName  = vodPath + index + ".mp4";

                // 如果文件不存在，生成一个临时文件。正式环境换成用户视频文件。
                generateTempFile(fineName, random.nextInt(300000) + 100000);
                if (isVodMode()) {
                    uploader.addFile(fineName, getVodInfo());
                } else {
                    uploader.addFile(fineName, endpoint, bucket, ossName, getVodInfo());
                }

                OSSLog.logDebug("添加了一个文件：" + fineName);
                // 获取刚添加的文件。
                UploadFileInfo uploadFileInfo = uploader.listFiles().get(uploader.listFiles().size() - 1);

                // 添加到列表。
                ItemInfo info = new ItemInfo();
                info.setFile(uploadFileInfo.getFilePath());
                info.setProgress(0);
                if (!isVodMode()) {
                    info.setOss("http://" + uploadFileInfo.getBucket() + "." +
                            uploadFileInfo.getEndpoint().substring(7) + "/" +
                            uploadFileInfo.getObject());
                }
                info.setStatus(uploadFileInfo.getStatus().toString());
                vodUploadAdapter.add(info);

                index++;
            }
        });

        Button btnDelete = (Button) findViewById(R.id.btn_deleteFile);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vodUploadAdapter.getCount() == 0) {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "列表为空啦!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int index = uploader.listFiles().size() - 1;
                UploadFileInfo info = uploader.listFiles().get(index);
                uploader.deleteFile(index);
                OSSLog.logDebug("删除了一个文件：" + info.getFilePath());

                vodUploadAdapter.remove(vodUploadAdapter.getItem(index));

                for(UploadFileInfo uploadFileInfo : uploader.listFiles()) {
                    if (isVodMode()) {
                        OSSLog.logDebug("file path:" + uploadFileInfo.getFilePath() +
                                ", status:" + uploadFileInfo.getStatus());
                    } else {
                        OSSLog.logDebug("file path:" + uploadFileInfo.getFilePath() +
                                ", endpoint: " + uploadFileInfo.getEndpoint() +
                                ", bucket:" + uploadFileInfo.getBucket() +
                                ", object:" + uploadFileInfo.getObject() +
                                ", status:" + uploadFileInfo.getStatus());
                    }
                }
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btn_cancelFile);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast.makeText(context, "取消文件上传", Toast.LENGTH_SHORT).show();

                int index = uploader.listFiles().size() - 1;
                UploadFileInfo info = uploader.listFiles().get(index);
                uploader.cancelFile(index);

                for(int i=0; i<vodUploadAdapter.getCount(); i++) {
                    if (vodUploadAdapter.getItem(i).getFile().equals(info.getFilePath())) {
                        vodUploadAdapter.getItem(i).setStatus(info.getStatus().toString());
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }

                return;
            }
        });

        Button btnResumeFile = (Button) findViewById(R.id.btn_resumeFile);
        btnResumeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast.makeText(context, "恢复文件上传", Toast.LENGTH_SHORT).show();

                int index = uploader.listFiles().size() - 1;
                UploadFileInfo info = uploader.listFiles().get(index);
                uploader.resumeFile(index);

                for(int i=0; i<vodUploadAdapter.getCount(); i++) {
                    if (vodUploadAdapter.getItem(i).getFile().equals(info.getFilePath())) {
                        vodUploadAdapter.getItem(i).setStatus(info.getStatus().toString());
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }

                return;
            }
        });

        Button btnList = (Button) findViewById(R.id.btn_getList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast.makeText(context, "获取文件列表，" + uploader.listFiles().size(),
                        Toast.LENGTH_SHORT).show();

                for(UploadFileInfo uploadFileInfo : uploader.listFiles()) {
                    if (isVodMode()) {
                        OSSLog.logDebug("file path:" + uploadFileInfo.getFilePath() +
                                ", status:" + uploadFileInfo.getStatus());
                    } else {
                        OSSLog.logDebug("file path:" + uploadFileInfo.getFilePath() +
                                ", endpoint: " + uploadFileInfo.getEndpoint() +
                                ", bucket:" + uploadFileInfo.getBucket() +
                                ", object:" + uploadFileInfo.getObject() +
                                ", status:" + uploadFileInfo.getStatus());
                    }
                }
                return;
            }
        });

        Button btnClear = (Button) findViewById(R.id.btn_clearList);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                uploader.clearFiles();

                Toast.makeText(context, "清理文件列表完成。", Toast.LENGTH_SHORT).show();
                vodUploadAdapter.clear();
                OSSLog.logDebug("列表大小为：" + uploader.listFiles().size());

                return;
            }
        });

        Button btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast.makeText(context, "开始上传", Toast.LENGTH_SHORT).show();

                uploader.start();
            }
        });

        Button btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast.makeText(context, "停止上传", Toast.LENGTH_SHORT).show();

                uploader.stop();
            }
        });

        Button btnPause = (Button) findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast.makeText(context, "暂停上传", Toast.LENGTH_SHORT).show();

                uploader.pause();
                return;
            }
        });

        Button btnResume = (Button) findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast.makeText(context, "恢复上传", Toast.LENGTH_SHORT).show();

                uploader.resume();
                return;
            }
        });
    }

    private VodInfo getVodInfo() {
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle("标题" + index);
        vodInfo.setDesc("描述." + index);
        vodInfo.setCateId(index);
        vodInfo.setIsProcess(true);
        vodInfo.setCoverUrl("http://www.taobao.com/" + index + ".jpg");
        List<String> tags = new ArrayList<>();
        tags.add("标签" + index);
        vodInfo.setTags(tags);
        if (isVodMode()) {
            vodInfo.setIsShowWaterMark(false);
            vodInfo.setPriority(7);
        } else {
            vodInfo.setUserData("自定义数据" + index);
        }
        return vodInfo;
    }

    private void generateTempFile(String filePath, long fileSize) {
        String content = "1";
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.createNewFile();
                dir.mkdir();
            } else {
                return;
            }

            File f = new File(filePath);
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);

            long size = 0;
            while(fileSize > size) {
                fos.write(content.getBytes());
                size += content.length();
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isVodMode() {
        return (null != uploadAuth && uploadAuth.length() > 0);
    }

    private boolean isSTSMode() {
        if (!isVodMode()) {
            return (null != secretToken && secretToken.length() > 0 &&
                    null != expireTime && expireTime.length() > 0);
        }
        return false;
    }

    private void getIntentExtra(){
        uploadAuth = getIntent().getStringExtra("UploadAuth");
        uploadAddress = getIntent().getStringExtra("UploadAddress");
    }

}
