package com.example.hachikocoffee;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MaxTwoRowsLayoutManager extends LinearLayoutManager {
    private int maxRowCount = 2;
    public MaxTwoRowsLayoutManager(Context context) {
        super(context);
    }

    public MaxTwoRowsLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MaxTwoRowsLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMaxRowCount(int count) {
        this.maxRowCount = count;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        int itemCount = getItemCount();
        int maxRows = 2; // Số hàng tối đa
        int maxColumns = Math.max(1, itemCount / maxRows); // Số cột tối đa, tối thiểu là 1
        int width = getWidth();
        int height = getHeight();
        int itemWidth = width / maxColumns;
        int itemHeight = height / maxRows;
        int rowIndex = 0;
        int columnIndex = 0;
        for (int i = 0; i < itemCount; i++) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) getChildAt(i).getLayoutParams();
            params.width = itemWidth;
            params.height = itemHeight;
            params.leftMargin = columnIndex * itemWidth;
            params.topMargin = rowIndex * itemHeight;
            getChildAt(i).setLayoutParams(params);
            if (columnIndex == maxColumns - 1) {
                rowIndex++;
                columnIndex = 0;
            } else {
                columnIndex++;
            }
        }
    }
}
