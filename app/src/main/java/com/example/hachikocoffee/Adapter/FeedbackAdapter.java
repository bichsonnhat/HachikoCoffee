package com.example.hachikocoffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachikocoffee.Domain.FeedbackDomain;
import com.example.hachikocoffee.Domain.UserDomain;
import com.example.hachikocoffee.R;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private final ArrayList<FeedbackDomain> feedbackDomainArrayList;

    public FeedbackAdapter(ArrayList<FeedbackDomain> feedbackDomainArrayList) {
        this.feedbackDomainArrayList = feedbackDomainArrayList;
    }

    @NonNull
    @Override
    public FeedbackAdapter.FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_feedback, parent, false);
        return new FeedbackAdapter.FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.FeedbackViewHolder holder, int position) {
        FeedbackDomain feedback = feedbackDomainArrayList.get(position);
        if (feedback == null) {
            return;
        }
        int userID = feedback.getUserID();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("USER");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        UserDomain user = issue.getValue(UserDomain.class);
                        if (user.getUserID() == userID) {
                            holder.vh_feedback_nameAccount.setText(""+user.getName());
                            holder.vh_feedback_emailAccount.setText(""+user.getEmail());
                            holder.vh_feedback_feedbackText.setText(""+feedback.getDescription());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (feedbackDomainArrayList != null) {
            return feedbackDomainArrayList.size();
        }
        return 0;
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {
        private final TextView vh_feedback_nameAccount;
        private final TextView vh_feedback_emailAccount;
        private final TextView vh_feedback_feedbackText;
        public FeedbackViewHolder(@NonNull View parent) {
            super(parent);
            vh_feedback_nameAccount = parent.findViewById(R.id.vh_feedback_nameAccount);
            vh_feedback_emailAccount = parent.findViewById(R.id.vh_feedback_emailAccount);
            vh_feedback_feedbackText = parent.findViewById(R.id.vh_feedback_feedbackText);
        }
    }
}
