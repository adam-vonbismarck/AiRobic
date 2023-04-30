package edu.brown.cs.student.main.database;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class NonSusDatabaseCommands {

    private final String DATABASE = "https://cs32airobic-default-rtdb.firebaseio.com/";
    private final String END = ".json";

    public void put (String data, String where) throws IOException {
        URL url = new URL(this.DATABASE + where + this.END);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String response = br.lines().findFirst().orElse("");
            System.out.println(response);
        }
    }

    public void update (String data, String where) throws IOException {
        URL url = new URL(this.DATABASE + where + this.END);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PATCH");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = data.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String response = br.lines().findFirst().orElse("");
            System.out.println(response);
        }
    }

    public void delete (String where) throws IOException {
        URL url = new URL(this.DATABASE + where + this.END);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        int status = con.getResponseCode();
        System.out.println("Response Code: " + status);
    }

    public String get (String where) throws IOException {
        URL url = new URL(this.DATABASE + where + this.END);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String response = br.lines().findFirst().orElse("");
            System.out.println(response);
            return response;
        }
    }
}
