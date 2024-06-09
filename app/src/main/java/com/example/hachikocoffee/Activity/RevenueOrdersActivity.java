package com.example.hachikocoffee.Activity;

import android.app.DatePickerDialog;
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

import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class RevenueOrdersActivity extends AppCompatActivity {
    private EditText startDate;
    private EditText endDate;
    private Button btn_calendarStart;
    private Button btn_calendarEnd;
    private PieChart pieChart;
    private ImageView back_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_orders);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        btn_calendarStart = findViewById(R.id.btn_calendarStart);
        btn_calendarEnd = findViewById(R.id.btn_calendarEnd);
        pieChart = findViewById(R.id.pieChart);
        back_button = findViewById(R.id.back_button);
        String start = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String end = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        startDate.setText(""+start);
        endDate.setText(""+end);
        checkValidDate();

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_calendarStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePicker();
            }
        });

        btn_calendarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePicker();
            }
        });
        startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValidDate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        endDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValidDate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showStartDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String x, y;
                        if (dayOfMonth < 10){
                            x = "0" + dayOfMonth;
                        } else {
                            x = String.valueOf(dayOfMonth);
                        }
                        if (monthOfYear + 1 < 10){
                            y = "0" + (monthOfYear + 1);
                        } else {
                            y = String.valueOf(monthOfYear + 1);
                        }
                        startDate.setText(x + "/" + y + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }


    private void showEndDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String x, y;
                        if (dayOfMonth < 10){
                            x = "0" + dayOfMonth;
                        } else {
                            x = String.valueOf(dayOfMonth);
                        }
                        if (monthOfYear + 1 < 10){
                            y = "0" + (monthOfYear + 1);
                        } else {
                            y = String.valueOf(monthOfYear + 1);
                        }
                        endDate.setText(x + "/" + y + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void checkValidDate() {
        String start = startDate.getText().toString();
        String end = endDate.getText().toString();
        if (!start.isEmpty() && !end.isEmpty()) {
            String[] startArr = start.split("/");
            String[] endArr = end.split("/");
            if (Integer.parseInt(startArr[2]) > Integer.parseInt(endArr[2])) {
                Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(startArr[2]) == Integer.parseInt(endArr[2])) {
                if (Integer.parseInt(startArr[1]) > Integer.parseInt(endArr[1])) {
                    Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(startArr[1]) == Integer.parseInt(endArr[1])) {
                    if (Integer.parseInt(startArr[0]) > Integer.parseInt(endArr[0])) {
                        Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            // Handle here
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("ORDER");
            orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Double total_pending = 0.0;
                        Double total_finished = 0.0;
                        Double total_cancelled = 0.0;
                        for (DataSnapshot issue : snapshot.getChildren()){
                            OrderDomain order = issue.getValue(OrderDomain.class);
                            String orderTime = order.getOrderTime().substring(0, 10);
                            String[] orderDate = orderTime.split("/");
                            if (Integer.parseInt(orderDate[2]) < Integer.parseInt(startArr[2]) || Integer.parseInt(orderDate[2]) > Integer.parseInt(endArr[2])){
                                continue;
                            }
                            if (Integer.parseInt(orderDate[2]) == Integer.parseInt(startArr[2]) && Integer.parseInt(orderDate[1]) < Integer.parseInt(startArr[1])){
                                continue;
                            }
                            if (Integer.parseInt(orderDate[2]) == Integer.parseInt(startArr[2]) && Integer.parseInt(orderDate[1]) == Integer.parseInt(startArr[1]) && Integer.parseInt(orderDate[0]) < Integer.parseInt(startArr[0])){
                                continue;
                            }
                            if (Integer.parseInt(orderDate[2]) == Integer.parseInt(endArr[2]) && Integer.parseInt(orderDate[1]) > Integer.parseInt(endArr[1])){
                                continue;
                            }
                            if (Integer.parseInt(orderDate[2]) == Integer.parseInt(endArr[2]) && Integer.parseInt(orderDate[1]) == Integer.parseInt(endArr[1]) && Integer.parseInt(orderDate[0]) > Integer.parseInt(endArr[0])){
                                continue;
                            }
                            if (order.getOrderStatus().equals("Pending")){
                                total_pending += order.getCost();
                            } else if (order.getOrderStatus().equals("Finished")){
                                total_finished += order.getCost();
                            } else {
                                total_cancelled += order.getCost();
                            }
                        }
                        if (total_pending == 0.0 && total_finished == 0.0 && total_cancelled == 0.0){
                            Toast.makeText(RevenueOrdersActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ArrayList<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(total_pending.floatValue(), "Pending"));
                        entries.add(new PieEntry(total_finished.floatValue(), "Finished"));
                        entries.add(new PieEntry(total_cancelled.floatValue(), "Cancelled"));


                        PieDataSet pieDataSet = new PieDataSet(entries, "");
                        pieDataSet.setColors(new int[]{Color.parseColor("#f1c560"), Color.parseColor("#9dc467"), Color.parseColor("#d1555d")});

                        pieDataSet.setValueTextSize(15f);
                        pieDataSet.setValueTextColor(R.color.black);

                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);

                        pieChart.getDescription().setEnabled(false);
                        pieChart.animateY(1000);
                        pieChart.invalidate();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
