/*
 * Copyright (c) 2014. 52inc
 * All Rights Reserved.
 */

package com.r0adkll.slidableactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.r0adkll.slidableactivity.widget.SliderPanel;

/**
 * This activity provides a vessel for having an activity that you can slide out of the way (aka finish() the
 * activity.
 *
 * Just extend this activity to automatically add the slide-away functionality.
 * This Activity attaches itself to the decor view upon creation, and adds the slide
 * funcionality.
 *
 * Project: PilotPass
 * Package: com.ftinc.mariner.pilotpass.ui.model
 * Created by drew.heavner on 8/14/14.
 */
public abstract class SlidableActivity extends Activity implements SliderPanel.OnPanelSlideListener {

    /*
     * The Touch framework to facilitate the slide menu mechanism
     * for this activity
     */
    private SliderPanel mSliderPanel;

    /**
     * Callend when the activity is created, here we hi-jack the decore view and insert
     * our own slide controller to enable sliding of the activity
     *
     * call super.onCreate(savedInstanceState) after calling {@link #setContentView(int)}
     *
     * @param savedInstanceState        the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hijack the decorview
        ViewGroup decorView = (ViewGroup)getWindow().getDecorView();
        View oldScreen = decorView.getChildAt(0);
        decorView.removeViewAt(0);

        // Setup the slider panel and attach it to the decor
        mSliderPanel = new SliderPanel(this, oldScreen);
        mSliderPanel.addView(oldScreen);
        decorView.addView(mSliderPanel, 0);

        // Set the panel slide listener for when it becomes closed or opened
        mSliderPanel.setOnPanelSlideListener(this);
    }

    @Override
    public void onClosed() {
        // Finish this activity
        finish();
    }

    @Override
    public void onOpened() {
        // Do Nothing
    }

    /**
     * Lock the slideable activity so you can't slide it away
     */
    public void lock(){
        mSliderPanel.lock();
    }

    /**
     * Un-lock the slideable activity so you can slide it away
     */
    public void unlock(){
        mSliderPanel.unlock();
    }
}
