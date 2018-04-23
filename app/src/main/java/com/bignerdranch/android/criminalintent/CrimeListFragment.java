package com.bignerdranch.android.criminalintent;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
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
            mDataTextView.setText(DateFormat.format("EEEE, MMM dd, yyyy", date));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();
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
}
