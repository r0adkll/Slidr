package com.r0adkll.slidableactivity;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.r0adkll.slidableactivity.widget.SliderPanel;

/**
 * This attacher class is used to attach the sliding mechanism to any {@link android.app.Activity}
 * that lets the user slide (or swipe) the activity away as a form of back or up action. The action
 * causes {@link android.app.Activity#finish()} to be called.
 *
 *
 * Created by r0adkll on 8/18/14.
 */
public class SlidableAttacher {

    /**
     * Attach a slideable mechanism to an activity that adds the slide to dismiss functionality
     *
     * @param activity      the activity to attach the slider to
     * @return              a {@link com.r0adkll.slidableactivity.SlideLockInterface} that allows
     *                      the user to lock/unlock the sliding mechanism for whatever purpose.
     */
    public static SlideLockInterface attach(final Activity activity){

        // Hijack the decorview
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        View oldScreen = decorView.getChildAt(0);
        decorView.removeViewAt(0);

        // Setup the slider panel and attach it to the decor
        final SliderPanel panel = new SliderPanel(activity, oldScreen);
        panel.setId(R.id.slidable_panel);
        panel.addView(oldScreen);
        decorView.addView(panel, 0);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new SliderPanel.OnPanelSlideListener() {
            @Override
            public void onClosed() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }

            @Override
            public void onOpened() {}
        });

        // Setup the lock interface
        SlideLockInterface lockInterface = new SlideLockInterface() {
            @Override
            public void lock() {
                panel.lock();
            }

            @Override
            public void unlock() {
                panel.unlock();
            }
        };

        // Return the lock interface
        return lockInterface;
    }

}
