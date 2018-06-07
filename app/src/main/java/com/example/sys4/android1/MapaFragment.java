package com.example.sys4.android1;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.sys4.android1.dao.AlunoDAO;
import com.example.sys4.android1.modelo.Aluno;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by sys4 on 07/06/18.
 */

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private Localizador localizador;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng posicao = pegaCoordenadaDoEndereco("Rua Rodrigo Otavio, Manaus");
        if(posicao != null) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicao, 17);
            googleMap.moveCamera(update);
        }

        AlunoDAO dao = new AlunoDAO(getContext());
        for(Aluno aluno: dao.findAll()) {
            LatLng coordenada = pegaCoordenadaDoEndereco(aluno.getEndereco());
            if(coordenada != null) {
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(coordenada);
                marcador.title(aluno.getNome());
                marcador.snippet(aluno.getNota().toString());
                googleMap.addMarker(marcador);
            }
        }
        localizador = new Localizador(getContext(), googleMap, getActivity());
    }

    private LatLng pegaCoordenadaDoEndereco(String endereco) {
        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);
            if (!resultados.isEmpty()) {
                LatLng posicao = new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void atualizaLocalizacao() {
        localizador.atualizaLocalizacao();
    }
}
