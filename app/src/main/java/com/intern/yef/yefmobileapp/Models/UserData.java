package com.intern.yef.yefmobileapp.Models;


public class UserData {

    private String name;
    private String email;
    private String uid;

    public UserData(){

    }

    public UserData(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }
}

