package com.r0adkll.slidr.example;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;
import com.r0adkll.slidr.example.model.AndroidOS;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by r0adkll on 1/11/15.
 */
public class OSVersionAdapter extends BetterRecyclerAdapter<AndroidOS, OSVersionAdapter.OSViewHolder> {

    @Override
    public OSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item, parent, false);
        return new OSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OSViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        AndroidOS os = getItem(i);
        viewHolder.title.setText(os.name);
        viewHolder.description.setText(os.description);
    }

    public static class OSViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.title)         public TextView title;
        @Bind(R.id.description)   public TextView description;

        public OSViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
