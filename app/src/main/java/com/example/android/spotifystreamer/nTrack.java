package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to store top ten tracks for selected artist
 */
public class nTrack implements Parcelable {
    public final Parcelable.Creator<nTrack> CREATOR = new Parcelable.Creator<nTrack>() {

        @Override
        public nTrack createFromParcel(Parcel source) {
            return new nTrack(source);
        }

        @Override
        public nTrack[] newArray(int size) {
            return new nTrack[size];
        }
    };
    String albumImage;
    String trackName;
    String albumName;

    public nTrack(String aImage, String tName, String aName) {
        this.albumImage = aImage;
        this.trackName = tName;
        this.albumName = aName;
    }

    private nTrack(Parcel in) {
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
