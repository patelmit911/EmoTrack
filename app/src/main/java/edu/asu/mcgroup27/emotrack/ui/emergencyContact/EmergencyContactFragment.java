package edu.asu.mcgroup27.emotrack.ui.emergencyContact;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.asu.mcgroup27.emotrack.Adapters.EmergencyContactAdapter;
import edu.asu.mcgroup27.emotrack.Adapters.FriendRequestAdapter;
import edu.asu.mcgroup27.emotrack.R;
import edu.asu.mcgroup27.emotrack.database.FirebaseDBHelper;
import edu.asu.mcgroup27.emotrack.dialogs.EmergencyContactDialog;

public class EmergencyContactFragment extends Fragment {

    private EmergencyContactViewModel emergencyContactViewModel;
    EmergencyContactDialog register;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        final EmergencyContactAdapter adapter = new EmergencyContactAdapter(getContext());
        View root = inflater.inflate(R.layout.fragment_emergency_contact, container, false);
        FloatingActionButton fab = root.findViewById(R.id.emergencyContactAddButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register.onCreateDialog(savedInstanceState);
//                FirebaseDBHelper.getUserFriendReqs().push().setValue("TEST");
            }
        });
        ListView listview = root.findViewById(R.id.emergencyContactListView);
        listview.setAdapter(adapter);
        return root;
    }
}