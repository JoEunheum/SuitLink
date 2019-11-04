package com.example.suitlink;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Personal_Item {
    private String profile_image;
    private String profile_id;
    private int cardview_edit;
    private Uri suit_image;
    private String jacket;
    private String chest;
    private String pents;
    private String shoes;
    private String etc;
    private String color;
    private String size;
    private String price;
    private String number;
    private String address;
    private String rental_Time;
    private String return_Time;
    private String rental_How;
    private String post_key;
    private String post_uid;
    private String post_name;

    public String getPost_name(){
        return post_name;
    }

    public void setPost_name(String post_name){
        this.post_name=post_name;
    }

    public String getPost_uid(){
        return post_uid;
    }

    public void setPost_uid(String post_uid){
        this.post_uid = post_uid;
    }

    public String getPost_key(){
        return post_key;
    }

    public void setPost_key(String post_key){
        this.post_key = post_key;
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

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getJacket() {
        return jacket;
    }

    public void setJacket(String jacket) {
        this.jacket = jacket;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRental_Time() {
        return rental_Time;
    }

    public void setRental_Time(String rental_Time) {
        this.rental_Time = rental_Time;
    }

    public String getReturn_Time() {
        return return_Time;
    }

    public void setReturn_Time(String return_Time) {
        this.return_Time = return_Time;
    }

    public String getRental_How() {
        return rental_How;
    }

    public void setRental_How(String rental_How) {
        this.rental_How = rental_How;
    }



    public Personal_Item(){

    }

    //프로필 이미지 부분
    // 나중에 프로필이 저장되는 기능을 구현하면 uri로 가져와야할듯, 지금은 저장 되어있다고 가정함. //비트맵으로 가져오니까 인트로 처리해줘야하나?
    public void set_profile_image(String profile_image){
        this.profile_image = profile_image;
    }

    public String get_profile_image(){
        return profile_image;
    }

    // id 부분
    // 아이디도 로그인 한 아이디와 연동해서 집어넣어야할듯.(DB필요)
    public void set_profile_id(String id){
        this.profile_id = id;
    }

    public String get_profile_id(){
        return profile_id;
    }

    //편집쪽은 어떻게 해야하지? 여기서 처리해야하나? 아니면 어뎁터? 잘모르겠다.
    public int get_cardview_edit(){
        return cardview_edit;
    }

    // 정장 사진 부분
    // 사진 칸을 클릭해서 정보를 저장 할 수 있도록 해줘야함.. 일단 모르니 예제보면서 이해하면 고치자.
    public void set_suit_image(Uri suit_image){
        this.suit_image = suit_image;
    }

    public Uri get_suit_image(){
        return suit_image;
    }

    // 객체 생성하면 넣어준다. RecyclerView에 출력될 내용으로 봐야될듯
    public Personal_Item(String profile_image, String profile_id, int cardview_edit, Uri suit_image,String post_key, String post_uid,String post_name){
        this.profile_image = profile_image;
        this.profile_id = profile_id;
        this.cardview_edit = cardview_edit;
        this.suit_image = suit_image;
        this.post_key = post_key;
        this.post_uid = post_uid;
        this.post_name = post_name;
    }

    public Personal_Item(String profile_image, String profile_id, int cardview_edit, Uri suit_image, String jacket, String chest, String pents, String shoes, String etc, String color, String size, String price, String number, String address, String rental_Time, String return_Time, String rental_How, String post_uid, String post_key){
        this.profile_image = profile_image;
        this.profile_id = profile_id;
        this.cardview_edit = cardview_edit;
        this.suit_image = suit_image;
        this.jacket = jacket;
        this.chest = chest;
        this.pents = pents;
        this.shoes = shoes;
        this.etc = etc;
        this.color = color;
        this.size = size;
        this.price = price;
        this.number = number;
        this.address = address;
        this.rental_Time = rental_Time;
        this.return_Time = return_Time;
        this.rental_How = rental_How;
        this.post_uid = post_uid;
        this.post_key = post_key;
    }
}
