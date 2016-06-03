package com.example.qualson_kjy.search.presenter;

import com.example.qualson_kjy.search.model.ChannelRoot;
import com.example.qualson_kjy.search.model.ChannelRootService;
import com.example.qualson_kjy.search.model.Image;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.widget.WidgetObservable;

public class MainPresenter implements BasePresenter {

    private final String BASE_URL = "https://apis.daum.net/search/";
    private final String API_KEY = "b1287d92732e330277cf287f6b64f748";
    private final String JSON = "json";
    private final int NUM = 20;

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
        Observable.create((final Subscriber<? super Result> subscriber) -> {
            try {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                Call<ChannelRoot> call = retrofit.create(ChannelRootService.class).getChannel(API_KEY, keyword, JSON, count, NUM);
                call.enqueue(new Callback<ChannelRoot>() {
                    @Override
                    public void onResponse(Call<ChannelRoot> call, Response<ChannelRoot> response) {
                        if (response.isSuccessful()) {
                            Observable.from(response.body().getChannel().getItem()).subscribe(channelItem -> imageList.add(new Image(page++, channelItem)));
                            subscriber.onNext(new Result("SUCCESS", true));
                        } else {
                            subscriber.onNext(new Result("FAIL", false));
                        }
//                                List<ChannelItem> channelItems = response.body().getChannel().getItem();
//                                for (int i = 0; i < channelItems.size(); i++) {
//                                    imageList.add(new Image(page++, channelItems.get(i)));
//                                }

//                        if (response.isSuccessful()) {
//                            for (ChannelItem channelItem : response.body().getChannel().getItem()) {
//                                imageList.add(new Image(page++, channelItem));
//                            }
//                            subscriber.onNext(new Result("SUCCESS", true));
//                        } else {
//                            subscriber.onNext(new Result("FAIL", false));
//                        }
                    }
                    @Override
                    public void onFailure(Call<ChannelRoot> call, Throwable t) {
                        subscriber.onNext(new Result("FAIL", false));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onNext(new Result("FAIL", false));
            }
        }).subscribe(result -> {
            if (result.success) {
                myView.success(imageList);
            } else {
                myView.error(result.error);
            }

        });
    }


    public interface View {
        void success(ArrayList<Image> imageList);

        void error(String message);
    }

    public class Result {
        private String error;
        private boolean success;

        public Result(String error, boolean success) {
            this.error = error;
            this.success = success;
        }
    }
}
