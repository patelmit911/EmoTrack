package edu.asu.mcgroup27.emotrack.messaging;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;

public class SendMessageTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... args) {
        try {
            URL url = new URL("https://us-central1-emotrack-9e9b9.cloudfunctions.net/app/send");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("topic", args[0]);
            body.put("title", args[1]);
            body.put("body", args[2]);

            DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
            os.writeBytes(body.toString());
            os.flush();
            os.close();

            Log.i("NOTIFICATION", httpURLConnection.getResponseCode() + " " + httpURLConnection.getResponseMessage());
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://us-central1-emotrack-9e9b9.cloudfunctions.net/app/send");
        httppost.addHeader("Content-Type", "application/json");
        JSONObject body = new JSONObject();
        try {
            body.put("topic", args[0]);
            body.put("title", args[1]);
            body.put("body", args[2]);
            httppost.setEntity(new StringEntity(body.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return null;
    }
}
