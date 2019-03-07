package com.zhongchuang.canting.allive.vodupload_demo.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.alibaba.sdk.android.vod.upload.common.UploadStateType;
import com.alibaba.sdk.android.vod.upload.common.VodUploadStateType;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.vodupload_demo.adapter.VODUploadAdapter;
import com.zhongchuang.canting.allive.vodupload_demo.data.ItemInfo;
import com.zhongchuang.canting.allive.vodupload_demo.utils.FileUtils;
import com.zhongchuang.canting.allive.vodupload_demo.utils.NetWatchdog;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示OSS多文件上传示例,上传完成之后直接上传到用户的bucket，可指定用户自己的endpoint、bucket、objectKey
 * 功能点：
 * 1.多文件队列上传
 * 2.可指定endpoint、bucket、objectKey
 * 3.STS过期能够刷新继续上传
 * Created by Mulberry on 2018/1/4.
 */
public class    OSSMultiUploadActivity extends AppCompatActivity {
    private String accessKeyId;
    private String accessKeySecret;
    private String secretToken;
    private String expireTime;

    private String endpoint;
    private String bucket;
    private String prefix;
    private String objectKey;
    // 工作流的输入路径。
    private String vodPath = "";

    private int index = 0;

    private Handler handler;
    private VODUploadClient uploader;
    private VODUploadAdapter vodUploadAdapter;
    private NetWatchdog netWatchdog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_upload);

        getIntentExtra();


        final List<ItemInfo> list = new ArrayList<>();
        vodUploadAdapter = new VODUploadAdapter(OSSMultiUploadActivity.this,
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


        uploader = new VODUploadClientImpl(getApplicationContext());
        VODUploadCallback callback = new VODUploadCallback() {
            @Override
            public void onUploadSucceed(UploadFileInfo info) {
                OSSLog.logDebug("onsucceed ------------------" + info.getFilePath());
                for(int i = 0; i< vodUploadAdapter.getCount(); i++) {
                    if (vodUploadAdapter.getItem(i).getFile().equals(info.getFilePath())){
                        vodUploadAdapter.getItem(i).setStatus(info.getStatus().toString());
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }
            }

            @Override
            public void onUploadFailed(UploadFileInfo info, String code, String message) {
                OSSLog.logError("onfailed ------------------ " + info.getFilePath() + " " + code + " " + message);
                for(int i = 0; i< vodUploadAdapter.getCount(); i++) {
                    if (vodUploadAdapter.getItem(i).getFile().equals(info.getFilePath())){
                        vodUploadAdapter.getItem(i).setStatus(info.getStatus().toString());
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }
            }

            @Override
            public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
                OSSLog.logDebug("onProgress ------------------ " + info.getFilePath() + " " + uploadedSize + " " + totalSize);
                for(int i = 0; i< vodUploadAdapter.getCount(); i++) {
                    if (vodUploadAdapter.getItem(i).getStatus() == String.valueOf(UploadStateType.SUCCESS)
                            || vodUploadAdapter.getItem(i).getStatus() == String.valueOf(UploadStateType.CANCELED)){
                        continue;
                    }
                    if (vodUploadAdapter.getItem(i).getFile().equals(info.getFilePath())) {
                        vodUploadAdapter.getItem(i).setProgress(uploadedSize * 100 / totalSize);
                        vodUploadAdapter.getItem(i).setStatus(info.getStatus().toString());
                        handler.sendEmptyMessage(0);
                        break;
                    }
                }

            }

            @Override
            public void onUploadTokenExpired() {
                OSSLog.logError("onExpired ------------- ");
                // 实现时，重新获取STS临时账号用于恢复上传
                uploader.resumeWithToken(accessKeyId, accessKeySecret, secretToken, expireTime);
            }

            @Override
            public void onUploadRetry(String code, String message) {
                OSSLog.logError("onUploadRetry ------------- ");
            }

            @Override
            public void onUploadRetryResume() {
                OSSLog.logError("onUploadRetryResume ------------- ");
            }

            @Override
            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
                OSSLog.logError("onUploadStarted ------------- ");
                OSSLog.logError("file path:" + uploadFileInfo.getFilePath() +
                        ", endpoint: " + uploadFileInfo.getEndpoint() +
                        ", bucket:" + uploadFileInfo.getBucket() +
                        ", object:" + uploadFileInfo.getObject() +
                        ", status:" + uploadFileInfo.getStatus());
            }
        };

        if (!TextUtils.isEmpty(accessKeyId) && !TextUtils.isEmpty(accessKeySecret) &&
                !TextUtils.isEmpty(secretToken) && !TextUtils.isEmpty(expireTime) ) {
            // OSS直接上传:STS方式，安全但是较为复杂，建议生产环境下使用。
            // 临时账号过期时，在onUploadTokenExpired事件中，用resumeWithToken更新临时账号，上传会续传。
            uploader.init(accessKeyId, accessKeySecret, secretToken, expireTime, callback);
        }

        //设置分片大小
        uploader.setPartSize(500 * 1024);
        Button btnAdd = (Button) findViewById(R.id.btn_addFile);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCreatingFile){
                    Toast.makeText(v.getContext(),"Video ic copying，Please wait!",Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(accessKeyId) || TextUtils.isEmpty(accessKeySecret) ||
                        TextUtils.isEmpty(secretToken) || TextUtils.isEmpty(expireTime) ||
                        TextUtils.isEmpty(endpoint) || TextUtils.isEmpty(bucket)){
                    Toast.makeText(v.getContext(),"OSS上传请务必保证STS(accessKeyId/accessKeySecret/secretTokenexpireTime)和endpoint及bucket信息不为空！",Toast.LENGTH_LONG).show();
                    return;
                }
                // 保证本地文件地址存在，如果文件不存在，拷贝一个临时文件，正式环境替换用户自己的视频
                getFileTask().execute();
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
                        OSSLog.logDebug("file path:" + uploadFileInfo.getFilePath() +
                                ", endpoint: " + uploadFileInfo.getEndpoint() +
                                ", bucket:" + uploadFileInfo.getBucket() +
                                ", object:" + uploadFileInfo.getObject() +
                                ", status:" + uploadFileInfo.getStatus());
                }
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btn_cancelFile);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();


                int index = uploader.listFiles().size() - 1;
                if (index < 0) {
                    Toast.makeText(context, "请先添加文件再执行取消操作.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "取消文件上传", Toast.LENGTH_SHORT).show();
                UploadFileInfo info = uploader.listFiles().get(index);
                uploader.cancelFile(index);

                if (vodUploadAdapter.getItem(index).getStatus() != String.valueOf(UploadStateType.CANCELED)) {
                    OSSLog.logError("uploader index" + index);
                    vodUploadAdapter.getItem(index).setStatus(info.getStatus().toString());
                    handler.sendEmptyMessage(0);
                }

                return;
            }
        });

        Button btnResumeFile = (Button) findViewById(R.id.btn_resumeFile);
        btnResumeFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploader == null){
                    return;
                }
                Context context = getApplicationContext();
                if (uploader.listFiles().size()<=0)
                {
                    Toast.makeText(context, "请先添加文件再执行恢复操作.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "恢复文件上传", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < uploader.listFiles().size(); i++) {
                    UploadFileInfo info = uploader.listFiles().get(i);
                    if (info.getStatus()== UploadStateType.CANCELED) {
                        uploader.resumeFile(i);
                        vodUploadAdapter.getItem(i).setStatus(uploader.listFiles().get(i).getStatus().toString());
                    }
                }
                handler.sendEmptyMessage(0);
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
                        OSSLog.logDebug("file path:" + uploadFileInfo.getFilePath() +
                                ", endpoint: " + uploadFileInfo.getEndpoint() +
                                ", bucket:" + uploadFileInfo.getBucket() +
                                ", object:" + uploadFileInfo.getObject() +
                                ", status:" + uploadFileInfo.getStatus());
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
        //
        netWatchdog = new NetWatchdog(this);
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                if (uploader!=null){
                    VodUploadStateType type = uploader.getStatus();
                    if (type== VodUploadStateType.PAUSED){
                        uploader.resume();
                    }
                }


            }

            @Override
            public void onNetUnConnected() {
                if (uploader!=null){
                    VodUploadStateType type = uploader.getStatus();
                    if (type== VodUploadStateType.STARTED){
                        uploader.pause();
                    }
                }
            }
        });
        netWatchdog.startWatch();


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
        vodInfo.setIsShowWaterMark(false);
        vodInfo.setPriority(7);
        vodInfo.setUserData("自定义数据" + index);
        return vodInfo;
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
    private boolean isCreatingFile;
    private String videoPath;

    private AsyncTask getFileTask (){
        return new AsyncTask() {
            @Override
            protected String doInBackground(Object[] objects) {
                isCreatingFile = true;
                videoPath = FileUtils.createFileFromInputStream(OSSMultiUploadActivity.this,"aliyunmedia.mp4",""+index).getPath();
                return null;
            }

            @Override
            protected void onPostExecute(Object s) {
                isCreatingFile = false;


                String fineName = videoPath;
                String ossName  = prefix + videoPath;//"video/" + fineName; //vodPath + index + ".mp4";

                uploader.addFile(fineName, endpoint, bucket, ossName, getVodInfo());
                OSSLog.logDebug("添加了一个文件：" + fineName);
                // 获取刚添加的文件。
                UploadFileInfo uploadFileInfo = uploader.listFiles().get(uploader.listFiles().size() - 1);

                // 添加到列表。
                ItemInfo info = new ItemInfo();
                info.setFile(uploadFileInfo.getFilePath());
                info.setProgress(0);
                info.setOss("http://" + uploadFileInfo.getBucket() + "." +
                        uploadFileInfo.getEndpoint().substring(7) + "/" +
                        uploadFileInfo.getObject());
                info.setStatus(uploadFileInfo.getStatus().toString());
                vodUploadAdapter.add(info);

                index++;

            }
        };
    }

    private void getIntentExtra(){
        accessKeyId = getIntent().getStringExtra("accessKeyId");
        accessKeySecret = getIntent().getStringExtra("accessKeySecret");
        secretToken = getIntent().getStringExtra("securityToken");
        expireTime = getIntent().getStringExtra("expiration");
        endpoint = getIntent().getStringExtra("Endpoint");
        bucket = getIntent().getStringExtra("Bucket");
        prefix = getIntent().getStringExtra("Prefix");
    }



    @Override
    protected void onDestroy() {
        netWatchdog.stopWatch();
        super.onDestroy();

    }
}
