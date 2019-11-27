package edu.asu.mcgroup27.emotrack.ui.emergencyContact;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmergencyContactViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EmergencyContactViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}