package com.example.pein.myapplication;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pein on 15/11/30.
 */
public class MyListFragment extends ListFragment {
    private static final ArrayList<String> tests = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 100; i++) {
            String item = "" + i*i;
            tests.add(item);
        }

        TestAdapter adapter = new TestAdapter(tests);

        setListAdapter(adapter);
    }

    private class TestAdapter extends ArrayAdapter<String> {
        public TestAdapter(ArrayList<String> tests) {
            super(getActivity(), 0, tests);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.fragment_blank, null);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.textView);

            textView.setText(tests.get(position));

            return convertView;
        }
    }
}

