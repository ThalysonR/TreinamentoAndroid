package com.example.sys4.android1.firebase;

import android.util.Log;

import com.example.sys4.android1.retrofit.RetrofitInicializador;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sys4 on 11/06/18.
 */

public class AgendaInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token Firebase", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        enviaTokenParaServidor(refreshedToken);
    }

    private void enviaTokenParaServidor(final String refreshedToken) {
        Call<Void> call = new RetrofitInicializador().getDispositivoService().enviaToken(refreshedToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("Token enviado", refreshedToken);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Token falhou", "onFailure: ",t );
            }
        });
    }
}
