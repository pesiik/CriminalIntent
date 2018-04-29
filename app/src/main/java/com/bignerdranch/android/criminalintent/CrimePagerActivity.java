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

public class CrimePagerActivity extends AppCompatActivity {

    protected static final String EXTRA_CRIME_POSITION = "com.bignerdranch.android.criminalintent.crime_position";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mFirstCrimeButton;
    private Button mLastCrimeButton;

    public static Intent newIntent(Context packageContext,int position){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_POSITION, position);
        return intent;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        int positionItem = getIntent().getIntExtra(EXTRA_CRIME_POSITION, -1);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {

                return CrimeFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });


        mViewPager.setCurrentItem(positionItem);


    }

}
