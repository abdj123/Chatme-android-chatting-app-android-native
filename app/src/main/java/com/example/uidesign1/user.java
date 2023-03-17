package com.example.uidesign1;

public class user {
    private String email;

    private String username;

    private String profilepictuer;

    private String status;

    private String bio;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    user(){

    }
    user(String email,String username,String profilepictuer,String status){
        this.email=email;
        this.profilepictuer=profilepictuer;
        this.username=username;
        this.status=status;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilepictuer() {
        return profilepictuer;
    }

    public void setProfilepictuer(String profilepictuer) {
        this.profilepictuer = profilepictuer;
    }




}