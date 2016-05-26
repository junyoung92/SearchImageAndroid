package com.example.qualson_kjy.search.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.qualson_kjy.search.R;
import com.example.qualson_kjy.search.model.Image;
import com.example.qualson_kjy.search.view.activities.ImageActivity_;
import com.example.qualson_kjy.search.view.fragments.ImageFragment;
import com.example.qualson_kjy.search.view.activities.ImageActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Qualson_KJY on 2016-04-29.
 */
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

        new Thread(new Runnable() {
            Handler handler = new Handler();

            @Override
            public void run() {
                try {
                    final Bitmap bitmap;
                    URL url = new URL(imageList.get(position).getThumbnail());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.imageView.setImageBitmap(bitmap);

                        }
                    });
                    // ((MainActivity) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    });
//                    viewHolder.imageView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            viewHolder.imageView.setImageBitmap(bitmap);
//                        }
//                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageActivity_.class);
                int page = imageList.get(position).getPage();
                i.putExtra(ImageFragment.PAGE, page);
                i.putExtra(ImageFragment.LIST, imageList);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }

}