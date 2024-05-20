package com.example.hachikocoffee.BottomSheetDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.MainActivity;
import com.example.hachikocoffee.Domain.NotificationDomain;
import com.example.hachikocoffee.Domain.ShopDomain;
import com.example.hachikocoffee.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class NotificationBottomSheet extends BottomSheetDialogFragment {
    private static final String KEY_NOTIFICATION_DETAIL = "NotificationDetail";
    private NotificationDomain notification;

    private ImageView notificationImage;
    private TextView notificationTitle;
    private TextView notificationDescription;
    private Button acceptButton;
   

    public static NotificationBottomSheet newInstance(NotificationDomain notification) {
        NotificationBottomSheet notificationBottomSheet = new NotificationBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NOTIFICATION_DETAIL, notification);
        notificationBottomSheet.setArguments(bundle);
        return notificationBottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            notification = (NotificationDomain) bundle.get(KEY_NOTIFICATION_DETAIL);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.notification_detail_popup, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);
        return bottomSheetDialog;
    }

    private void initView(View view) {
        notificationImage = view.findViewById(R.id.notificationImage);
        notificationDescription = view.findViewById(R.id.notificationDescription);
        notificationTitle = view.findViewById(R.id.notificationTitle);
        acceptButton = view.findViewById(R.id.acceptButton);

        Glide.with(notificationImage.getContext()).load(notification.getImageURL()).into(notificationImage);
        notificationDescription.setText(notification.getDescription());
        notificationTitle.setText(notification.getTitle());

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).navigateToOrderFragment();
            }
        });

    }
}
