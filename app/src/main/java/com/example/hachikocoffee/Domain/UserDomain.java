package com.example.hachikocoffee.Domain;

import java.io.Serializable;

public class UserDomain implements Serializable {
    private int UserID;
    private String PhoneNumber;
    private String Name;
    private String Birthday;
    private String Email;

    private String Gender;

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public UserDomain() {}
    public UserDomain(int userID, String phoneNumber, String name, String birthday, String email, String gender) {
        UserID = userID;
        PhoneNumber = phoneNumber;
        Name = name;
        Birthday = birthday;
        Email = email;
        Gender = gender;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
