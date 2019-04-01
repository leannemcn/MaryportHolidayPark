package com.example.b00682737.maryportholidaypark.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment {
    private int imageResource;

    public static ImageFragment getInstance(int imageSource) {
        ImageFragment f = new ImageFragment();
        Bundle args = new Bundle();

        args.putInt("image_source", imageSource);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageResource = getArguments().getInt("image_source");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imagegallery, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set Image
        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(imageResource);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


