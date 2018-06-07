package com.example.sys4.android1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sys4.android1.modelo.Prova;

public class DetalhesProvaFragment extends Fragment {

    private TextView campoMateria;
    private TextView campoData;
    private ListView campoTopicos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalhes_prova, container, false);

        campoMateria = view.findViewById(R.id.detalhes_prova_materia);
        campoData = view.findViewById(R.id.detalhes_prova_data);
        campoTopicos = view.findViewById(R.id.detalhes_prova_topicos);

        Bundle parametros = getArguments();
        if (parametros != null) {
            Prova prova = (Prova) parametros.getSerializable("prova");
            populaCamposCom(prova);
        }

        return view;
    }

    public void populaCamposCom(Prova prova) {
        campoMateria.setText(prova.getMateria());
        campoData.setText(prova.getData());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, prova.getTopicos());
        campoTopicos.setAdapter(adapter);
    }

}
