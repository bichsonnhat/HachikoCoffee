package com.example.hachikocoffee.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.hachikocoffee.Domain.NotificationDomain;
import com.example.hachikocoffee.Listener.Callback;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddNotificationActivity extends AppCompatActivity {
    private static Callback callback;
    private ImageView notiMgmt_back;
    private EditText notiMgmt_addTitle, notiMgmt_addImageURL, notiMgmt_addDescription, notiMgmt_addDate;
    private AppCompatButton notiMgmt_addConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

        notiMgmt_back = findViewById(R.id.notiMgmt_back);
        notiMgmt_addTitle = findViewById(R.id.notiMgmt_addTitle);
        notiMgmt_addImageURL = findViewById(R.id.notiMgmt_addImageURL);
        notiMgmt_addDescription = findViewById(R.id.notiMgmt_addDescription);
        notiMgmt_addDate = findViewById(R.id.notiMgmt_addDate);
        notiMgmt_addConfirm = findViewById(R.id.notiMgmt_addConfirm);

        notiMgmt_addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNotificationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                notiMgmt_addDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        addAttributeTextChanged();

        notiMgmt_back.setOnClickListener(v -> finish());

        notiMgmt_addConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
        notiMgmt_addConfirm.setEnabled(false);
        notiMgmt_addConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NOTIFICATION");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int index = 0;
                            for (DataSnapshot issue : snapshot.getChildren()) {
                                NotificationDomain notificationDomain = issue.getValue(NotificationDomain.class);
                                index = Math.max(index, notificationDomain.getNotificationID());
                            }
                            addNotification(index + 1);
                            Toast.makeText(AddNotificationActivity.this, "Thêm thông báo thành công!", Toast.LENGTH_SHORT).show();
                            callback.onCallback();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //
                    }
                });
            }
        });
    }

    private void addNotification(int index) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NOTIFICATION");
        NotificationDomain notificationDomain = new NotificationDomain(
                notiMgmt_addTitle.getText().toString(),
                notiMgmt_addDescription.getText().toString(),
                notiMgmt_addImageURL.getText().toString(),
                index,
                notiMgmt_addDate.getText().toString()
        );
        ref.child(String.valueOf(index)).setValue(notificationDomain);
    }

    private void addAttributeTextChanged() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {checkAllFieldsFilled();}
            @Override
            public void afterTextChanged(Editable s) {}
        };

        notiMgmt_addTitle.addTextChangedListener(textWatcher);
        notiMgmt_addImageURL.addTextChangedListener(textWatcher);
        notiMgmt_addDescription.addTextChangedListener(textWatcher);
        notiMgmt_addDate.addTextChangedListener(textWatcher);
    }

    private void checkAllFieldsFilled() {
        if (!notiMgmt_addTitle.getText().toString().isEmpty() &&
                !notiMgmt_addImageURL.getText().toString().isEmpty() &&
                !notiMgmt_addDescription.getText().toString().isEmpty() &&
                !notiMgmt_addDate.getText().toString().isEmpty()) {
            notiMgmt_addConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            notiMgmt_addConfirm.setEnabled(true);
        } else {
            notiMgmt_addConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            notiMgmt_addConfirm.setEnabled(false);
        }
    }

    public static void setEditInterfaceInstance(NotificationManagementActivity context) {
        callback = (Callback) context;
    }
}