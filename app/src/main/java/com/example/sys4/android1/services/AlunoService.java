package com.example.sys4.android1.services;

import com.example.sys4.android1.dto.AlunoSync;
import com.example.sys4.android1.modelo.Aluno;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by sys4 on 08/06/18.
 */

public interface AlunoService {

    @POST(value = "aluno")
    Call<Void> insere(@Body Aluno aluno);

    @GET("aluno")
    Call<AlunoSync> lista();

    @DELETE("aluno/{id}")
    Call<Void> delete(@Path("id") String id);

    @GET("aluno/diff")
    Call<AlunoSync> novos(@Header("datahora") String versao);

    @PUT("aluno/lista")
    Call<AlunoSync> atualiza(@Body List<Aluno> alunos);
}
