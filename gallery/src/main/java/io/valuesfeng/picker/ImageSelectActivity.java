package io.valuesfeng.picker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;

import io.valuesfeng.picker.control.AlbumCollection;
import io.valuesfeng.picker.control.PictureCollection;
import io.valuesfeng.picker.control.SelectedUriCollection;
import io.valuesfeng.picker.model.Album;
import io.valuesfeng.picker.model.SelectionSpec;
import io.valuesfeng.picker.utils.BundleUtils;
import io.valuesfeng.picker.utils.MediaStoreCompat;
import io.valuesfeng.picker.utils.PhotoUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ImageSelectActivity extends FragmentActivity implements AlbumCollection.OnDirectorySelectListener {

    public static final String EXTRA_RESULT_SELECTION = BundleUtils.buildKey(ImageSelectActivity.class, "EXTRA_RESULT_SELECTION");
    public static final String EXTRA_SELECTION_SPEC = BundleUtils.buildKey(ImageSelectActivity.class, "EXTRA_SELECTION_SPEC");
    public static final String EXTRA_RESUME_LIST = BundleUtils.buildKey(ImageSelectActivity.class, "EXTRA_RESUME_LIST");
//    public static final String EXTRA_ENGINE = BundleUtils.buildKey(ImageSelectActivity.class, "EXTRA_ENGINE");

    public static final String STATE_CAPTURE_PHOTO_URI = BundleUtils.buildKey(ImageSelectActivity.class, "STATE_CAPTURE_PHOTO_URI");

    private RelativeLayout rlTop;
    private TextView mFoldName;
    private View mListViewGroup;
    private ListView mListView;
    private GridView mGridView;
    public static final int REQUEST_CODE_CAPTURE = 3;
    private MediaStoreCompat mMediaStoreCompat;
    private Button commit;
    private ImageView galleryTip;
    private SelectionSpec selectionSpec;
    private ImageView btnBack;
    private AlbumCollection albumCollection = new AlbumCollection();
    private final PictureCollection mPhotoCollection = new PictureCollection();
    private final SelectedUriCollection mCollection = new SelectedUriCollection(this);
    private String mCapturePhotoUriHolder;
    private ProgressDialog mProgressDialog;
    private boolean add_watermark;
    private String watermark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        mCapturePhotoUriHolder = savedInstanceState != null ? savedInstanceState.getString(STATE_CAPTURE_PHOTO_URI) : "";
        selectionSpec = getIntent().getParcelableExtra(ImageSelectActivity.EXTRA_SELECTION_SPEC);

        //是否添加水印
        add_watermark = getIntent().getBooleanExtra("add_watermark", false);
        watermark = getIntent().getStringExtra("watermark");
        mMediaStoreCompat = new MediaStoreCompat(this, new Handler(Looper.getMainLooper()));

        mCollection.onCreate(savedInstanceState);
        mCollection.prepareSelectionSpec(selectionSpec);
        mCollection.setDefaultSelection(getIntent().<Uri>getParcelableArrayListExtra(EXTRA_RESUME_LIST));
        mCollection.setOnSelectionChange(new SelectedUriCollection.OnSelectionChange() {
            @Override
            public void onChange(int maxCount, int selectCount) {
                commit.setText("确定(" + selectCount + "/" + maxCount + ")");
            }
        });

        mGridView = (GridView) findViewById(R.id.gridView);

        mListView = (ListView) findViewById(R.id.listView);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        mListViewGroup = findViewById(R.id.listViewParent);
        mListViewGroup.setOnClickListener(mOnClickFoldName);
        mFoldName = (TextView) findViewById(R.id.foldName);
        galleryTip = (ImageView) findViewById(R.id.gallery_tip);
        LinearLayout selectFold = (LinearLayout) findViewById(R.id.selectFold);
        commit = (Button) findViewById(R.id.commit);
        commit.setText("确定(0/" + selectionSpec.getMaxSelectable() + ")");
        if (selectionSpec.isSingleChoose()) {
            commit.setVisibility(View.GONE);
        }
        mFoldName.setText("最近图片");
        selectFold.setOnClickListener(mOnClickFoldName);

        albumCollection.onCreate(ImageSelectActivity.this, this, selectionSpec, mListView);
        albumCollection.loadAlbums();

        mPhotoCollection.onCreate(ImageSelectActivity.this, mGridView, mCollection, selectionSpec);
        mPhotoCollection.loadAllPhoto();

        //loadingview
        mProgressDialog = new ProgressDialog(ImageSelectActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("加载中...");

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCollection.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "未选择图片", Toast.LENGTH_LONG).show();
                } else {
                    compressResult();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /**
     * 压缩选择的图片
     */
    public void compressResult() {
        mProgressDialog.show();

        Observable.create(new Observable.OnSubscribe<ArrayList<String>>() {
            @Override
            public void call(Subscriber<? super ArrayList<String>> subscriber) {
                ArrayList<String> arryList = new ArrayList<String>();
                //TODO 压缩操作
                for (Uri u : mCollection.asList()) {
                    String imgPath = PhotoUtils.getRealFilePath(ImageSelectActivity.this, u);
                    arryList.add(PhotoUtils.commpressImg(ImageSelectActivity.this, imgPath, add_watermark,watermark));
                }
                subscriber.onNext(arryList);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ArrayList<String> strings) {

                        mProgressDialog.dismiss();
                        //回调结果
                        setResult(strings);
                    }
                });

    }

    public void setResult(ArrayList<String> imgPath) {
        ArrayList<String> imgPaths=new ArrayList<>();
        for(int i=0;i<imgPath.size();i++){
            imgPaths.add(imgPath.get(imgPath.size()-i-1));
        }

        Intent intent = new Intent();
        intent.putExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION, imgPaths.size()>0?imgPaths:imgPath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void setResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ImageSelectActivity.EXTRA_RESULT_SELECTION,
                (ArrayList<? extends Parcelable>) mCollection.asList());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    View.OnClickListener mOnClickFoldName = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListViewGroup.getVisibility() == View.VISIBLE) {
                hideFolderList();
            } else {
                showFolderList();
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mCollection.onSaveInstanceState(outState);
        albumCollection.onSaveInstanceState(outState);
        outState.putString(STATE_CAPTURE_PHOTO_URI, mCapturePhotoUriHolder);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        albumCollection.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Uri captured = mMediaStoreCompat.getCapturedPhotoUri(data, mCapturePhotoUriHolder);
            if (captured != null) {
                mCollection.add(captured);
                mMediaStoreCompat.cleanUp(mCapturePhotoUriHolder);
                if (mCollection.isSingleChoose()) {
                    compressResult();
                }
            }
        }
    }

    public void prepareCapture(String uri) {
        mCapturePhotoUriHolder = uri;
    }

    private void showFolderList() {
        galleryTip.setImageResource(R.drawable.gallery_up);
        mListViewGroup.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.listview_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.listview_fade_in);

        mListView.startAnimation(animation);
        mListViewGroup.startAnimation(fadeIn);
        //mListViewGroup.setVisibility(View.VISIBLE);
    }

    private void hideFolderList() {
        galleryTip.setImageResource(R.drawable.gallery_down);
        Animation animation = AnimationUtils.loadAnimation(ImageSelectActivity.this, R.anim.listview_down);
        Animation fadeOut = AnimationUtils.loadAnimation(ImageSelectActivity.this, R.anim.listview_fade_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mListViewGroup.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mListView.startAnimation(animation);
        mListViewGroup.startAnimation(fadeOut);
    }

    public MediaStoreCompat getMediaStoreCompat() {
        return mMediaStoreCompat;
    }

    @Override
    protected void onDestroy() {
        mMediaStoreCompat.destroy();
        albumCollection.onDestroy();
        mPhotoCollection.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mCollection.isEmpty()) {
            setResult(Activity.RESULT_CANCELED);
            super.onBackPressed();
        }
    }

    /**
     * 选择相机
     */
    public void showCameraAction() {
        mCapturePhotoUriHolder = mMediaStoreCompat.invokeCameraCapture(this, ImageSelectActivity.REQUEST_CODE_CAPTURE);
    }

    /**
     * 选择相册的回调，根据选择的相册，mPhotoCollection重新加载该相册中的图片
     *
     * @param album
     */
    @Override
    public void onSelect(Album album) {
        hideFolderList();
        mFoldName.setText(album.getDisplayName(this));
        mPhotoCollection.resetLoad(album);
    }

    @Override
    public void onReset(Album album) {
        mPhotoCollection.load(album);
    }
}
