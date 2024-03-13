package com.xust.sims.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringJoiner;

@Service
public class PredictionServiceImpl implements PredictionService {

    @Override
    public String RandomForestClient(String[] data) throws Exception {
        StringJoiner sj = new StringJoiner(",", "[", "]");
        for (String s : data) {
            sj.add("\"" + s + "\"");
        }
        String jsonInputString = "{\"data\": " + sj.toString() + "}";

        HttpURLConnection con = getHttpURLConnection(jsonInputString);
        String prediction = "";

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            prediction = response.toString();
        }
        catch (Exception e) {
            prediction = "-100";
        }
        con.disconnect();
        return prediction;
    }

    private static HttpURLConnection getHttpURLConnection(String jsonInputString) throws IOException {
        URL url = new URL("http://localhost:5000/predict");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        catch (Exception e) {

        }
        return con;
    }
}
