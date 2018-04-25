package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mCurrentPosition = -1;
    private boolean mSubtitleVisible;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null)
        {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else {
            if(mCurrentPosition > -1){
                mAdapter.notifyItemChanged(mCurrentPosition);
                mCurrentPosition = -1;
            }
            else {
                mAdapter.notifyDataSetChanged();
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getContext(),mCurrentPosition);
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private abstract class AbstractCrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDataTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        public AbstractCrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutId){
            super(inflater.inflate(layoutId,parent, false));

            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDataTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            Date date = mCrime.getDate();
            mDataTextView.setText(DateFormat.format("EEEE, MMM dd, yyyy", date).toString() + DateFormat.format("HH:mm:ss", mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            mCurrentPosition = getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getContext(), mCurrentPosition);
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<AbstractCrimeHolder>{
        private List<Crime> mCrimes;

        private static final int LIST_ITEM_CRIME = 0;
        private static final int LIST_ITEM_CRIME_POLICE = 1;

        @Override
        public int getItemViewType(int position) {
            boolean requiresPolice = mCrimes.get(position).isRequiresPolice();
            if(requiresPolice) {
                return LIST_ITEM_CRIME_POLICE;
            }
            else {
                return LIST_ITEM_CRIME;
            }
        }

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public void onBindViewHolder(@NonNull AbstractCrimeHolder holder, int position, @NonNull List<Object> payloads) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime) ;
        }

        private class CrimeHolder extends AbstractCrimeHolder{
            public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
                super(inflater, parent, R.layout.list_item_crime);
            }
        }

        private class PoliceCrimeHolder extends AbstractCrimeHolder{
            public PoliceCrimeHolder(LayoutInflater inflater, ViewGroup parent){
                super(inflater, parent, R.layout.list_item_crime_police);
            }
        }

        @NonNull
        @Override
        public AbstractCrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if(viewType == LIST_ITEM_CRIME){
                return new CrimeHolder(layoutInflater, parent);
            } else if (viewType == LIST_ITEM_CRIME_POLICE){
                return new PoliceCrimeHolder(layoutInflater, parent);
            }
            else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull AbstractCrimeHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
           subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

}
