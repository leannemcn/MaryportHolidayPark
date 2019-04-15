package com.example.b00682737.maryportholidaypark.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.b00682737.maryportholidaypark.Fragments.ImageFragment;

public class ImageAdapter extends FragmentStatePagerAdapter {
    private int[] images;

    public ImageAdapter(FragmentManager fm, int[] images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.getInstance(images[position]);
    }

    @Override
    public int getCount() {
        if (images == null) {
            return 0;
        } else {
            return images.length;
        }
    }
}

