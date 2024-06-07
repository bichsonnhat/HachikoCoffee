package com.example.hachikocoffee.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Activity.EditNotificationActivity;
import com.example.hachikocoffee.Domain.NotificationDomain;
import com.example.hachikocoffee.R;

import java.util.ArrayList;
import static com.example.hachikocoffee.Activity.EditNotificationActivity.setEditInterfaceInstance;

public class NotificationManagementAdapter extends RecyclerView.Adapter<NotificationManagementAdapter.NotificationViewHolder> {
    private final ArrayList<NotificationDomain> mListNotificationManagement;

    public NotificationManagementAdapter(ArrayList<NotificationDomain> mListNotificationManagement) {
        this.mListNotificationManagement = mListNotificationManagement;
    }


    @NonNull
    @Override
    public NotificationManagementAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_notification_item, parent, false);
        return new NotificationManagementAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationManagementAdapter.NotificationViewHolder holder, int position) {
        NotificationDomain notification = mListNotificationManagement.get(position);
        if (notification == null) {
            return;
        }

        holder.item_notiTitle.setText(notification.getTitle());
        holder.item_notiID.setText(String.valueOf(notification.getNotificationID()));
        Glide.with(holder.item_notiImg.getContext()).load(notification.getImageURL()).into(holder.item_notiImg);

        holder.item_notiEdit.setOnClickListener(v -> {
            setEditInterfaceInstance(v.getContext());
            Intent intent = new Intent(v.getContext(), EditNotificationActivity.class);
            intent.putExtra("NotificationID", notification.getNotificationID());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (mListNotificationManagement != null) {
            return mListNotificationManagement.size();
        }
        return 0;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView item_notiTitle;
        private final TextView item_notiID;
        private final ImageView item_notiEdit;
        private final ImageView item_notiImg;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            item_notiTitle = itemView.findViewById(R.id.item_notiTitle);
            item_notiID = itemView.findViewById(R.id.item_notiID);
            item_notiEdit = itemView.findViewById(R.id.item_notiEdit);
            item_notiImg = itemView.findViewById(R.id.item_notiImg);
        }
    }
}
