package com.example.qualson_kjy.search.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.qualson_kjy.search.R;
import com.example.qualson_kjy.search.model.Image;
import com.example.qualson_kjy.search.view.activities.ImageActivity_;
import com.example.qualson_kjy.search.view.fragments.ImageFragment;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Image> imageList;

    public MainAdapter(Context c, ArrayList<Image> imageList) {
        this.context = c;
        this.imageList = imageList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Image getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.view_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_iv);
            viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        imageList.get(position).setPage(position);

        Observable.create((Subscriber<? super String> subscriber) -> {
            subscriber.onNext(imageList.get(position).getThumbnail());
        }).map(string -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(string).openConnection();
                conn.setDoInput(true);
                conn.connect();
                return BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception e) {
                return null;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(viewHolder.imageView::setImageBitmap);

        viewHolder.imageView.setOnClickListener(v -> {
            Intent i = new Intent(context, ImageActivity_.class);
            i.putExtra(ImageFragment.PAGE, imageList.get(position).getPage());
            i.putExtra(ImageFragment.LIST, imageList);
            context.startActivity(i);
        });

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }

}