package com.example.vescalendar;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Background extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        String result= null;
        String email = strings[0];
        String pass = strings[1];

        String connect = "http://192.168.0.106/login.php?email="+email+"&password="+pass;
        try {
            URL url = new URL(connect);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoInput(true);
            http.setDoOutput(true);


            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
            String line ="";
            while ((line = reader.readLine()) != null)
            {
                if(result==null){
                    result=line;
                }else{
                    result += line;
                }

            }
            reader.close();
            ips.close();
            http.disconnect();
            return result;

        } catch (MalformedURLException e) {
            result = "Not Connected";
        } catch (IOException e) {
            result = "Not Connected";
        }
        return result;

    }

}
