package com.zoom2uwarehouse.adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zoom2uwarehouse.R;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {

    //Creating an array list of POJO objects

    private ArrayList<Pair<String, Integer>> array1;

    private final LayoutInflater inflater;
    private Context context;


    public DrawerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    //This method inflates view present in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_item, parent, false);
        return new MyViewHolder(view);
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.content.setText(array1.get(position).first);
        holder.content.setCompoundDrawablesWithIntrinsicBounds(array1.get(position).second,0,0,0);
    }

    //Setting the array list
    public void setListContent(ArrayList<Pair<String, Integer>> array ) {
        array1= array;

        notifyItemRangeChanged(0, array1.size());

    }


    @Override
    public int getItemCount() {
        return array1.size();
    }


    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView  content;

        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        //    content = itemView.findViewById(R.id.textView6);


        }

        @Override
        public void onClick(View v) {

        }
    }



}