package com.example.qualson_kjy.search.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ImagePresenter implements BasePresenter {
    private View myView;
    private String image;

    public void initialize(ImagePresenter.View myView, String image) {
        this.myView = myView;
        this.image = image;
    }

    @Override
    public void execute() {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    URL url = new URL(image);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    subscriber.onNext(BitmapFactory.decodeStream(conn.getInputStream()));
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                myView.setImage(bitmap);
                Log.i("observable", "next");
            }
        });
//        final Handler handler = new Handler();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final Bitmap bitmap;
//                    URL url = new URL(image);
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
//                            myView.setImage(bitmap);
//                        }
//                    });
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    public interface View {
        void setImage(Bitmap bitmap);
    }
}
