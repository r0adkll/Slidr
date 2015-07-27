/*
 * Copyright (c) 2014. 52inc
 * All Rights Reserved.
 */

package com.r0adkll.slidr.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.r0adkll.slidr.model.SlidrConfig;
import static com.r0adkll.slidr.model.SlidrPosition.*;

/**
 * Project: PilotPass
 * Package: com.ftinc.mariner.pilotpass.widgets
 * Created by drew.heavner on 8/14/14.
 */
public class SliderPanel extends FrameLayout {

    /***********************************************************************************************
     *
     * Constants
     *
     */

    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /***********************************************************************************************
     *
     * Variables
     *
     */

    private int mScreenWidth;
    private int mScreenHeight;
    private View mDimView;
    private View mDecorView;
    private ViewDragHelper mDragHelper;
    private OnPanelSlideListener mListener;
    private boolean mIsLocked = false;

    private SlidrConfig mConfig;

    /**
     * Constructor
     *
     * @param context
     */
    public SliderPanel(Context context, View decorView) {
        super(context);
        mDecorView = decorView;
        mConfig = new SlidrConfig.Builder().build();
        init();
    }

    public SliderPanel(Context context, View decorView, SlidrConfig config){
        super(context);
        mDecorView = decorView;
        mConfig = config;
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
     *
     */
    private void init(){
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;

        ViewDragHelper.Callback callback;
        int position;
        switch (mConfig.getPosition()){
            case LEFT:
                callback = mLeftCallback;
                position = ViewDragHelper.EDGE_LEFT;
                break;
            case RIGHT:
                callback = mRightCallback;
                position = ViewDragHelper.EDGE_RIGHT;
                break;
            case TOP:
                callback = mTopCallback;
                position = ViewDragHelper.EDGE_TOP;
                break;
            case BOTTOM:
                callback = mBottomCallback;
                position = ViewDragHelper.EDGE_BOTTOM;
                break;
            case VERTICAL:
                callback = mVerticalCallback;
                position = ViewDragHelper.EDGE_TOP | ViewDragHelper.EDGE_BOTTOM;
                break;
            case HORIZONTAL:
                callback = mHorizontalCallback;
                position = ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_RIGHT;
                break;
            default:
                callback = mLeftCallback;
                position = ViewDragHelper.EDGE_LEFT;
        }

        mDragHelper = ViewDragHelper.create(this, mConfig.getSensitivity(), callback);
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setEdgeTrackingEnabled(position);

        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);

        // Setup the dimmer view
        mDimView = new View(getContext());
        mDimView.setBackgroundColor(mConfig.getScrimColor());
        mDimView.setAlpha(mConfig.getScrimStartAlpha());

        // Add the dimmer view to the layout
        addView(mDimView);

        /*
         * This is so we can get the height of the view and
         * ignore the system navigation that would be included if we
         * retrieved this value from the DisplayMetrics
         */
        post(new Runnable() {
            @Override
            public void run() {
                mScreenHeight = getHeight();
            }
        });

    }

    /***********************************************************************************************
     *
     * Touch Methods
     *
     */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean interceptForDrag;

        // Fix for pull request #13 and issue #12
        try {
            interceptForDrag = mDragHelper.shouldInterceptTouchEvent(ev);
        }catch (Exception e){
            interceptForDrag = false;
        }
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


    /***********************************************************************************************
     *
     * ViewDragHelper Callback classes that define how the drag helper operates
     *
     */


    /**
     * The drag helper callback interface for the Left position
     */
    private ViewDragHelper.Callback mLeftCallback = new ViewDragHelper.Callback() {

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

            int left = releasedChild.getLeft();
            int settleLeft = 0;
            int leftThreshold = (int) (getWidth() * mConfig.getDistanceThreshold());
            boolean isVerticalSwiping = Math.abs(yvel) > mConfig.getVelocityThreshold();

            if(xvel > 0){

                if(Math.abs(xvel) > mConfig.getVelocityThreshold() && !isVerticalSwiping){
                    settleLeft = mScreenWidth;
                }else if(left > leftThreshold){
                    settleLeft = mScreenWidth;
                }

            }else if(xvel == 0){
                if(left > leftThreshold){
                    settleLeft = mScreenWidth;
                }
            }

            mDragHelper.settleCapturedViewAt(settleLeft, releasedChild.getTop());
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1f - ((float)left / (float)mScreenWidth);

            if(mListener != null) mListener.onSlideChange(percent);

            // Update the dimmer alpha
            applyScrim(percent);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if(mListener != null) mListener.onStateChanged(state);
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
     * The drag helper callbacks for dragging the slidr attachment from the right of the screen
     */
    private ViewDragHelper.Callback mRightCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.getId() == mDecorView.getId();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return clamp(left, -mScreenWidth, 0);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mScreenWidth;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            int left = releasedChild.getLeft();
            int settleLeft = 0;
            int leftThreshold = (int) (getWidth() * mConfig.getDistanceThreshold());
            boolean isVerticalSwiping = Math.abs(yvel) > mConfig.getVelocityThreshold();

            if(xvel < 0){

                if(Math.abs(xvel) > mConfig.getVelocityThreshold() && !isVerticalSwiping){
                    settleLeft = -mScreenWidth;
                }else if(left < -leftThreshold){
                    settleLeft = -mScreenWidth;
                }

            }else if(xvel == 0){
                if(left < -leftThreshold){
                    settleLeft = -mScreenWidth;
                }
            }

            mDragHelper.settleCapturedViewAt(settleLeft, releasedChild.getTop());
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1f - ((float)Math.abs(left) / (float)mScreenWidth);

            if(mListener != null) mListener.onSlideChange(percent);

            // Update the dimmer alpha
            applyScrim(percent);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if(mListener != null) mListener.onStateChanged(state);
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
     * The drag helper callbacks for dragging the slidr attachment from the top of the screen
     */
    private ViewDragHelper.Callback mTopCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.getId() == mDecorView.getId();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return clamp(top, 0, mScreenHeight);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mScreenHeight;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            int top = releasedChild.getTop();
            int settleTop = 0;
            int topThreshold = (int) (getHeight() * mConfig.getDistanceThreshold());
            boolean isSideSwiping = Math.abs(xvel) > mConfig.getVelocityThreshold();

            if(yvel > 0){
                if(Math.abs(yvel) > mConfig.getVelocityThreshold() && !isSideSwiping){
                    settleTop = mScreenHeight;
                }else if(top > topThreshold){
                    settleTop = mScreenHeight;
                }
            }else if(yvel == 0){
                if(top > topThreshold){
                    settleTop = mScreenHeight;
                }
            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), settleTop);
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1f - ((float)Math.abs(top) / (float)mScreenHeight);

            if(mListener != null) mListener.onSlideChange(percent);

            // Update the dimmer alpha
            applyScrim(percent);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if(mListener != null) mListener.onStateChanged(state);
            switch (state){
                case ViewDragHelper.STATE_IDLE:
                    if(mDecorView.getTop() == 0){
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
     * The drag helper callbacks for dragging the slidr attachment from the bottom of hte screen
     */
    private ViewDragHelper.Callback mBottomCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.getId() == mDecorView.getId();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return clamp(top, -mScreenHeight, 0);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mScreenHeight;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            int top = releasedChild.getTop();
            int settleTop = 0;
            int topThreshold = (int) (getHeight() * mConfig.getDistanceThreshold());
            boolean isSideSwiping = Math.abs(xvel) > mConfig.getVelocityThreshold();

            if(yvel < 0){
                if(Math.abs(yvel) > mConfig.getVelocityThreshold() && !isSideSwiping){
                    settleTop = -mScreenHeight;
                }else if(top < -topThreshold){
                    settleTop = -mScreenHeight;
                }
            }else if(yvel == 0){
                if(top < -topThreshold){
                    settleTop = -mScreenHeight;
                }
            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), settleTop);
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1f - ((float)Math.abs(top) / (float)mScreenHeight);

            if(mListener != null) mListener.onSlideChange(percent);

            // Update the dimmer alpha
            applyScrim(percent);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if(mListener != null) mListener.onStateChanged(state);
            switch (state){
                case ViewDragHelper.STATE_IDLE:
                    if(mDecorView.getTop() == 0){
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
     * The drag helper callbacks for dragging the slidr attachment in both vertical directions
     */
    private ViewDragHelper.Callback mVerticalCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.getId() == mDecorView.getId();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return clamp(top, -mScreenHeight, mScreenHeight);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mScreenHeight;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            int top = releasedChild.getTop();
            int settleTop = 0;
            int topThreshold = (int) (getHeight() * mConfig.getDistanceThreshold());
            boolean isSideSwiping = Math.abs(xvel) > mConfig.getVelocityThreshold();

            if(yvel > 0){

                // Being slinged down
                if(Math.abs(yvel) > mConfig.getVelocityThreshold() && !isSideSwiping){
                    settleTop = mScreenHeight;
                }else if(top > topThreshold){
                    settleTop = mScreenHeight;
                }

            }else if(yvel < 0){
                // Being slinged up
                if(Math.abs(yvel) > mConfig.getVelocityThreshold() && !isSideSwiping){
                    settleTop = -mScreenHeight;
                }else if(top < -topThreshold){
                    settleTop = -mScreenHeight;
                }

            }else{

                if(top > topThreshold){
                    settleTop = mScreenHeight;
                }else if(top < -topThreshold){
                    settleTop = -mScreenHeight;
                }

            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), settleTop);
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1f - ((float)Math.abs(top) / (float)mScreenHeight);

            if(mListener != null) mListener.onSlideChange(percent);

            // Update the dimmer alpha
            applyScrim(percent);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if(mListener != null) mListener.onStateChanged(state);
            switch (state){
                case ViewDragHelper.STATE_IDLE:
                    if(mDecorView.getTop() == 0){
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
     * The drag helper callbacks for dragging the slidr attachment in both horizontal directions
     */
    private ViewDragHelper.Callback mHorizontalCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child.getId() == mDecorView.getId();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return clamp(left, -mScreenWidth, mScreenWidth);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mScreenWidth;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            int left = releasedChild.getLeft();
            int settleLeft = 0;
            int leftThreshold = (int) (getWidth() * mConfig.getDistanceThreshold());
            boolean isVerticalSwiping = Math.abs(yvel) > mConfig.getVelocityThreshold();

            if(xvel > 0){

                if(Math.abs(xvel) > mConfig.getVelocityThreshold() && !isVerticalSwiping){
                    settleLeft = mScreenWidth;
                }else if(left > leftThreshold){
                    settleLeft = mScreenWidth;
                }

            }else if(xvel < 0){

                if(Math.abs(xvel) > mConfig.getVelocityThreshold() && !isVerticalSwiping){
                    settleLeft = -mScreenWidth;
                }else if(left < -leftThreshold){
                    settleLeft = -mScreenWidth;
                }

            }else{
                if(left > leftThreshold){
                    settleLeft = mScreenWidth;
                }else if(left < -leftThreshold){
                    settleLeft = -mScreenWidth;
                }
            }

            mDragHelper.settleCapturedViewAt(settleLeft, releasedChild.getTop());
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1f - ((float)Math.abs(left) / (float)mScreenWidth);

            if(mListener != null) mListener.onSlideChange(percent);

            // Update the dimmer alpha
            applyScrim(percent);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if(mListener != null) mListener.onStateChanged(state);
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

    /***********************************************************************************************
     *
     * Helper Methods
     *
     */

    /**
     * Apply the scrim to the dim view
     * @param percent
     */
    public void applyScrim(float percent){
        float alpha = (percent * (mConfig.getScrimStartAlpha() - mConfig.getScrimEndAlpha())) + mConfig.getScrimEndAlpha();
        mDimView.setAlpha(alpha);
    }

    /***********************************************************************************************
     *
     * Static methods and Interfaces
     *
     */

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
     * The panel sliding interface that gets called
     * whenever the panel is closed or opened
     */
    public interface OnPanelSlideListener{
        void onStateChanged(int state);
        void onClosed();
        void onOpened();
        void onSlideChange(float percent);
    }

}
