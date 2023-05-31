package com.anonymous.mealmate.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.databinding.ActivityUserdataupdateBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.User;
import com.anonymous.mealmate.viewmodel.UserViewModel;

public class UserDataUpdateActivity extends AppCompatActivity {
    /**
     * class info :
     *  Activity class , MVVM 패턴 적용, data binding 사용, observer 사용
     *  observer 를 사용 하여 data 변경 감지 -> adapter 에 갱신 신호를 보낸다.
     *  view control 은 activity 의 observer 에서 trigger 사용 하여 일괄 처리
     *
     * how to coding  :
     *  data handle logic 은 xml 에서 viewModel method 직접 호출 하므로
     *  Fragment , Activity 에서는 View inflate Control logic 만 작성 ex) fragment, intent , dialog
     *
     * onCreate method :
     *  Android LifeCycle 에 의해 Control, System 에 의해 관리 된다.
     *
     * features:
     *  ControlViewState 에서 신호를 받아 observe
     *  observer 사용 하여 수신 받은 signal 에 따라 다른 기능을 수행 하는 logic 작성
     *  -> View State Handling
     *
     * Note :!!
     *  if DataBinding enabled
     *  Android Studio auto generated Binding classes  ex) ActivityMainBinding.class , FragmentFoodBinding.class
     *  binding class need to set LifeCycleOwner and XML variable instances
     *  -> activity must setting these things
     *
     *  how to receive view that binding class owned , to inflate
     *  -> binding.getRoot() : View type
     *  setContentView(binding.getRoot());
     */
    private ActivityUserdataupdateBinding binding;
    private UserViewModel userViewModel;
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * get ViewModel, set binding part
         */
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding = ActivityUserdataupdateBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        binding.setUserViewModel(userViewModel);
        setContentView(binding.getRoot());
        //test
        binding.setUser(new User("서현승",User.GENDER_MALE,178,74,User.ACTIVITY_RATIO_HIGH,User.PURPOSE_DIET));


        /**
         * View Flipper setting
         *
         * 1. add Event Listener to finish activity
         * 2. add Event Listener to changing viewFlipper's page
         */
        viewFlipper = binding.vfSurvey;
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewFlipper.showPrevious();
                finish();
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });

        /**
         * observer :
         *  1. Signal that ControlViewState send
         *
         *  2. get finish signal from ControlViewState class livedata
         *  -> finish Activity by finish signal
         */
        ControlViewState.getInstance().getStateSignalLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer signal) {
                switch (signal){
                    case ControlViewState.ACTIVITY_FINISH:finish();return;
                }
            }
        });


    }
}
