package com.example.schumannechoes;



public class LocationModel {
    private String name;
    private double latitude;
    private double longitude;
    private boolean visited;

    public LocationModel(String name, double latitude, double longitude, boolean visited) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.visited = visited;
    }

    // --- Getters and Setters ---
    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }
}