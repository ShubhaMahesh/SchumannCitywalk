package com.example.schumannechoes;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private List<Sight> sights;

    public List<Sight> getSights(Context context) {
        List<Sight> sights = new ArrayList<>();
        sights.add(new Sight(context.getString(R.string.sight1_title), context.getString(R.string.sight1_info), R.drawable.sight1,R.raw.r_schumann_1,50.7174,12.4961));
        sights.add(new Sight(context.getString(R.string.sight2_title), context.getString(R.string.sight2_info), R.drawable.sight2,-1,50.7214,12.4385));
        sights.add(new Sight(context.getString(R.string.sight3_title), context.getString(R.string.sight3_info), R.drawable.sight3,-1,50.7165,12.4945));
        sights.add(new Sight(context.getString(R.string.sight4_title), context.getString(R.string.sight4_info), R.drawable.sight4,-1,50.7176,12.4946));
        sights.add(new Sight(context.getString(R.string.sight5_title), context.getString(R.string.sight5_info), R.drawable.sight5,-1,50.7179,12.4951));
        sights.add(new Sight(context.getString(R.string.sight6_title), context.getString(R.string.sight6_info), R.drawable.sight6,-1,50.7175,12.4964));
        sights.add(new Sight(context.getString(R.string.sight7_title), context.getString(R.string.sight7_info), R.drawable.sight7,-1,50.7177,12.4973));
        sights.add(new Sight(context.getString(R.string.sight8_title), context.getString(R.string.sight8_info), R.drawable.sight8,-1,50.7296,12.4998));
        sights.add(new Sight(context.getString(R.string.sight9_title), context.getString(R.string.sight9_info), R.drawable.sight9,R.raw.r_schumann_1,50.7176,12.4981));
        sights.add(new Sight(context.getString(R.string.sight10_title), context.getString(R.string.sight10_info), R.drawable.sight10,-1,50.7175,12.4964));
        sights.add(new Sight(context.getString(R.string.sight11_title), context.getString(R.string.sight11_info), R.drawable.sight11,R.raw.r_schumann_1,50.7177,12.4973));
        sights.add(new Sight(context.getString(R.string.sight12_title), context.getString(R.string.sight12_info), R.drawable.sight12,R.raw.r_schumann_1,50.7174,12.4961));
        sights.add(new Sight(context.getString(R.string.sight13_title), context.getString(R.string.sight13_info), R.drawable.sight13,-1,50.7209,12.5000));
        sights.add(new Sight(context.getString(R.string.sight14_title), context.getString(R.string.sight14_info), R.drawable.sight14,-1,50.7178,12.5018));
        return sights;
    }

    public void setSights(List<Sight> sights) {
        this.sights = sights;
    }
}