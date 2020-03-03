package com.shivamgaba.managerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.ViewHolder> {
    Context context;
    ArrayList<driver> drivers;
    ArrayList<String> onlineStatusList;

    public DriverAdapter(Context context, ArrayList<driver> drivers, ArrayList<String> onlineStatusList) {
        this.context = context;
        this.drivers = drivers;
        this.onlineStatusList = onlineStatusList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDriverName, tvTruckNumber, tvEmailId, tvPhoneNumber;
        ImageView ivDriverPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvDriverName=itemView.findViewById(R.id.tvDriverName);
            tvTruckNumber=itemView.findViewById(R.id.tvTruckNumber);
            tvEmailId=itemView.findViewById(R.id.tvEmailId);
            tvPhoneNumber=itemView.findViewById(R.id.tvPhoneNumber);
            ivDriverPic=itemView.findViewById(R.id.ivDriverPic);
        }
    }


    @NonNull
    @Override
    public DriverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drivers_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(drivers.get(position));


        holder.tvDriverName.setText("Name : "+drivers.get(position).getDriverName()+"    ");
        if(onlineStatusList.get(position).equals("yes"))
        {
            holder.tvDriverName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.online_circle,0);
        }
        else
        {
            holder.tvDriverName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ofline_circle,0);
        }
        holder.tvPhoneNumber.setText("Phone No : "+drivers.get(position).getDriverPhoneNumber());
        holder.tvEmailId.setText("Email Id : "+drivers.get(position).getEmailId());
        holder.tvTruckNumber.setText("Truck Number : "+drivers.get(position).getTruckNumber());
        Glide.with(getApplicationContext()).load(drivers.get(position).getDriverPicUrl()).into(holder.ivDriverPic);
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }
}
