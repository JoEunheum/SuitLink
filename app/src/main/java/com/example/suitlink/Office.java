package com.example.suitlink;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Office {

    public String size;
    public String color;
    public String price;
    public String rental_time;
    public String rental_how;
    public String division="";
    public int suitUri;

    public Office(){

    }

    //정장정보
    public Office(String size , String color, String price,String rental_time,String rental_how,ArrayList<String> division, int suitUri){
        this.size = size;
        this.color = color;
        this.price =price;
        this.rental_time = rental_time;
        this.rental_how = rental_how;
        for(int i = 0;i<division.size();i++){
            this.division+=division.get(i)+"_";
        }
        this.suitUri = suitUri;
    }

    //무료 대여
    public Office(String size , String color,String rental_time,String rental_how,ArrayList<String> division, int suitUri){
        this.size = size;
        this.color = color;
        this.rental_time = rental_time;
        this.rental_how = rental_how;
        for(int i = 0;i<division.size();i++){
            this.division+=division.get(i)+"_";
        }
        this.suitUri = suitUri;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("size",size);
        result.put("color",color);
        result.put("price",price);
        result.put("rental_time",rental_time);
        result.put("rental_how",rental_how);
        result.put("division",division);
        result.put("profileUri",suitUri);
        return result;
    }
}
