package com.example.sys4.android1.retrofit;

import com.example.sys4.android1.services.AlunoService;
import com.example.sys4.android1.services.DispositivoService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by sys4 on 08/06/18.
 */

public class RetrofitInicializador {

    private final Retrofit retrofit;

    public RetrofitInicializador() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public AlunoService getAlunoService() {
        return retrofit.create(AlunoService.class);
    }

    public DispositivoService getDispositivoService() {
        return retrofit.create(DispositivoService.class);
    }
}
