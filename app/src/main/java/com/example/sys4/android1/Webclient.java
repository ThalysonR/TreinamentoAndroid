package com.example.sys4.android1;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by sys4 on 06/06/18.
 */

public class Webclient {
    public String post(String json) throws IOException {
        String endereco = "https://www.caelum.com.br/mobile/";
        return realizaConexao(json, endereco);
    }

    public String realizaConexao(String json, String endereco) throws IOException {
        URL url = new URL(endereco);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        PrintStream stream = new PrintStream(connection.getOutputStream());
        stream.print(json);

        connection.connect();
        Scanner scanner = new Scanner(connection.getInputStream());
        String resposta = scanner.next();

        return resposta;
    }

    public String insere(String json) throws IOException {
        String endereco = "http://10.0.2.2:8080/api/aluno";
        return realizaConexao(json, endereco);
    }
}
