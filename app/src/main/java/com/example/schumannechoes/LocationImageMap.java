package com.example.schumannechoes;


public class LocationImageMap {
    // Replace with your actual image resource mapping
    public static int getImageForLocation(String name) {
        switch (name) {
            case "Robert Schumann House": return R.drawable.sight1;
            case "Hotel 'Zur grünen Tanne'": return R.drawable.sight2;
            case "Grünhain Chapel and Lyceum": return R.drawable.sight3;
            case "Priest Houses": return R.drawable.sight4;
            case "St. Mary's Cathedral": return R.drawable.sight5;
            case "Inn 'Zum Großen Christoph'": return R.drawable.sight6;
            case "Town Hall": return R.drawable.sight7;
            case "Lion Pharmacy and Herb Vault": return R.drawable.sight8;
            case "Gewandhaus": return R.drawable.sight9;
            case "Inn 'Zum Goldnen Anker'": return R.drawable.sight10;
            case "Robert Schumann Monument": return R.drawable.sight11;
            case "Schumann's Youth Home": return R.drawable.sight12;
            case "Osterstein Castle": return R.drawable.sight13;
            case "Paradies Bridge": return R.drawable.sight14;
            default: return R.drawable.sight14;
        }

    }
}