package com.example.sys4.android1.sync;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.sys4.android1.ListaAlunosActivity;
import com.example.sys4.android1.dao.AlunoDAO;
import com.example.sys4.android1.dto.AlunoSync;
import com.example.sys4.android1.event.AtualizaListaAlunoEvent;
import com.example.sys4.android1.modelo.Aluno;
import com.example.sys4.android1.preferences.AlunoPreferences;
import com.example.sys4.android1.retrofit.RetrofitInicializador;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoSincronizador {
    private final Context context;
    private EventBus bus = EventBus.getDefault();
    private AlunoPreferences preferences;

    public AlunoSincronizador(Context context) {
        this.context = context;
        preferences = new AlunoPreferences(context);
    }

    public void buscaTodos() {
        if(preferences.temVersao()) {
            buscaNovos();
        } else {
            buscaAlunos();
        }
    }

    private void buscaNovos() {
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().novos(preferences.getVersao());
        call.enqueue(buscaAlunoCallback());
    }

    private void buscaAlunos() {
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().lista();
        call.enqueue(buscaAlunoCallback());
    }

    @NonNull
    private Callback<AlunoSync> buscaAlunoCallback() {
        return new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                sincroniza(alunoSync);
                Log.i("Versao", "onResponse: " + preferences.getVersao());
                bus.post(new AtualizaListaAlunoEvent());
                sincronizaAlunosInternos();
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e(ContentValues.TAG, "onFailure: ", t);
                bus.post(new AtualizaListaAlunoEvent());
            }
        };
    }

    public void sincroniza(AlunoSync alunoSync) {
        String versao = alunoSync.getMomentoDaUltimaModificacao();

        Log.i("Versao externa", "sincroniza: " + versao);

        if(temVersaoNova(versao)) {
            preferences.salvaVersao(versao);
            Log.i("Versao atual", "sincroniza: " + preferences.getVersao());
            AlunoDAO dao = new AlunoDAO(context);
            dao.sincroniza(alunoSync.getAlunos());
        }
    }

    private boolean temVersaoNova(String versao) {
        if(!preferences.temVersao())
            return true;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        try {
            Date dataExterna = format.parse(versao);
            String versaoInterna = preferences.getVersao();
            Log.i("Versao interna", "temVersaoNova: " + versaoInterna);
            Date dataInterna = format.parse(versaoInterna);
            return dataExterna.after(dataInterna);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void sincronizaAlunosInternos() {
        final AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.listaNaoSincronizados();
        if(alunos != null) {
            Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().atualiza(alunos);
            call.enqueue(new Callback<AlunoSync>() {
                @Override
                public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                    AlunoSync alunoSync = response.body();
                    sincroniza(alunoSync);
                }

                @Override
                public void onFailure(Call<AlunoSync> call, Throwable t) {

                }
            });
        }

    }

    public void deleta(final Aluno aluno) {
        Call<Void> call = new RetrofitInicializador().getAlunoService().delete(aluno.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AlunoDAO dao = new AlunoDAO(context);
                dao.delete(aluno);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}