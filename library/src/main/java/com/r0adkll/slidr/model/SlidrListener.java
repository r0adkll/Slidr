package com.r0adkll.slidr.model;

/**
 * This listener interface is for receiving events from the sliding panel such as state changes
 * and slide progress
 *
 * Project: Slidr
 * Package: com.r0adkll.slidr.model
 * Created by drew.heavner on 2/24/15.
 */
public interface SlidrListener {

    /**
     * This is called when the {@link android.support.v4.widget.ViewDragHelper} calls it's
     * state change callback.
     *
     * @see android.support.v4.widget.ViewDragHelper#STATE_IDLE
     * @see android.support.v4.widget.ViewDragHelper#STATE_DRAGGING
     * @see android.support.v4.widget.ViewDragHelper#STATE_SETTLING
     *
     * @param state     the {@link android.support.v4.widget.ViewDragHelper} state
     */
    public void onSlideStateChanged(int state);

    public void onSlideChange(float percent);

    public void onSlideOpened();

    public void onSlideClosed();

}
