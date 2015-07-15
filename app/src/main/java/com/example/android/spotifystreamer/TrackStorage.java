package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to store top ten tracks for selected artist
 */
public class TrackStorage implements Parcelable {
    public final Parcelable.Creator<TrackStorage> CREATOR = new Parcelable.Creator<TrackStorage>() {

        @Override
        public TrackStorage createFromParcel(Parcel source) {
            return new TrackStorage(source);
        }

        @Override
        public TrackStorage[] newArray(int size) {
            return new TrackStorage[size];
        }
    };
    String albumImage;
    String trackName;
    String albumName;

    public TrackStorage(String aImage, String tName, String aName) {
        this.albumImage = aImage;
        this.trackName = tName;
        this.albumName = aName;
    }

    private TrackStorage(Parcel in) {
        albumImage = in.readString();
        trackName = in.readString();
        albumName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(albumImage);
        dest.writeString(trackName);
        dest.writeString(albumName);
    }
}
