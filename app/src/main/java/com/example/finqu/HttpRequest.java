package com.example.finqu;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequest extends AsyncTask<Void, Void, String> {
    String url;
    String method;
    HttpsURLConnection connection;

    public HttpRequest(String urlParam, String methodParam) {
        this.url = urlParam;
        this.method = methodParam;
        try {
            this.connection = (HttpsURLConnection) new URL(urlParam).openConnection();
            this.connection.setRequestMethod(methodParam);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRequestProperty(String key, String value) {
        this.connection.setRequestProperty(key, value);
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder result = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while(true) {
                String line = reader.readLine();

                if(line == null) {
                    break;
                }

                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
