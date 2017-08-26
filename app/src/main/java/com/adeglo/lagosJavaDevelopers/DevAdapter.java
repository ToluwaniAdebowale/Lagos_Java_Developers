package com.adeglo.lagosJavaDevelopers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class DevAdapter  extends RecyclerView.Adapter<DevAdapter.CustomViewHolder>{

    List<JsonObject> list_of_users = new ArrayList<>();

    public DevAdapter(String rawdata){
        JsonObject ji = new JsonParser().parse(rawdata).getAsJsonObject();
        JsonArray jar = ji.get("items").getAsJsonArray();
        //insert users into virtual list
        for(int r=0; r<jar.size(); r++){
            list_of_users.add(jar.get(r).getAsJsonObject());
        }
    }

    /**
     * This function below updates the recycler view through its adapter from an external class
     * **/
    public void updatelist(String rawdata_for_more_users) {
        JsonObject ji = new JsonParser().parse(rawdata_for_more_users).getAsJsonObject();
        JsonArray jar = ji.get("items").getAsJsonArray();
        for(int r=0; r<jar.size(); r++){
            list_of_users.add(jar.get(r).getAsJsonObject());
        }
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout_view,parent,false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list_of_users.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder{
        CircleImageView ppic;
        TextView name;
        Context context;
        View clicka;
        public CustomViewHolder(View itemView){
            super(itemView);
            ppic = (CircleImageView) itemView.findViewById(R.id.ppic);
            name = (TextView) itemView.findViewById(R.id.username);
            clicka = itemView.findViewById(R.id.clicka);
            context = itemView.getContext();
        }
        void bind(int position){
            final JsonObject obj = list_of_users.get(position);
            String nam = obj.get("login").getAsString(),
                    avatar_url = obj.get("avatar_url").getAsString();

            name.setText(nam);

            //load user's profile picture with Picasso
            Picasso.with(context).load(avatar_url).placeholder(R.drawable.ppic100).into(ppic);

            //on click of a user, launch profile page
            clicka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, com.intija.recyclerviewexample.ProfileView.class).putExtra("userdetails", obj.toString()));
                }
            });

        }
    }
}
