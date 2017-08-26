package com.adeglo.lagosJavaDevelopers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileView extends AppCompatActivity {
CircleImageView ppic;
    TextView name;
    TextView gittext;
    String gitlink,usernam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String object = getIntent().getStringExtra("userdetails");
        JsonObject obj = new JsonParser().parse(object).getAsJsonObject();

        ppic = (CircleImageView) findViewById(R.id.ppic);
        name = (TextView)findViewById(R.id.username);
        gittext = (TextView)findViewById(R.id.gitlink);
        gitlink = obj.get("html_url").getAsString();
        usernam=obj.get("login").getAsString();

        setTitle(usernam+"'s profile");


        Picasso.with(this).load(obj.get("avatar_url").getAsString()).fit().placeholder(R.drawable.ppic100).into(ppic);
        Picasso.with(this).load(obj.get("avatar_url").getAsString()).fit().placeholder(R.drawable.ppic100).into((ImageView)findViewById(R.id.bkimg));
        name.setText(obj.get("login").getAsString());
        gittext.setText(gitlink);

    }
    public void shareIntent(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,"Check out this awesome developer @"+usernam+", "+gitlink);
        startActivity(i);
    }
    public void viewprofile(View v){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(gitlink));
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
