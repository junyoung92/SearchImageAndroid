package com.example.qualson_kjy.search.presenter;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Qualson_KJY on 2016-05-09.
 */
public class ImagePresenter implements BasePresenter {
    private View myView;
    private String image;

    public void initialize(ImagePresenter.View myView, String image) {
        this.myView = myView;
        this.image = image;
    }


    @Override
    public void execute() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap;
                    URL url = new URL(image);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            myView.setImage(bitmap);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public interface View {
        void setImage(Bitmap bitmap);
    }
}
