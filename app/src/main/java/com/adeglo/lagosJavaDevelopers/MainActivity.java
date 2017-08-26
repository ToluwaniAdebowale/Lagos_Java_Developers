package com.adeglo.lagosJavaDevelopers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;

/**
 * NOTE that for app to work well, some dependencies were added to the app's build.gradle file
 * Also some permissions were added to the manifest file.. these permissions are needed
 * for app to connect to the internet**/

//For the GLORY of the codes ;)

public class MainActivity extends AppCompatActivity {

    private RecyclerView rview;
    private com.adeglo.lagosJavaDevelopers.DevAdapter adapter;
    private String page = "1";
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connecting recycler view and progress bar with XML
        rview = (RecyclerView) findViewById(R.id.rview);
        //the line of code below means the users should be shown linearly
        rview.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progress);
        //this below simply means the progress bar has no percentage
        progressBar.setIndeterminate(true);

        /** Ignore this for now..its used for handling lags while scrolling**/
        rview.getRecycledViewPool().setMaxRecycledViews(0,0);
        rview.setHasFixedSize(true);
        rview.setItemViewCacheSize(20);
        rview.setDrawingCacheEnabled(true);
        rview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);



        //adding listener to recycler ciew to detect if scrolling has gotten to the bottom, then load more github users
        rview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                // if recycler view cannot scroll vertically i.e if it has reached bottom
                if(!recyclerView.canScrollVertically(1)){
                    //this function is used to load the users from the internet
                    loadUsers(page);
                }
            }
        });

        //load users for first time
        loadUsers(page);
    }

    private void loadUsers(String page) {
        //check if device is connected to the internet then load it
        if(isConnected(this)){
            new getusers().execute(page);
        }
    }

    /**
     * This function below is used to check if your device is connected to the internet
     * **/
    public static boolean isConnected(Context con){
        ConnectivityManager cd = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
        if (cd != null) {
            NetworkInfo[] info = cd.getAllNetworkInfo();
            if (info != null)
                for(int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }

        }
        return false;
    }

    //this class connects to the internet
    private class getusers extends AsyncTask<String,Void,String>{
        String page = "1";
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            //this is called when the raw data have been gotten from the internet.. what next? put it to the UI as done below
           //check if its a valid json data
           if(s.contains("[") && s.contains("]")) {
               progressBar.setVisibility(View.GONE);
               if (getusers.this.page.equals("1")) {
                   rview.setAdapter(new com.adeglo.lagosJavaDevelopers.DevAdapter(s));
                   //increment the page number
                   MainActivity.this.page = String.valueOf(Integer.parseInt(getusers.this.page) + 1);
               } else {
                   ((com.adeglo.lagosJavaDevelopers.DevAdapter) rview.getAdapter()).updatelist(s);
                   MainActivity.this.page = String.valueOf(Integer.parseInt(page) + 1);
               }
           }else{
               Toast.makeText(getBaseContext(),"Unable to load", Toast.LENGTH_SHORT).show();
           }
        }

        @Override
        protected String doInBackground(String... strings) {
            page = strings[0];
            return (new com.adeglo.lagosJavaDevelopers.HUinvoker()).getResponseFromUrl("https://api.github.com/search/users?q=location:lagos&page="+page,new HashMap<String, String>());
        }
    }
}
