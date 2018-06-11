package com.example.sys4.android1.converter;

import com.example.sys4.android1.modelo.Aluno;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by sys4 on 06/06/18.
 */

public class AlunoConverter {
    public String converteParaJson(List<Aluno> alunos) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(alunos);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(yaml);

        return json;
    }
    public String converteParaJson(Aluno aluno) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(aluno);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(yaml);

        return json;
    }
}
