package edu.asu.mcgroup27.emotrack.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.asu.mcgroup27.emotrack.MainActivity;
import edu.asu.mcgroup27.emotrack.R;
import edu.asu.mcgroup27.emotrack.VizActivity;
import edu.asu.mcgroup27.emotrack.database.FirebaseDBHelper;
import edu.asu.mcgroup27.emotrack.database.UserDBRefListener;
import edu.asu.mcgroup27.emotrack.database.UserMetaData;
import edu.asu.mcgroup27.emotrack.database.UserMetaDataListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class HomeListAdapter extends BaseAdapter implements ListAdapter {
    private final String TAG = "HomeListAdapter";

    private ArrayList<String> list;
    private Context context;
    private DatabaseReference dbreqref;
    private DatabaseReference dbfriendref;

    public HomeListAdapter(Context context) {
        this.list = new ArrayList<>();
        this.context = context;
        this.dbfriendref = FirebaseDBHelper.getUserFriends();
        dbfriendref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.add(dataSnapshot.getValue().toString());
                notifyDataSetChanged();
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

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_home_friend_list_custom, null);
        }

        final TextView listItemText = view.findViewById(R.id.friendListName);
        listItemText.setText(list.get(position));

        final ImageView imageView = view.findViewById(R.id.friendListImage);
        FirebaseDBHelper.getUserMetaData(list.get(position), new UserMetaDataListener() {
            @Override
            public void onObtained(UserMetaData userMetaData) {
                String image = userMetaData.getPhotoURI();
                if (image != null) {
                    Glide.with(context)
                            .load(image)
                            .centerCrop()
                            .crossFade()
                            .bitmapTransform(new CropCircleTransformation(context))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                }
                String displayName = userMetaData.getDisplayName();
                if (displayName != null){
                    listItemText.setText(displayName);
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestFrom = list.get(position);
                FirebaseDBHelper.getUserMetaData(requestFrom, new UserMetaDataListener() {
                    @Override
                    public void onObtained(UserMetaData userMetaData) {
                        String twitterID = userMetaData.getTwitterID();
                        String cur_user = userMetaData.getEmail();
                        Log.v(TAG, "<Suprateem>twitterID: " + twitterID);

                        Intent intent = new Intent(context, VizActivity.class);
                        intent.putExtra("twitterID", twitterID);
                        intent.putExtra("cur_user", cur_user);
                        context.startActivity(intent);
                    }
                });

            }
        });

        return view;
    }
}


