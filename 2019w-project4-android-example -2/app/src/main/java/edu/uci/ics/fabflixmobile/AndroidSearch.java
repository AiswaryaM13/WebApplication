package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class AndroidSearch extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Bundle bundle = getIntent().getExtras();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_red, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void connectToTomcat2(View view) {
        String title1 = ((EditText) findViewById(R.id.m_title)).getText().toString();
        String numperpage = ((EditText) findViewById(R.id.numperpage)).getText().toString();
        String pagenum = ((EditText) findViewById(R.id.pagenum)).getText().toString();
        String sortby = ((EditText) findViewById(R.id.sortby)).getText().toString();
        String sort = ((EditText) findViewById(R.id.sort)).getText().toString();

        Intent goToIntent2 = new Intent(this, AndroidMovies.class);

        int i=Integer.parseInt(pagenum);
        System.out.println(i);
        Log.d("title is ", title1);



        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // 10.0.2.2 is the host machine when running the android emulator


        final StringRequest movieRequest = new StringRequest(Request.Method.GET, "https://3.17.128.45:8443/project1-master-team90/api/movies?title="+title1+"&year="+""+"&director="+""+"&starname="+""+"&genre="+""+"&letter="+""+"&pagenum="+pagenum+"&numperpage="+numperpage+"&sortby="+sortby+"&sort="+sort,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // System.out.println(response.getJSONArray);

                        System.out.println(response);

                        Log.d("login.success", response);
                        ((TextView) findViewById(R.id.http_response)).setText(response);

                        goToIntent2.putExtra("response", response);
                        goToIntent2.putExtra("title1", title1);
                        goToIntent2.putExtra("pagenum", pagenum);
                        goToIntent2.putExtra("numperpage", numperpage);
                        goToIntent2.putExtra("sort", sort);
                        goToIntent2.putExtra("sortby", sortby);

                        startActivity(goToIntent2);

                        // Add the request to the RequestQueue.
                    //  queue.add(afterLoginRequest);
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

        };

        movieRequest.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // !important: queue.add is where the login request is actually sent
        queue.add(movieRequest);

    }

}
