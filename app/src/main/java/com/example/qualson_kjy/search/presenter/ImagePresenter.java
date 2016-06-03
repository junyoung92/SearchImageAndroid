package com.example.qualson_kjy.search.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
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
        Observable.create((Subscriber<? super Bitmap> subscriber) -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(image).openConnection();
                conn.setDoInput(true);
                conn.connect();
                subscriber.onNext(BitmapFactory.decodeStream(conn.getInputStream()));
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(bitmap -> myView.setImage(bitmap));

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
