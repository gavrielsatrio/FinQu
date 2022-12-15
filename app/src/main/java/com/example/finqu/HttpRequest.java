package com.example.finqu;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequest extends AsyncTask<Void, Void, String> {
    String url;
    String method;
    HttpsURLConnection connection;
    String body = "";

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

    public HttpRequest setRequestProperty(String key, String value) {
        this.connection.setRequestProperty(key, value);

        return this;
    }

    public HttpRequest setBody(String value) {
        this.body = value;

        return this;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(!body.equals("")) {
            try {
                DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
                writer.writeBytes(body);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
