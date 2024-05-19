package com.example.hachikocoffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Domain.NotificationDomain;
import com.example.hachikocoffee.R;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifiactionViewHolder> {

    private final List<NotificationDomain> mListNoti;

    public NotificationAdapter(List<NotificationDomain> mListNoti) {
        this.mListNoti = mListNoti;
    }

    public NotifiactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_notification, parent, false);
        return new NotifiactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiactionViewHolder holder, int position) {
        NotificationDomain noti = mListNoti.get(position);
        if (noti == null) {
            return;
        }
        Glide.with(holder.imgNoti.getContext()).load(noti.getImageURL()).into(holder.imgNoti);
        holder.tvNotiName.setText(noti.getTitle());
        holder.tvNotiText.setText(noti.getDescription());
        holder.tvNotiDate.setText(noti.getDate());
    }

    @Override
    public int getItemCount() {
        if (mListNoti != null) {
            return mListNoti.size();
        }
        return 0;
    }

    public static class NotifiactionViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgNoti;
        private final TextView tvNotiName;
        private final TextView tvNotiText;
        private final TextView tvNotiDate;


        public NotifiactionViewHolder(@NonNull View itemView) {
            super(itemView);

            imgNoti = itemView.findViewById(R.id.noti_img);
            tvNotiName = itemView.findViewById(R.id.noti_name);
            tvNotiText = itemView.findViewById(R.id.noti_txt);
            tvNotiDate = itemView.findViewById(R.id.noti_date);

        }
    }
}
