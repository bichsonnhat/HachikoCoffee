package com.example.hachikocoffee.Domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class UserDomain implements Parcelable {
    private int UserID;
    private String PhoneNumber;
    private String Name;
    private String Birthday;
    private String Email;

    private String Gender;

    protected UserDomain(Parcel in) {
        UserID = in.readInt();
        PhoneNumber = in.readString();
        Name = in.readString();
        Birthday = in.readString();
        Email = in.readString();
        Gender = in.readString();
    }

    public static final Creator<UserDomain> CREATOR = new Creator<UserDomain>() {
        @Override
        public UserDomain createFromParcel(Parcel in) {
            return new UserDomain(in);
        }

        @Override
        public UserDomain[] newArray(int size) {
            return new UserDomain[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(UserID);
        dest.writeString(PhoneNumber);
        dest.writeString(Name);
        dest.writeString(Birthday);
        dest.writeString(Email);
        dest.writeString(Gender);
    }
}
