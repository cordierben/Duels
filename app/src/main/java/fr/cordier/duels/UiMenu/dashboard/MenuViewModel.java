package fr.cordier.duels.UiMenu.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Menu");
    }

    public LiveData<String> getText() {
        return mText;
    }
}