package cz.uhk.umte.borikpa1.businessnews.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceGenerator {
    private static final String API_BASE_URL = "https://api.iextrading.com/";

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    );

    private static Retrofit retrofit = builder.client(httpClient.build()).build();

    public static <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
