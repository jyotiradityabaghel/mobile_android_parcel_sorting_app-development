package com.zoom2uwarehouse.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.get_set.Get_list;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Run_adapter_pickup extends RecyclerView.Adapter<Run_adapter_pickup.ViewHolder> {

    private ArrayList<Get_list> mRestaurants ;
    private Context context;

    public Run_adapter_pickup(Context context1, ArrayList<Get_list> restaurants) {

        mRestaurants = restaurants;
        this.context = context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView46) TextView textView1;
        @BindView(R.id.textView47) TextView textView2;
        @BindView(R.id.backgroundView) RelativeLayout backgroundView;


        ViewHolder(View v) {

            super (v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public Run_adapter_pickup.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

        View view1 = LayoutInflater.from(context).inflate(R.layout.activity_run_pickup, parent, false);

        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder (Run_adapter_pickup.ViewHolder holder, int position) {

       /* holder.textView1.setText(mRestaurants.get(position).getName());

        holder.textView2.setText(mRestaurants.get(position).getValue());*/
        // holder.imageView1.setImageResource (image.getResourceId (position, -1));
   /*    Picasso.with (context).load (image.getResourceId (position,-1)).resize (400,280).
                placeholder (R.drawable.ic_share_white_24dp).into (holder.imageView1);*/
        if ( position % 2 == 0) {
            holder.backgroundView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            holder.backgroundView.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }
        int i=getItemCount()-1;


        if (i == position)
        {

            if ( position % 2 == 0) {
                holder.backgroundView.setBackgroundResource(R.drawable.custom_curve_bottom_white);
            } else {
                holder.backgroundView.setBackgroundResource(R.drawable.custom_curve_bottom_light);
            }
        }
    }

    @Override
    public int getItemCount () {

        return mRestaurants.size();
    }
}