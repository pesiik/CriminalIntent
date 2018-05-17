package com.bignerdranch.android.criminalintent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

public class PhotoFragment extends DialogFragment {

    private static final String ARG_PHOTO = "photo";

    private ImageView mPhotoView;
    private File mPhotoFile;

    public static PhotoFragment newInstance(File photoFile){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, photoFile);

        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPhotoFile = (File) getArguments().getSerializable(ARG_PHOTO);

        View view = inflater.inflate(R.layout.photo_fragment,container, false);
        mPhotoView = (ImageView) view.findViewById(R.id.photo_view);

        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageBitmap(null);
        }
        else {
            Bitmap bitmap = PicturesUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
