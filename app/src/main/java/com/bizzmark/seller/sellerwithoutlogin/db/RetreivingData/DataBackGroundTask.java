package com.bizzmark.seller.sellerwithoutlogin.db.RetreivingData;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bizzmark.seller.sellerwithoutlogin.sellerapp.EarnFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Tharun on 02-05-2017.
 */

public class DataBackGroundTask extends AsyncTask<Void, Void, String> {
    String json_url;
    String Json_string;
    String JSON_STRING;
    Context context;

    public DataBackGroundTask(Context context){
        this.context = context;
    }
    @Override
    protected void onPreExecute(){
        json_url = "";
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while ((Json_string = bufferedReader.readLine()) != null) {
                stringBuilder.append(Json_string + "\n");
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return stringBuilder.toString().trim();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.v("Json Response", result);
        JSON_STRING = result;
    }

    public void parseJson(View view){
        if (JSON_STRING == null){
            Toast.makeText(context, "List is Empty", Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(context, EarnFragment.class);
            intent.putExtra("json_data", JSON_STRING);
            context.startActivity(intent);
        }
    }
}
