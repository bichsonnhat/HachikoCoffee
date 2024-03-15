package com.example.hachikocoffee.Fragment;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hachikocoffee.Photo;
import com.example.hachikocoffee.PhotoAdapter;
import com.example.hachikocoffee.R;
import com.example.hachikocoffee.ShortcutDomain;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewShortcutList;
    private ArrayList<ShortcutDomain> itemList;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;

    private Handler handler;
    private Runnable runnable;
    private int delay = 3000;

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

        recyclerViewShortcutList = view.findViewById(R.id.recyclerView_Shortcut);
        viewPager = view.findViewById(R.id.viewpager);
        circleIndicator = view.findViewById(R.id.circle_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewShortcutList.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();
        itemList.add(new ShortcutDomain("Giao hàng", "shipping"));
        itemList.add(new ShortcutDomain("Mang đi", "takeaway"));
        itemList.add(new ShortcutDomain("Giao hàng", "shipping"));
        itemList.add(new ShortcutDomain("Giao hàng", "shipping"));
        itemList.add(new ShortcutDomain("Giao hàng", "shipping"));

        adapter = new MyAdapter(itemList);
        recyclerViewShortcutList.setAdapter(adapter);

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

        return view;
    }

    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.image_1));
        list.add(new Photo(R.drawable.image_2));
        list.add(new Photo(R.drawable.image_3));
        list.add(new Photo(R.drawable.image_4));

        return list;
    }



    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private ArrayList<ShortcutDomain> itemList;

        public MyAdapter(ArrayList<ShortcutDomain> itemList) {
            this.itemList = itemList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_shortcut, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.shortcutName.setText(itemList.get(position).getTitle());
            String picUrl ="";
            switch (position){
                case 0: {
                    picUrl = "shipping";
                    break;
                }
                case 1: {
                    picUrl = "takeaway";
                    break;
                }
                case 2: {
                    picUrl = "shipping";
                    break;
                }
                case 3: {
                    picUrl = "shipping";
                    break;
                }
                case 4: {
                    picUrl = "shipping";
                    break;
                }
            }

            int drawableResourceId = holder.itemView.getContext().getResources()
                    .getIdentifier(picUrl, "drawable",
                            holder.itemView.getContext().getPackageName());
            Glide.with(holder.itemView.getContext())
                    .load(drawableResourceId)
                    .into(holder.shortcutPic);



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShortcutDomain item = itemList.get(position);
//                    Toast.makeText(holder.itemView.getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

            // Thiết lập background cho itemView
            holder.itemView.setBackgroundResource(R.drawable.background_shortcut);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    // Tạo ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shortcutName;
        ImageView shortcutPic;
        ConstraintLayout mainLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            shortcutName = itemView.findViewById(R.id.shortcutName);
            shortcutPic = itemView.findViewById(R.id.shortcutPic);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }

    }
}