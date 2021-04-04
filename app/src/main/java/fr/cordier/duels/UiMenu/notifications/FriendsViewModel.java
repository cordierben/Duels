package fr.cordier.duels.UiMenu.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FriendsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FriendsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Friends");
    }

    public LiveData<String> getText() {
        return mText;
    }
}