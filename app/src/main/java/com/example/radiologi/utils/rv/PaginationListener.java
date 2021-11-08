package com.example.radiologi.utils.rv;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationListener extends RecyclerView.OnScrollListener {
    public static final int PAGE_START = 1;

    /**
     * Set scrolling threshold here (for now i'm assuming 15 item in one page)
     */
    private static final int PAGE_SIZE = 10;

    @NonNull
    private LinearLayoutManager layoutManager;

    /**
     * Supporting only LinearLayoutManager for now.
     */
    protected PaginationListener(@NonNull LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()){
            Log.d("SCROLL_STATE", "IS LAST");
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE){
                Log.d("SCROLL_STATE", "LOAD_MORE");
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();
    public abstract boolean isLastPage();
    public abstract boolean isLoading();
}
