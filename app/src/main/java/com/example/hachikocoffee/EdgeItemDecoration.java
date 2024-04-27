package com.example.hachikocoffee;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EdgeItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    public EdgeItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % 2; // item column

        if (column == 0) {
            // Item ở cột đầu tiên (bên trái)
            outRect.right = spacing / 2; // Khoảng cách bên phải của item
        } else {
            // Item ở cột thứ hai (bên phải)
            outRect.left = spacing / 2; // Khoảng cách bên trái của item
        }

        // Thêm khoảng cách ở đầu và cuối mỗi item (nếu cần)
//        outRect.top = spacing;
//        outRect.bottom = spacing;
    }
}
