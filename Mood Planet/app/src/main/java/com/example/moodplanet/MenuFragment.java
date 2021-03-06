package com.example.moodplanet;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class
MenuFragment extends Fragment {

    ImageButton addBtn, homeBtn, chartBtn, journalBtn, settingBtn, quoteBtn;
    long count;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        // get image button view
        homeBtn = v.findViewById(R.id.homeButton);
        chartBtn = v.findViewById(R.id.chartButton);
        addBtn = v.findViewById(R.id.addButton);
        journalBtn = v.findViewById(R.id.journalButton);
        settingBtn = v.findViewById(R.id.settingButton);
        quoteBtn = v.findViewById(R.id.quoteButton);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), AddEntryActivity.class);
                startActivity(intent);
            }
        });

        chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                count = HomeActivity.count;
//                if (count > 0)
                    startActivity(new Intent(getActivity().getBaseContext(), ChartMainActivity.class));
//                else
//                    Toast.makeText(getActivity(),"No mood entries to chart!",Toast.LENGTH_LONG).show();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        journalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), JournalActivity.class);
                startActivity(intent);
            }
        });

        quoteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), QuoteActivity.class);
                startActivity(intent);
            }
        });

        return v;

    }
}