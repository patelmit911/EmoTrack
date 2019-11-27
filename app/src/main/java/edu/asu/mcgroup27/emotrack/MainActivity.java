package edu.asu.mcgroup27.emotrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import edu.asu.mcgroup27.emotrack.database.FirebaseDBHelper;
import edu.asu.mcgroup27.emotrack.database.UserMetaData;
import edu.asu.mcgroup27.emotrack.database.UserMetaDataListener;
import edu.asu.mcgroup27.emotrack.messaging.SendMessageTask;
import edu.asu.mcgroup27.emotrack.ui.DisplayContent;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private ImageView profileImageView;

    public static ArrayList<DisplayContent> friendlist = new ArrayList<DisplayContent>();
    Thread friendThread;

    private final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateUserMetaData();


        /*FloatingActionButton fab = findViewById(R.id.addFriendFloatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending Friend Request", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                FirebaseDBHelper.getUserFriendReqs().push().setValue("TEST");
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View navHeaderView = navigationView.getHeaderView(0);
        profileNameTextView = navHeaderView.findViewById(R.id.profileNameTextView);
        profileEmailTextView = navHeaderView.findViewById(R.id.profileEmailTextView);
        profileImageView = navHeaderView.findViewById(R.id.profileImageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            Uri imageUri = user.getPhotoUrl();
            if(imageUri != null) {
                int imageResolution = 256;
                String path = imageUri.getPath();
                path = "https://lh5.googleusercontent.com"
                        + path;
                path = path.replace("s96-c", "s" + imageResolution + "-c");

                profileNameTextView.setText(user.getDisplayName());
                profileEmailTextView.setText(user.getEmail());
                Glide.with(getApplicationContext())
                        .load(path)
                        .centerCrop()
                        .crossFade()
                        .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImageView);
                /*Glide.with(getApplicationContext())
                        .load("")
                        .placeholder(R.drawable.navback)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(navbg);*/

            }
        }

//        SendMessageTask sendMessageTask = new SendMessageTask();
//        sendMessageTask.execute("weather", "HELLO", "FROM app");

        Log.v(TAG, "<Suprateem>biometric setting: " + Util.getBiometric(getApplicationContext()));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        friendThread = new Thread(new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                Log.i("thread", "run");
                //video_start = true;
                friendlist = new ArrayList<DisplayContent>();

                // code to update friendlist goes here
                /*video_cursor = managedQuery(videoSrc, null, null, null, null);

                if (video_cursor.moveToFirst()) {
                    do {
                        DisplayContent obj = new DisplayContent();
                        id = video_cursor.getInt(video_cursor
                                .getColumnIndex("_id"));
                        name = video_cursor.getString(video_cursor
                                .getColumnIndex("title"));
                        duration = video_cursor.getString(video_cursor
                                .getColumnIndex("duration"));
                        path = video_cursor.getString(video_cursor
                                .getColumnIndex("_data"));
                        duration = calcDuration(duration);

                        obj.setId(id);
                        obj.setName(name);
                        obj.setDuration(duration);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 1;
                        obj.setThmb(MediaStore.Video.Thumbnails
                                .getThumbnail(getContentResolver(), id,
                                        MediaStore.Video.Thumbnails.MICRO_KIND,
                                        options));
                        obj.setMediaUri(path);

                        videolist.add(obj);

                    } while (video_cursor.moveToNext());
                }*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logoff:
                // add code for signoff
                AuthUI.getInstance()
                        .signOut(getApplicationContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Intent intent = new Intent(MainActivity.this, LauncherActivity.class);
                                startActivity(intent);
                                MainActivity.this.finish();
                            }
                        });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void updateUserMetaData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDBHelper.getUserMetaDataRef();
        ref.child("displayName").setValue(user.getDisplayName());
        ref.child("email").setValue(user.getEmail());
        ref.child("phone").setValue(user.getPhoneNumber());
        ref.child("photoURI").setValue(user.getPhotoUrl().toString());
    }
}
