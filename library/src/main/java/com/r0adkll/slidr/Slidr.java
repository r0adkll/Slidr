package com.r0adkll.slidr;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.widget.SliderPanel;

/**
 * This attacher class is used to attach the sliding mechanism to any {@link android.app.Activity}
 * that lets the user slide (or swipe) the activity away as a form of back or up action. The action
 * causes {@link android.app.Activity#finish()} to be called.
 *
 *
 * Created by r0adkll on 8/18/14.
 */
public class Slidr {

    /**
     * Attach a slideable mechanism to an activity that adds the slide to dismiss functionality
     *
     * @param activity      the activity to attach the slider to
     * @return              a {@link com.r0adkll.slidr.model.SlidrInterface} that allows
     *                      the user to lock/unlock the sliding mechanism for whatever purpose.
     */
    public static SlidrInterface attach(Activity activity){
        return attach(activity, -1, -1);
    }

    /**
     * Attach a slideable mechanism to an activity that adds the slide to dismiss functionality
     * and allows for the statusbar to transition between colors
     *
     * @param activity          the activity to attach the slider to
     * @param statusBarColor1   the primaryDark status bar color of the interface that this will slide back to
     * @param statusBarColor2   the primaryDark status bar color of the activity this is attaching to that will transition
     *                          back to the statusBarColor1 color
     *
     * @return              a {@link com.r0adkll.slidr.model.SlidrInterface} that allows
     *                      the user to lock/unlock the sliding mechanism for whatever purpose.
     */
    public static SlidrInterface attach(final Activity activity, final int statusBarColor1, final int statusBarColor2){

		// Setup the slider panel and attach it to the decor
		final SliderPanel panel = initSliderPanel(activity, null);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new SliderPanel.OnPanelSlideListener() {

            private final ArgbEvaluator mEvaluator = new ArgbEvaluator();

            @Override
            public void onStateChanged(int state) {

            }

            @Override
            public void onClosed() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }

            @Override
            public void onOpened() {

			}

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSlideChange(float percent) {
                // Interpolate the statusbar color
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                        statusBarColor1 != -1 && statusBarColor2 != -1){
                    int newColor = (int) mEvaluator.evaluate(percent, statusBarColor1, statusBarColor2);
                    activity.getWindow().setStatusBarColor(newColor);
                }
            }
        });

		// Return the lock interface
		return initInterface(panel);
    }

    /**
     * Attach a slider mechanism to an activity based on the passed {@link com.r0adkll.slidr.model.SlidrConfig}
     *
     * @param activity      the activity to attach the slider to
     * @param config        the slider configuration to make
     * @return              a {@link com.r0adkll.slidr.model.SlidrInterface} that allows
     *                      the user to lock/unlock the sliding mechanism for whatever purpose.
     */
    public static SlidrInterface attach(final Activity activity, final SlidrConfig config){

        // Setup the slider panel and attach it to the decor
        final SliderPanel panel = initSliderPanel(activity, config);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new SliderPanel.OnPanelSlideListener() {

            private final ArgbEvaluator mEvaluator = new ArgbEvaluator();

            @Override
            public void onStateChanged(int state) {
                if(config.getListener() != null){
                    config.getListener().onSlideStateChanged(state);
                }
            }

            @Override
            public void onClosed() {
                if(config.getListener() != null){
                    config.getListener().onSlideClosed();
                }

                activity.finish();
                activity.overridePendingTransition(0, 0);
            }

            @Override
            public void onOpened() {
                if(config.getListener() != null){
                    config.getListener().onSlideOpened();
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSlideChange(float percent) {
                // Interpolate the statusbar color
                // TODO: Add support for KitKat
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                        config.areStatusBarColorsValid()){

                    int newColor = (int) mEvaluator.evaluate(percent, config.getPrimaryColor(),
                            config.getSecondaryColor());

                    activity.getWindow().setStatusBarColor(newColor);
                }

                if(config.getListener() != null){
                    config.getListener().onSlideChange(percent);
                }
            }
        });

        // Return the lock interface
        return initInterface(panel);
    }

	private static SliderPanel initSliderPanel(final Activity activity, final SlidrConfig config) {
		// Hijack the decorview
		ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
		View oldScreen = decorView.getChildAt(0);
		decorView.removeViewAt(0);

		// Setup the slider panel and attach it to the decor
		SliderPanel panel = new SliderPanel(activity, oldScreen, config);
		panel.setId(R.id.slidable_panel);
		oldScreen.setId(R.id.slidable_content);
		panel.addView(oldScreen);
		decorView.addView(panel, 0);
		return panel;
	}

	private static SlidrInterface initInterface(final SliderPanel panel) {
		// Setup the lock interface
		SlidrInterface slidrInterface = new SlidrInterface() {
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
		return slidrInterface;
	}

}
