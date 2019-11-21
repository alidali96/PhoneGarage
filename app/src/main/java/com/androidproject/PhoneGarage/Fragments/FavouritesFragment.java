package com.androidproject.PhoneGarage.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidproject.PhoneGarage.HelperAdapter.RecyclerViewAdapter;
import com.androidproject.PhoneGarage.JavaBeans.Data;
import com.androidproject.PhoneGarage.JavaBeans.Phone;
import com.androidproject.PhoneGarage.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {

    public static ArrayList<Phone> favouritPhoneList;

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mAdapter;
    TextView favouriteText;


    public static ArrayList<Phone> phones = new ArrayList<>();

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        // TEST
        phones.clear();

        phones.add(Data.getInstance(getContext()).getPhonesList().get(0));
        phones.add(Data.getInstance(getContext()).getPhonesList().get(1));
        phones.add(Data.getInstance(getContext()).getPhonesList().get(2));
        phones.add(Data.getInstance(getContext()).getPhonesList().get(3));

//        CompareFragment.phones.add(Data.getInstance(getContext()).getPhonesList().get(0));
//        CompareFragment.phones.add(Data.getInstance(getContext()).getPhonesList().get(1));
//        CompareFragment.phones.add(Data.getInstance(getContext()).getPhonesList().get(2));
//        CompareFragment.phones.add(Data.getInstance(getContext()).getPhonesList().get(3));

//        CompareFragment.addPhoneToCompare(Data.getInstance(getContext()).getPhonesList().get(0));
//        CompareFragment.addPhoneToCompare(Data.getInstance(getContext()).getPhonesList().get(1));
//        CompareFragment.addPhoneToCompare(Data.getInstance(getContext()).getPhonesList().get(2));
//        CompareFragment.addPhoneToCompare(Data.getInstance(getContext()).getPhonesList().get(3));




        
        favouriteText = view.findViewById(R.id.favouriteText);
        favouriteText.setVisibility(View.GONE);

        if (phones.isEmpty()) {
            favouriteText.setVisibility(View.VISIBLE);
        }


        mRecyclerView = view.findViewById(R.id.recyclerview2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RecyclerViewAdapter(getContext(), phones);

        mRecyclerView.setAdapter(mAdapter);
//>>>>>>> Staging

        return view;
    }

}