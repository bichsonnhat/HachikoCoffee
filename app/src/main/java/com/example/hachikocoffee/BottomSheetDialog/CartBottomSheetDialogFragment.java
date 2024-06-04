package com.example.hachikocoffee.BottomSheetDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Activity.YourAddressPick;
import com.example.hachikocoffee.Activity.YourVoucherPick;
import com.example.hachikocoffee.Adapter.CartAdapter;
import com.example.hachikocoffee.Domain.AddressDomain;
import com.example.hachikocoffee.Domain.CartItem;
import com.example.hachikocoffee.Domain.DiscountDomain;
import com.example.hachikocoffee.Domain.OrderDomain;
import com.example.hachikocoffee.Domain.OrderItemDomain;
import com.example.hachikocoffee.Listener.OnAddressPickListener;
import com.example.hachikocoffee.Management.ManagementCart;
import com.example.hachikocoffee.Listener.OnCartChangedListener;
import com.example.hachikocoffee.Management.ManagementMinDistance;
import com.example.hachikocoffee.Management.ManagementUser;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.example.hachikocoffee.Adapter.AddressAdapter1.setInterfaceInstance;


public class CartBottomSheetDialogFragment extends BottomSheetDialogFragment implements CartAdapter.OnFragmentDismissListener, OnAddressPickListener{

    private static final int REQUEST_CODE_VOUCHER_PICK = 1;
    private static final String TODAY = "Hôm nay";
    private static final String TOMORROW = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM"));
    private static final String DAY_AFTER_TOMORROW = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM"));;

    private final String[] dayValues = {TODAY, TOMORROW, DAY_AFTER_TOMORROW};
    private String[] todayTimeValues;
    private String[] otherDayTimeValues;
    private double totalafterFee = 0;
    private double discountmoney = 0;
    private double shippingFee = 0;
    TextView itemCount;
    TextView totalItemCost;
    TextView totalAfterFee;
    TextView recipentName;
    TextView recipentPhone;
    TextView orderDay;
    TextView orderTime;
    TextView chonKhuyenMai;
    TextView khuyenMai;
    TextView ndKhuyenMai;
    ImageView ar_khuyenmai;
    TextView discountMoney;
    TextView feeCost;
    TextView totalCostAfterFee;
    TextView location, sublocation;
    ImageView deleteVoucherBtn;
    RecyclerView recyclerViewCart;
    AppCompatButton confirmOrderBtn;
    private CartAdapter cartAdapter;
    private RelativeLayout btnPickAddress;
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
        RelativeLayout voucherBtn = view.findViewById(R.id.voucherPick);
        TextView deleteCartBtn = view.findViewById(R.id.deleteCart);
        totalItemCost = view.findViewById(R.id.itemsCost);
        recyclerViewCart = view.findViewById(R.id.recyclerView_CartItems);
        itemCount = view.findViewById(R.id.itemCount);
        totalAfterFee = view.findViewById(R.id.totalAfterFee);
        recipentName = view.findViewById(R.id.name);
        recipentPhone = view.findViewById(R.id.phone);
        orderDay = view.findViewById(R.id.day);
        orderTime = view.findViewById(R.id.time);
        chonKhuyenMai = view.findViewById(R.id.chonKhuyenMai);
        khuyenMai = view.findViewById(R.id.khuyenMai);
        ndKhuyenMai = view.findViewById(R.id.ndKhuyenMai);
        ar_khuyenmai = view.findViewById(R.id.ar_khuyenmai);
        discountMoney = view.findViewById(R.id.discountMoney);
        feeCost = view.findViewById(R.id.feeCost);
        totalCostAfterFee = view.findViewById(R.id.totalCostAfterFee);
        deleteVoucherBtn = view.findViewById(R.id.deleteVoucherBtn);
        btnPickAddress = view.findViewById(R.id.locationBtn);
        location = view.findViewById(R.id.location);
        sublocation = view.findViewById(R.id.sublocation);
        confirmOrderBtn = view.findViewById(R.id.confirmOrderBtn);

        updateVoucher();

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(requireContext()));

        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(50)
                .build();
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(Color.WHITE));
        confirmOrderBtn.setBackground(materialShapeDrawable);

        MaterialShapeDrawable materialShapeDrawable1 = new MaterialShapeDrawable(shapeAppearanceModel);
        materialShapeDrawable1.setFillColor(ColorStateList.valueOf(Color.parseColor("#fef7e5")));
        addBtn.setBackground(materialShapeDrawable1);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int voucherID = -1;
                DiscountDomain curVoucher = ManagementCart.getInstance().getVoucher();
                if (curVoucher != null){
                    voucherID = ManagementCart.getInstance().getVoucher().getVoucherID();
                    if (curVoucher.getFreeShipping() != 1 && shippingFee == 0){
                        Toast.makeText(requireContext(), "Vui lòng định vị trước khi đặt hàng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        if (location.getText().equals("Vui lòng định vị")){
                            Toast.makeText(requireContext(), "Vui lòng định vị trước khi đặt hàng", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                else{
                    if (shippingFee == 0){
                        Toast.makeText(requireContext(), "Vui lòng định vị trước khi đặt hàng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                UUID uuid = UUID.randomUUID();
                String orderID = uuid.toString();
                int userID = ManagementUser.getInstance().getUserId();
                String orderAdress = (String) location.getText();
                String orderCreatedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                String orderTime;
                if (!ManagementCart.getInstance().getOrderTime().equals(""))
                {
                    orderTime = ManagementCart.getInstance().getOrderTime();
                }
                else{
                    orderTime = orderCreatedTime;
                }
                String orderMethod = "Tiền mặt";

                double cost = totalafterFee;
                double discount = discountmoney;
                double feeship = shippingFee;
                String recipentname = (String) recipentName.getText();
                String recipentphone = (String) recipentPhone.getText();
                int storeId = ManagementCart.getInstance().getStoreId();
                String orderStatus = "Pending";

                OrderDomain order = new OrderDomain(orderID, userID, 1, orderAdress, orderTime, orderMethod,
                        cost, voucherID, recipentname, recipentphone, storeId, orderStatus, orderCreatedTime, discount, feeship);
                DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("ORDER");
                orderRef.child(orderID).setValue(order);

                DatabaseReference orderItemRef = FirebaseDatabase.getInstance().getReference("ORDERITEM");
                ArrayList<CartItem> cartItems = ManagementCart.getInstance().getCartItems();
                for (int i = 0; i < cartItems.size(); i++) {
                    CartItem item = cartItems.get(i);
                    UUID id = UUID.randomUUID();
                    String orderItemID = id.toString();
                    String note = item.getNote();
                    String productId = item.getProductId();
                    int quantity = item.getQuantity();
                    String size = item.getSize();
                    String topping = "";
                    if (item.getToppings() != null){
                        topping = item.getToppings().toString();
                    }
                    double totalOrderItemPice = item.getTotalCost();

                    OrderItemDomain orderItem = new OrderItemDomain(orderItemID, orderID, productId, quantity, size, topping, note, totalOrderItemPice);
                    orderItemRef.child(orderItemID).setValue(orderItem);
                }
                DatabaseReference userVoucherRef = FirebaseDatabase.getInstance().getReference("USERVOUCHER");
                int finalVoucherID = voucherID;
                userVoucherRef.orderByChild("UserID").equalTo(ManagementUser.getInstance().getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot userVoucherSnapshot : dataSnapshot.getChildren()) {
                                String voucherIDD = String.valueOf(userVoucherSnapshot.child("VoucherID").getValue(Long.class));
                                String curVoucherID = String.valueOf(finalVoucherID);
                                if (voucherIDD.equals(curVoucherID)){
                                    DatabaseReference isUseRef = userVoucherSnapshot.getRef().child("isUse");
                                    isUseRef.setValue(1);
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xử lý lỗi nếu có
                        System.err.println("Error retrieving user vouchers: " + databaseError.getMessage());
                    }
                });

                ManagementCart.getInstance().clearCart();
                Toast.makeText(requireContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
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
        voucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourVoucherPick.class);
                startActivityForResult(intent, REQUEST_CODE_VOUCHER_PICK);
            }
        });

        deleteVoucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagementCart.getInstance().removeVoucherFromFireBase(String.valueOf(ManagementUser.getInstance().getUserId()));
            }
        });
        btnPickAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInterfaceInstance(CartBottomSheetDialogFragment.this);
                Intent intent = new Intent(getActivity(), YourAddressPick.class);
                startActivity(intent);
            }
        });


        ArrayList<CartItem> cartItems = ManagementCart.getInstance().getCartItems();
        if (cartItems.size() > 0)
        {
            cartAdapter = new CartAdapter(cartItems, getChildFragmentManager());
            cartAdapter.setOnFragmentDismissListener(this);
            recyclerViewCart.setAdapter(cartAdapter);
        }
        if (ManagementCart.getInstance().getLocation().equals("") ){
            location.setText("Vui lòng định vị");
            sublocation.setVisibility(View.GONE);
        }
        else if (ManagementCart.getInstance().getSubLocation().equals("")){
            location.setText(ManagementCart.getInstance().getLocation());
            sublocation.setVisibility(View.GONE);
        }
        else{
            location.setText(ManagementCart.getInstance().getLocation());
            sublocation.setText(ManagementCart.getInstance().getSubLocation());
            sublocation.setVisibility(View.VISIBLE);
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
                updateVoucher();
            }
        });



        return dialog;
    }

    @SuppressLint("SetTextI18n")
    private void updateVoucher() {
        double discount = 0;
        double fee = 0;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        if (ManagementMinDistance.getInstance().getMinDistance() != Double.MAX_VALUE){
            fee = ManagementMinDistance.getInstance().getMinDistance() * 1000;
        }

        DiscountDomain curVoucher = ManagementCart.getInstance().getVoucher();
        if (curVoucher != null){
            chonKhuyenMai.setVisibility(View.GONE);
            khuyenMai.setVisibility(View.VISIBLE);
            ndKhuyenMai.setVisibility(View.VISIBLE);
            discountMoney.setVisibility(View.VISIBLE);
            ar_khuyenmai.setVisibility(View.GONE);
            deleteVoucherBtn.setVisibility(View.VISIBLE);

            if (curVoucher.getValueDouble() != 0){
                discount = curVoucher.getValueDouble() * ManagementCart.getInstance().getTotalCost();
            }
            if (curVoucher.getValueInteger() != 0){
                discount = curVoucher.getValueInteger();
            }
            if (curVoucher.getFreeShipping() == 1){
                fee = 0;
            }

            double total = ManagementCart.getInstance().getTotalCost() - discount + fee;
            totalafterFee = total;
            shippingFee = fee;
            discountmoney = discount;

            String discount_money = new DecimalFormat("#,###", symbols).format(discount);
            String ship_cost = new DecimalFormat("#,###", symbols).format(fee);
            String total_afterfee = new DecimalFormat("#,###", symbols).format(total);
            discountMoney.setText("- " + discount_money + "đ");
            feeCost.setText(ship_cost + "đ");
            totalAfterFee.setText(total_afterfee + "đ");
            totalCostAfterFee.setText(total_afterfee + "đ");

            ndKhuyenMai.setText(ManagementCart.getInstance().getVoucher().getTitle());
        }
        else{
            khuyenMai.setVisibility(View.GONE);
            ndKhuyenMai.setVisibility(View.GONE);
            chonKhuyenMai.setVisibility(View.VISIBLE);
            ar_khuyenmai.setVisibility(View.VISIBLE);
            discountMoney.setVisibility(View.GONE);
            deleteVoucherBtn.setVisibility(View.GONE);


            double total = ManagementCart.getInstance().getTotalCost() - discount + fee;
            totalafterFee = total;
            shippingFee = fee;
            discountmoney = discount;

            String ship_cost = new DecimalFormat("#,###", symbols).format(fee);
            String total_afterfee = new DecimalFormat("#,###", symbols).format(total);

            feeCost.setText(ship_cost + "đ");
            totalAfterFee.setText(total_afterfee + "đ");
            totalCostAfterFee.setText(total_afterfee + "đ");
        }
    }

    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void updateRecyclerview(){
//        if (ManagementCart.getInstance().getCartItems().isEmpty())
//        {
////            ManagementCart.getInstance().setVoucher(null);
//            dismiss();
//
//            return;
//        }
        Log.d("Size", "" + ManagementCart.getInstance().getItemsCount());
//


        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        String a = new DecimalFormat("#,###", symbols).format(ManagementCart.getInstance().getTotalCost());
        totalItemCost.setText(a +"đ");


        itemCount.setText("Giao hàng • " + ManagementCart.getInstance().getItemsCount() + " sản phẩm");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_VOUCHER_PICK && resultCode == Activity.RESULT_OK && data != null) {
            DiscountDomain selectedDiscount = (DiscountDomain) data.getSerializableExtra("selectedDiscount");
            ManagementCart.getInstance().setVoucher(selectedDiscount);
            Log.d("Voucher", "truee");
            ManagementCart.getInstance().updateVoucherToFireBase(String.valueOf(ManagementUser.getInstance().getUserId()));

            Log.d("SelectedDisCount", "" + selectedDiscount.getTitle());
            chonKhuyenMai.setVisibility(View.GONE);
            khuyenMai.setVisibility(View.VISIBLE);
            ndKhuyenMai.setVisibility(View.VISIBLE);

            ndKhuyenMai.setText(selectedDiscount.getTitle());
        }
    }

    @Override
    public void onDismissFragment() {
        dismissAllowingStateLoss();
    }

    @SuppressLint("SetTextI18n")
    public void onAddressPick(AddressDomain address) {
        location.setText(""+address.getTitle());
        sublocation.setText(""+address.getDescription());
        sublocation.setVisibility(View.VISIBLE);
        ManagementCart.getInstance().setLocation(address.getTitle());
        ManagementCart.getInstance().setSubLocation(address.getDescription());
    }
}