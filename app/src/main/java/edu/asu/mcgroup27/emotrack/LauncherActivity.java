package edu.asu.mcgroup27.emotrack;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.mcgroup27.emotrack.ui.auth.ui.login.LoginActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        Intent start;
        if(currentUser == null) {
            start = new Intent(this, LoginActivity.class);
        }
        else {
            start = new Intent(this, MainActivity.class);
        }
        startActivity(start);
        LauncherActivity.this.finish();
    }
}
