package com.example.android.popularmovieproject1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MD on 2016-04-06.
 */
public class MovieInfo implements Parcelable{
    private String title;
    private String posterPath;
    private String overView;
    private String releaseDate;
    private String voteAverage;

    public MovieInfo(String title, String posterPath, String overView, String releaseDate, String voteAverage) {
        this.title = title;
        this.posterPath = posterPath;
        this.overView = overView;
        this.releaseDate = releaseDate;
        this. voteAverage = voteAverage;

    }
    //just for the test on posterPath
    public MovieInfo() {

    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }



    /*Pacelable를 사용하면 대량의 정보를 전달할때 빠르게 전달할 수 있다 serializable보다 빠름*/

    private MovieInfo(Parcel in){
        title = in.readString();
        posterPath = in.readString();
    }
    /*Parcelable 인터페이스를 사용하기 위해 구현해야하는 메소드들 : describeContents, writeToParcel*/
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterPath);
    }

    public final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel parcel) {
            return new MovieInfo(parcel);
        }

        @Override
        public MovieInfo[] newArray(int i) {
            return new MovieInfo[i];
        }
    };
}
