package com.example.sys4.android1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sys4.android1.dao.AlunoDAO;
import com.example.sys4.android1.modelo.Aluno;
import com.example.sys4.android1.providers.GenericFileProvider;

import java.io.File;


public class FormularioActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 567;
    private FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);
        final Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");

        if(aluno != null) {
            helper.preencheFormulario(aluno);
        }

        Button botaoFoto = findViewById(R.id.formulario_botao_foto);
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                Uri photoURI = FileProvider.getUriForFile(FormularioActivity.this, FormularioActivity.this.getApplicationContext().getPackageName() + ".com.example.sys4.android1.providers.GenericFileProvider", arquivoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case CODIGO_CAMERA: {
//                    System.out.println();
                    helper.carregaImagem(caminhoFoto);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok: {

                Aluno aluno = helper.getAluno();
                AlunoDAO dao = new AlunoDAO(this);
                if(aluno.getId() != null) {
                    dao.update(aluno);
                } else {
                    dao.insert(aluno);
                }

                Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " salvo!", Toast.LENGTH_SHORT).show();


                finish();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
