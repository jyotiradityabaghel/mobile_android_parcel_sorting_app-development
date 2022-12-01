package com.zoom2uwarehouse.re_assign_delivery;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zoom2uwarehouse.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 24-Dec-2018.
 */

public class AdapterAssignToOtherCourier extends RecyclerView.Adapter<AdapterAssignToOtherCourier.MyViewHolder>
        implements Filterable {

    private List<MyTeamList_Model> arrayOfDrivers;
    private List<MyTeamList_Model> arrayOfReAssignCouriers;
    private Context adapterViewContext;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MyTeamList_Model item, MyViewHolder holder, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout courierListReassignDelivery;
        TextView driverNameTxt, companyNameTxt;

        public MyViewHolder(View itemView) {
            super(itemView);
            courierListReassignDelivery = (RelativeLayout) itemView.findViewById(R.id.courierListReassignDelivery);
            driverNameTxt = (TextView) itemView.findViewById(R.id.driverNameTxt);
            companyNameTxt = (TextView) itemView.findViewById(R.id.companyNameTxt);
        }

        public void bind(final MyTeamList_Model item, final OnItemClickListener listener, final MyViewHolder holder, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, holder, position);
                }
            });
        }
    }

    public AdapterAssignToOtherCourier(Context adapterViewContext, List<MyTeamList_Model> arrayOfReAssignCouriers, OnItemClickListener listner){
        this.adapterViewContext = adapterViewContext;
        this.arrayOfDrivers = arrayOfReAssignCouriers;
        this.arrayOfReAssignCouriers = arrayOfReAssignCouriers;
        this.listener = listner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.row_reassign_driver, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyTeamList_Model reassignteamData = arrayOfReAssignCouriers.get(position);
        holder.bind(reassignteamData, listener, holder, position);
        holder.driverNameTxt.setText(reassignteamData.getFirstName()+" "+reassignteamData.getLastName());
        if (!reassignteamData.getCompanyName().equals("")) {
            holder.companyNameTxt.setVisibility(View.VISIBLE);
            holder.companyNameTxt.setText(reassignteamData.getCompanyName());
        } else
            holder.companyNameTxt.setVisibility(View.GONE);

        if (reassignteamData.isSetFlagToSelectItem())
            holder.courierListReassignDelivery.setBackgroundColor(adapterViewContext.getResources().getColor(R.color.colorPrimary2));
        else
            holder.courierListReassignDelivery.setBackgroundColor(adapterViewContext.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return arrayOfReAssignCouriers.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    arrayOfReAssignCouriers = arrayOfDrivers;
                } else {
                    List<MyTeamList_Model> filteredList = new ArrayList<>();
                    for (MyTeamList_Model row : arrayOfDrivers) {
                        String courierName = row.getFirstName()+" "+row.getLastName();
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (courierName.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    arrayOfReAssignCouriers = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayOfReAssignCouriers;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayOfReAssignCouriers = (ArrayList<MyTeamList_Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
