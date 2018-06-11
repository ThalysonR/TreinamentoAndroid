package com.example.sys4.android1.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sys4 on 11/06/18.
 */

public class AlunoPreferences {
    private static final String ALUNO_PREFERENCES = "com.example.sys4.android1.preferences.AlunoPreferences";
    private static final String VERSAO_DO_DADO = "versao_do_dado";
    private Context context;

    public AlunoPreferences(Context context) {
        this.context = context;
    }

    public void salvaVersao(String versao) {
        SharedPreferences preferences = getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(VERSAO_DO_DADO, versao);
        editor.commit();
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(ALUNO_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getVersao() {
        SharedPreferences preferences = getSharedPreferences();
        return preferences.getString(VERSAO_DO_DADO, "");
    }

    public boolean temVersao() {
        return !getVersao().isEmpty();
    }
}
