package com.finn.laakso.hangboardapp;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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

    private final static float mMinZoom = 1.f;
    private final static float mMaxZoom = 3.f;
    private float mScaleFactor = 1.f;
    private ScaleGestureDetector mScaleGestureDetector;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(mMinZoom, Math.min(mMaxZoom,mScaleFactor));
            invalidate();
            requestLayout();

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
        int scaledWidht = Math.round(mImageWidth*mScaleFactor);
        int scaledHeight = Math.round(mImageHeight* mScaleFactor);

        setMeasuredDimension(
                Math.min(imageWidth, scaledWidht),
                Math.min(imageHeight, scaledHeight));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
/*
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
        }*/

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
        //canvas.scale(mScaleFactor,mScaleFactor);
        canvas.scale(mScaleFactor,mScaleFactor,mScaleGestureDetector.getFocusX(),mScaleGestureDetector.getFocusY());
        canvas.drawBitmap(mBitmap,0,0,null);
        canvas.restore();

    }


    public void setImageBitmap(Bitmap imageBtimap) {
        // mBitmap = imageBtimap;
        Bitmap bitmap = imageBtimap;
        float aspectRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
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

