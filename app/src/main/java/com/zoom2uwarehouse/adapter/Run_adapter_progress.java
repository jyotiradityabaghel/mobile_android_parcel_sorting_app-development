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

public class Run_adapter_progress extends RecyclerView.Adapter<Run_adapter_progress.ViewHolder> {

    private ArrayList<Get_list> mRestaurants ;
    private Context context;

    public Run_adapter_progress(Context context1, ArrayList<Get_list> restaurants) {

        mRestaurants = restaurants;
        this.context = context1;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        TextView textView2;
        RelativeLayout backgroundView;

        ViewHolder(View v) {

            super (v);
            textView1 = v.findViewById (R.id.textView46);
            textView2 = v.findViewById (R.id.textView47);
            backgroundView= v.findViewById (R.id.backgroundView);
        }
    }

    @Override
    public Run_adapter_progress.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {

        View view1 = LayoutInflater.from(context).inflate(R.layout.activity_run_progress, parent, false);

        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder (Run_adapter_progress.ViewHolder holder, int position) {


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