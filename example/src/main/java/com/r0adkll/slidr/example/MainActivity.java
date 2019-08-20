package com.r0adkll.slidr.example;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.r0adkll.slidr.example.model.AndroidOS;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler) RecyclerView mRecycler;

    private OSVersionAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecycler();
    }


    private void initRecycler(){
        mAdapter = new OSVersionAdapter();
        mAdapter.addAll(getData());
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setOnItemClickListener(new BetterRecyclerAdapter.OnItemClickListener<AndroidOS>() {
            @Override
            public void onItemClick(View view, AndroidOS androidOS, int i) {
                // Launch the slidable activity
                Intent viewer = new Intent(MainActivity.this, ViewerActivity.class);
                viewer.putExtra(ViewerActivity.EXTRA_OS, androidOS);
                startActivity(viewer);
            }
        });
    }


    private List<AndroidOS> getData(){
        InputStream is = getResources().openRawResource(R.raw.android_versions);
        InputStreamReader isr = new InputStreamReader(is);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<AndroidOS>>(){}.getType();
        List<AndroidOS> oss = gson.fromJson(isr, listType);
        return oss;
    }
}
