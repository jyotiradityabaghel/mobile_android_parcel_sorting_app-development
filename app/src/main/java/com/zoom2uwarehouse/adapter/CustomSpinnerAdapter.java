package com.zoom2uwarehouse.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.zoom2uwarehouse.R;

import java.util.ArrayList;

/**
 * @author avadhesh
 * Created by ubuntu on 7/2/18.
 */

class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Typeface font;
    private final Context activity;
    private ArrayList<String> asr;

    public CustomSpinnerAdapter(Context context,Typeface font, ArrayList<String> asr) {
        this.asr = asr;
        activity = context;
        this.font=font;
    }


    public int getCount() {
        return asr.size();
    }

    public Object getItem(int i) {
        return asr.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(11, 11, 11, 11);
        txt.setTextSize(14);
        txt.setTypeface(font);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(asr.get(position));
        txt.setTextColor(Color.parseColor("#4B5054"));
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setTypeface(font);
        txt.setPadding(11, 11, 11, 11);
        txt.setTextSize(14);
        txt.setTextColor(Color.parseColor("#4B5054"));
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_arrow, 0);
        txt.setText(asr.get(i));
        return txt;
    }

}
