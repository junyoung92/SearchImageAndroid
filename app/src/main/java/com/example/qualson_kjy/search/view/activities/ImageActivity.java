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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_image)
public class ImageActivity extends AppCompatActivity {

    @ViewById(R.id.image_viewpager)
    protected ViewPager viewPager;

    private PagerAdapter pagerAdapter;
    private ArrayList<Image> imageList;

    @AfterViews
    protected void init() {
        Intent i = getIntent();
        int pageInt = i.getIntExtra(ImageFragment.PAGE, -1);
        imageList = (ArrayList<Image>) i.getSerializableExtra(ImageFragment.LIST);
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