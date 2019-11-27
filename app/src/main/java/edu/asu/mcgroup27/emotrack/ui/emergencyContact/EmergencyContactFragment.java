package edu.asu.mcgroup27.emotrack.ui.emergencyContact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.asu.mcgroup27.emotrack.Adapters.EmergencyContactAdapter;
import edu.asu.mcgroup27.emotrack.Adapters.FriendRequestAdapter;
import edu.asu.mcgroup27.emotrack.R;
import edu.asu.mcgroup27.emotrack.database.FirebaseDBHelper;
import edu.asu.mcgroup27.emotrack.dialogs.EmergencyContactDialog;

public class EmergencyContactFragment extends Fragment {

    private Dialog register;
    private ArrayList<String> list;
    private DatabaseReference dbconref;
    private DatabaseReference dbfriendref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        EmergencyContactAdapter adapter = new EmergencyContactAdapter(getContext());
        final View root = inflater.inflate(R.layout.fragment_emergency_contact, container, false);
        this.list = new ArrayList<>();
        dbconref = FirebaseDBHelper.getUserEmergencyContact();
        dbfriendref = FirebaseDBHelper.getUserFriends();
        dbfriendref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.add(dataSnapshot.getValue().toString());
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
        FloatingActionButton fab = root.findViewById(R.id.emergencyContactAddButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register = onCreateDialog(savedInstanceState);
                register.show();
            }
        });
        ListView listview = root.findViewById(R.id.emergencyContactListView);
        listview.setAdapter(adapter);
        return root;
    }
    private Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select a Contact")
                .setItems(charSequences, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDBHelper.insertItem(dbconref, list.get(which));
                    }
                });
        return builder.create();
    }
}