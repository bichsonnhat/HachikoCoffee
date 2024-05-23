package com.example.hachikocoffee;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hachikocoffee.Domain.AdvertisementDomain;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PhotoDetail extends AppCompatActivity {
    ImageView closeBtn;
    private TextView AdvertisementDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail);

        ImageView imageView = findViewById(R.id.photodetail_image);
        int position = getIntent().getIntExtra("position", 0);

        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.image_1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.image_2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.image_3);
                break;
            case 3:
                imageView.setImageResource(R.drawable.image_4);
                break;
        }

        closeBtn = findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        AdvertisementDescription = findViewById(R.id.AdvertisementDescription);
        DatabaseReference advertisementRef = FirebaseDatabase.getInstance().getReference().child("ADVERTISEMENT");
        advertisementRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AdvertisementDomain advertisement = dataSnapshot.getValue(AdvertisementDomain.class);
                    if (advertisement.getAdID() == position + 1){
                        AdvertisementDescription.setText(""+advertisement.getDescription());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
