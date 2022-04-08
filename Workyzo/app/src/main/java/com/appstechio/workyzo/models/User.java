package com.appstechio.workyzo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private String UserId;
    private String First_Name;
    private String Last_Name;
    private String Username;
    private Boolean Visible_as_freelancer;
    private String Email_Address;
    private Map<String,String> Address;
    private String Mobile_Number;
    private Map<String,Object> Salary;
    private String Professional_Headline;
    private String User_Summary;
    private String Token;
    private String Country_Flag;
    private String Profile_Image;
    private String Key;
    private ArrayList<String> Top_Skills;
    private ArrayList<HashMap> Education;
    private ArrayList<HashMap> Experience;
    private ArrayList<String> Languages;
    private ArrayList<HashMap> Reviews;
    //private ArrayList<HashMap> Complaints;
    private String Completed_Jobs;
    private String Posted_Jobs;
    private String Applied_Jobs;
    private String Hired_Jobs;

    public User(){

    }

    public User(String userId, String username, String first_name, String last_name, String token, String mobile_Number, String profile_Image,String key) {
        UserId = userId;
        Username = username;
        First_Name = first_name;
        Last_Name = last_name;
        Token = token;
        Mobile_Number = mobile_Number;
        Profile_Image = profile_Image;
        Key = key;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

   // public ArrayList<HashMap> getComplaints() {
    //    return Complaints;
   // }

   // public void setComplaints(ArrayList<HashMap> complaints) {
   //     Complaints = complaints;
   // }

    public String getCompleted_Jobs() {
        return Completed_Jobs;
    }

    public void setCompleted_Jobs(String completed_Jobs) {
        Completed_Jobs = completed_Jobs;
    }

    public String getPosted_Jobs() {
        return Posted_Jobs;
    }

    public void setPosted_Jobs(String posted_Jobs) {
        Posted_Jobs = posted_Jobs;
    }

    public String getApplied_Jobs() {
        return Applied_Jobs;
    }

    public void setApplied_Jobs(String applied_Jobs) {
        Applied_Jobs = applied_Jobs;
    }

    public String getHired_Jobs() {
        return Hired_Jobs;
    }

    public void setHired_Jobs(String hired_Jobs) {
        Hired_Jobs = hired_Jobs;
    }

    public ArrayList<HashMap> getReviews() {
        return Reviews;
    }

    public void setReviews(ArrayList<HashMap> reviews) {
        Reviews = reviews;
    }

    public ArrayList<HashMap> getEducation() {
        return Education;
    }

    public void setEducation(ArrayList<HashMap> education) {
        Education = education;
    }

    public ArrayList<HashMap> getExperience() {
        return Experience;
    }

    public void setExperience(ArrayList<HashMap> experience) {
        Experience = experience;
    }

    public ArrayList<String> getLanguages() {
        return Languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        Languages = languages;
    }

    public ArrayList<String> getTop_Skills() {
        return Top_Skills;
    }

    public void setTop_Skills(ArrayList<String> top_Skills) {
        Top_Skills = top_Skills;
    }

    public String getCountry_Flag() {
        return Country_Flag;
    }

    public void setCountry_Flag(String country_Flag) {
        Country_Flag = country_Flag;
    }

    public String getEmail_Address() {
        return Email_Address;
    }

    public void setEmail_Address(String email_Address) {
        Email_Address = email_Address;
    }

    public Map<String, String> getAddress() {
        return Address;
    }

    public void setAddress(Map<String,String> address) {
        Address = address;
    }

    public Map<String, Object> getSalary() {
        return Salary;
    }

    public void setSalary(Map<String, Object> salary) {
        Salary = salary;
    }

    public String getProfessional_Headline() {
        return Professional_Headline;
    }

    public void setProfessional_Headline(String professional_Headline) {
        Professional_Headline = professional_Headline;
    }

    public String getUser_Summary() {
        return User_Summary;
    }

    public void setUser_Summary(String user_Summary) {
        User_Summary = user_Summary;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getFirst_name() {
        return First_Name;
    }

    public void setFirst_name(String first_name) {
        First_Name = first_name;
    }

    public String getLast_name() {
        return Last_Name;
    }

    public void setLast_name(String last_name) {
        Last_Name = last_name;
    }

    public Boolean getVisible_as_freelancer() {
        return Visible_as_freelancer;
    }

    public void setVisible_as_freelancer(Boolean visible_as_freelancer) {
        Visible_as_freelancer = visible_as_freelancer;
    }

    public String getToken() {
        return Token;
    }

    public String getMobile_Number() {
        return Mobile_Number;
    }

    public void setMobile_Number(String mobile_Number) {
        Mobile_Number = mobile_Number;
    }

    public String getProfile_Image() {
        return Profile_Image;
    }

    public void setProfile_Image(String profile_Image) {
        Profile_Image = profile_Image;
    }

    public void setToken(String token) {
        Token = token;
    }
}
