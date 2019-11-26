package edu.asu.mcgroup27.emotrack.ui.friendrequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.asu.mcgroup27.emotrack.R;
import edu.asu.mcgroup27.emotrack.database.FirebaseDBHelper;
import edu.asu.mcgroup27.emotrack.Adapters.FriendRequestAdapter;

public class FriendRequestFragment extends Fragment {

    private FriendRequestViewModel friendRequestViewModel;
    private ArrayList<String> list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DatabaseReference dbref = FirebaseDBHelper.getUserFriendReqs();
        list = new ArrayList<String>();
        final FriendRequestAdapter adapter = new FriendRequestAdapter(list, getContext());
        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.add(dataSnapshot.getValue().toString());
                adapter.notifyDataSetChanged();
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

        View root = inflater.inflate(R.layout.fragment_request, container, false);

        ListView listview = root.findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        return root;
    }
}