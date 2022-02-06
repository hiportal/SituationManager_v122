package com.ex.situationmanager.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ex.situationmanager.R;

import java.util.ArrayList;

public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.ViewHolder> {
    private String mData;

    public DispatchAdapter(String list) {
        mData = list ;
    }

    @Override
    public DispatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_dispatch, parent, false) ;
        DispatchAdapter.ViewHolder vh = new DispatchAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(DispatchAdapter.ViewHolder holder, int position) {
        String text = mData ;
        holder.textView1.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;

        ViewHolder(View itemView) {
            super(itemView) ;

            textView1 = itemView.findViewById(R.id.textView);
        }
    }
}
