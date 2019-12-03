package edu.asu.mcgroup27.emotrack.messaging;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import edu.asu.mcgroup27.emotrack.database.FirebaseDB;
import okhttp3.OkHttpClient;

public class SendMessageTask extends AsyncTask<String, Void, Void> {

    public static void sendNotification(String email, final String title, final String message) {
        DatabaseReference userList = FirebaseDB.getInstance().getReference("userlist");
        userList.orderByValue().equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    SendMessageTask sendMessageTask = new SendMessageTask();
                    sendMessageTask.execute(snapshot.getKey(), title, message);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postNotification(String topic, String title, String message) {
        try {
            URL url = new URL("https://us-central1-emotrack-9e9b9.cloudfunctions.net/app/send");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put("topic", topic);
            body.put("title", title);
            body.put("body", message);

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
    }

    @Override
    protected Void doInBackground(String... args) {

        String uid = args[0];
        String title = args[1];
        String message = args[2];


        postNotification(uid, title, message);



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
