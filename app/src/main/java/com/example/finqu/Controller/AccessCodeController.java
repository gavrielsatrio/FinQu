package com.example.finqu.Controller;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AccessCodeController {
    public static void setCode(String code, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("accesscode.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(code);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCode(Context context) {
        try {
            InputStream inputStream = context.openFileInput("accesscode.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String accessCode = bufferedReader.readLine();

                inputStream.close();

                return accessCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
