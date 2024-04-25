package com.example.hachikocoffee.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.hachikocoffee.Adapter.CategoryAdapter;
import com.example.hachikocoffee.Adapter.CategoryDialogAdapter;
import com.example.hachikocoffee.CategoryDialog;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.MaxTwoRowsLayoutManager;
import com.example.hachikocoffee.OnDismissListener;
import com.example.hachikocoffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    private RecyclerView recyclerViewCategory;
    private SeekBar seekbarHorizontalScroll;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        recyclerViewCategory = view.findViewById(R.id.recyclerView_Category);
        seekbarHorizontalScroll = view.findViewById(R.id.seekbar);

        initCategory();
        initSeekbar();
        topBarOnClick(view);


        return view;

    }

    private void initSeekbar() {
        seekbarHorizontalScroll.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int maxScrollPosition = recyclerViewCategory.computeHorizontalScrollRange() - recyclerViewCategory.getWidth();
                    int newScrollPosition = (int) ((float) progress / seekBar.getMax() * maxScrollPosition);
                    recyclerViewCategory.scrollToPosition(newScrollPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        RecyclerViewScrollListener scrollListener = new RecyclerViewScrollListener();
        recyclerViewCategory.addOnScrollListener(scrollListener);
    }

    private void topBarOnClick(View view){
        LinearLayout topBar = view.findViewById(R.id.topBar);
        CategoryDialog categoryDialog = new CategoryDialog();
        ImageView arrowBtn = view.findViewById(R.id.arrowBtn);


        Animation rotateDownAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_down);
        Animation rotateUpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_up);

        topBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowBtn.startAnimation(rotateUpAnimation);
                arrowBtn.setImageResource(R.drawable.arrow_up);
                categoryDialog.show(getChildFragmentManager(), "category_dialog");
                
                categoryDialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDissmiss() {
                        arrowBtn.startAnimation(rotateDownAnimation);
                        arrowBtn.setImageResource(R.drawable.arrow_down);
                    }
                });
            }
        });
    }

    private void initCategory(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("CATEGORY");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<CategoryDomain> items = new ArrayList<>();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CategoryDomain category = issue.getValue(CategoryDomain.class);
                        items.add(category);
                    }
                    displayCategoryData(items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
                // Notify user about the error
            }
        });
    }

    private void displayCategoryData(ArrayList<CategoryDomain> items) {
        if (!items.isEmpty()) {
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

            // Thiết lập layout theo chiều ngang
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

            // Thiết lập cho RecyclerView
            recyclerViewCategory.setLayoutManager(layoutManager);

            // Đặt Adapter cho RecyclerView
            recyclerViewCategory.setAdapter(new CategoryAdapter(items));
        }
    }

    private class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int totalItems = recyclerView.getLayoutManager().getItemCount();
            int currentPosition = recyclerView.computeHorizontalScrollOffset();
            int maxScrollPosition = recyclerView.computeHorizontalScrollRange() - recyclerView.getWidth();
            int progress = (int) ((float) currentPosition / maxScrollPosition * seekbarHorizontalScroll.getMax());
            seekbarHorizontalScroll.setProgress(progress);
        }
    }
}