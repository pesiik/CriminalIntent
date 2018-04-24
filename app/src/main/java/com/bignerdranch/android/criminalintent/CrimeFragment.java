package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_POSITION = "crime_position";

    Crime mCrime;
    EditText mTitleField;
    Button mDateButton;
    CheckBox mSolvedCheckBox;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments().getInt(ARG_CRIME_POSITION);
        mCrime = CrimeLab.get(getContext()).getCrime(position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitleField.setText(mCrime.getTitle());

        mDateButton = v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mSolvedCheckBox.setChecked(mCrime.isSolved());



        return v;
    }

    public static CrimeFragment newInstance(int position){
        Bundle args = new Bundle();
        args.putInt(ARG_CRIME_POSITION, position);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
