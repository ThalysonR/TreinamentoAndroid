package com.example.sys4.android1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.sys4.android1.modelo.Aluno;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by sys4 on 01/06/18.
 */

public class AlunoDAO extends SQLiteOpenHelper {

    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE Alunos (" +
                "id CHAR(36) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL, " +
                "caminhoFoto TEXT, " +
                "sincronizado INT DEFAULT 0, " +
                "desativado INT DEFAULT 0" +
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
            case 2:
                sql = "CREATE TABLE Alunos_novo " +
                        "(id CHAR(36) PRIMARY KEY, " +
                        "nome TEXT NOT NULL, " +
                        "endereco TEXT, " +
                        "telefone TEXT, " +
                        "site TEXT," +
                        " nota REAL, " +
                        "caminhoFoto TEXT" +
                        ")";
                sqLiteDatabase.execSQL(sql);

                sql = "INSERT INTO Alunos_novo " +
                        "(id, nome, endereco, telefone, site, nota, caminhoFoto) " +
                        "SELECT id, nome, endereco, telefone, site, nota, caminhoFoto " +
                        "FROM Alunos";
                sqLiteDatabase.execSQL(sql);

                sql = "DROP TABLE Alunos";
                sqLiteDatabase.execSQL(sql);

                sql = "ALTER TABLE Alunos_novo RENAME TO Alunos";
                sqLiteDatabase.execSQL(sql);
                if(current == 3)
                    break;
            case 3:
                sql = "SELECT * FROM Alunos";
                Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
                sql = "UPDATE Alunos SET id=? WHERE id=?";
                List<Aluno> alunos = getAlunos(cursor);

                for (Aluno aluno:
                     alunos) {
                    sqLiteDatabase.execSQL(sql, new String[]{geraUUID(), aluno.getId()});
                }
                if(current == 4)
                    break;
            case 4:
                sql = "ALTER TABLE Alunos ADD COLUMN sincronizado INT DEFAULT 0";
                sqLiteDatabase.execSQL(sql);
                if(current == 5)
                    break;
            case 5:
                sql = "ALTER TABLE Alunos ADD COLUMN desativado INT DEFAULT  0";
                sqLiteDatabase.execSQL(sql);
                if(current == 6)
                    break;
        }
    }

    private String geraUUID() {
        return UUID.randomUUID().toString();
    }

    public void insert(Aluno aluno) {
        SQLiteDatabase database = getWritableDatabase();

        String uuid = aluno.getId() == null? geraUUID(): aluno.getId();

        ContentValues dados = pegaDadosAluno(aluno);
        dados.put("id", uuid);

        database.insert("Alunos", null, dados);
        aluno.setId(uuid);
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
        dados.put("sincronizado", aluno.getSincronizado());
        dados.put("desativado", aluno.getDesativado());
        return dados;
    }

    public List<Aluno> findAll() {
        String sql = "SELECT * FROM Alunos WHERE desativado = 0;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        List<Aluno> alunos = getAlunos(cursor);
        Log.i(TAG, "findAll: " + alunos);
        db.close();
        return alunos;
    }

    @NonNull
    private List<Aluno> getAlunos(Cursor cursor) {
        List<Aluno> alunos = new ArrayList<Aluno>();
        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(cursor.getString(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));
            aluno.setSincronizado(cursor.getInt(cursor.getColumnIndex("sincronizado")));
            aluno.setDesativado(cursor.getInt(cursor.getColumnIndex("desativado")));

            alunos.add(aluno);
        }
        cursor.close();
        return alunos;
    }

    public boolean delete(Aluno aluno) {
//        String sql = "DELETE FROM Alunos WHERE id = " + id;
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {aluno.getId()};
        Log.i(TAG, "delete: Entrou");
        try {
            if(aluno.estaDesativado()) {
                Log.i(TAG, "delete: Desativado");
                db.delete("Alunos", "id = ?", params);
            } else {
                aluno.desativa();
                Log.i(TAG, "delete: Set Desativado");
                Log.i(TAG, "delete: " + aluno.toString());
                update(aluno);
            }
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
        String[] params = {aluno.getId()};

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

    public void sincroniza(List<Aluno> alunos) {
        for (Aluno aluno :
                alunos) {

            aluno.sincroniza();

            if(existe(aluno)){
                if(aluno.estaDesativado()){
                    delete(aluno);
                } else {
                    update(aluno);
                }
            } else if(!aluno.estaDesativado()) {
                insert(aluno);
                Log.i("Aluno n existe, ativo", "sincroniza: ");
            }
        }
    }

    private boolean existe(Aluno aluno) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT id FROM Alunos WHERE id=? LIMIT 1";
        Cursor cursor = database.rawQuery(sql, new String[]{aluno.getId()});
        int count = cursor.getCount();
        cursor.close();
        close();

        return count > 0;
    }

    public List<Aluno> listaNaoSincronizados() {
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM Alunos WHERE sincronizado = 0";
        Cursor cursor = database.rawQuery(sql, null);
        List<Aluno> alunos = getAlunos(cursor);
        close();
        return alunos;
    }
}
