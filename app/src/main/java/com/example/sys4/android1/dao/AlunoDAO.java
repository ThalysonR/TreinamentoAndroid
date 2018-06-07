package com.example.sys4.android1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.sys4.android1.modelo.Aluno;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sys4 on 01/06/18.
 */

public class AlunoDAO extends SQLiteOpenHelper {

    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE Alunos (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT," +
                " nota REAL, " +
                "caminhoFoto TEXT" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int current) {
        String sql;
        switch (old) {
            case 1:
                sql = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT";
                sqLiteDatabase.execSQL(sql);
                if(current == 2)
                    break;
        }
    }

    public void insert(Aluno aluno) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues dados = pegaDadosAluno(aluno);

        database.insert("Alunos", null, dados);
        close();
    }

    @NonNull
    private ContentValues pegaDadosAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());
        return dados;
    }

    public List<Aluno> findAll() {
        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        List<Aluno> alunos = new ArrayList<Aluno>();
        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }
        cursor.close();
        db.close();
        return alunos;
    }

    public boolean delete(Long id) {
//        String sql = "DELETE FROM Alunos WHERE id = " + id;
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {id.toString()};

        try {
            db.delete("Alunos", "id = ?", params);
//            db.execSQL(sql);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            close();
        }
        return true;
    }

    public void update(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = pegaDadosAluno(aluno);
        String[] params = {aluno.getId().toString()};

        db.update("Alunos", dados, "id = ?", params);
    }

    public boolean ehAluno(String telefone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Alunos WHERE telefone = ?", new String[]{telefone});
        int count = cursor.getCount();
        cursor.close();
        close();
        return count > 0;
    }
}
