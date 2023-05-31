package com.anonymous.mealmate.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import com.anonymous.mealmate.R;

import com.anonymous.mealmate.databinding.ActivityMainBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.view.fragment.CalendarFragment;
import com.anonymous.mealmate.view.fragment.FoodFragment;
import com.anonymous.mealmate.view.fragment.HomeFragment;
import com.anonymous.mealmate.view.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
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
     *  Main Activity 로 모든 activity
     *  가장 아래 activity frame, ControlViewState 를 통한 signal ex) intent
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

    private ActivityMainBinding binding;


    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment;
    private CalendarFragment calendarFragment;
    private FoodFragment foodFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         *   binding setup part
         */
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        binding.setLifecycleOwner(this);
        binding.setSelectedDate(Date.getInstance());
        setContentView(binding.getRoot());

        /**
         * fragment connecting part
         */
        homeFragment = new HomeFragment();
        calendarFragment = new CalendarFragment();
        foodFragment = new FoodFragment();
        userFragment = new UserFragment();
        // 초기 실행 프래그먼트, 홈화면
        fragmentManager.beginTransaction().replace(R.id.fl_navigation, homeFragment).commit();

        /**
         *  BottomNavigation View EventListener
         *  fragment replace logic  coded
         */
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_navigation_home:
                        fragmentManager.beginTransaction().replace(R.id.fl_navigation, homeFragment).commit();
                        Date.getInstance().setToDayDateTime();
                        return true;
                    case R.id.item_navigation_calendar:
                        fragmentManager.beginTransaction().replace(R.id.fl_navigation, calendarFragment).commit();
                        return true;
                    case R.id.item_navigation_food:
                        fragmentManager.beginTransaction().replace(R.id.fl_navigation, foodFragment).commit();
                        return true;
                    case R.id.item_navigation_user:
                        fragmentManager.beginTransaction().replace(R.id.fl_navigation, userFragment).commit();
                        return true;

                    default:
                        return false;
                }
            }
        });
        //todo  배경화면 null 처리 , 이후 xml 에서 코드 작성 필요
        binding.bottomNavigationView.setItemIconTintList(null);

        /**
         * ControlViewState active intent signal
         * 데이터 전달 로직이 아니 므로 간단 하게  event listener 작성 하여 view state handling
         *
         * 플로팅 버튼 눌러 SetMealActivity intent
         */
        binding.fabMealModify.setOnClickListener((v) -> {
            ControlViewState.getInstance().activeIntentSignal(ControlViewState.INTENT_MAIN_TO_SETMEAL);
        });

        /**
         * Signal that ControlViewState send
         * get intent signal from ControlViewState class livedata
         * active intent another activity by intent signal
         */
        ControlViewState.getInstance().getStateSignalLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer intentSignal) {
                Intent intent;
                switch (intentSignal) {
                    case ControlViewState.INTENT_MAIN_TO_SETMEAL:
                        intent = new Intent(MainActivity.this, SetMealItemActivity.class);
                        startActivity(intent);
                        break;
                    case ControlViewState.INTENT_MAIN_TO_USERUPDATEDATA:
                        intent = new Intent(MainActivity.this, UserDataUpdateActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        return;
                }

            }
        });
        /**
         * Signal that ControlViewState send
         * get dialog signal from ControlViewState class livedata
         * active dialog on this activity by dialog signal
         */
        ControlViewState.getInstance().getStateSignalLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer dialogSignal) {
                switch (dialogSignal) {
                    //Todo dialog 작성하여 연결
                    case ControlViewState.DIALOG_FOOD_DATASET:
                }

            }
        });

    }
}
