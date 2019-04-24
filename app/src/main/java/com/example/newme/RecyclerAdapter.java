package com.example.newme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    String data1[], data2[];
    Context ctx;

    RecyclerAdapter() {
        //necessary default constructor for AnroidManifest.xml
    }

    RecyclerAdapter(Context ct, String s1[], String s2[]) {
        ctx = ct;
        data1 = s1;
        data2 = s2;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater myInflater = LayoutInflater.from(ctx);
        View myOwnView = myInflater.inflate(R.layout.my_row, parent, false);
        return new RecyclerHolder(myOwnView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder recyclerHolder, int i) {
        recyclerHolder.t1.setText(data1[i]);
        recyclerHolder.t2.setText(data2[i]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView t1, t2;
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.text1);
            t2 = (TextView) itemView.findViewById(R.id.text2);
        }
    }
}
