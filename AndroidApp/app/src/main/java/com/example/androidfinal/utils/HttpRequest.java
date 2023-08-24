package com.example.androidfinal.utils;

import com.example.androidfinal.models.IModel;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    public String token;
    private String url;

    public HttpRequest(String url, String token) {
        this.url = url;
        this.token = token;
    }

    // add token to request header
    private OkHttpClient setHeaderToken() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    public <T> T buildService(Class<T> callDefClassType, boolean includeTokenHeader) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.url)
                .addConverterFactory(GsonConverterFactory.create());
        if (includeTokenHeader) {
            builder.client(setHeaderToken());
        }
        return builder.build().create(callDefClassType);
    }

    public IModel buildService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(IModel.class);
    }

    public IModel buildTokenService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(this.url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(setHeaderToken())
                .build();
        return retrofit.create(IModel.class);
    }
}
