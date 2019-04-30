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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AndroidLogin extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("last_activity") != null) {
                Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();
            }
        }
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


    public void connectToTomcat(View view) {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        Log.d("username is ", username);
        Log.d("password is", password);
        Intent goToIntent = new Intent(this, AndroidSearch.class);
        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, "https://3.17.128.45:8443/project1-master-team90/api/search",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        startActivity(goToIntent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("username.error", error.toString());
                    }
                }
        );


        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://3.17.128.45:8443/project1-master-team90/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String[]comma=response.split(",");
                        String[]colon=comma[0].split(":");
                        String result=colon[1];
                        System.out.println(result);
                        Log.d("login.success", response);
                        ((TextView) findViewById(R.id.http_response)).setText(response);

                       queue.add(afterLoginRequest);
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
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };


        queue.add(loginRequest);

    }



}
