package com.example.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class to store searched for artists
 */
public class ArtistStorage implements Parcelable {
    public final Creator<ArtistStorage> CREATOR = new Creator<ArtistStorage>() {

        @Override
        public ArtistStorage createFromParcel(Parcel source) {
            return new ArtistStorage(source);
        }

        @Override
        public ArtistStorage[] newArray(int size) {
            return new ArtistStorage[size];
        }
    };
    String artistImage;
    String artistName;
    String artistId;

    public ArtistStorage(String aImage, String aName, String aId) {
        this.artistImage = aImage;
        this.artistName = aName;
        this.artistId = aId;
    }

    private ArtistStorage(Parcel in) {
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
