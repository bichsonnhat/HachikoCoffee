package com.example.hachikocoffee;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoDetail extends AppCompatActivity {
    ImageView closeBtn;

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

    }
}
