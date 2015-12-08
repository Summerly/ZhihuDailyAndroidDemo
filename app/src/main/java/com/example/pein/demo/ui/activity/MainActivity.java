package com.example.pein.demo.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.pein.demo.Constants;
import com.example.pein.demo.R;
import com.example.pein.demo.database.DemoDatabase;
import com.example.pein.demo.ui.fragment.BeforeFragment;
import com.example.pein.demo.ui.fragment.LatestFragment;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "XiYuexin";
    private static final String STRING_REQUEST_TAG = "latest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.init();

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (date != null && !date.equals(Constants.getTomorrowDate())) {
            if (fragment == null) {
                fragment = new BeforeFragment();
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                fragment.setArguments(bundle);
                fm.beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .commit();
            }
        } else {
            if (fragment == null) {
                fragment = new LatestFragment();
                fm.beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .commit();
            }
        }

        ImageView icon = new ImageView(this);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_tag_faces_black);
        icon.setImageDrawable(drawable);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        DemoDatabase.getLatestStories(getApplicationContext());
    }
}
