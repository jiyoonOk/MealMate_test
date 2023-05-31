package com.anonymous.mealmate.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anonymous.mealmate.databinding.FragmentUserBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.User;
import com.anonymous.mealmate.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationView;

public class UserFragment extends Fragment {
    /**
     * class info :
     * Fragment class , MVVM 패턴 적용, data binding 사용, observer 사용
     * observer 를 사용 하여 data 변경 감지 -> adapter 에 갱신 신호를 보낸다.
     * view control 은 activity 의 observer 에서 trigger 사용 하여 일괄 처리
     *
     * how to coding  :
     * data handle logic 은 xml 에서 viewModel method 직접 호출 하므로
     * Fragment , Activity 에서는 View inflate Control logic 만 작성 ex) fragment, intent , dialog
     *
     * onCreateView method :
     *  activity activity 의 정보를 받아  ex) inflater, container, savedInstanceState
     *  View binding 을 처리 하여 return , activity 에 inflate
     *
     *  features :
     *  trigger class ControlViewState 사용
     *  activity 로 intent signal 전달
     *  navigation View 에 view State Control 하는 eventListener 작성
     *  -> ControlViewState active intent signal
     */
    private FragmentUserBinding binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * binding , viewModel setting part
         */
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding = FragmentUserBinding.inflate(inflater);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setUserViewModel(userViewModel);
        // todo 임의 로 설정한 user data 이후 viewModel class 에서 유저 정보 저장후 아래 코드 삭제
        binding.setUser(new User("서현승", User.GENDER_MALE, 178, 74, User.ACTIVITY_RATIO_HIGH, User.PURPOSE_DIET));

        /**
         *  navigation Event Listener , to Control View State ex) intent
         *  /todo 아이템 별 구분 하여 동작 하는 클릭 이벤트 로 구현 , 현재는 어떤 아이템 을 눌러도 똑같은 기능이 수행됨
         */
        binding.nvUserMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ControlViewState.getInstance().activeIntentSignal(ControlViewState.INTENT_MAIN_TO_USERUPDATEDATA);
                return false;
            }
        });

        /**
         *  return view that binding setting completed
         * */
        return binding.getRoot();
    }
}
