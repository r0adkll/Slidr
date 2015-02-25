package com.r0adkll.slidr.model;

/**
 * This class contains the configuration information for all the options available in
 * this library
 *
 * Created by r0adkll on 1/12/15.
 */
public class SlidrConfig {

    private int colorPrimary = -1;
    private int colorSecondary = -1;
    private SlidrPosition position = SlidrPosition.LEFT;
    private float touchSize = -1f;
    private float sensitivity = 1f;
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

        public Builder primaryColor(int color){
            config.colorPrimary = color;
            return this;
        }

        public Builder secondaryColor(int color){
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

        public Builder listener(SlidrListener listener){
            config.listener = listener;
            return this;
        }

        public SlidrConfig build(){
            return config;
        }

    }

}
