package com.example.newme;

public class User {
        String email;
        String pin;
        String firstName;
        String lastName;
        String hash;
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

}
