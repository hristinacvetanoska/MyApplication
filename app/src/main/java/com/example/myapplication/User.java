package com.example.myapplication;

public class User {
    public String fullName, email, phoneNumber, role, password;
    //Activities activities;
    public int rating, BrojNaRejting, SumaRejting;
    public User(){

    }

    public User(String fullName, String email, String phoneNumber, String role, String password, int rating,int BrojNaRejting,int  SumaRejting){
        this.fullName=fullName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.role = role;
        this.password = password;
        this.rating = 0;
        this.BrojNaRejting =  0;
        this.SumaRejting = 0;
    }

}