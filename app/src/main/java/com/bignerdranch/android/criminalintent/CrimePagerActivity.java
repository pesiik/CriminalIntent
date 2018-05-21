package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity  implements CrimeFragment.Callbacks{

    protected static final String EXTRA_CRIME_POSITION = "com.bignerdranch.android.criminalintent.crime_position";
    protected static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mFirstCrimeButton;
    private Button mLastCrimeButton;

    public static Intent newIntent(Context packageContext,int position){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_POSITION, position);
        return intent;
    }

    public static Intent newIntent(Context packageContext, UUID id){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        int positionItem = 0;
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        mFirstCrimeButton = (Button) findViewById(R.id.first_button);
        mLastCrimeButton = (Button) findViewById(R.id.last_button);



        mFirstCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);

            }
        });
        mLastCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size()-1);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                mFirstCrimeButton.setEnabled(position > 0);
                mLastCrimeButton.setEnabled(position < mCrimes.size() - 1);
            }
        });

        mCrimes = CrimeLab.get(this).getCrimes();

        for (Crime crime : mCrimes){
            if(crime.getId() == id){
                break;
            }
            positionItem++;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {

                return CrimeFragment.newInstance(mCrimes.get(position).getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });


        mViewPager.setCurrentItem(positionItem);


    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
