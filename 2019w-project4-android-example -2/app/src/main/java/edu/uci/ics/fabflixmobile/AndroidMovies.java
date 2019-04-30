package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AndroidMovies extends ActionBarActivity {

    String pagenum="";
    String title1="";
    String sort="";
    String sortby="";
    String numperpage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent goToIntent4 = new Intent(this, AndroidSinglemovie.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        Bundle bundle = getIntent().getExtras();
        //Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();

        String resp = bundle.getString("response");
        title1 = bundle.getString("title1");
        sort = bundle.getString("sort");
        sortby = bundle.getString("sortby");
        pagenum = bundle.getString("pagenum");
        numperpage = bundle.getString("numperpage");
//        if (resp != null && !"".equals(resp)) {
//            ((TextView) findViewById(R.id.last_page_msg_container)).setText(resp);
//        }
        ArrayList<AndroidResult> androidResults = new ArrayList<>();
        try{
            JSONArray json= new JSONArray(resp);
            for(int i=0;i<json.length();i++)
            {
                JSONObject jsonObject=json.getJSONObject(i);
                String title=jsonObject.getString("title");
                String year=jsonObject.getString("year");
                String director=jsonObject.getString("director");
                String rating=jsonObject.getString("rating");
                ArrayList<String> stars= new ArrayList<>();
                ArrayList<String> genres= new ArrayList<>();
                for(int j=0;j<jsonObject.getString("star_name").split(",").length;j++)
                {
                    stars.add(jsonObject.getString("star_name").split(",")[j]);
                }
                for(int j=0;j<jsonObject.getString("genreName").split(",").length;j++)
                {
                    genres.add(jsonObject.getString("genreName").split(",")[j]);
                }
                //Log.d("title:",title);
                androidResults.add(new AndroidResult(title,year, director,rating,genres,stars));
            }
            ListViewAdapter adapter= new ListViewAdapter(androidResults,this);
            ListView listView = (ListView)findViewById(R.id.list);
            listView.setAdapter(adapter);

            final Button mButton1=(Button)findViewById(R.id.next);
            String a= (String) mButton1.getText();
            // System.out.println(a);

            final Button mButton2=(Button)findViewById(R.id.prev);
            String b= (String) mButton2.getText();

            mButton1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    pagenum=(Integer.parseInt(pagenum)+1)+"";
                    connectToTomcat4();
                }
            });

            mButton2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    if(!pagenum.equals("1"))
                    {
                        pagenum=(Integer.parseInt(pagenum)-1)+"";
                        connectToTomcat4();
                    }
                }
            });
            System.out.println("The pagenum is " + pagenum);


           // listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("HI in single movie part");
                    AndroidResult res = androidResults.get(position);

                    goToIntent4.putExtra("title", res.getTitle());
                    goToIntent4.putExtra("year", res.getYear());
                    goToIntent4.putExtra("director", res.getDirector());
                    goToIntent4.putExtra("rating", res.getRating());
                    System.out.println("genres are"+ res.getGenres());
                    System.out.println("stars are"+ res.getStars());
                    goToIntent4.putExtra("genres", res.getGenres().toString());
                    goToIntent4.putExtra("stars", res.getStars().toString());


                    startActivity(goToIntent4);
                }
            });
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void connectToTomcat4() {

        Intent goToIntent3 = new Intent(this, AndroidMovies.class);

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest movieRequest = new StringRequest(Request.Method.GET, "https://3.17.128.45:8443/project1-master-team90/api/movies?title="+title1+"&year="+""+"&director="+""+"&starname="+""+"&genre="+""+"&letter="+""+"&pagenum="+pagenum+"&numperpage="+numperpage+"&sortby="+sortby+"&sort="+sort,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);
                        //((TextView) findViewById(R.id.http_response)).setText(response);
                        goToIntent3.putExtra("response", response);
                        goToIntent3.putExtra("title1", title1);
                        goToIntent3.putExtra("pagenum", pagenum);
                        goToIntent3.putExtra("numperpage", numperpage);
                        goToIntent3.putExtra("sort", sort);
                        goToIntent3.putExtra("sortby", sortby);

                        startActivity(goToIntent3);


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());
                    }
                }
        ) {
//
        };

        movieRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // !important: queue.add is where the login request is actually sent
        queue.add(movieRequest);
    }

}
