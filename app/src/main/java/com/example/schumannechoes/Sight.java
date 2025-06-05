package com.example.schumannechoes;
public class Sight {
    private String name;
    private String description;
    private int imageResId;
    private int musicResId;
    private double latitude;
    private double longitude;

    public Sight(String name, String description, int imageResId, int musicResId, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.musicResId = musicResId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public int getMusicResId() { return musicResId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}