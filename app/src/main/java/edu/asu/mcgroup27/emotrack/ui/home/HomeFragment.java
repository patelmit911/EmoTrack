package edu.asu.mcgroup27.emotrack.ui.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.ArrayList;

import edu.asu.mcgroup27.emotrack.MainActivity;
import edu.asu.mcgroup27.emotrack.R;
import edu.asu.mcgroup27.emotrack.database.FirebaseDBHelper;
import edu.asu.mcgroup27.emotrack.database.UserDBRefListener;
import edu.asu.mcgroup27.emotrack.database.UserMetaData;
import edu.asu.mcgroup27.emotrack.database.UserMetaDataListener;
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
    private Dialog register;
    public static ArrayList<DisplayContent> friendlist = new ArrayList<DisplayContent>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        savedBundle = savedInstanceState;

        friendlist = new ArrayList<DisplayContent>();
        getFriendList();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.v(TAG, "<Suprateem>onResume");

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register = onCreateDialog();
                register.show();

            }
        });

        friendListView = getActivity().findViewById(R.id.friendListView);
        friendAdapter = new CustomAdapter(this, friendlist);

        friendListView.setAdapter(friendAdapter);
    }

    public void sendFriendRequest() {
        EditText et = register.findViewById(R.id.username);
        uName = et.getText().toString();
        FirebaseDBHelper.getUserFriendReqs(uName, new UserDBRefListener() {
            @Override
            public void onObtained(DatabaseReference databaseReference) {
                FirebaseDBHelper.insertItem(databaseReference, FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }
        });
    }

    //public Dialog onCreateDialog(Bundle savedInstanceState) {

    public Dialog onCreateDialog() {

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
                        //saveUserName();
                        sendFriendRequest();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public void getFriendList() {
        DatabaseReference fr = FirebaseDBHelper.getUserFriends();
        fr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String friend = dataSnapshot.getValue().toString(); // getEmail
                FirebaseDBHelper.getUserMetaData(friend, new UserMetaDataListener() {
                    @Override
                    public void onObtained(UserMetaData userMetaData) {
                        Log.v(TAG, "<Suprateem>friends email: "+ userMetaData.getEmail());
                        Log.v(TAG, "<Suprateem>friends photo URI: " + userMetaData.getPhotoURI());
                        DisplayContent obj = new DisplayContent();
                        obj.setName(userMetaData.getDisplayName());
                        obj.setId(userMetaData.getTwitterID());
                        Uri imageUri = Uri.parse(userMetaData.getPhotoURI());
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                            obj.setThmb(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //userMetaData.
                        friendlist.add(obj);
                        if(friendAdapter != null)
                            friendAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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