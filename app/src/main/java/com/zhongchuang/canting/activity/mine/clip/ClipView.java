package com.zhongchuang.canting.activity.mine.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.utils.DensityUtil;


/***
 * 功能描述:多图裁剪
 * 作者:qiujialiu
 * 时间:2016/12/20
 * 版本:1.0
 ***/

public class ClipView extends View {
    // Private Constants ///////////////////////////////////////////////////////
    private static final float BMP_LEFT = 0f;
    private static final float BMP_TOP = 0f;

    private static final float DEFAULT_BORDER_RECT_WIDTH = 200f;
    private static final float DEFAULT_BORDER_RECT_HEIGHT = 200f;

    private static final int POS_TOP_LEFT = 0;
    private static final int POS_TOP_RIGHT = 1;
    private static final int POS_BOTTOM_LEFT = 2;
    private static final int POS_BOTTOM_RIGHT = 3;
    private static final int POS_TOP = 4;
    private static final int POS_BOTTOM = 5;
    private static final int POS_LEFT = 6;
    private static final int POS_RIGHT = 7;
    private static final int POS_CENTER = 8;

    // this constant would be best to use event number
    private static final float BORDER_LINE_WIDTH = 6f;
    private static final float BORDER_CORNER_LENGTH = 30f;
    private static final float TOUCH_FIELD = 20f;

    // Member Variables ////////////////////////////////////////////////////////
    private String mBmpPath;
    private Bitmap mBmpToCrop;

    private RectF mBmpBound;
    private Paint mBmpPaint;

    private Paint mBorderPaint;// 裁剪区边框
    private Paint mGuidelinePaint;
    private Paint mCornerPaint;
    private Paint mBgPaint;

    private RectF mDefaultBorderBound;
    private RectF mBorderBound;

    private PointF mLastPoint = new PointF();

    private float mBorderWidth;
    private float mBorderHeight;

    private Bitmap mBitmapIndicate;

    private int touchPos;
    private float currentScale = 1;
    private float currentScales = 1;

    // Constructors ////////////////////////////////////////////////////////////
    public ClipView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        contexts=context;
        init(context);
    }

    public ClipView(Context context, float scale) {
        super(context);
        // TODO Auto-generated constructor stub
        currentScales = scale;
        contexts=context;
        init(context);
    }
   private Context contexts;
    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        contexts=context;
        init(context);
    }

    // View Methods ////////////////////////////////////////////////////////////
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        // super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        // super.onDraw(canvas);
        if (mBmpPath != null) {
            canvas.drawBitmap(mBmpToCrop, null, mBmpBound, mBmpPaint);
            //canvas.drawBitmap(mBmpToCrop,0,0,mBmpPaint);
            int bitmapRadius = (int) (mBitmapIndicate.getWidth() / 2f);
            canvas.drawRect(mBorderBound.left, mBorderBound.top, mBorderBound.right, mBorderBound.bottom, mBorderPaint);
            canvas.drawBitmap(mBitmapIndicate, mBorderBound.left - bitmapRadius, mBorderBound.top - bitmapRadius, mCornerPaint);
            canvas.drawBitmap(mBitmapIndicate, mBorderBound.right - bitmapRadius, mBorderBound.top - bitmapRadius, mCornerPaint);
            canvas.drawBitmap(mBitmapIndicate, mBorderBound.left - bitmapRadius, mBorderBound.bottom - bitmapRadius, mCornerPaint);
            canvas.drawBitmap(mBitmapIndicate, mBorderBound.right - bitmapRadius, mBorderBound.bottom - bitmapRadius, mCornerPaint);
            //drawGuidlines(canvas);
            drawBackground(canvas);
        }
    }

    public void release() {
        mBmpToCrop.recycle();
        System.gc();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        // super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setLastPosition(event);
                getParent().requestDisallowInterceptTouchEvent(true);
                // onActionDown(event.getX(), event.getY());
                touchPos = detectTouchPosition(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(event.getX(), event.getY());
                setLastPosition(event);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return touchPos != -1;
    }

    // Public Methods //////////////////////////////////////////////////////////
    public String getBmpPath() {
        return mBmpPath;
    }

    public void setBmpPath(String picPath) {
        if (mBmpToCrop != null && !mBmpToCrop.isRecycled()) {
            mBmpToCrop.recycle();
        }
        this.mBmpPath = picPath;
        setBmp();
    }

    public Bitmap getCroppedImage() {
        // 先不考虑图片被压缩的情况 就当作现在的图片就是1：1的
//            float scaleX =  mBorderBound.width()/mBmpToCrop.getWidth();
//            float scaleY =  mBorderBound.height()/mBmpToCrop.getHeight();
        if (mBmpToCrop == null || mBmpToCrop.getHeight() <= 0) {
            setBmpPath(mBmpPath);
            return getClipBitmap();
        } else {
            return getClipBitmap();
        }

    }

    private Bitmap getClipBitmap() {
        float width = mBorderBound.width() * currentScale;
        float height = mBorderBound.height() * currentScale;

        if (width > height) {

            return Bitmap.createBitmap(mBmpToCrop, (int) (((mBorderBound.left - mBmpBound.left) * currentScale)), (int) (((mBorderBound.top - mBmpBound.top) * currentScale)),
                    (int) (currentScales == 1 ? height : width), (int) height);
        } else {
            return Bitmap.createBitmap(mBmpToCrop, (int) (((mBorderBound.left - mBmpBound.left) * currentScale)), (int) (((mBorderBound.top - mBmpBound.top) * currentScale)),
                    (int) width - 1, (int) width - 1);
        }
    }

    // Private Methods /////////////////////////////////////////////////////////
    private void init(Context context) {
        mBitmapIndicate = BitmapFactory.decodeResource(context.getResources(), R.drawable.store_icon_crop_tool);
        mBmpPaint = new Paint();
        // 以下是抗锯齿
        mBmpPaint.setAntiAlias(true);// 防止边缘的锯齿
        mBmpPaint.setFilterBitmap(true);// 对位图进行滤波处理

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(Color.parseColor("#AAfed000"));
        mBorderPaint.setStrokeWidth(BORDER_LINE_WIDTH);

        mGuidelinePaint = new Paint();
        mGuidelinePaint.setColor(Color.parseColor("#00FFFFFF"));
        mGuidelinePaint.setStrokeWidth(1f);

        mCornerPaint = new Paint();

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.parseColor("#B0000000"));
        mBgPaint.setAlpha(150);

    }

    private void setBmp() {
        mBmpToCrop = getBitmapFromPath();
        float scaleX = 1;
        float scaleY = 1;
        mBmpBound = new RectF();
        if (mBmpToCrop == null) {
            return;
        }
        if (mBmpToCrop.getWidth() > DensityUtil.getWidth(contexts)) {
            scaleX = ((float) mBmpToCrop.getWidth()) / DensityUtil.getWidth(contexts);
        } else {
            scaleX = ((float) mBmpToCrop.getWidth()) / DensityUtil.getWidth(contexts);
        }
        currentScale = scaleX;
        mBmpBound.left = BMP_LEFT;
        mBmpBound.right = DensityUtil.getWidth(contexts);
        if ((mBmpToCrop.getHeight() / scaleX) < (DensityUtil.getWidth(contexts) - DensityUtil.dip2px(44))) {
            mBmpBound.top = ((DensityUtil.getHeight(contexts) - DensityUtil.dip2px(44)) - (mBmpToCrop.getHeight() / scaleX)) / 2;
            mBmpBound.bottom = mBmpBound.top + (mBmpToCrop.getHeight() / scaleX);
        } else {
            mBmpBound.top = BMP_TOP;
            mBmpBound.bottom = mBmpBound.top + (mBmpToCrop.getHeight() / scaleX);
        }
        int height = (mBmpBound.height() < (DensityUtil.getHeight(contexts) - DensityUtil.dip2px(44))) ? (int) (DensityUtil.getHeight(contexts) - DensityUtil.dip2px(44)) : (int) mBmpBound.height();
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
//            mBmpBound.top = BMP_TOP;
//            mBmpBound.right = mBmpBound.left + mBmpToCrop.getWidth();
//            mBmpBound.bottom = mBmpBound.top + mBmpToCrop.getHeight();

        // 使裁剪框一开始出现在图片的中心位置
        mDefaultBorderBound = new RectF();
//            mDefaultBorderBound.left = (mBmpBound.left + mBmpBound.right - DEFAULT_BORDER_RECT_WIDTH) / 2;
//            mDefaultBorderBound.top = (mBmpBound.top + mBmpBound.bottom - DEFAULT_BORDER_RECT_HEIGHT) / 2;
//            mDefaultBorderBound.right = mDefaultBorderBound.left + DEFAULT_BORDER_RECT_WIDTH;
//            mDefaultBorderBound.bottom = mDefaultBorderBound.top + DEFAULT_BORDER_RECT_HEIGHT;
        if ((mBmpBound.bottom - mBmpBound.top) > (mBmpBound.right - mBmpBound.left)) {//高大于宽
            mDefaultBorderBound.left = mBmpBound.left + 10;
            mDefaultBorderBound.right = mBmpBound.right - 10;
            mDefaultBorderBound.top = mBmpBound.top;
            if (currentScales != 1) {
                mDefaultBorderBound.bottom = mDefaultBorderBound.top + ((mDefaultBorderBound.right - mDefaultBorderBound.left) / 16) * 9;
            } else {
                mDefaultBorderBound.bottom = mDefaultBorderBound.top + (mDefaultBorderBound.right - mDefaultBorderBound.left);
            }

        } else {

            if (currentScales != 1) {
                mDefaultBorderBound.top = mBmpBound.top + 10;
                mDefaultBorderBound.bottom = (float) (mDefaultBorderBound.top + (mBmpBound.right - mBmpBound.left) * (9 / 16.0));
                mDefaultBorderBound.left=0;
                mDefaultBorderBound.right=(mBmpBound.right - mBmpBound.left);

            } else {
                mDefaultBorderBound.top = mBmpBound.top + 10;
                mDefaultBorderBound.bottom = mBmpBound.bottom - 10;


                mDefaultBorderBound.left = mBmpBound.left + (((mBmpBound.right - mBmpBound.left) - (mDefaultBorderBound.bottom - mDefaultBorderBound.top)) / 2);
                mDefaultBorderBound.right = mDefaultBorderBound.left + (mDefaultBorderBound.bottom - mDefaultBorderBound.top);

            }
        }
//            if (DevicesUtil.getHeight(getContext())-DensityUtil.dip2px(44) < DensityUtil.getScreenWidth()){
//                mDefaultBorderBound.top = BMP_LEFT;
//                mDefaultBorderBound.left = (DensityUtil.getScreenWidth() - (DevicesUtil.getHeight(getContext())-DensityUtil.dip2px(44)))/2;
//                mDefaultBorderBound.right = mDefaultBorderBound.left + DevicesUtil.getHeight(getContext());
//                mDefaultBorderBound.bottom = mDefaultBorderBound.top + DevicesUtil.getHeight(getContext());
//            }else {
//                mDefaultBorderBound.left = BMP_LEFT;
//                mDefaultBorderBound.top = (DevicesUtil.getHeight(getContext()) - DensityUtil.dip2px(44) - DensityUtil.getScreenWidth()) / 2;
//                mDefaultBorderBound.right = mDefaultBorderBound.left + DevicesUtil.getWidth(getContext());
//                mDefaultBorderBound.bottom = mDefaultBorderBound.top + DevicesUtil.getWidth(getContext());
//            }


        mBorderBound = new RectF();
        mBorderBound.left = mDefaultBorderBound.left;
        mBorderBound.top = mDefaultBorderBound.top;
        mBorderBound.right = mDefaultBorderBound.right;
        mBorderBound.bottom = mDefaultBorderBound.bottom;

        getBorderEdgeLength();
        invalidate();
    }

    private Bitmap getBitmapFromPath() {
        return decodeBitmap(mBmpPath);
    }

    public Bitmap decodeBitmap(String path) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = decodeBitmap2(path, 2);
        }
        return bitmap;
    }

    private Bitmap decodeBitmap2(String path, int scale) {
        Bitmap bitmap;
        BitmapFactory.Options opts2 = new BitmapFactory.Options();
        opts2.inJustDecodeBounds = false;
        opts2.inSampleSize = scale;
        try {
            bitmap = BitmapFactory.decodeFile(path, opts2);
        } catch (OutOfMemoryError e) {
            return decodeBitmap2(path, scale + 1);
        }
        return bitmap;
    }

    private void drawBackground(Canvas canvas) {

        /*-
          -------------------------------------
          |                top                |
          -------------------------------------
          |      |                    |       |<——————————mBmpBound
          |      |                    |       |
          | left |                    | right |
          |      |                    |       |
          |      |                  <─┼───────┼────mBorderBound
          -------------------------------------
          |              bottom               |
          -------------------------------------
         */

        // Draw "top", "bottom", "left", then "right" quadrants.
        // because the border line width is larger than 1f, in order to draw a complete border rect ,
        // i have to change zhe rect coordinate to draw
        float delta = BORDER_LINE_WIDTH / 2;
        float left = mBorderBound.left - delta;
        float top = mBorderBound.top - delta;
        float right = mBorderBound.right + delta;
        float bottom = mBorderBound.bottom + delta;

        // -------------------------------------------------------------------------------移动到上下两端会多出来阴影
        canvas.drawRect(mBmpBound.left, mBmpBound.top, mBmpBound.right, top, mBgPaint);
        canvas.drawRect(mBmpBound.left, bottom, mBmpBound.right, mBmpBound.bottom, mBgPaint);
        canvas.drawRect(mBmpBound.left, top, left, bottom, mBgPaint);
        canvas.drawRect(right, top, mBmpBound.right, bottom, mBgPaint);
    }

    // 画裁剪区域中间的参考线
    private void drawGuidlines(Canvas canvas) {
        // Draw vertical guidelines.
        final float oneThirdCropWidth = mBorderBound.width() / 3;

        final float x1 = mBorderBound.left + oneThirdCropWidth;
        canvas.drawLine(x1, mBorderBound.top, x1, mBorderBound.bottom, mGuidelinePaint);
        final float x2 = mBorderBound.right - oneThirdCropWidth;
        canvas.drawLine(x2, mBorderBound.top, x2, mBorderBound.bottom, mGuidelinePaint);

        // Draw horizontal guidelines.
        final float oneThirdCropHeight = mBorderBound.height() / 3;

        final float y1 = mBorderBound.top + oneThirdCropHeight;
        canvas.drawLine(mBorderBound.left, y1, mBorderBound.right, y1, mGuidelinePaint);
        final float y2 = mBorderBound.bottom - oneThirdCropHeight;
        canvas.drawLine(mBorderBound.left, y2, mBorderBound.right, y2, mGuidelinePaint);
    }

    private void onActionDown(float x, float y) {

    }

    private void onActionMove(float x, float y) {
        float deltaX = x - mLastPoint.x;
        float deltaY = y - mLastPoint.y;

        // 这里先不考虑裁剪框放最大的情况
        switch (touchPos) {
            case POS_CENTER:
                mBorderBound.left += deltaX;
                // fix border position
                if (mBorderBound.left < mBmpBound.left)
                    mBorderBound.left = mBmpBound.left;
                if (mBorderBound.left > mBmpBound.right - mBorderWidth)
                    mBorderBound.left = mBmpBound.right - mBorderWidth;

                mBorderBound.top += deltaY;
                if (mBorderBound.top < mBmpBound.top)
                    mBorderBound.top = mBmpBound.top;

                if (mBorderBound.top > mBmpBound.bottom - mBorderHeight)
                    mBorderBound.top = mBmpBound.bottom - mBorderHeight;

                mBorderBound.right = mBorderBound.left + mBorderWidth;
                mBorderBound.bottom = mBorderBound.top + mBorderHeight;

                break;

//                case POS_TOP:
//                    resetTop(deltaY);
//                    break;
//                case POS_BOTTOM:
//                    resetBottom(deltaY);
//                    break;
//                case POS_LEFT:
//                    resetLeft(deltaX);
//                    break;
//                case POS_RIGHT:
//                    resetRight(deltaX);
//                    break;
            case POS_TOP_LEFT:
                float dex4 = (deltaX * deltaX) + (deltaY * deltaY);
                if ((deltaX) >= 0 && deltaY >= 0) {
                    if (currentScales == 1 || Scal()) {
                        resetTop((float) Math.sqrt(dex4));
                        resetLeft(currentScales == 1 ? (float) Math.sqrt(dex4) : (float) (Math.sqrt(dex4) * (16.0 / 9)));
                    }

                } else if ((deltaX) < 0 && deltaY < 0) {
                    if (canSetLeftTop(true)) {
                        resetTop(-((float) Math.sqrt(dex4)));
                        resetLeft(currentScales == 1 ? -(float) Math.sqrt(dex4) : -(float) (Math.sqrt(dex4) * (16.0 / 9)));
                    }

                }
                break;
            case POS_TOP_RIGHT:
                float dex3 = (deltaX * deltaX) + (deltaY * deltaY);
                if ((deltaX) > 0 && deltaY < 0) {
                    if (canSetRightTop(true)) {
                        resetTop(-(float) Math.sqrt(dex3));
                        resetRight(currentScales == 1 ? (float) Math.sqrt(dex3) : (float) (Math.sqrt(dex3) * (16.0 / 9)));
                    }
                } else if ((deltaX) <= 0 && deltaY >= 0) {
                    if (currentScales == 1 || Scal()) {
                        resetTop(((float) Math.sqrt(dex3)));
                        resetRight(currentScales == 1 ? -(float) Math.sqrt(dex3) : -(float) (Math.sqrt(dex3) * (16.0 / 9)));
                    }

                }
                break;
            case POS_BOTTOM_LEFT:
                float dex2 = (deltaX * deltaX) + (deltaY * deltaY);
                if ((deltaX) > 0 && deltaY < 0) {
                    if (currentScales == 1 || Scal()) {
                        resetBottom(-(float) Math.sqrt(dex2));
                        resetLeft(currentScales == 1 ? (float) Math.sqrt(dex2) : (float) (Math.sqrt(dex2) * (16.0 / 9)));
                    }

                } else if ((deltaX) <= 0 && deltaY >= 0) {
                    if (canSetLeftBottom(true)) {
                        resetBottom(((float) Math.sqrt(dex2)));
                        resetLeft(currentScales == 1 ? -(float) Math.sqrt(dex2) : -(float) (Math.sqrt(dex2) * (16.0 / 9)));
                    }
                }
                break;
            case POS_BOTTOM_RIGHT:
                float dex = (deltaX * deltaX) + (deltaY * deltaY);
                if ((deltaX) > 0 && deltaY > 0) {
                    if (canSetRightBottom(true)) {
                        resetBottom((float) Math.sqrt(dex));
                        resetRight(currentScales == 1 ? (float) Math.sqrt(dex) : (float) (Math.sqrt(dex) * (16.0 / 9)));
                    }
                } else if ((deltaX) <= 0 && deltaY <= 0) {
                    if (currentScales == 1 || Scal()) {
                        resetBottom(-((float) Math.sqrt(dex)));
                        resetRight(currentScales == 1 ? -(float) Math.sqrt(dex) : -(float) (Math.sqrt(dex) * (16.0 / 9)));
                    }

                }
                break;
            default:
                break;
        }
        invalidate();
    }

    private void onActionUp(float x, float y) {

    }

    public boolean Scal() {
        return mBorderBound.bottom - mBorderBound.top > 2 * BORDER_CORNER_LENGTH + 5;
    }

    private int detectTouchPosition(float x, float y) {
        if (x > mBorderBound.left + TOUCH_FIELD && x < mBorderBound.right - TOUCH_FIELD
                && y > mBorderBound.top + TOUCH_FIELD && y < mBorderBound.bottom - TOUCH_FIELD)
            return POS_CENTER;

        if (x > mBorderBound.left + BORDER_CORNER_LENGTH && x < mBorderBound.right - BORDER_CORNER_LENGTH) {
            if (y > mBorderBound.top - TOUCH_FIELD && y < mBorderBound.top + TOUCH_FIELD)
                return POS_TOP;
            if (y > mBorderBound.bottom - TOUCH_FIELD && y < mBorderBound.bottom + TOUCH_FIELD)
                return POS_BOTTOM;
        }

        if (y > mBorderBound.top + BORDER_CORNER_LENGTH && y < mBorderBound.bottom - BORDER_CORNER_LENGTH) {
            if (x > mBorderBound.left - TOUCH_FIELD && x < mBorderBound.left + TOUCH_FIELD)
                return POS_LEFT;
            if (x > mBorderBound.right - TOUCH_FIELD && x < mBorderBound.right + TOUCH_FIELD)
                return POS_RIGHT;
        }

        // 前面的逻辑已经排除掉了几种情况 所以后面的 ┏ ┓ ┗ ┛ 边角就按照所占区域的方形来判断就可以了
        if (x > mBorderBound.left - TOUCH_FIELD && x < mBorderBound.left + BORDER_CORNER_LENGTH) {
            if (y > mBorderBound.top - TOUCH_FIELD && y < mBorderBound.top + BORDER_CORNER_LENGTH)
                return POS_TOP_LEFT;
            if (y > mBorderBound.bottom - BORDER_CORNER_LENGTH && y < mBorderBound.bottom + TOUCH_FIELD)
                return POS_BOTTOM_LEFT;
        }

        if (x > mBorderBound.right - BORDER_CORNER_LENGTH && x < mBorderBound.right + TOUCH_FIELD) {
            if (y > mBorderBound.top - TOUCH_FIELD && y < mBorderBound.top + BORDER_CORNER_LENGTH)
                return POS_TOP_RIGHT;
            if (y > mBorderBound.bottom - BORDER_CORNER_LENGTH && y < mBorderBound.bottom + TOUCH_FIELD)
                return POS_BOTTOM_RIGHT;
        }

        return -1;
    }

    private void setLastPosition(MotionEvent event) {
        mLastPoint.x = event.getX();
        mLastPoint.y = event.getY();
    }

    private void getBorderEdgeLength() {
        mBorderWidth = mBorderBound.width();
        mBorderHeight = mBorderBound.height();
    }

    private void getBorderEdgeWidth() {
        mBorderWidth = mBorderBound.width();
    }

    private void getBorderEdgeHeight() {
        mBorderHeight = mBorderBound.height();
    }

    private boolean resetLeft(float delta) {
        if (!isTheBottomRightEdge()) {
            mBorderBound.left += delta;
            getBorderEdgeWidth();
            fixBorderLeft();
            return true;
        } else {
            return false;
        }
    }

    private boolean resetTop(float delta) {
        if (!isTheBottomRightEdge()) {
            mBorderBound.top += delta;
            getBorderEdgeHeight();
            fixBorderTop();
            return true;
        } else {
            return false;
        }

    }

    private boolean resetRight(float delta) {

        if (!isTheBottomRightEdge() || delta < 0) {
            mBorderBound.right += delta;
            getBorderEdgeWidth();
            fixBorderRight();
            return true;
        }
        return false;
    }

    private boolean isTheBottomRightEdge() {
//        if (mBorderBound.bottom >= (mBmpBound.bottom - 1) || mBorderBound.right >= (mBmpBound.right - 1)) {
//            return true;
//        }
        return false;
    }

    private boolean canSetRightBottom(boolean scaleBig) {
        if (!scaleBig) {
            return true;
        }
        return mBorderBound.bottom <= (mBmpBound.bottom - 1) && mBorderBound.right <= (mBmpBound.right - 1);
    }

    private boolean canSetLeftBottom(boolean scaleBig) {
        if (!scaleBig) {
            return true;
        }
        return mBorderBound.bottom <= (mBmpBound.bottom - 1) && mBorderBound.left >= (mBmpBound.left + 1);
    }

    private boolean canSetRightTop(boolean scaleBig) {
        if (!scaleBig) {
            return true;
        }
        return mBorderBound.top >= (mBmpBound.top + 1) && mBorderBound.right <= (mBmpBound.right - 1);
    }

    private boolean canSetLeftTop(boolean scaleBig) {
        if (!scaleBig) {
            return true;
        }
        return mBorderBound.top >= (mBmpBound.top + 1) && mBorderBound.left >= (mBmpBound.left + 1);
    }

    private boolean resetBottom(float delta) {
        if (!isTheBottomRightEdge() || delta < 0) {
            mBorderBound.bottom += delta;
            getBorderEdgeHeight();
            fixBorderBottom();
            return true;
        } else {
            return false;
        }
    }

    private void fixBorderLeft() {
        // fix left
        if (mBorderBound.left < mBmpBound.left)
            mBorderBound.left = mBmpBound.left;
        if (mBorderWidth < 2 * BORDER_CORNER_LENGTH)
            mBorderBound.left = mBorderBound.right - 2 * BORDER_CORNER_LENGTH;
    }

    private void fixBorderTop() {
        // fix top
        if (mBorderBound.top < mBmpBound.top)
            mBorderBound.top = mBmpBound.top;
        if (mBorderHeight < 2 * BORDER_CORNER_LENGTH)
            mBorderBound.top = mBorderBound.bottom - 2 * BORDER_CORNER_LENGTH;
    }

    private void fixBorderRight() {
        // fix right
        if (mBorderBound.right > mBmpBound.right)
            mBorderBound.right = mBmpBound.right;
        if (mBorderWidth < 2 * BORDER_CORNER_LENGTH)
            mBorderBound.right = mBorderBound.left + 2 * BORDER_CORNER_LENGTH;
    }

    private void fixBorderBottom() {
        // fix bottom
        if (mBorderBound.bottom > mBmpBound.bottom)
            mBorderBound.bottom = mBmpBound.bottom;
        if (mBorderHeight < 2 * BORDER_CORNER_LENGTH)
            mBorderBound.bottom = mBorderBound.top + 2 * BORDER_CORNER_LENGTH;
    }
}

