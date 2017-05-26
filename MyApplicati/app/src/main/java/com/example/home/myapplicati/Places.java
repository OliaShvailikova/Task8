package com.example.home.myapplicati;


import org.json.JSONException;
import org.json.JSONObject;

public class Places {
    private String nameOfPlace;
    private  double latitudeOfPlace;
    private double longitudeOfPlace;
    private String addressOfPlace;
    private String description;
    private float distance;
    private String image;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    Places(String nameOfPlace, double latitudeOfPlace, double longitudeOfPlace, String addressOfPlace, float distance, String description, String image){
        this.nameOfPlace=nameOfPlace;
        this.latitudeOfPlace=latitudeOfPlace;
        this.longitudeOfPlace=longitudeOfPlace;
        this.addressOfPlace=addressOfPlace;
        this.distance=distance;
        this.description=description;
        this.image=image;
    }

    public float getDistance() {
        return distance;
    }

    public String getAddressOfPlace() {
        return addressOfPlace;
    }

    public double getLongitudeOfPlace() {

        return longitudeOfPlace;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitudeOfPlace() {

        return latitudeOfPlace;
    }


    public String getNameOfPlace() {

        return nameOfPlace;
    }

    public void setNameOfPlace(String nameOfPlace) {
        this.nameOfPlace = nameOfPlace;
    }

    public JSONObject getJsonObject(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("name",getNameOfPlace());
            jsonObject.put("lat",getLatitudeOfPlace());
            jsonObject.put("long",getLongitudeOfPlace());
            jsonObject.put("description",getDescription());
            jsonObject.put("image",getImage());
        } catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}
