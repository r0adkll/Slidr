package com.r0adkll.slidr.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorAdapter extends BetterRecyclerAdapter<Integer, ColorAdapter.ColorViewHolder> {

    public ColorAdapter() {
        List<Integer> colorList = Arrays.asList(R.color.red_500, R.color.orange_500,
                R.color.yellow_500, R.color.blue_500, R.color.green_500, R.color.purple_500,
                R.color.amber_500, R.color.pink_500, R.color.brown_500, R.color.lime_500,
                R.color.indigo_500, R.color.cyan_500);
        addAll(colorList);
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        Integer color = getItem(i);
        viewHolder.color.setBackgroundColor(ContextCompat.getColor(viewHolder.color.getContext(), color));
    }

    public static class ColorViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.color)   public View color;

        public ColorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
