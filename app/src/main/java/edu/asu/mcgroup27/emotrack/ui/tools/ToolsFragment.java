package edu.asu.mcgroup27.emotrack.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import edu.asu.mcgroup27.emotrack.R;
import edu.asu.mcgroup27.emotrack.Util;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        final Switch bioSetting = root.findViewById(R.id.biometric_setting);
        bioSetting.setChecked(Util.getBiometric(getContext()) == 1 ? true:false);
        bioSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Util.setBiometric(getContext(), 1);
                } else {
                    Util.setBiometric(getContext(), 0);
                }
            }
        });

        return root;
    }
}