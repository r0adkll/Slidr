package com.r0adkll.slidableactivity;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.r0adkll.slidableactivity.widget.SliderPanel;

/**
 * Created by r0adkll on 8/18/14.
 */
public class SlidableAttacher {

    /**
     * Attach a slidable mechanism to an activity that adds the slide to dismiss functionality
     *
     * @param activity
     */
    public static void attach(final Activity activity){

        // Hijack the decorview
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        View oldScreen = decorView.getChildAt(0);
        decorView.removeViewAt(0);

        // Setup the slider panel and attach it to the decor
        SliderPanel panel = new SliderPanel(activity, oldScreen);
        panel.addView(oldScreen);
        decorView.addView(panel, 0);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new SliderPanel.OnPanelSlideListener() {
            @Override
            public void onClosed() {
                activity.finish();
            }

            @Override
            public void onOpened() {}
        });

    }

}
