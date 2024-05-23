package com.example.hachikocoffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Listener.OnAddressChangedListener;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CompanyAddressActivity extends AppCompatActivity {

    private int UserID;
    private EditText etCompanyAddress;
    private EditText etCompanyAddressBuilding;
    private EditText etCompanyGate;
    private EditText etCompanyNote;
    private EditText etCompanyAddressReceiverName;
    private EditText etCompanyAddressReceiverPhone;
    private TextView tvCompanyAddressName;
    private Button btnCompanyAddressSave;
    private static OnAddressChangedListener onAddressChangedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_address);
        UserID = ManagementUser.getInstance().getUserId();
        etCompanyAddress = findViewById(R.id.etCompanyAddress);
        etCompanyAddressBuilding = findViewById(R.id.etCompanyAddressBuilding);
        etCompanyGate = findViewById(R.id.etCompanyGate);
        etCompanyNote = findViewById(R.id.etCompanyNote);
        etCompanyAddressReceiverName = findViewById(R.id.etCompanyAddressReceiverName);
        etCompanyAddressReceiverPhone = findViewById(R.id.etCompanyAddressReceiverPhone);
        btnCompanyAddressSave = findViewById(R.id.btnCompanyAddressSave);
        tvCompanyAddressName = findViewById(R.id.tvCompanyAddressName);
        Button btnetCompanyAddressBack = findViewById(R.id.btnCompanyAddressBack);
        btnetCompanyAddressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        etCompanyAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etCompanyAddress.length() == 0){
                    etCompanyAddress.setError("Vui lòng nhập địa chỉ!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCompanyAddressReceiverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etCompanyAddressReceiverName.length() == 0){
                    etCompanyAddressReceiverName.setError("Vui lòng nhập tên người nhận!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCompanyAddressReceiverPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etCompanyAddressReceiverPhone.length() == 0){
                    etCompanyAddressReceiverPhone.setError("Vui lòng nhập số điện thoại người nhận!");
                }
                checkAllFieldsFilled();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCompanyAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("ADDRESS");
                UUID uuid = UUID.randomUUID();
                String addressID = uuid.toString();
                AddressDomain addressDomain = new AddressDomain(addressID, etCompanyAddress.getText().toString(), etCompanyAddress.getText().toString(), etCompanyGate.getText().toString(), etCompanyNote.getText().toString(), etCompanyAddressReceiverName.getText().toString(), etCompanyAddressReceiverPhone.getText().toString(), "Công ty", UserID);
//                 (String addressID, String description, String detail, String gate, String note, String recipentName, String recipentPhone, String title, int userID)
                addressRef.push().setValue(addressDomain);

                if (onAddressChangedListener != null){
                    onAddressChangedListener.onAddressChanged();
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyAddressActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn đã thêm địa chỉ công ty thành công!");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String companyAddress = etCompanyAddress.getText().toString() + ", " + etCompanyAddressBuilding.getText().toString() + ", " + etCompanyGate.getText().toString() + ", " + etCompanyNote.getText().toString();
//                        String userNameAndTelephone = etCompanyAddressReceiverName.getText().toString() + " " + etCompanyAddressReceiverPhone.getText().toString();
//
//                        // Trả kết quả về activity trước
//                        Intent resultIntent = new Intent(CompanyAddressActivity.this, SavedAddressActivity.class);
//                        resultIntent.putExtra("show_Company_address", true);
//                        resultIntent.putExtra("Address", companyAddress);
//                        resultIntent.putExtra("NameAndTelephone", userNameAndTelephone);
//                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });

                AlertDialog alertDialog = builder.show();
            }
        });
    }

    private void checkAllFieldsFilled() {
        if (!TextUtils.isEmpty(etCompanyAddress.getText().toString()) && !TextUtils.isEmpty(etCompanyAddressReceiverName.getText().toString()) && !TextUtils.isEmpty(etCompanyAddressReceiverPhone.getText().toString())){
            btnCompanyAddressSave.setBackground(ContextCompat.getDrawable(this, R.drawable.background_color));
            btnCompanyAddressSave.setEnabled(true);
        } else {
            btnCompanyAddressSave.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_darkgrey));
            btnCompanyAddressSave.setEnabled(false);
        }
    }

    public static void setCompanyInterfaceInstance(SavedAddressActivity context){
        onAddressChangedListener = context;
    }
}