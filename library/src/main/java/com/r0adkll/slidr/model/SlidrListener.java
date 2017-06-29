package com.r0adkll.slidr.model;

/**
 * This listener interface is for receiving events from the sliding panel such as state changes
 * and slide progress
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
    void onSlideStateChanged(int state);

    void onSlideChange(float percent);

    void onSlideOpened();

    void onSlideClosed();
}
