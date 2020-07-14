package com.example.bengkel.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItem extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItem(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left=space;
        outRect.right=space;
        outRect.bottom=space;
            outRect.top=space;

    }
}
