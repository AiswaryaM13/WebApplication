package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<AndroidResult> {
    private ArrayList<AndroidResult> androidResults;

    public ListViewAdapter(ArrayList<AndroidResult> androidResults, Context context) {
        super(context, R.layout.layout_listview_row, androidResults);
        this.androidResults = androidResults;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_listview_row, parent, false);

        AndroidResult androidResult = androidResults.get(position);

        TextView titleView = (TextView)view.findViewById(R.id.title);
        TextView yearView = (TextView)view.findViewById(R.id.year);
        TextView directorView = (TextView)view.findViewById(R.id.director);
        TextView ratingView = (TextView)view.findViewById(R.id.rating);
        TextView genresView = (TextView)view.findViewById(R.id.genres);
        TextView starsView = (TextView)view.findViewById(R.id.stars);

        titleView.setText(androidResult.getTitle());
        yearView.setText(androidResult.getYear());
        directorView.setText(androidResult.getDirector());
        ratingView.setText(androidResult.getRating());
        genresView.setText(androidResult.getGenres().toString());
        starsView.setText(androidResult.getStars().toString());


        return view;
    }
}