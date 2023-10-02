package com.r0adkll.slidr.example;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftinc.kit.util.SizeUtils;
import com.ftinc.kit.util.Utils;
import com.ftinc.kit.widget.AspectRatioImageView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.example.model.AndroidOS;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewerActivity extends AppCompatActivity {

    public static final String EXTRA_OS = "extra_os_version";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.cover) AspectRatioImageView mCover;
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.description) TextView mDescription;
    @BindView(R.id.date) TextView mDate;
    @BindView(R.id.version) TextView mVersion;
    @BindView(R.id.sdk) TextView mSdk;
    @BindView(R.id.position) TextView mPosition;

    private AndroidOS mOS;
    private SlidrConfig mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        ButterKnife.bind(this);

        // Get the status bar colors to interpolate between
        int primary = getResources().getColor(R.color.primaryDark);
        int secondary = getResources().getColor(R.color.red_500);

        // Build the slidr config
        int numPositions = SlidrPosition.values().length;
        SlidrPosition position = SlidrPosition.values()[Utils.getRandom().nextInt(numPositions)];
        mPosition.setText(position.name());

        mConfig = new SlidrConfig.Builder()
                .primaryColor(primary)
                .secondaryColor(secondary)
                .position(SlidrPosition.VERTICAL)
                .velocityThreshold(2400)
//                .distanceThreshold(.25f)
//                .edge(true)
                .touchSize(SizeUtils.dpToPx(this, 32))
                .build();

        // Attach the Slidr Mechanism to this activity
        Slidr.attach(this, mConfig);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mOS = getIntent().getParcelableExtra(EXTRA_OS);
        if(savedInstanceState != null) mOS = savedInstanceState.getParcelable(EXTRA_OS);

        // Set layout contents
        mTitle.setText(mOS.name);
        mDescription.setText(mOS.description);
        mDate.setText(String.valueOf(mOS.year));
        mVersion.setText(mOS.version);
        mSdk.setText(String.valueOf(mOS.sdk_int));

        // Load header image
        Glide.with(this)
                .load(mOS.image_url)
                .into(mCover);
    }

    @OnClick({R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5})
    void onColorClicked(View v){
        int color = ((ColorDrawable)v.getBackground()).getColor();
        getWindow().setStatusBarColor(color);
        mConfig.setColorSecondary(color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_OS, mOS);
    }
}
