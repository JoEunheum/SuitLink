package com.example.suitlink;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
class User {
    String name;
    String birthday;
    String sex;
    String number;
    String email;
    String imageUri;
    String status;
    String chest;
    String pents;
    String shoes;

    public User() {

    }

    public User(String status, String chest, String pents, String shoes) {
        this.status = status;
        this.chest = chest;
        this.pents = pents;
        this.shoes = shoes;
    }

    public User(String name, String birthday, String sex, String number, String imageString, String email) {
        this.name = name;
        this.imageUri = imageString;
        this.birthday = birthday;
        this.sex = sex;
        this.number = number;
        this. email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getPents() {
        return pents;
    }

    public void setPents(String pents) {
        this.pents = pents;
    }

    public String getShoes() {
        return shoes;
    }

    public void setShoes(String shoes) {
        this.shoes = shoes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
