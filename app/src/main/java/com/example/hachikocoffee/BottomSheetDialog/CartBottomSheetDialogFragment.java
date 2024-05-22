package com.example.hachikocoffee.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Adapter.CartAdapter;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CartBottomSheetDialogFragment extends BottomSheetDialogFragment{

    private static final String TODAY = "Hôm nay";
    private static final String TOMORROW = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM"));
    private static final String DAY_AFTER_TOMORROW = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM"));;

    private final String[] dayValues = {TODAY, TOMORROW, DAY_AFTER_TOMORROW};
    private String[] todayTimeValues;
    private String[] otherDayTimeValues;
    TextView itemCount;
    TextView totalItemCost;
    TextView totalAfterFee;
    TextView recipentName;
    TextView recipentPhone;
    TextView orderDay;
    TextView orderTime;
    RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_cart, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView backButton = view.findViewById(R.id.backBtn);
        ConstraintLayout addBtn = view.findViewById(R.id.addBtn);
        RelativeLayout infoBtn = view.findViewById(R.id.infoBtn);
        RelativeLayout timeBtn = view.findViewById(R.id.timeBtn);
        TextView deleteCartBtn = view.findViewById(R.id.deleteCart);
        totalItemCost = view.findViewById(R.id.itemsCost);
        AppCompatButton confirmBtn = view.findViewById(R.id.confirmBtn);
        recyclerViewCart = view.findViewById(R.id.recyclerView_CartItems);
        itemCount = view.findViewById(R.id.itemCount);
        totalAfterFee = view.findViewById(R.id.totalAfterFee);
        recipentName = view.findViewById(R.id.name);
        recipentPhone = view.findViewById(R.id.phone);
        orderDay = view.findViewById(R.id.day);
        orderTime = view.findViewById(R.id.time);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(requireContext()));

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(50)
                .build();
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(Color.WHITE));
        confirmBtn.setBackground(materialShapeDrawable);

        MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable1.setFillColor(ColorStateList.valueOf(Color.parseColor("#fef7e5")));
        addBtn.setBackground(materialShapeDrawable1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        deleteCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagementCart.getInstance().clearCart();
                dismiss();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });
        
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });


        ArrayList<CartItem> cartItems = ManagementCart.getInstance().getCartItems();
        if (cartItems.size() > 0)
        {
            cartAdapter = new CartAdapter(cartItems, getChildFragmentManager());
            recyclerViewCart.setAdapter(cartAdapter);
        }

        recipentName.setText(ManagementCart.getInstance().getRecipentName());
        recipentPhone.setText(ManagementCart.getInstance().getRecipentPhone());
        itemCount.setText("Giao hàng • " + ManagementCart.getInstance().getItemsCount() + " sản phẩm");

        //format money
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
        totalItemCost.setText(a +"đ");
    }

    private void showTimePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_time, null);
        builder.setView(dialogView);

        final NumberPicker dayPicker = dialogView.findViewById(R.id.dayPicker);
        final NumberPicker timePicker = dialogView.findViewById(R.id.timePicker);
        final TextView time = dialogView.findViewById(R.id.time);
        ImageView backBtn = dialogView.findViewById(R.id.backBtn);
        AppCompatButton btnConfirm = dialogView.findViewById(R.id.btnConfirm);

//        dayPicker.setDividerDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable));
//        timePicker.setDividerDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable));

        dayPicker.setWrapSelectorWheel(false);
        timePicker.setWrapSelectorWheel(false);

        dayPicker.setMinValue(0);
        dayPicker.setMaxValue(dayValues.length - 1);
        dayPicker.setDisplayedValues(dayValues);

        final AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dayInput = dayValues[dayPicker.getValue()];
                orderDay.setText(dayInput);
                if (dayPicker.getValue() == 0){
                    String timeInput = todayTimeValues[timePicker.getValue()];
                    orderTime.setText(timeInput);
                    Log.d("Picker", dayInput + " " +timeInput);
                }
                else{
                    String timeInput = otherDayTimeValues[timePicker.getValue()];
                    orderTime.setText(timeInput);
                    Log.d("Picker", dayInput + " " +timeInput);
                }



                int currentYear = LocalDate.now().getYear();
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = currentDate.format(formatter);

                if (dayPicker.getValue() == 0 && timePicker.getValue() == 0){
                    LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0);
                    int currentHour = currentTime.getHour();
                    int currentMinute = currentTime.getMinute();
                    @SuppressLint("DefaultLocale") String formattedTime = String.format("%02d:%02d", currentHour, currentMinute);

                    ManagementCart.getInstance().setOrderTime(formattedDate + " " + formattedTime);
                }
                else if (dayPicker.getValue() == 0){
                    ManagementCart.getInstance().setOrderTime(formattedDate + " " + todayTimeValues[timePicker.getValue()]);
                }
                else{
                    ManagementCart.getInstance().setOrderTime(dayValues[dayPicker.getValue()] + "/" + currentYear + " " +  otherDayTimeValues[timePicker.getValue()]);
                }

                ManagementCart.getInstance().updateTimeToFireBase(String.valueOf(ManagementUser.getInstance().getUserId()));
                dialog.dismiss();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setupTimePicker(timePicker, newVal);
                if (dayPicker.getValue() == 0){
                    time.setText(dayValues[0] + " - " + todayTimeValues[timePicker.getValue()]);
                }
                else{
                    time.setText(dayValues[dayPicker.getValue()] + " - " + otherDayTimeValues[timePicker.getValue()]);
                }
            }
        });

        timePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                time.setText(dayPicker.getValue() + " - " + timePicker.getValue());
                if (dayPicker.getValue() == 0){
                    time.setText(dayValues[0] + " - " + todayTimeValues[timePicker.getValue()]);
                }
                else{
                    time.setText(dayValues[dayPicker.getValue()] + " - " + otherDayTimeValues[timePicker.getValue()]);
                }
            }
        });

        dayPicker.setValue(0);
        setupTimePicker(timePicker, 0);

        dialog.show();
    }

    private void setupTimePicker(NumberPicker timePicker, int dayIndex) {
        String[] timeValues = dayIndex == 0 ? getTodayTimeValues() : getOtherDayTimeValues();
        timePicker.setMinValue(0);
        timePicker.setMaxValue(timeValues.length - 1);
        timePicker.setDisplayedValues(timeValues);
        timePicker.setValue(0);
    }

    private String[] getTodayTimeValues() {
        if (todayTimeValues == null) {
            todayTimeValues = generateTimeValues(getCurrentTime().get(Calendar.HOUR_OF_DAY), getCurrentTime().get(Calendar.MINUTE), 20, 31);
        }
        todayTimeValues[0] = "Càng sớm càng tốt";
        return todayTimeValues;
    }

    private String[] getOtherDayTimeValues() {
        if (otherDayTimeValues == null) {
            otherDayTimeValues = generateTimeValues(8, 0, 20, 31);
        }
        return otherDayTimeValues;
    }

    private String[] generateTimeValues(int startHour, int startMinute, int endHour, int endMinute) {
        List<String> timeValues = new ArrayList<>();
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR_OF_DAY, startHour);
        currentTime.set(Calendar.MINUTE, startMinute);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, endHour);
        endTime.set(Calendar.MINUTE, endMinute);

        while (currentTime.before(endTime)) {
            timeValues.add(formatTime(currentTime));
            currentTime.add(Calendar.MINUTE, 15);
        }

        return timeValues.toArray(new String[0]);
    }

    private String formatTime(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private Calendar getCurrentTime() {
        Calendar currentTime = Calendar.getInstance();
        int currentMinute = currentTime.get(Calendar.MINUTE);
        currentTime.set(Calendar.MINUTE, (currentMinute / 30 * 30) + 30);
        return currentTime;
    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_info, null);
        builder.setView(dialogView);

        final EditText editName = dialogView.findViewById(R.id.editName);
        final EditText editPhone = dialogView.findViewById(R.id.editPhone);
        AppCompatButton btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        ImageView backBtn = dialogView.findViewById(R.id.backBtn);

        editName.setText(recipentName.getText());
        editPhone.setText(recipentPhone.getText());

        final AlertDialog dialog = builder.create();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = editName.getText().toString();
                String inputPhone = editPhone.getText().toString();

                recipentName.setText(inputName);
                recipentPhone.setText(inputPhone);

                ManagementCart.getInstance().setRecipentName(inputName);
                ManagementCart.getInstance().setRecipentPhone(inputPhone);
                ManagementCart.getInstance().updateNameAndPhoneToFireBase(String.valueOf(ManagementUser.getInstance().getUserId()));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            View parentLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (parentLayout != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parentLayout);
                setupFullHeight(parentLayout);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        ManagementCart.getInstance().addOnCartChangedListener(new OnCartChangedListener() {
            @Override
            public void onCartChanged() {
                updateRecyclerview();
            }
        });


        return dialog;
    }

    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerview(){
        if (ManagementCart.getInstance().getCartItems().isEmpty())
        {
            dismiss();
            return;
        }
        Log.d("Size", "" + ManagementCart.getInstance().getItemsCount());
        cartAdapter.setCartItems(ManagementCart.getInstance().getCartItems());
        cartAdapter.notifyDataSetChanged();

        recyclerViewCart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Log.d("Change", "true");

                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator('.');
                String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
                totalItemCost.setText(a +"đ");


                itemCount.setText("Giao hàng • " + ManagementCart.getInstance().getItemsCount() + " sản phẩm");

                recyclerViewCart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

}