package com.example.hachikocoffee.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.util.HashMap;
import java.util.Map;

public class EditNotificationActivity extends AppCompatActivity {
    private static Callback callback;
    private ImageView notiMgmt_back;
    private EditText notiMgmt_editTitle, notiMgmt_editImageURL, notiMgmt_editDescription, notiMgmt_editDate;
    private ConstraintLayout notiMgmt_editDelete;
    private AppCompatButton notiMgmt_editConfirm;
    private int NotificationID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notification);

        notiMgmt_back = findViewById(R.id.notiMgmt_back);
        notiMgmt_editTitle = findViewById(R.id.notiMgmt_editTitle);
        notiMgmt_editImageURL = findViewById(R.id.notiMgmt_editImageURL);
        notiMgmt_editDescription = findViewById(R.id.notiMgmt_editDescription);
        notiMgmt_editDate = findViewById(R.id.notiMgmt_editDate);
        notiMgmt_editDelete = findViewById(R.id.notiMgmt_editDelete);
        notiMgmt_editConfirm = findViewById(R.id.notiMgmt_editConfirm);

        notiMgmt_editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditNotificationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                notiMgmt_editDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        addAttributeTextChanged();

        Intent intent = getIntent();
        NotificationID = intent.getIntExtra("NotificationID", 0);
        initDefaultNotification();

        notiMgmt_back.setOnClickListener(v -> finish());

        notiMgmt_editConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
        notiMgmt_editConfirm.setEnabled(false);
        notiMgmt_editConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification(NotificationID);
                Toast.makeText(EditNotificationActivity.this, "Cập nhật thông báo thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        notiMgmt_editDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNotification(NotificationID);
            }
        });
    }

    private void initDefaultNotification() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NOTIFICATION");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        NotificationDomain notification = issue.getValue(NotificationDomain.class);
                        if (notification.getNotificationID() == NotificationID) {
                            notiMgmt_editTitle.setText(notification.getTitle());
                            notiMgmt_editImageURL.setText(notification.getImageURL());
                            notiMgmt_editDescription.setText(notification.getDescription());
                            notiMgmt_editDate.setText(notification.getDate());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    private void updateNotification(int index) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NOTIFICATION");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        NotificationDomain notification = issue.getValue(NotificationDomain.class);
                        if (notification.getNotificationID() == index) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("title", notiMgmt_editTitle.getText().toString());
                            updates.put("imageURL", notiMgmt_editImageURL.getText().toString());
                            updates.put("description", notiMgmt_editDescription.getText().toString());
                            updates.put("date", notiMgmt_editDate.getText().toString());
                            issue.getRef().updateChildren(updates);
                            callback.onCallback();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    private void deleteNotification(int index) {
        AlertDialog alertDialog = new AlertDialog.Builder(EditNotificationActivity.this, R.style.AlertDialog_AppCompat_Custom)
                .setTitle("Xóa thông báo")
                .setMessage("Xác nhận xóa thông báo này?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("NOTIFICATION");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot issue : snapshot.getChildren()) {
                                        NotificationDomain notification = issue.getValue(NotificationDomain.class);
                                        if (notification.getNotificationID() == index) {
                                            issue.getRef().removeValue();
                                            Toast.makeText(EditNotificationActivity.this, "Xóa thông báo thành công!", Toast.LENGTH_SHORT).show();
                                            callback.onCallback();
                                            finish();
                                            break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                //
                            }
                        });
                    }
                })
                .setNegativeButton("Không", null)
                .show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        positiveButton.setTextColor(Color.parseColor("#000000"));
        negativeButton.setTextColor(Color.parseColor("#E47905"));

    }

    private void addAttributeTextChanged() {
        notiMgmt_editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        notiMgmt_editImageURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        notiMgmt_editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        notiMgmt_editDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });
    }

    private void checkAllFieldsFilled() {
        if (!notiMgmt_editTitle.getText().toString().isEmpty() &&
                !notiMgmt_editImageURL.getText().toString().isEmpty() &&
                !notiMgmt_editDescription.getText().toString().isEmpty() &&
                !notiMgmt_editDate.getText().toString().isEmpty()) {
            notiMgmt_editConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            notiMgmt_editConfirm.setEnabled(true);
        } else {
            notiMgmt_editConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            notiMgmt_editConfirm.setEnabled(false);
        }
    }

    public static void setEditInterfaceInstance(Context context) {
        callback = (Callback) context;
    }
}