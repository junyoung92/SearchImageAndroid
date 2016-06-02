package com.example.qualson_kjy.search.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

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

//        Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                try {
//                    URL url = new URL(imageList.get(position).getThumbnail());
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setDoInput(true);
//                    conn.connect();
//                    subscriber.onNext(BitmapFactory.decodeStream(conn.getInputStream()));
//                } catch (Exception e) {
//                    subscriber.onError(e);
//                }
//            }
//        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
//            @Override
//            public void call(Bitmap bitmap) {
//                viewHolder.imageView.setImageBitmap(bitmap);
//            }
//        });

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(imageList.get(position).getThumbnail());
            }
        }).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    return BitmapFactory.decodeStream(conn.getInputStream());
                } catch (Exception e) {
                    return null;
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                viewHolder.imageView.setImageBitmap(bitmap);
            }
        });

        clickObservable(viewHolder.imageView, position).subscribe(new Action1<Intent>() {
            @Override
            public void call(Intent i) {
                context.startActivity(i);
            }
        });

//        new Thread(new Runnable() {
//            Handler handler = new Handler();
//
//            @Override
//            public void run() {
//                try {
//                    final Bitmap bitmap;
//                    URL url = new URL(imageList.get(position).getThumbnail());
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    InputStream is = conn.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(is);
//
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            viewHolder.imageView.setImageBitmap(bitmap);
//
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

//        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, ImageActivity_.class);
//                int page = imageList.get(position).getPage();
//                i.putExtra(ImageFragment.PAGE, page);
//                i.putExtra(ImageFragment.LIST, imageList);
//                context.startActivity(i);
//            }
//        });
        return convertView;
    }

    private Observable clickObservable(View view, final int position) {
        final PublishSubject publishSubject = PublishSubject.create();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageActivity_.class);
                i.putExtra(ImageFragment.PAGE, imageList.get(position).getPage());
                i.putExtra(ImageFragment.LIST, imageList);
                publishSubject.onNext(i);

            }
        });
        return publishSubject;
    }

    class ViewHolder {
        ImageView imageView;
    }

}