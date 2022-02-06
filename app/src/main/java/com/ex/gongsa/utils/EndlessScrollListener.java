package com.ex.gongsa.utils;

import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int curPage = 0;
    private int nextPage = 0;
    private int lastPage = 0;
    public EndlessScrollListener(){

    }

    public EndlessScrollListener(int curPage, int nextPage,int lastPage){
        this.curPage=curPage;
        this.nextPage=nextPage;
        this.lastPage=lastPage;
    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(RecyclerView view, int scrollState) {
        // Don't take any action on changed
    }
}
