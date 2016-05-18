package com.example.qualson_kjy.search.view.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qualson_kjy.search.R;
import com.example.qualson_kjy.search.model.Image;
import com.example.qualson_kjy.search.presenter.ImagePresenter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFragment extends Fragment implements ImagePresenter.View {

    public static final String PAGE = "PAGE";
    public static final String LIST = "LIST";

    private int mPageNumber;
    private TextView textView;
    private ImageView imageView;
    private ArrayList<Image> imageList;

    private ImagePresenter imagePresenter = new ImagePresenter();

    public static ImageFragment create(int pageNumber, ArrayList<Image> arrayList) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE, pageNumber);
        args.putSerializable(LIST, arrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(PAGE);
        imageList = (ArrayList<Image>) getArguments().getSerializable(LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_image, container, false);
        textView = (TextView) rootView.findViewById(R.id.image_tv);
        imageView = (ImageView) rootView.findViewById(R.id.image_iv);

        textView.setText(imageList.get(mPageNumber).getTitle());

        imagePresenter.initialize(ImageFragment.this, imageList.get(mPageNumber).getImage());
        imagePresenter.execute();

        return rootView;
    }

    @Override
    public void setImage(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        new PhotoViewAttacher(imageView);
    }
}