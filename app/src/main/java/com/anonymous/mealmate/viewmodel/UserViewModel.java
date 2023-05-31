package com.anonymous.mealmate.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.User;
import com.anonymous.mealmate.model.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;

    private LiveData<List<User>> userLiveData;

    public UserViewModel (Application application) {
        super(application);
        repository = UserRepository.getInstance(application);

//        userLiveData = repository.
    }

    public void insert(User user) { repository.insertUser(user); }

    public void update(User user) { repository.updateUser(user); }

    public void delete(User user) { repository.deleteUser(user); }

    public void onUpdateUserData(User user){
       // if()
        //repository.updateUser(user);
        ControlViewState.getInstance().activeIntentSignal(ControlViewState.ACTIVITY_FINISH);
    }
}
