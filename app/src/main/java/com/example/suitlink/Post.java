package com.example.suitlink;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Post {

    public String size;
    public String color;
    public String price;
    public String address;
    public String number;
    public String rental_time;
    public String return_time;
    public String rental_how;
    public String rental_check;
    public String uid;
    public ArrayList <String> suitimage = new ArrayList<>();
    public String division="";
    public String name;
    public String profileUri;
    public int office_suit;

    public Post(){

    }

    //정장정보
    public Post(String size , String color, String price, String address,String number,String rental_time,String return_time,String rental_how, String uid, ArrayList<String> division, String name, String profileUri, ArrayList<String> suitimage,String rental_check){
        this.size = size;
        this.color = color;
        this.price =price;
        this.address = address;
        this.number = number;
        this.rental_time = rental_time;
        this.return_time = return_time;
        this.rental_how = rental_how;
        this.rental_check = rental_check;
        this.uid = uid;
        for(int i = 0;i<division.size();i++){
            this.division+=division.get(i)+" ";
        }
        this.suitimage = suitimage;
        this.name = name;
        this.profileUri = profileUri;
    }

    //정장정보
    public Post(String size , String color, String price, String address,String number,String rental_time, String return_time, String rental_how, String uid, ArrayList<String> division, String name, ArrayList<String> suitimage,String rental_check){
        this.size = size;
        this.color = color;
        this.price =price;
        this.address = address;
        this.number = number;
        this.rental_time = rental_time;
        this.rental_how = rental_how;
        this.rental_check = rental_check;
        this.uid = uid;
        this.return_time = return_time;
        for(int i = 0;i<division.size();i++){
            this.division+=division.get(i)+" ";
        }
        this.suitimage = suitimage;
        this.name = name;
    }

    //대여정장정보
    public Post(String size , String color, String price, String address,String number,String rental_time,String rental_how, String uid, String division, String name, ArrayList<String> suitimage,String rental_check){
        this.size = size;
        this.color = color;
        this.price =price;
        this.address = address;
        this.number = number;
        this.rental_time = rental_time;
        this.rental_how = rental_how;
        this.rental_check = rental_check;
        this.uid = uid;
        this.division=division;
        this.suitimage = suitimage;
        this.name = name;
    }

    public Post(String size, String color, String price, String address, String number, String rental_time, String rental_how, String office_name, ArrayList<String> division, String office_name1, int office_suit, String rent) {
        this.size = size;
        this.color = color;
        this.price=price;
        this.address = address;
        this.number = number;
        this.rental_time = rental_time;
        this.rental_how = rental_how;
        this.uid = office_name;
        for(int i = 0;i<division.size();i++){
            this.division+=division.get(i)+" ";
        }
        this.name = office_name1;
        this.office_suit = office_suit;
        this.rental_check = rent;
    }


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("size",size);
        result.put("color",color);
        result.put("price",price);
        result.put("address",address);
        result.put("number",number);
        result.put("rental_time",rental_time);
        result.put("return_time",return_time);
        result.put("rental_how",rental_how);
        result.put("rental_check",rental_check);
        result.put("uid",uid);
        result.put("suitimage",suitimage);
        result.put("division",division);
        result.put("name",name);
        result.put("profileUri",profileUri);
        return result;
    }
}
