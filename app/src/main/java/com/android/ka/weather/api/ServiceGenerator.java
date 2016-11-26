package com.android.ka.weather.api;

import com.android.ka.weather.AppConfig;
import com.android.ka.weather.common.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ka on 26/11/2016.
 */

public class ServiceGenerator {

    static final Logger LOGGER = Logger.getLogger(ServiceGenerator.class);
    private static final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
    private static final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create());

    static {
        httpClientBuilder.readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
    }

    public static <T> T create(Class<T> serviceClass) {
        Retrofit retrofit = retrofitBuilder.client(httpClientBuilder.build()).build();
        return retrofit.create(serviceClass);
    }
}
