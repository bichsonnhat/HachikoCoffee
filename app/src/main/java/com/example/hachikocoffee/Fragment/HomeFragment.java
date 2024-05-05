package com.example.hachikocoffee.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.example.hachikocoffee.Adapter.NewListAdapter;
import com.example.hachikocoffee.Domain.CategoryDomain;
import com.example.hachikocoffee.Domain.ItemsDomain;
import com.example.hachikocoffee.EdgeItemDecoration;
import com.example.hachikocoffee.Photo;
import com.example.hachikocoffee.Adapter.PhotoAdapter;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.Domain.ShortcutDomain;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import com.example.hachikocoffee.Adapter.ShortcutAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter1;
    private RecyclerView recyclerViewShortcutList;
    private RecyclerView recyclerViewNewList;

    private ArrayList<ShortcutDomain> itemList;
    private ArrayList<ItemsDomain> itemList1;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;

    private Handler handler;
    private Runnable runnable;
    private int delay = 5000;
    private int MonMoiPhaiThuID = 0;

    DatabaseReference categories;
    DatabaseReference items;
    ValueEventListener categoryListener;
    ValueEventListener itemsListener;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        initShorcut(view);
        initViewPager(view);
        initNewList(view);
        return view;
    }

    public  void initNewList(View view){
        recyclerViewNewList = view.findViewById(R.id.recyclerView_Newlist);
        itemList1 = new ArrayList<>();
        //recyclerViewNewList.setHasFixedSize(true);
        categories = FirebaseDatabase.getInstance().getReference("CATEGORY");
        items = FirebaseDatabase.getInstance().getReference("PRODUCTS");
        setupCategoryListener();
    }

    private void setupCategoryListener() {
        categoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                        CategoryDomain category = categorySnapshot.getValue(CategoryDomain.class);
                        if (category != null && category.getTitle().equals("Món Mới Phải Thử")) {
                            setupItemsListener(category.getCategoryID());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read category values.", error.toException());
            }
        };
        categories.addListenerForSingleValueEvent(categoryListener);
    }

    private void setupItemsListener(int categoryId) {
        itemsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList1.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ItemsDomain item = itemSnapshot.getValue(ItemsDomain.class);
                    if (item != null && item.getCategoryID() == categoryId) {
                        itemList1.add(item);
                    }
                }
                updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read item values.", error.toException());
            }
        };
        items.addValueEventListener(itemsListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateAdapter() {
        if (adapter1 == null) {
            adapter1 = new NewListAdapter(itemList1);
            recyclerViewNewList.setAdapter(adapter1);

            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerViewNewList.setLayoutManager(layoutManager);
            recyclerViewNewList.addItemDecoration(new EdgeItemDecoration(55));

        } else {
            adapter1.notifyDataSetChanged();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanupListeners();
    }

    private void cleanupListeners() {
        if (categoryListener != null) {
            categories.removeEventListener(categoryListener);
        }
        if (itemsListener != null) {
            items.removeEventListener(itemsListener);
        }
    }

    public void initShorcut(View view){
        recyclerViewShortcutList = view.findViewById(R.id.recyclerView_Shortcut);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewShortcutList.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();
        itemList.add(new ShortcutDomain("Giao hàng", "shipping"));
        itemList.add(new ShortcutDomain("Mang đi", "takeaway"));
        itemList.add(new ShortcutDomain("Giao hàng", "shipping"));
        itemList.add(new ShortcutDomain("Giao hàng", "shipping"));
        itemList.add(new ShortcutDomain("Giao hàng", "shipping"));

        adapter = new ShortcutAdapter(itemList);
        recyclerViewShortcutList.setAdapter(adapter);
    }

    public void initViewPager(View view){
        viewPager = view.findViewById(R.id.viewpager);

        viewPager.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 16);
            }
        });
        viewPager.setClipToOutline(true);

        circleIndicator = view.findViewById(R.id.circle_indicator);

        photoAdapter = new PhotoAdapter(requireContext(), getListPhoto());
        viewPager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                int position = viewPager.getCurrentItem();
                position = (position + 1) % photoAdapter.getCount();
                viewPager.setCurrentItem(position);
                handler.postDelayed(this, delay);
            }
        };
        handler.postDelayed(runnable, delay);
    }

    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.image_1));
        list.add(new Photo(R.drawable.image_2));
        list.add(new Photo(R.drawable.image_3));
        list.add(new Photo(R.drawable.image_4));

        return list;
    }
}