package edu.asu.mcgroup27.emotrack.ui.friendrequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FriendRequestViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FriendRequestViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}