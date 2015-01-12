package com.r0adkll.slidr.example.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by r0adkll on 1/11/15.
 */
public class AndroidOS implements Parcelable{
    public String name;
    public String version;
    public int sdk_int;
    public String description;
    public int year;
    public String image_url;
    public String icon_url;

    public AndroidOS(){}

    private AndroidOS(Parcel in){
        name = in.readString();
        version = in.readString();
        sdk_int = in.readInt();
        description = in.readString();
        year = in.readInt();
        image_url = in.readString();
        icon_url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(version);
        dest.writeInt(sdk_int);
        dest.writeString(description);
        dest.writeInt(year);
        dest.writeString(image_url);
        dest.writeString(icon_url);
    }

    public static final Creator<AndroidOS> CREATOR = new Creator<AndroidOS>() {
        @Override
        public AndroidOS createFromParcel(Parcel source) {
            return new AndroidOS(source);
        }

        @Override
        public AndroidOS[] newArray(int size) {
            return new AndroidOS[size];
        }
    };

}
