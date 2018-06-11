package com.example.sys4.android1.tasks;

import android.os.AsyncTask;

import com.example.sys4.android1.Webclient;
import com.example.sys4.android1.converter.AlunoConverter;
import com.example.sys4.android1.modelo.Aluno;

import java.io.IOException;

/**
 * Created by sys4 on 07/06/18.
 */

public class InsereAlunoTask extends AsyncTask {
    private final Aluno aluno;

    public InsereAlunoTask(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            new Webclient().insere(new AlunoConverter().converteParaJson(aluno));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
