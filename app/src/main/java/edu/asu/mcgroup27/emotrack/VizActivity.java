package edu.asu.mcgroup27.emotrack;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import edu.asu.mcgroup27.emotrack.database.FirebaseDBHelper;
import edu.asu.mcgroup27.emotrack.database.UserDBRefListener;
import edu.asu.mcgroup27.emotrack.messaging.SendMessageTask;

public class VizActivity extends AppCompatActivity {
    private final String TAG = "VizActivity";

    private BarChart chart;
    private String twitterID;
    private String currentUserEmail;
    private int count = 0;
    private int emotion_index = 0;
    private Iterator<String> iter;
    private double[] emotion = new double[10];
    public static final int[] EMOTION_COLORS = {
            Color.rgb(255, 0, 0),
            Color.rgb(0, 0, 255),
            Color.rgb(255, 255, 0),
            Color.rgb(255, 0, 255),
            Color.rgb(127, 0, 255),
            Color.rgb(0, 255, 255),
            Color.rgb(0, 255, 0),
            Color.rgb(128, 128, 128),
            Color.rgb(255, 128, 0),
            Color.rgb(0, 150, 136),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG, "onCreate");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.viz_layout);

        setTitle("Emotional Visualisation");

        chart = findViewById(R.id.chart1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        twitterID = getIntent().getStringExtra("twitterID");
        currentUserEmail = getIntent().getStringExtra("cur_user");

        DownloadDataTask downloadDataTask = new DownloadDataTask();
        downloadDataTask.execute(twitterID, "50");
    }

    private void setData(int count, double[] emotion) {
        Log.v(TAG, "setData");
        String[] emotionList = new String[]{"Anger", "Anticipation", "Disgust", "Fear", "Joy", "Negative", "Positive", "Sadness", "Surprise", "Trust"};
        ArrayList<BarEntry> values = new ArrayList<>();
        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            sum += emotion[i];
        }
        for (int i = 0; i < count; i++) {
            float val = (float) (emotion[i] / sum) * 100;
            values.add(new BarEntry(i, val, emotionList[i]));
        }

        BarDataSet set1;
        set1 = new BarDataSet(values, "Emotion Visualisation");
        set1.setDrawIcons(false);
        set1.setColors(EMOTION_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);

        chart.setData(data);
    }

    public void showVisualisation(double[] emotion) {
        chart = findViewById(R.id.chart1);

        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);

        chart.getAxisLeft().setDrawGridLines(false);

        chart.animateY(1500);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        setData(10, emotion);
    }

    public void sendNotification(View view) {
        FirebaseDBHelper.getUserEmergencyContact(currentUserEmail, new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        for (DataSnapshot emergencyContact : dataSnapshot.getChildren()) {
                            SendMessageTask.sendNotification(emergencyContact.getValue().toString(), "Attention for " + currentUserEmail, "Notification from " + user.getEmail());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public class DownloadDataTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject result = null;

            try {
                URL url = new URL("http://18.220.0.189:5000/getData");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                JSONObject body = new JSONObject();
                body.put("twitter", strings[0]);
                body.put("number", Integer.valueOf(strings[1]));

                DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                os.writeBytes(body.toString());
                os.flush();
                os.close();


                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    responseStreamReader.close();
                    String response = stringBuilder.toString();
                    result = new JSONObject(response);
                }
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v(TAG, "Result from cloud: " + result);

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Log.v(TAG, "onPostExecute");
            try {
                emotion_index = 0;
                Iterator<String> iter1 = jsonObject.keys();
                while (iter1.hasNext()) {
                    JSONObject emo_obj = jsonObject.getJSONObject(iter1.next());
                    iter = emo_obj.keys();
                    count = 0;
                    double dominance = 0.0;
                    while (iter.hasNext()) {
                        String key = iter.next();
                        dominance += emo_obj.getDouble(key);
                        count += 1;
                    }
                    dominance = dominance / count;
                    emotion[emotion_index] = dominance;
                    emotion_index += 1;

                    showVisualisation(emotion);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

