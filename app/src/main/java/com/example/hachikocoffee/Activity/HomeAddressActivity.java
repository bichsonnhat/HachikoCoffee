package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.Listener.OnAddressChangedListener;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class HomeAddressActivity extends AppCompatActivity {
    private int UserID;
    TextView tvHomeAddressName;
    EditText etHomeAddress, etHomeAddressBuilding, etHomeAddressGate, etHomeAddressNote, etHomeAddressReceiverName, etHomeAddressReceiverPhone;
    Button btnHomeAddressSave;
    private static OnAddressChangedListener onAddressChangedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserID = ManagementUser.getInstance().getUserId();
        setContentView(R.layout.activity_home_address);
        tvHomeAddressName = findViewById(R.id.homeAddressName);
        etHomeAddress = findViewById(R.id.homeAddress);
        etHomeAddressBuilding = findViewById(R.id.homeAdressBuilding);
        etHomeAddressGate = findViewById(R.id.homeAddressGate);
        etHomeAddressNote = findViewById(R.id.homeAddressNote);
        etHomeAddressReceiverName = findViewById(R.id.homeAddressReceiverName);
        etHomeAddressReceiverPhone = findViewById(R.id.homeAddressReceiverPhone);
        btnHomeAddressSave = findViewById(R.id.btnHomeAddressSave);
        btnHomeAddressSave.setEnabled(false);

        Button btnHomeAddressBack = findViewById(R.id.btnHomeAddressBack);
        btnHomeAddressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("USER");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserDomain user = issue.getValue(UserDomain.class);
                        if (user.getUserID() == UserID){
                            String fullname = user.getName();
                            String[] name = fullname.split(",");
                            etHomeAddressReceiverName.setText(name[0]);
                            etHomeAddressReceiverPhone.setText(user.getPhoneNumber());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        etHomeAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etHomeAddress.getText().toString().isEmpty()){
                    etHomeAddress.setError("Vui lòng nhập địa chỉ!");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etHomeAddressReceiverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etHomeAddressReceiverName.getText().toString().isEmpty()){
                    etHomeAddressReceiverName.setError("Vui lòng nhập tên người nhận!");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etHomeAddressReceiverPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etHomeAddressReceiverPhone.getText().toString().isEmpty()){
                    etHomeAddressReceiverPhone.setError("Vui lòng nhập số điện thoại người nhận!");
                }
                checkAllFieldsRequire();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnHomeAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHomeAddress();
                onAddressChangedListener.onAddressChanged();
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeAddressActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn đã thêm địa chỉ nhà thành công!");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String homeAddress = etHomeAddress.getText().toString() + ", " + etHomeAddressBuilding.getText().toString() + ", " + etHomeAddressGate.getText().toString() + ", " + etHomeAddressNote.getText().toString();
//                        String userNameAndTelephone = etHomeAddressReceiverName.getText().toString() + " " + etHomeAddressReceiverPhone.getText().toString();
//
//                        // Trả kết quả về activity trước
//                        Intent resultIntent = new Intent(HomeAddressActivity.this, SavedAddressActivity.class);
//                        resultIntent.putExtra("show_home_address", true);
//                        resultIntent.putExtra("Address", homeAddress);
//                        resultIntent.putExtra("NameAndTelephone", userNameAndTelephone);
//                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });

                AlertDialog alertDialog = builder.show();
            }
        });
    }

    private void saveHomeAddress() {
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
        String addressID = UUID.randomUUID().toString();
        AddressDomain address = new AddressDomain(addressID, etHomeAddress.getText().toString(), etHomeAddressBuilding.getText().toString(), etHomeAddressGate.getText().toString(), etHomeAddressNote.getText().toString(), etHomeAddressReceiverName.getText().toString(), etHomeAddressReceiverPhone.getText().toString(), "Nhà", UserID);
        addressRef.child(addressID).setValue(address);
        // (String addressID, String description, String detail, String gate, String note, String recipentName, String recipentPhone, String title, int userID)
        addressRef.child(addressID).setValue(address);
    }

    private void checkAllFieldsRequire() {
        if (!etHomeAddress.getText().toString().isEmpty() && !etHomeAddressReceiverName.getText().toString().isEmpty() && !etHomeAddressReceiverPhone.getText().toString().isEmpty()) {
            btnHomeAddressSave.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            btnHomeAddressSave.setEnabled(true);
        } else {
            btnHomeAddressSave.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            btnHomeAddressSave.setEnabled(false);
        }
    }

    public static void setHomeInterfaceInstance(SavedAddressActivity context){
        onAddressChangedListener = context;
    }
}