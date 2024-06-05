package com.example.hachikocoffee.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hachikocoffee.Adapter.OrderAdapter;
import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.OrderDetail;
import com.example.hachikocoffee.databinding.ActivityFinishedOrdersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FinishedOrdersActivity extends AppCompatActivity {
    String startDate;
    String endDate;
    ActivityFinishedOrdersBinding binding;
    ArrayList<OrderDomain> finishedOrderList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        binding = ActivityFinishedOrdersBinding.inflate(getLayoutInflater());
        LayoutInflater inflater = getLayoutInflater();
        setContentView(binding.getRoot());

        binding.startDate.setText(startDate);
        binding.endDate.setText(endDate);

        initFinishedOrdersList();

        binding.btnCalendarStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker1();
            }
        });

        binding.btnCalendarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker2();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addValidation();
    }

    private void initFinishedOrdersList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        binding.recyclerViewFinishedOrders.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ORDER");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        OrderDomain order = issue.getValue(OrderDomain.class);

                        if (order != null && "Finished".equals(order.getOrderStatus())){
                            String createdTime = order.getOrderCreatedTime().substring(0, 10);
                            if (compareDates(createdTime, startDate) >= 0 && compareDates(createdTime, endDate) <= 0){
                                finishedOrderList.add(order);
                            }
                        }
                    }
                    displayProcessingOrderList(finishedOrderList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void addValidation() {
        binding.startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int check = compareDates(s.toString(), binding.endDate.getText().toString());
                if (check > 0){
                    binding.startDate.setError("Ngày bắt đầu không được lớn hơn ngày kết thúc");
                    binding.recyclerViewFinishedOrders.setVisibility(View.INVISIBLE);
                }
                else{
                    binding.recyclerViewFinishedOrders.setVisibility(View.VISIBLE);
                    finishedOrderList.clear();
                    initFinishedOrdersList();
                    binding.startDate.setError(null);
                }
            }
        });

        binding.endDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int check = compareDates(s.toString(), binding.startDate.getText().toString());
                if (check < 0){
                    binding.endDate.setError("Ngày kết thúc không được nhỏ hơn ngày kết thúc");
                    binding.recyclerViewFinishedOrders.setVisibility(View.INVISIBLE);
                }
                else{
                    binding.recyclerViewFinishedOrders.setVisibility(View.VISIBLE);
                    finishedOrderList.clear();
                    initFinishedOrdersList();
                    binding.endDate.setError(null);
                }
            }
        });
    }

    private void showDatePicker2() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FinishedOrdersActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        endDate = formattedDate;
                        binding.endDate.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void showDatePicker1() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FinishedOrdersActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        startDate = formattedDate;
                        binding.startDate.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void displayProcessingOrderList(ArrayList<OrderDomain> processingOrderList) {
        if (!processingOrderList.isEmpty()) {
            OrderAdapter orderAdapter = new OrderAdapter(processingOrderList, this::onClickToOrderDetailFunc);
            binding.recyclerViewFinishedOrders.setAdapter(orderAdapter);
        }
    }

    private void onClickToOrderDetailFunc(OrderDomain order) {
        Intent intent = new Intent(FinishedOrdersActivity.this, OrderDetail.class);
        startActivity(intent);
    }
    private int compareDates(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate parsedDate1 = LocalDate.parse(date1, formatter);
            LocalDate parsedDate2 = LocalDate.parse(date2, formatter);

            return parsedDate1.compareTo(parsedDate2);
        } catch (DateTimeParseException e) {
            Toast.makeText(FinishedOrdersActivity.this,"Định dạng ngày không hợp lệ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
}