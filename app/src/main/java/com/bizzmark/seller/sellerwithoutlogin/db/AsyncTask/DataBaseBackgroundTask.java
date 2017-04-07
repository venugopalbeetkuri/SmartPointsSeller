package com.bizzmark.seller.sellerwithoutlogin.db.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by Tharun on 24-03-2017.
 */

public class DataBaseBackgroundTask extends AsyncTask<String,Void,String> {

    String add_points_url;
    Context context;

    public DataBaseBackgroundTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        add_points_url="https://wwwbizzmarkin.000webhostapp.com/add_store_points.php";
    }

    @Override
    protected String doInBackground(String... args) {

        String deviceid,storename,billamount,points,type,time;

        deviceid=args[0];
        storename=args[1];
        billamount=args[2];
        points=args[3];
        type=args[4];
        time=args[5];


        try {

            URL url=new URL(add_points_url);

            JSONObject obj=new JSONObject();
            obj.put("deviceid",deviceid);
            obj.put("storename",storename);
            obj.put("billamount",billamount);
            obj.put("points",points);
            obj.put("type",type);
            obj.put("time",time);
            Log.e("data",obj.toString());


            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setDoInput(true);


            OutputStream outputStream=httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            bufferedWriter.write(getPostDataString(obj));

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream=httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();

            return "One Record Entered Successfully";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
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
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
    }

    public String getPostDataString(JSONObject params)throws Exception{

        StringBuilder result=new StringBuilder();
        boolean first=true;

        Iterator<String> itr=params.keys();

        while (itr.hasNext()){

            String key=itr.next();

            Object value=params.get(key);

            if (first)
                first=false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key,"UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(),"UTF-8"));
        }

        return result.toString();
    }
}
