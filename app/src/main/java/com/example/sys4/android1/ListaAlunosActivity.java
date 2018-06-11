package com.example.sys4.android1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sys4.android1.adapter.AlunosAdapter;
import com.example.sys4.android1.dao.AlunoDAO;
import com.example.sys4.android1.event.AtualizaListaAlunoEvent;
import com.example.sys4.android1.modelo.Aluno;
import com.example.sys4.android1.sync.AlunoSincronizador;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ListaAlunosActivity extends AppCompatActivity {

    private final AlunoSincronizador sincronizador = new AlunoSincronizador(this);
    private ListView listaAlunos;
    private SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);

        swipe = findViewById(R.id.swipe_lista_aluno);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sincronizador.buscaTodos();
            }
        });

        listaAlunos = findViewById(R.id.lista_alunos);
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int pos, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(pos);
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentVaiProFormulario.putExtra("aluno", aluno);
                startActivity(intentVaiProFormulario);
//                Toast.makeText(ListaAlunosActivity.this, aluno.getDesativado(), Toast.LENGTH_SHORT).show();
            }
        });

        Button novoAluno = findViewById(R.id.lista_alunos_novoAluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        registerForContextMenu(listaAlunos);
        sincronizador.buscaTodos();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void atualizaListaAlunoEvent(AtualizaListaAlunoEvent event){
        if(swipe.isRefreshing()) {
            swipe.setRefreshing(false);
        }
        listar();
    }

    @Override
    protected void onResume() {
        super.onResume();



        listar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_enviar_notas:
                new EnviaAlunosTask(this).execute();
                break;
            case R.id.menu_baixar_provas:
                Intent vaiParaProvas = new Intent(this, ProvasActivity.class);
                startActivity(vaiParaProvas);
                break;
            case R.id.menu_mapa:
                Intent vaiParaMapa = new Intent(this, MapaActivity.class);
                startActivity(vaiParaMapa);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void listar() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.findAll();
        for (Aluno aluno :
                alunos) {
            Log.i("Sincronizado", "listar: " + aluno.getSincronizado());
        }
//        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, R.layout.list_item, alunos);
        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem itemSite = menu.add("Visitar Site");
        String site = aluno.getSite();
        if(!site.startsWith("http://")) {
            site = "http://" + site;
        }
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);

        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemSMS.setIntent(intentSMS);

        MenuItem itemMapa = menu.add("Visualizar no mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);

        MenuItem itemLigar = menu.add("Ligar");
        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 123);
                } else {

                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }

                return false;
            }
        });



        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                if (dao.delete(aluno)) {
                    Toast.makeText(ListaAlunosActivity.this, aluno.getNome() + " deletado.", Toast.LENGTH_SHORT).show();
                    listar();
                } else {
                    Toast.makeText(ListaAlunosActivity.this, "Falha ao deletar aluno selecionado.", Toast.LENGTH_SHORT).show();
                }

                sincronizador.deleta(aluno);
                Log.i("OnDelete", "Aluno após delete: " + aluno.toString());
                return false;
            }

        });


    }

}
