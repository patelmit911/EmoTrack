package edu.asu.mcgroup27.emotrack.messaging;

import android.os.AsyncTask;

public class SendMessageTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... args) {
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
