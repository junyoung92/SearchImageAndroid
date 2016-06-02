package com.example.qualson_kjy.search.presenter;

import com.example.qualson_kjy.search.model.ChannelItem;
import com.example.qualson_kjy.search.model.ChannelRoot;
import com.example.qualson_kjy.search.model.Image;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MainPresenter implements BasePresenter {

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
        Observable.create(new Observable.OnSubscribe<Result>() {
            @Override
            public void call(final Subscriber<? super Result> subscriber) {
                try {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://apis.daum.net/search/").addConverterFactory(GsonConverterFactory.create()).build();
                    ChannelRootService channelRootService = retrofit.create(ChannelRootService.class);
                    Call<ChannelRoot> call = channelRootService.getChannel(API_KEY, keyword, JSON, count, NUM);
                    call.enqueue(new Callback<ChannelRoot>() {
                        @Override
                        public void onResponse(Call<ChannelRoot> call, Response<ChannelRoot> response) {
                            if (response.isSuccessful()) {
                                List<ChannelItem> channelItem = response.body().getChannel().getItem();
                                for (int i = 0; i < channelItem.size(); i++) {
                                    imageList.add(new Image(page++, channelItem.get(i)));
                                }
                                subscriber.onNext(new MainPresenter.Result("SUCCESS", true));
                            } else {
                                subscriber.onNext(new MainPresenter.Result("FAIL", false));
                            }
                        }

                        @Override
                        public void onFailure(Call<ChannelRoot> call, Throwable t) {
                            subscriber.onNext(new MainPresenter.Result("FAIL", false));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onNext(new MainPresenter.Result("FAIL", false));
                }
            }
        }).subscribe(new Action1<Result>() {
            @Override
            public void call(Result result) {
                if (result.success) {
                    myView.success(imageList);
                } else {
                    myView.error(result.error);
                }
            }
        });
    }

    public interface ChannelRootService {
        @GET("image")
        Call<ChannelRoot> getChannel(
                @Query("apikey") String apikey,
                @Query("q") String keyword,
                @Query("output") String json,
                @Query("pageno") int count,
                @Query("result") int result);
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
