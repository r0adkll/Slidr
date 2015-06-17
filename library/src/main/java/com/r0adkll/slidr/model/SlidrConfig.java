package com.r0adkll.slidr.model;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;

/**
 * This class contains the configuration information for all the options available in
 * this library
 *
 * Created by r0adkll on 1/12/15.
 */
public class SlidrConfig {

    private int colorPrimary = -1;
    private int colorSecondary = -1;
    private float touchSize = -1f;
    private float sensitivity = 1f;
    private int scrimColor = Color.BLACK;
    private float scrimStartAlpha = 0.8f;
    private float scrimEndAlpha = 0f;
    private float velocityThreshold = 5f;

    private SlidrPosition position = SlidrPosition.LEFT;
    private SlidrListener listener;

    /**
     * Hidden Constructor
     * Use the builder pattern
     */
    private SlidrConfig(){}

    /**
     * Get the primary color that the slider will interpolate. That is this color is the color
     * of the status bar of the Activity you are returning to
     *
     * @return      the primary status bar color
     */
    public int getPrimaryColor(){
        return colorPrimary;
    }

    /**
     * Get the secondary color that the slider will interpolatel That is the color of the Activity
     * that you are making slidable
     *
     * @return      the secondary status bar color
     */
    public int getSecondaryColor(){
        return colorSecondary;
    }

    /**
     * Get the color of the background scrim
     *
     * @return      the scrim color integer
     */
    public int getScrimColor(){
        return scrimColor;
    }

    /**
     * Get teh start alpha value for when the activity is not swiped at all
     *
     * @return      the start alpha value (0.0 to 1.0)
     */
    public float getScrimStartAlpha(){
        return scrimStartAlpha;
    }

    /**
     * Get the end alpha value for when the user almost swipes the activity off the screen
     *
     * @return      the end alpha value (0.0 to 1.0)
     */
    public float getScrimEndAlpha(){
        return scrimEndAlpha;
    }

    /**
     * Get the position of the slidable mechanism for this configuration. This is the position on
     * the screen that the user can swipe the activity away from
     *
     * @return      the slider position
     */
    public SlidrPosition getPosition(){
        return position;
    }

    /**
     * Get the touch 'width' to be used in the gesture detection. This value should incorporate with
     * the device's touch slop
     *
     * @return      the touch area size
     */
    public float getTouchSize(){
        return touchSize;
    }

    /**
     * Get the touch sensitivity set in the {@link android.support.v4.widget.ViewDragHelper} when
     * creating it.
     *
     * @return      the touch sensitivity
     */
    public float getSensitivity(){
        return sensitivity;
    }

    /**
     * Get the slidr listener set by the user to respond to certain events in the sliding
     * mechanism.
     *
     * @return      the slidr listener
     */
    public SlidrListener getListener(){
        return listener;
    }

    /**
     * Return whether or not the set status bar colors are valid
     * @return
     */
    public boolean areStatusBarColorsValid(){
        return colorPrimary != -1 && colorSecondary != -1;
    }

    /**
     * The Builder for this configuration class. This is the only way to create a
     * configuration
     */
    public static class Builder{

        private SlidrConfig config;

        public Builder(){
            config = new SlidrConfig();
        }

        public Builder primaryColor(@ColorInt int color){
            config.colorPrimary = color;
            return this;
        }

        public Builder secondaryColor(@ColorInt int color){
            config.colorSecondary = color;
            return this;
        }

        public Builder position(SlidrPosition position){
            config.position = position;
            return this;
        }

        public Builder touchSize(float size){
            config.touchSize = size;
            return this;
        }

        public Builder sensitivity(float sensitivity){
            config.sensitivity = sensitivity;
            return this;
        }

        public Builder scrimColor(@ColorInt int color){
            config.scrimColor = color;
            return this;
        }

        public Builder scrimStartAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha){
            config.scrimStartAlpha = alpha;
            return this;
        }

        public Builder scrimEndAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha){
            config.scrimEndAlpha = alpha;
            return this;
        }

        public Builder velocityThreshold(float threshold){
            config.velocityThreshold = threshold;
            return this;
        }

        public Builder listener(SlidrListener listener){
            config.listener = listener;
            return this;
        }

        public SlidrConfig build(){
            return config;
        }

    }

}
