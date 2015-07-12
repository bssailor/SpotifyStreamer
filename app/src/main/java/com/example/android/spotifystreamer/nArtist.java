package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to store searched for artists
 */
public class nArtist implements Parcelable {
    public final Creator<nArtist> CREATOR = new Creator<nArtist>() {

        @Override
        public nArtist createFromParcel(Parcel source) {
            return new nArtist(source);
        }

        @Override
        public nArtist[] newArray(int size) {
            return new nArtist[size];
        }
    };
    String artistImage;
    String artistName;
    String artistId;

    public nArtist(String aImage, String aName, String aId) {
        this.artistImage = aImage;
        this.artistName = aName;
        this.artistId = aId;
    }

    private nArtist(Parcel in) {
        artistImage = in.readString();
        artistName = in.readString();
        artistId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistImage);
        dest.writeString(artistName);
        dest.writeString(artistId);
    }
}
