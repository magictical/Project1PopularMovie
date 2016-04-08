package com.example.android.popularmovieproject1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MD on 2016-04-06.
 */
public class PostAdapter extends ArrayAdapter<MoviePoster> {

    public PostAdapter(Activity context, List<MoviePoster> image)
    {
        super(context, 0, image);
    }
    /*custom adapter를 사용할 경우 반드시 getView Override 해줘야함*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoviePoster moviePoster = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_fragment, parent, false);
        }

        /*가져올 이미지를 findviewby로 가져오고
        * 변수에 저장(setImageResource사용)*/
        ImageView postView = (ImageView) convertView.findViewById(R.id.list_item_poster_image);
        postView.setImageResource(moviePoster.posterImage);

        TextView titleView = (TextView) convertView.findViewById(R.id.list_item_poster_title);
        titleView.setText(moviePoster.name);

        return convertView;
    }
}
