package com.finn.laakso.hangboardapp;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Created by Laakso on 10.4.2018.
 */

public class PinchZoomImageView extends ImageView {

    private Bitmap mBitmap;
    private int mImageWidth;
    private int mImageHeight;

    private final static float mMinZoom = 0.5f;
    private final static float mMaxZoom = 1.2f;
    private float mScaleFactor = .8f;
    private ScaleGestureDetector mScaleGestureDetector;

    private final static int NONE = 0;
    private final static int PAN = 1;
    private final static int ZOOM = 2;
    private int mEventState;

    private float mStartX = 0;
    private float mStartY = 0;
    private float mTranslateX = 0;
    private float mTranslateY = 0;
    private float mPreviousTranslateX = 0;
    private float mPreviousTranslateY = 0;



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
           // Log.e("MSCALEFACTOR", "mSF:" + mScaleFactor);
           // mScaleFactor = mScaleFactor* (float) Math.sqrt((double) detector.getScaleFactor() );
            if (detector.getScaleFactor() > 1) {
                mScaleFactor = mScaleFactor + 0.004f;
            }
            else {
                mScaleFactor = mScaleFactor - 0.004f;
            }
          //  Log.e("SCALEFACTOR: " , "DETECTOR: " + detector.getScaleFactor());
           // Log.e("SCALEFACTOR: " , "DETECTOR NEW: " + (float) Math.sqrt((double) detector.getScaleFactor()));
            mScaleFactor = Math.max(mMinZoom, Math.min(mMaxZoom,mScaleFactor));
            //invalidate();
            //requestLayout();

            return super.onScale(detector);
        }
    }



    public PinchZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int imageWidth = MeasureSpec.getSize(widthMeasureSpec);
        int imageHeight = MeasureSpec.getSize(heightMeasureSpec);
        int scaledWidth = Math.round(mImageWidth*mScaleFactor);
        int scaledHeight = Math.round(mImageHeight* mScaleFactor);

        setMeasuredDimension(
                Math.min(imageWidth, scaledWidth),
                Math.min(imageHeight, scaledHeight));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mEventState = PAN;
                mStartX = event.getX() - mPreviousTranslateX;
                mStartY = event.getY() - mPreviousTranslateY;
                break;
            case MotionEvent.ACTION_UP:
                mEventState = NONE;
                mPreviousTranslateX = mTranslateX;
                mPreviousTranslateY = mTranslateY;
                break;
            case MotionEvent.ACTION_MOVE:
                mTranslateX = event.getX() - mStartX;
                mTranslateY = event.getY() - mStartY;
                //Log.e("ACTIONMOVE", "MOIVEX: " +event.getX());
                //Log.e("ACTIONMOVE", "MOIVEY: " +event.getX());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mEventState = ZOOM;
                break;
        }
        mScaleGestureDetector.onTouchEvent(event);
        if (( mEventState == PAN && mScaleFactor != mMinZoom) || mEventState == ZOOM ) {
            invalidate();
            requestLayout();
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(mScaleFactor,mScaleFactor);
       // canvas.scale(mScaleFactor,mScaleFactor,mScaleGestureDetector.getFocusX(),mScaleGestureDetector.getFocusY());
      /*   if ((mTranslateX * -1) < 0) {
            mTranslateX = 0;
        } else if ((mTranslateX *-1) > mImageWidth * mScaleFactor - getWidth()) {
            mTranslateX = (mImageWidth * mScaleFactor- getWidth()) * -1;
        }
        if ((mTranslateY * -1) < 0) {
            mTranslateY = 0;
        } else if ((mTranslateY *-1) > mImageHeight * mScaleFactor - getHeight()) {
            mTranslateY = (mImageHeight * mScaleFactor- getHeight()) * -1;
        }*/

        if ((mTranslateX ) < -50) {
            mTranslateX = -50;}
            else if  (mTranslateX+mImageWidth*mScaleFactor > getWidth() + 50) {
            mTranslateX = getWidth() + 50-mImageWidth*mScaleFactor;
        }
        if ((mTranslateY ) < -50) {
            mTranslateY = -50; }
            else if (mTranslateY + mImageHeight*mScaleFactor > getHeight()+ 50) {
            mTranslateY = getHeight() + 50-mImageHeight*mScaleFactor;
        }


      Log.e("TRANSLATE:","mTX: "+ mTranslateX);
        Log.e("TRANSLATE:","mTY: "+ mTranslateY);
        Log.e("IMAGE:" , "imagewidht*scale: "+mImageWidth*mScaleFactor);
        Log.e("IMAGE:","imageheight*sclae: "+mImageHeight*mScaleFactor);
        Log.e("PHONE:" , "widht: "+getWidth());
        Log.e("PHONE:","height: "+getHeight());


        canvas.translate(mTranslateX/mScaleFactor, mTranslateY/mScaleFactor);
        canvas.drawBitmap(mBitmap,0,0,null);
        canvas.restore();

    }

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public float getImageWidth() {
        return  mImageWidth * mScaleFactor;
    }

    public float getImageHeight() {
        return mImageHeight * mScaleFactor;
    }

    public float getImageX() {
        return mTranslateX;
    }
    public float getImageY() {
        return  mTranslateY;
    }


    public void setImageBitmap(Bitmap imageBitmap) {
        // mBitmap = imageBtimap;
        Bitmap bitmap = imageBitmap;
        float aspectRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
        Log.e("BITMAP", "X: " + bitmap.getWidth());
        Log.e("BITMAP", "Y: " + bitmap.getHeight());
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.e("DISPLAYMETRICS", "getDM WIDHTPIXELS: " + displayMetrics.widthPixels);
        mImageWidth = displayMetrics.widthPixels;
        mImageHeight = Math.round(mImageWidth * aspectRatio);
        mBitmap = Bitmap.createScaledBitmap(bitmap,mImageWidth, mImageHeight,false);
        invalidate();
        requestLayout();
    }

    public void setImage(Resources res,int id) {
        mBitmap = BitmapFactory.decodeResource(res,id);

    }

}

