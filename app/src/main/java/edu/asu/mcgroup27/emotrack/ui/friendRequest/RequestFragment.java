package edu.asu.mcgroup27.emotrack.ui.friendRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.asu.mcgroup27.emotrack.R;

public class RequestFragment extends Fragment {
    private DatabaseReference dbref;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ArrayList<String> list = new ArrayList<String>();
        list.add("Friend1");
        list.add("Friend2");
        list.add("Friend3");
        list.add("Friend4");

        View root = inflater.inflate(R.layout.fragment_request, container, false);

        CustomAdapter adapter = new CustomAdapter(list, getContext());
        ListView listview = root.findViewById(R.id.list_view);
        listview.setAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
