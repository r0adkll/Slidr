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

    public void onSlideChange(float percent);

    public void onSlideOpened();

    public void onSlideClosed();

}
