package com.example.schumannechoes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schumannechoes.fragments.SightInfoFragment;

import java.util.List;

public class SightsAdapter extends RecyclerView.Adapter<SightsAdapter.SightViewHolder> {

    private List<Sight> sights;

    public SightsAdapter(List<Sight> sights) {
        this.sights = sights;
    }

    @NonNull
    @Override
    public SightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sight, parent, false);
        return new SightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SightViewHolder holder, int position) {
        Sight sight = sights.get(position);
        holder.title.setText(sight.getName());
        String truncatedDescription = sight.getDescription().length() > 40
                ? sight.getDescription().substring(0, 40) + "..."
                : sight.getDescription();
        holder.description.setText(truncatedDescription);
        holder.image.setImageResource(sight.getImageResId());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            // Create a new instance of SightInfoFragment
            Fragment sightInfoFragment = new SightInfoFragment();
            Bundle args = new Bundle();
            args.putString("title", sight.getName());
            args.putString("description", sight.getDescription());
            args.putInt("imageResId", sight.getImageResId());
            args.putInt("musicResId", sight.getMusicResId());
            args.putDouble("latitude", sight.getLatitude());
            args.putDouble("longitude", sight.getLongitude());
            sightInfoFragment.setArguments(args);

            // Navigate to SightInfoFragment
            FragmentActivity activity = (FragmentActivity) holder.itemView.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, sightInfoFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    public void updateSights(List<Sight> newSights) {
        sights = newSights;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sights.size();
    }

    static class SightViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;

        SightViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sightTitle);
            description = itemView.findViewById(R.id.sightDescription);
            image = itemView.findViewById(R.id.sightImage);
        }
    }
}