package com.example.qualson_kjy.search.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.qualson_kjy.search.model.ChannelItem;
import com.example.qualson_kjy.search.model.ChannelRoot;
import com.example.qualson_kjy.search.model.Image;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qualson_KJY on 2016-05-09.
 */
public class MainPresenter implements BasePresenter {

    private String sUrl1 = "https://apis.daum.net/search/image?apikey=b1287d92732e330277cf287f6b64f748&q=";
    private String sUrl2 = "&output=json&pageno=";
    private String sUrl3 = "&result=20";

    private String keyword;
    private int count;
    private View myView;
    private ArrayList<Image> imageList;
    private int page = 0;

    public void initialize(MainPresenter.View myView, String keyword, int count, ArrayList<Image> imageList) {
        this.myView = myView;
        this.keyword = keyword;
        this.count = count;
        this.imageList = imageList;
    }

    @Override
    public void execute() {
        new Parse().execute();
    }

    private class Parse extends AsyncTask<Void, Void, Result> {
        @Override
        protected Result doInBackground(Void... arg0) {
            try {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(sUrl1);
                stringBuffer.append(keyword);
                stringBuffer.append(sUrl2);
                stringBuffer.append(count);
                stringBuffer.append(sUrl3);

                URL url = new URL(stringBuffer.toString());
                HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;

                while ((line = buffer.readLine()) != null) {
                    sb.append(line);
                }

                ChannelRoot channelRoot = new Gson().fromJson(sb.toString(), ChannelRoot.class);
                List<ChannelItem> channelItem = channelRoot.getChannel().getItem();
                for (int i = 0; i < channelItem.size(); i++) {
                    imageList.add(new Image(page++, channelItem.get(i)));
                    Log.i("Page", page + "");
                }
                return new MainPresenter.Result("SUCCESS", true);
            } catch (Exception e) {
                e.printStackTrace();
                return new MainPresenter.Result(e.getLocalizedMessage(), false);
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result.success) {
                myView.success(imageList);
            } else {
                myView.error(result.error);
            }
        }
    }

    public class Result {
        private String error;
        private boolean success;

        public Result(String error, boolean success) {
            this.error = error;
            this.success = success;
        }
    }

    public interface View {
        void success(ArrayList<Image> imageList);

        void error(String message);
    }
}
