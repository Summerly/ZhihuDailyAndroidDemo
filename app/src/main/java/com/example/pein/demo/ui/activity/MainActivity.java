package com.example.pein.demo.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.example.pein.demo.R;
import com.example.pein.demo.ui.fragment.LatestListFragment;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {
    private ConvenientBanner mConvenientBanner;

    private static final String TAG = "XiYuexin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.init();

        mConvenientBanner = (ConvenientBanner)findViewById(R.id.convenientBanner);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new LatestListFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.v("onResume");
    }
}
