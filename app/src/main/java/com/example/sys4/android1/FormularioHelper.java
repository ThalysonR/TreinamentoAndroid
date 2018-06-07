package com.example.sys4.android1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.sys4.android1.modelo.Aluno;

/**
 * Created by sys4 on 01/06/18.
 */

public class FormularioHelper {

    private Aluno aluno;
    EditText campoNome;
    EditText campoEndereco;
    EditText campoTelefone;
    EditText campoSite;
    ImageView campoFoto;
    RatingBar campoNota;

    public FormularioHelper(FormularioActivity activity) {
        campoNome = activity.findViewById(R.id.formulario_nome);
        campoEndereco = activity.findViewById(R.id.formulario_endereco);
        campoTelefone = activity.findViewById(R.id.formulario_telefone);
        campoSite = activity.findViewById(R.id.formulario_site);
        campoFoto = activity.findViewById(R.id.formulario_foto);
        campoNota = activity.findViewById(R.id.formulario_nota);
        this.aluno = new Aluno();
    }

    public Aluno getAluno() {
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota((double) campoNota.getProgress());
        aluno.setCaminhoFoto((String) campoFoto.getTag());
//        aluno.toString();

        return aluno;
    }

    public void preencheFormulario(Aluno aluno) {
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setProgress(aluno.getNota().intValue());
        carregaImagem(aluno.getCaminhoFoto());

        this.aluno = aluno;
    }

    public void carregaImagem(String caminhoFoto) {
        if(caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapreduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(bitmapreduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);
        }
    }
}
