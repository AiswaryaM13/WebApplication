package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AndroidSinglemovie extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlemovie);

        Bundle bundle = getIntent().getExtras();
        //Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();

        String title = bundle.getString("title");
        if (title != null && !"".equals(title)) {
            ((TextView) findViewById(R.id.title)).setText(title);
        }

        String year = bundle.getString("year");
        if (year != null && !"".equals(year)) {
            ((TextView) findViewById(R.id.year)).setText(year);
        }

        String dir = bundle.getString("director");
        if (dir != null && !"".equals(dir)) {
            ((TextView) findViewById(R.id.director)).setText(dir);
        }

        String rating = bundle.getString("rating");
        if (rating != null && !"".equals(rating)) {
            ((TextView) findViewById(R.id.rating)).setText(rating);
        }

        String genres = bundle.getString("genres");
        if (genres != null && !"".equals(genres)) {
            ((TextView) findViewById(R.id.genres)).setText(genres);
        }

        String stars = bundle.getString("stars");
        if (stars != null && !"".equals(stars)) {
            ((TextView) findViewById(R.id.stars)).setText(stars);
        }

    }
}



