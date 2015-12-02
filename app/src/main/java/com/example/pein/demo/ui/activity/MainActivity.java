package com.example.pein.demo.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.example.pein.demo.R;
import com.example.pein.demo.ui.fragment.LatestListFragment;

public class MainActivity extends AppCompatActivity {
    private ConvenientBanner mConvenientBanner;

    private static final String TAG = "XiYuexin";
    private String[] images = {"http://pic3.zhimg.com/a1692c70c6189aa391a7fe7fd8215292.jpg",
        "http://pic2.zhimg.com/5de633bc8fe2b7c081ecc44c89cb2991.jpg",
        "http://pic3.zhimg.com/dd60deb07b36f188e7d707b6816707d2.jpg",
        "http://pic3.zhimg.com/a2011c42103e857326a242022e1e7fa2.jpg",
        "http://pic3.zhimg.com/f9d50990530ed9aee6960ce0296764c2.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
