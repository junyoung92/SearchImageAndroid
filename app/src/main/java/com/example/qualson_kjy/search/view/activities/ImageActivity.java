package com.example.qualson_kjy.search.view.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.qualson_kjy.search.R;
import com.example.qualson_kjy.search.model.Image;
import com.example.qualson_kjy.search.view.fragments.ImageFragment;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ArrayList<Image> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent i = getIntent();
        int pageInt = i.getIntExtra(ImageFragment.PAGE, -1);
        imageList = (ArrayList<Image>) i.getSerializableExtra(ImageFragment.LIST);

        viewPager = (ViewPager) findViewById(R.id.image_viewpager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(pageInt);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.create(position, imageList);
        }

        @Override
        public int getCount() {
            return imageList.size();
        }
    }
}