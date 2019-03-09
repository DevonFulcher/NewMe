package com.example.newme;

import java.util.HashSet;
import java.util.Set;

public class User {
        String email;
        String pin;
        String firstName;
        String lastName;
        String hash;
        public static Set<User> userSet = new HashSet<User>();
        public User(String firstName,String lastName, String email, String pin,String hash){
            this.firstName = firstName;
            this.email = email;
            this.pin = pin;
            this.lastName = lastName;
            this.hash = hash;
        }
    public String getFirstName(){
        return this.firstName;
    }
    public String getPin(){ return this.pin; }
    public String getLastName(){ return this.lastName; }
    public String getHash(){ return this.hash; }





}
