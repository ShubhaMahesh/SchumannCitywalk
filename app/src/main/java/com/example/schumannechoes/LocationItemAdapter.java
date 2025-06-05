package com.example.schumannechoes;



import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationItemAdapter extends RecyclerView.Adapter<LocationItemAdapter.LocationItemViewHolder> {
    private List<LocationModel> locations;
    private Context context;
    private boolean isVisitedList;

    public LocationItemAdapter(Context context, List<LocationModel> locations, boolean isVisitedList) {
        this.context = context;
        this.locations = locations;
        this.isVisitedList = isVisitedList;
    }

    @NonNull
    @Override
    public LocationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
        return new LocationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationItemViewHolder holder, int position) {
        LocationModel loc = locations.get(position);
        // Set image resource - you can map each location to a drawable, or use placeholder
        int imageRes = LocationImageMap.getImageForLocation(loc.getName());
        holder.image.setImageResource(imageRes);
        if (isVisitedList) {
            holder.image.setAlpha(1.0f);
            holder.unlockText.setText(loc.getName()); // Show location name
            holder.unlockText.setTextColor(Color.parseColor("#217a05"));
        } else {
            holder.image.setAlpha(0.3f);
            holder.unlockText.setText("✓ unlock");
            holder.unlockText.setTextColor(Color.parseColor("#BBA700"));
        }
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class LocationItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView unlockText;
        public LocationItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            unlockText = itemView.findViewById(R.id.unlock_text);
        }
    }
}