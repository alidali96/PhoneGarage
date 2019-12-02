package com.androidproject.PhoneGarage.Fragments;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.androidproject.PhoneGarage.HelperAdapter.RecyclerViewAdapter;
import com.androidproject.PhoneGarage.JavaBeans.Data;
import com.androidproject.PhoneGarage.JavaBeans.ButtonClickListener;
import com.androidproject.PhoneGarage.JavaBeans.Phone;
import com.androidproject.PhoneGarage.R;
import com.androidproject.PhoneGarage.JavaBeans.SwipeHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mAdapter;


    ArrayList<Phone> phones;
    ArrayList<Phone> searchedPhones;
    EditText searchText;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        phones = Data.getInstance(getContext()).getPhonesList();

        mRecyclerView = view.findViewById(R.id.recyclerview2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RecyclerViewAdapter(getContext(), phones);

        mRecyclerView.setAdapter(mAdapter);

        searchText = view.findViewById(R.id.searchText);

        SwipeHelper swipeHelper = new SwipeHelper(getContext(), mRecyclerView, 200) {
            @Override
            public void insantiateMyButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper.MyButton> buffer) {

                buffer.add(new MyButton(getContext(), "heart", 60, 0, Color.parseColor("#FFBE3233"), new ButtonClickListener() {

                    @Override
                    public void onClick(int position) {
                        int result = FavouritesFragment.getInstance(getContext()).addPhoneToCompare(phones.get(position));
                        String message = "";
                        switch (result) {
                            case CompareFragment.PHONE_EXIST:
                                message = "Phone already exist in favourites.";
                                break;
                            case CompareFragment.PHONE_ADDED:
                                message = "Phone added to favourites.";
                                break;
                        }

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    }
                }));

                buffer.add(new MyButton(getContext(), "retweet", 70, 0, Color.parseColor("#FF4633F7"), new ButtonClickListener() {

                    @Override
                    public void onClick(int position) {
                        int result = CompareFragment.getInstance(getContext()).addPhoneToCompare(phones.get(position));
                        String message = "";
                        switch (result) {
                            case CompareFragment.LIST_FULL:
                                message = "Comparing Phones List is full.";
                                break;
                            case CompareFragment.PHONE_EXIST:
                                message = "Phone already exist in Comparing List.";
                                break;
                            case CompareFragment.PHONE_ADDED:
                                message = "Phone added to Comparing List.";
                        }

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    }
                }));
            }
        };


        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    // Update recycler view
                    mAdapter.updatePhonesList(searchPhones(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private ArrayList<Phone> searchPhones(String searchInput) {
        searchedPhones = new ArrayList<>();
        searchInput = searchInput.toUpperCase();

        for (Phone phone : phones) {
            String deviceName = phone.getDeviceName().toUpperCase();
            if (deviceName.contains(searchInput)) {
                searchedPhones.add(phone);
            }
        }
        return searchedPhones;
    }

//    private ArrayList<Phone> searchPhones(String searchInput) {
//        searchedPhones = new ArrayList<>();
//        searchInput = searchInput.toUpperCase();
//        String[] searchInputArray = searchInput.split(" ");
//
//        for (Phone phone : phones) {
//            String deviceName = phone.getDeviceName().toUpperCase();
//            String[] deviceNameArray = deviceName.split(" ");
//
//            for (String device : deviceNameArray) {
//                for(String search : searchInputArray) {
//                    if (device.contains(search)) {
//                        searchedPhones.add(phone);
//                    }
//                }
//            }
//        }
//        return searchedPhones;
//    }

}
