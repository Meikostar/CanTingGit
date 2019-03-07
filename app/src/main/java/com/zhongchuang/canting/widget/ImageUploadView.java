package com.zhongchuang.canting.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.viewcallback.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mykar on 17/4/12.
 */


public class ImageUploadView extends LinearLayout {
    private Context mContext;
    private View mView;
    private RecyclerView mRecyclerView;
    private ItemTouchHelper mHelper;
    private List<UploadFileBean> imagePaths;
    private MAdapter mAdapter;
    private int maxCount = 10;
    private OnActionListener mListener;
    private boolean mHaveMainImage = false;
    private boolean IsSubmit=false;
    MAdapter.LastHolder holder1;

    public void setOnActionListener(OnActionListener listener){
        this.mListener = listener;
        mAdapter.setOnActionListener(mListener);
    }

    //不滚动
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandHeight);
    }
    public boolean haveActionListener() {
        return mListener != null;
    }

    public void setHaveMainImage(boolean haveMainImage){
        this.mHaveMainImage = haveMainImage;
        mAdapter.setHaveMain(haveMainImage);
    }

    public void notifyItemChange(int position){
        mAdapter.notifyItemChanged(position);
    }

    public List<UploadFileBean> getImagePaths(){
        return imagePaths;
    }

    public interface OnActionListener{
        void onItemDelete(int position);
        void onItemClick(int position);
        void onAddMoreClick();
        void onItemPositionChange();
    }

    public void setMaxCount(int maxCount){
        if (maxCount>0){
            this.maxCount = maxCount;
            mAdapter.setMaxCount(maxCount);
        }
    }
    public void setProcess(double process){
        mAdapter.getUploadingView().setProgress(process);
    }


    public void setAllReady(int beginIndex){
        if (imagePaths!=null && imagePaths.size()>0){
            for (int i=0;i<imagePaths.size();i++){
                if (beginIndex==i){
                    imagePaths.get(i).setStatus(UploadFileBean.STATUS_BEGIN_UPLOAD);
                }else {
                    imagePaths.get(i).setStatus(UploadFileBean.STATUS_WAIT_UPLOAD);
                }
            }
        }
        mAdapter.setData(imagePaths);
    }

    public void beginUpload(int index){
        if (imagePaths!=null && imagePaths.size()>index){
            imagePaths.get(index).setStatus(UploadFileBean.STATUS_BEGIN_UPLOAD);
            mAdapter.notifyItemChanged(index);
        }
    }

    public void setUploadResult(int index,boolean success){
        if (imagePaths!=null && imagePaths.size()>index){
            imagePaths.get(index).setStatus(UploadFileBean.STATUS_COMMON);
            mAdapter.notifyItemChanged(index);
        }
    }

    //public void setStatus()

    public ImageUploadView(Context context) {
        super(context);
        init(context);
    }

    public ImageUploadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageUploadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setData(List<UploadFileBean> data){
        this.imagePaths = data;
        mAdapter.setData(data);
    }
    public void setHide(){
        holder1.mTextView.setVisibility(GONE);
    }
    private void init(Context context){
        this.mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.view_upload_group_view,this);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rl_upload_groud);
        mAdapter = new MAdapter(context);
        mAdapter.setMaxCount(maxCount);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridViewManagers(context,3));
//        initTouchHelper();
//        mHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
//                if (vh.getLayoutPosition()!=maxCount-1){
//                    mHelper.startDrag(vh);
//                }
            }
        });
    }

    private void initTouchHelper() {
        mHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position

                if (fromPosition<imagePaths.size()-1 && toPosition<imagePaths.size()-1 && imagePaths.get(toPosition).getType()!=2) {
                    if (mListener!=null){
                        mListener.onItemPositionChange();
                    }
                    if (fromPosition < toPosition) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(imagePaths, i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(imagePaths, i, i - 1);
                        }
                    }
                    mAdapter.notifyItemMoved(fromPosition, toPosition);
                }
                return true;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.itemView,"scaleX",1f,1.2f);
                    ObjectAnimator animator2 = ObjectAnimator.ofFloat(viewHolder.itemView,"scaleY",1f,1.2f);
                    AnimatorSet set = new AnimatorSet();
                    set.setDuration(200).play(animator).with(animator2);
                    set.start();
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.itemView,"scaleX",1.2f,1f);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(viewHolder.itemView,"scaleY",1.2f,1f);
                AnimatorSet set = new AnimatorSet();
                set.setDuration(200).play(animator).with(animator2);
                set.start();
            }
        });
    }

    public void addData(UploadFileBean bean) {
        if (imagePaths==null){
            imagePaths = new ArrayList<>();
        }
        imagePaths.add(bean);
        mAdapter.notifyItemInserted(imagePaths.size()-1);
    }

    public class MAdapter extends RecyclerView.Adapter{
        private static final int TYPE_ADD = 1;
        private static final int TYPE_IMAGE = 2;
        private List<UploadFileBean> imagePaths;
        private Context mContext;
        private YunUploadView uploadingView;
        private int maxCount;
        private OnActionListener mListener;
        private boolean haveMain;

        public void setHaveMain(boolean haveMain) {
            this.haveMain = haveMain;
        }

        public void setOnActionListener(OnActionListener listener){
            this.mListener = listener;
        }

        public void setMaxCount(int count){
            this.maxCount = count;
        }

        public YunUploadView getUploadingView() {
            return uploadingView;
        }

        public MAdapter(Context context){
            this.mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==TYPE_ADD){
                return new LastHolder(createView(parent,viewType));
            }else {
                return new GridHolder(createView(parent,viewType));
            }
        }

        public void setData(List<UploadFileBean> data){
            this.imagePaths = data;
//            if (imagePaths!=null && imagePaths.size()<maxCount){
//                if (imagePaths.get(imagePaths.size()-1).getType()!=3) {
//                    imagePaths.add(new UploadFileBean(3));
//                }
//            }
            notifyDataSetChanged();
        }

        private View createView(ViewGroup parent,int viewType) {
            if (viewType==TYPE_ADD){
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_upload_group_last,null);
                itemView.setLayoutParams(new ViewGroup.LayoutParams(parent.getMeasuredWidth()/3,parent.getMeasuredWidth()/3));
                return itemView;
            }else {
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_upload_group,null);
                itemView.setLayoutParams(new ViewGroup.LayoutParams(parent.getMeasuredWidth()/3,parent.getMeasuredWidth()/3));
                return itemView;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (TYPE_ADD == getItemViewType(position)){
                holder1= (LastHolder) holder;

                holder1.mTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener!=null){
                            mListener.onAddMoreClick();
                        }
                    }
                });
            }else {
                if(IsSubmit){
                    uploadingView.setDiss();
                }
                final UploadFileBean bean = imagePaths.get(position);
                final GridHolder thisHolder = (GridHolder) holder;
                if (position==0){
                    thisHolder.mUploadView.setHaveMainImage(true);
                    thisHolder.mUploadView.setMainImage(true);
                }else {
                    thisHolder.mUploadView.setMainImage(false);
                }
                if (bean.getType()==2){
//                    thisHolder.mUploadView.setIsVideo(true);
//                    //视频
//                    if (StringUtil.isNotEmpty(StorageManager.getInstance().getThumbnail(bean.getPath()))){
//                        thisHolder.mUploadView.setContent(StorageManager.getInstance().getThumbnail(bean.getPath()),null);
//                    }else {
//                        Observable.create(new Observable.OnSubscribe<String>() {
//                            @Override
//                            public void call(Subscriber<? super String> subscriber) {
//                                String path = BitmapUtil.saveBitmapToTempFile(BitmapUtil.createVideoThumbnail(bean.getPath(),480,480),"mp4_thumbnail_"+System.currentTimeMillis(),100,false);
//                                StorageManager.getInstance().addThumbnail(bean.getPath(),path);
//                                subscriber.onNext(path);
//                            }
//                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Action1<String>() {
//                                    @Override
//                                    public void call(String s) {
//                                        thisHolder.mUploadView.setContent(s,null);
//                                    }
//                                }, new Action1<Throwable>() {
//                                    @Override
//                                    public void call(Throwable throwable) {
//                                        throwable.printStackTrace();
//                                    }
//                                });
//                    }
                }else {
                    thisHolder.mUploadView.setIsVideo(false);
                    thisHolder.mUploadView.setContent(TextUtil.isEmpty(imagePaths.get(position).getForderPath())?imagePaths.get(position).getUrl_():imagePaths.get(position).getForderPath(), null);
                }
                if (bean.getStatus()==UploadFileBean.STATUS_UPLOADING || bean.getStatus()==UploadFileBean.STATUS_BEGIN_UPLOAD){
                    uploadingView = thisHolder.mUploadView;
                }
                if (bean.getStatus()==UploadFileBean.STATUS_UPLOADING ) {
                    thisHolder.mUploadView.setProgress(imagePaths.get(position).getProcess());
                }else if (bean.getStatus()==UploadFileBean.STATUS_BEGIN_UPLOAD){
                    thisHolder.mUploadView.beginUpload();
                }else if (bean.getStatus() == UploadFileBean.STATUS_WAIT_UPLOAD){
                    thisHolder.mUploadView.readyToUpload();
                }else {
                    thisHolder.mUploadView.resetView();
                }
                thisHolder.mUploadView.getDeleteView().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener!=null){
                            mListener.onItemDelete(position);
                        }
                    }
                });
                thisHolder.mUploadView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener!=null){
                            mListener.onItemClick(position);
                        }
                    }
                });
                if (haveMain) {
                    thisHolder.mUploadView.setMainImage(true);
                }else {
                    thisHolder.mUploadView.setMainImage(false);
                }

            }
        }

        @Override
        public int getItemCount() {
            if (imagePaths==null || imagePaths.size()==0){
                return 0;
            }else {
                if (imagePaths.size()==maxCount){
                    return imagePaths.size();
                }else {
                    return imagePaths.size()+1;
                }
            }
            // return imagePaths==null?0:imagePaths.size();
        }

        @Override
        public int getItemViewType(int position) {
            if ((position==imagePaths.size())){
                return TYPE_ADD;
            }else {
                return TYPE_IMAGE;
            }
        }

        class GridHolder extends RecyclerView.ViewHolder{
            YunUploadView mUploadView;
            public GridHolder(View itemView) {
                super(itemView);
                mUploadView = (YunUploadView) itemView.findViewById(R.id.yuv_upload);
            }
        }

        class LastHolder extends RecyclerView.ViewHolder{
            ImageView mTextView;
            public LastHolder(View itemView) {
                super(itemView);
                mTextView = (ImageView) itemView.findViewById(R.id.tv_add_detail);
            }
        }
    }
}

