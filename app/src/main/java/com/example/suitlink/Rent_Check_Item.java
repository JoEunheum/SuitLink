package com.example.suitlink;

import java.util.ArrayList;

public class Rent_Check_Item {

    private String rent_time;
    private String name;
    private int office_image; // 업체 사진은 인트로 받을 수 있음
    private String suit_image; // 다른 사진은 그냥?
    private String division;
    private String color;
    private String size;
    private String price;
    private String status;
    private String post_uid;
    private String post_key;
    private ArrayList<String> suit_uri = new ArrayList<>();

    public Rent_Check_Item(){

    }

    //개인대여 가져올 때
    public Rent_Check_Item(String rent_time, String name, String suit_image, String division, String color, String size, String price, String status,String post_key, String post_uid){
        this.rent_time = rent_time;
        this.name = name;
        this.suit_image = suit_image;
        this.division = division;
        this.color = color;
        this.price = price;
        this.size = size;
        this.status = status;
        this.post_key = post_key;
        this.post_uid= post_uid;
    }

    //업체에서 가져올 때
    public Rent_Check_Item(String rent_time, String name, int office_image, String division, String color, String size, String price, String status,String post_key, String post_uid){
        this.rent_time = rent_time;
        this.name = name;
        this.office_image = office_image;
        this.division = division;
        this.color = color;
        this.size = size;
        this.price = price;
        this.status = status;
        this.post_key= post_key;
        this.post_uid = post_uid;
    }

    //내가 쓴 글을 볼 때
    public Rent_Check_Item(String rental_time, String name, ArrayList<String> suit_uri, String division, String color, String size, String price, String rental_check, String post_key, String uid) {
        this.rent_time = rent_time;
        this.name = name;
        this.suit_uri= suit_uri;
        this.division = division;
        this.color = color;
        this.size = size;
        this.price = price;
        this.status = status;
        this.post_key= post_key;
        this.post_uid = post_uid;
    }

    public String getPost_uid() {
        return post_uid;
    }

    public void setPost_uid(String post_uid) {
        this.post_uid = post_uid;
    }

    public String getPost_key() {
        return post_key;
    }

    public void setPost_key(String post_key) {
        this.post_key = post_key;
    }

    public String getRent_time() {
        return rent_time;
    }

    public void setRent_time(String rent_time) {
        this.rent_time = rent_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffice_image() {
        return office_image;
    }

    public void setOffice_image(int officeimage) {
        this.office_image = officeimage;
    }

    public String getSuit_image() {
        return suit_image;
    }

    public void setSuit_image(String suitimage) {
        this.suit_image = suitimage;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
