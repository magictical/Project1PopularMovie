package com.example.android.popularmovieproject1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MD on 2016-04-06.
 */
public class MovieAdapter extends ArrayAdapter<MovieInfo> {



    List<MovieInfo>  movieInfoList = new ArrayList<MovieInfo>();

    public MovieAdapter(Activity context, List<MovieInfo> movieInfoList)
    {
        super(context, 0, movieInfoList);

        this.movieInfoList = movieInfoList;
    }


    /*custom adapter를 사용할 경우 반드시 getView Override 해줘야함*/

   //순서좀 알자 클래스가 호출(오브젝트 생성되면) 메소드가 호출되고 실행되는게 맞나 영어로 적절한 단어는?

    @Override
    //인자를 context로 부터 받는게 맞는가?
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;

        if (view == null) {
            //접근자가 어떻게 되는지 어떻게 읽어 들이는지 잘모르겠음
            //파라미터도 물어봐야할듯
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_fragment, parent, false);


            holder = new ViewHolder();
            holder.posterHolder = (ImageView) view.findViewById(R.id.list_item_poster_image);
            //현재의 view를 저장하겠다는 뜻
            view.setTag(holder);
            } else {
                //기존 view가 있을경우 getTag로 불러옴
                holder = (ViewHolder) view.getTag();
            }
        //position이 어떤식으로 쓰이는가?갈피를 못잡겠음
        MovieInfo setOfData = movieInfoList.get(position);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + setOfData.getPosterPath())
                .into(holder.posterHolder);



        return view;
    }

    public class ViewHolder {
        public ImageView posterHolder;
    }
}
