package com.example.adila.webpagesourcecode;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Adila on 20-Oct-17.
 */

public class AsyncTaskWebPage extends AsyncTaskLoader<String> {
    String url, result;
    boolean cancel = false;

    public AsyncTaskWebPage(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    public String loadInBackground() {
        InputStream input = null;
        HttpURLConnection connection = null;

        try{
            URL site = new URL(url);

            connection = (HttpURLConnection) site.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(12000);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                input = connection.getInputStream();

                if (input!=null){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line+"\n");
                    }
                    return stringBuilder.toString();
                }
            }
            else {
                return "Error Response Code"+connection.getResponseCode();
            }
        } catch (Exception e){
            e.printStackTrace();
            return "UNKNOWN ERROR";
        }
        finally {
            if(input!=null){
                try {
                    input.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onStartLoading(){
        if (result!=null || cancel){
            deliverResult(result);
        }
        else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(String data){
        super.deliverResult(data);
        result = data;
    }

    @Override
    public void onCanceled(String data){
        super.onCanceled(data);
        cancel = true;
    }
}
