package com.example.android.popularmovieproject1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MD on 2016-04-06.
 */
public class MoviePoster implements Parcelable{
    String name;
    int posterImage;

    public MoviePoster(String name, int posterImage) {
        this.name = name;
        this.posterImage = posterImage;
    }

    /*Pacelable를 사용하면 대량의 정보를 전달할때 빠르게 전달할 수 있다 serializable보다 빠름*/

    private MoviePoster(Parcel in){
        name = in.readString();
        posterImage = in.readInt();
    }
    /*Parcelable 인터페이스를 사용하기 위해 구현해야하는 메소드들 : describeContents, writeToParcel*/
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(posterImage);
    }

    public final Parcelable.Creator<MoviePoster> CREATOR = new Parcelable.Creator<MoviePoster>() {
        @Override
        public MoviePoster createFromParcel(Parcel parcel) {
            return new MoviePoster(parcel);
        }

        @Override
        public MoviePoster[] newArray(int i) {
            return new MoviePoster[i];
        }
    };
}
