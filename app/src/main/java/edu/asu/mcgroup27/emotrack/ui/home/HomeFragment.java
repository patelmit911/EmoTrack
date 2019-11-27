package edu.asu.mcgroup27.emotrack.ui.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import edu.asu.mcgroup27.emotrack.R;
import edu.asu.mcgroup27.emotrack.database.FirebaseDBHelper;
import edu.asu.mcgroup27.emotrack.ui.CustomAdapter;
import edu.asu.mcgroup27.emotrack.ui.DisplayContent;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;
    private Bundle savedBundle;
    private String uName = "";
    private CustomAdapter friendAdapter;
    static ListView friendListView;
    static ArrayList<DisplayContent> friendlist = new ArrayList<DisplayContent>();
    Dialog register;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        savedBundle = savedInstanceState;
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register = onCreateDialog(savedBundle);
                register.show();
            }
        });

        friendListView = getActivity().findViewById(R.id.videolistView);
        friendAdapter = new CustomAdapter(this, friendlist);
    }

    public void saveUserName() {
        EditText et = register.findViewById(R.id.username);
        uName = et.getText().toString();
        Log.v(TAG, "<Suprateem>saveUserName: " + uName);

        try {
            fetchTwitterInfo(uName);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                // Add action buttons
                .setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saveUserName();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public void fetchTwitterInfo(String userHandle) throws TwitterException {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ConfigurationBuilder cb = new ConfigurationBuilder();
                    cb.setDebugEnabled(true)
                            .setOAuthConsumerKey(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY))
                            .setOAuthConsumerSecret(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET))
                            .setOAuthAccessToken(getResources().getString(R.string.com_twitter_sdk_android_ACCESS_TOKEN))
                            .setOAuthAccessTokenSecret(getResources().getString(R.string.com_twitter_sdk_android_ACCESS_TOKEN_SECRET));
                    TwitterFactory tf = new TwitterFactory(cb.build());
                    twitter4j.Twitter twitter = tf.getInstance();

                    User user = twitter.showUser("sbhatt4g");
                    String profileImage = user.getProfileImageURL();
                    Log.v(TAG, "<Suprateem>fetchTwitterInfo: " + profileImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}