package com.example.sys4.android1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sys4.android1.ListaAlunosActivity;
import com.example.sys4.android1.R;
import com.example.sys4.android1.modelo.Aluno;

import java.util.List;

/**
 * Created by sys4 on 06/06/18.
 */

public class AlunosAdapter extends BaseAdapter {
    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) {
        this.alunos = alunos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return alunos.size();
    }

    @Override
    public Object getItem(int i) {
        return alunos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Aluno aluno = alunos.get(i);
        LayoutInflater inflater = LayoutInflater.from(context);
        View listView = view;
        if(listView == null) {
            listView = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        TextView campoNome = listView.findViewById(R.id.item_nome);
        TextView campoTelefone = listView.findViewById(R.id.item_telefone);
        ImageView campoFoto = listView.findViewById(R.id.item_foto);
        TextView campoEndereco = listView.findViewById(R.id.item_endereco);
        TextView campoSite = listView.findViewById(R.id.item_site);

        if(campoSite != null) {
            campoSite.setText(aluno.getSite());
        }
        if(campoEndereco != null) {
            campoEndereco.setText(aluno.getEndereco());
        }
        campoNome.setText(aluno.getNome());
        campoTelefone.setText(aluno.getTelefone());

        if(aluno.getCaminhoFoto() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(aluno.getCaminhoFoto());
            if(bitmap != null) {
                Bitmap bitmapreduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                campoFoto.setImageBitmap(bitmapreduzido);
                campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        return listView;
    }
}
