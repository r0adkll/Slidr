/*
 * Copyright (c) 2014. 52inc
 * All Rights Reserved.
 */

package com.r0adkll.slidableactivity.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Project: PilotPass
 * Package: com.ftinc.mariner.pilotpass.widgets
 * Created by drew.heavner on 8/14/14.
 */
public class SliderPanel extends FrameLayout {

    /****************************************************
     *
     * Constants
     *
     */

    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    private static final float MAX_DIM_ALPHA = 0.8f; // 80% black alpha shade

    /****************************************************
     *
     * Variables
     *
     */

    private int mScreenWidth;
    private View mDimView;
    private View mDecorView;
    private ViewDragHelper mDragHelper;
    private OnPanelSlideListener mListener;
    private boolean mIsLocked = false;

    /**
     * Constructor
     *
     * @param context
     */
    public SliderPanel(Context context, View decorView) {
        super(context);
        mDecorView = decorView;
        init();
    }

    /**
     * Set the panel slide listener that gets called based on slider changes
     * @param listener
     */
    public void setOnPanelSlideListener(OnPanelSlideListener listener){
        mListener = listener;
    }

    /**
     * Initialize the slider panel
     */
    private void init(){
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;

        mDragHelper = ViewDragHelper.create(this, 1f, mCallback);
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);

        // Setup the dimmer view
        mDimView = new View(getContext());
        mDimView.setBackgroundColor(Color.BLACK);
        mDimView.setAlpha(MAX_DIM_ALPHA);

        // Add the dimmer view to the layout
        addView(mDimView);

    }

    /**********************************************************
     *
     * Touch Methods
     *
     */


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final boolean interceptForDrag = mDragHelper.shouldInterceptTouchEvent(ev);
        return interceptForDrag && !mIsLocked;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        }catch (IllegalArgumentException e){
            return false;
        }

        return true;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * Lock this sliding panel to ignore touch inputs.
     */
    public void lock(){
        mDragHelper.cancel();
        mIsLocked = true;
    }

    /**
     * Unlock this sliding panel to listen to touch inputs.
     */
    public void unlock(){
        mDragHelper.cancel();
        mIsLocked = false;
    }


    /**
     * The drag helper callback interface
     */
    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.getId() == mDecorView.getId();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return clamp(left, 0, mScreenWidth);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mScreenWidth;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            final int width = getWidth();
            float offset = width - releasedChild.getLeft();
            int left = xvel < 0 || xvel == 0 && offset > 0.5f ? 0 : mScreenWidth;

            mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1 - ((float)left / (float)mScreenWidth);

            // Update the dimmer alpha
            float alpha = percent * MAX_DIM_ALPHA;
            mDimView.setAlpha(alpha);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            switch (state){
                case ViewDragHelper.STATE_IDLE:
                    if(mDecorView.getLeft() == 0){
                        // State Open
                        if(mListener != null) mListener.onOpened();
                    }else{
                        // State Closed
                        if(mListener != null) mListener.onClosed();
                    }
                    break;
                case ViewDragHelper.STATE_DRAGGING:

                    break;
                case ViewDragHelper.STATE_SETTLING:

                    break;
            }
        }

    };


    /**
     * Clamp Integer values to a given range
     *
     * @param value     the value to clamp
     * @param min       the minimum value
     * @param max       the maximum value
     * @return          the clamped value
     */
    public static int clamp(int value, int min, int max){
        return Math.max(min, Math.min(max, value));
    }

    /**
     *
     */
    public static interface OnPanelSlideListener{
        public void onClosed();
        public void onOpened();
    }

}
