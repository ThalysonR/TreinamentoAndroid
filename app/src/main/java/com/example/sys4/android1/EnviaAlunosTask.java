package com.example.sys4.android1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.sys4.android1.converter.AlunoConverter;
import com.example.sys4.android1.dao.AlunoDAO;
import com.example.sys4.android1.modelo.Aluno;

import java.io.IOException;
import java.util.List;

/**
 * Created by sys4 on 06/06/18.
 */

public class EnviaAlunosTask extends AsyncTask<Object, Object, String> {
    private Context context;
    private ProgressDialog dialog;

    public EnviaAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde", "Enviando alunos...", true, true);
    }

    @Override
    protected String doInBackground(Object[] objects) {
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.findAll();

        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.converteParaJson(alunos);

        Webclient client = new Webclient();
        String resposta = "";
        try {
            resposta = client.post(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

//
        return resposta;
    }

    @Override
    protected void onPostExecute(String resposta) {
        dialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }
}
