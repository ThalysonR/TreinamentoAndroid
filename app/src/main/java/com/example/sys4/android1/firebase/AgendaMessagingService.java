package com.example.sys4.android1.firebase;

import android.util.Log;

import com.example.sys4.android1.dao.AlunoDAO;
import com.example.sys4.android1.dto.AlunoSync;
import com.example.sys4.android1.event.AtualizaListaAlunoEvent;
import com.example.sys4.android1.sync.AlunoSincronizador;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

/**
 * Created by sys4 on 11/06/18.
 */

public class AgendaMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> mensagem = remoteMessage.getData();
        Log.i("Mensagem recebida", mensagem.toString());

        converteParaAluno(mensagem);
    }

    private void converteParaAluno(Map<String, String> mensagem) {
        String chaveDeAcesso = "alunoSync";
        if(mensagem.containsKey(chaveDeAcesso)) {
            String json = mensagem.get(chaveDeAcesso);
            ObjectMapper mapper = new ObjectMapper();
            try {
                AlunoSync alunoSync = mapper.readValue(json, AlunoSync.class);
                new AlunoSincronizador(AgendaMessagingService.this).sincroniza(alunoSync);
                EventBus eventBus = EventBus.getDefault();
                eventBus.post(new AtualizaListaAlunoEvent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
