package com.example.hachikocoffee.Management;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hachikocoffee.Domain.UserDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagementUser {
    private static ManagementUser instance;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private UserDomain user;
    private ManagementUser(){
        user = new UserDomain();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("USER");
    }
    public static ManagementUser getInstance(){
        if (instance == null) {
            instance = new ManagementUser();
        }
        return instance;
    }

    public void loadFromFirebase(int userId) {
        usersRef.child(String.valueOf(userId))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(UserDomain.class);
                    Log.d("ManagementUser", "user " + user.getName());
                } else {
                    Log.d("MangermentUsser", String.valueOf(userId));
                    Log.d("ManagementUser", "not found user");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    public UserDomain getUser(){
        return user;
    }
    public int getUserId(){
        return user.getUserID();
    }
    public void setUserID(int ID){ user.setUserID(ID);}
}
