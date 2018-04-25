package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "date";
    public static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Date date = (Date) getArguments().getSerializable(ARG_TIME);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hours);
            mTimePicker.setMinute(minutes);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v).setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = mTimePicker.getHour();
                        int minutes = mTimePicker.getMinute();

                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minutes);;
                        Date time = calendar.getTime();
                        sendResult(Activity.RESULT_OK, time);
                    }
                }).create();
    }

    private void sendResult(int resultCode, Date time){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
