package com.androidproject.PhoneGarage.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Debug;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidproject.PhoneGarage.JavaBeans.MainActivity;
import com.androidproject.PhoneGarage.JavaBeans.Phone;
import com.androidproject.PhoneGarage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompareFragment extends Fragment {

    public static ArrayList<Phone> comparePhoneList;

    public static final int PHONE_EXIST = -1;
    public static final int LIST_FULL = 0;
    public static final int PHONE_ADDED = 1;

    private static final int MAX_PHONES = 4;
    private static final String PHONES = "phones";

    private static SharedPreferences sharedPreferences;
    private static Gson gson;

    private static int currentPosition = 0;

    private static ArrayList<Phone> phones;


    private CompareAdapter adapter;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private TextView compareText;

    private boolean tablet;


    private static CompareFragment instance;

    public static CompareFragment getInstance(Context context) {
        if (instance == null)
            instance = new CompareFragment(context);
        return instance;
    }

    private CompareFragment(Context context) {
        initialize(context);
    }

    // Need a public constructor
    public CompareFragment() {
//        initialize(getContext());
    }

    private void initialize(Context context) {
        if (phones == null)
            phones = new ArrayList<>();
        if (gson == null)
            gson = new Gson();
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize(getContext());
        loadData();

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("orientation", "PORTRAIT");
            tablet = false;
        } else {
            Log.e("orientation", "LANDSCAPE");
            tablet = true;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("orientation", "PORTRAIT");
            tablet = false;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("orientation", "LANDSCAPE");
            tablet = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compare, container, false);

        compareText = view.findViewById(R.id.compareText);
        compareText.setVisibility(View.GONE);


        fab = view.findViewById(R.id.fabBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("POSITION", currentPosition + "");
                phones.remove(currentPosition);
                adapter.notifyDataSetChanged();
                if (phones.isEmpty()) {
                    fab.setVisibility(View.GONE);
                    compareText.setVisibility(View.VISIBLE);
                }
                saveData();
                Toast.makeText(getContext(), "Phone is deleted", Toast.LENGTH_SHORT).show();
            }
        });

        if (phones.isEmpty()) {
            fab.setVisibility(View.GONE);
            compareText.setVisibility(View.VISIBLE);
        }


        adapter = new CompareAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.compareViewPager);
        viewPager.setAdapter(adapter);

        // Get the current position of the ViewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;
    }


    public int addPhoneToCompare(Phone phone) {
        loadData();

        Log.e("CONTAIN", phones.contains(phone) + " contains");
        Log.e("CONTAIN", MainActivity.comparePhones(phones, phone) + " compares");
        Log.e("CONTAIN", phone + "");

        if (MainActivity.comparePhones(phones, phone)) return PHONE_EXIST;

        if (phones.size() >= MAX_PHONES) return LIST_FULL;

        phones.add(phone);
        saveData();

        return PHONE_ADDED;
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String jsonArray = gson.toJson(phones);

        Log.e("PHONES", jsonArray + "save");

        editor.putString(PHONES, jsonArray);
        editor.commit();

    }

    private void loadData() {
        if (sharedPreferences != null) {
            String jsonArray = sharedPreferences.getString(PHONES, gson.toJson(new ArrayList<>())); // Default is an empty ArrayList<>()
            Type phoneType = new TypeToken<ArrayList<Phone>>() {
            }.getType();
            phones = gson.fromJson(jsonArray, phoneType);

            Log.e("PHONES", jsonArray + "load");
            Log.e("PHONES", phones.toString() + "phones array");
        }
    }


    private class CompareAdapter extends FragmentStatePagerAdapter {

        public CompareAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DetailsFragment.newInstance(phones.get(position));
        }

        @Override
        public int getItemPosition(Object phone) {
            if (phones.contains(phone)) {
                return phones.indexOf(phone);
            } else {
                return POSITION_NONE;
            }
        }

        @Override
        public int getCount() {
            return phones.size();
        }
    }
}