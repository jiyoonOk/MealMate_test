package com.anonymous.mealmate.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.anonymous.mealmate.databinding.ActivitySetmealitemBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.view.adapter.MealItemAdapter;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import java.util.List;

public class SetMealItemActivity extends AppCompatActivity {
    /**
     * class info :
     *  Activity class , MVVM 패턴 적용, data binding 사용, observer 사용
     *  observer 를 사용 하여 data 변경 감지 -> adapter 에 갱신 신호를 보낸다.
     *  view control 은 activity 의 observer 에서 trigger 사용 하여 일괄 처리
     *
     * how to coding  :
     *  data handle logic 은 xml 에서 viewModel method 직접 호출 하므로
     *  Fragment , Activity 에서는 View inflate Control logic 만 작성
     *  ex) fragment, intent , dialog
     *
     * onCreate method :
     *  Android LifeCycle 에 의해 Control, System 에 의해 관리 된다.
     *
     * features:
     *  1. ControlViewState 에서 신호를 받아 observe
     *  observer 사용 하여 수신 받은 signal 에 따라 다른 기능을 수행 하는 logic 작성 ex) intent, dialog, fragment
     *  -> View State Handling
     *
     *  2. ViewModel 의 liveData 를 observe
     *  -> xml 의 data update 처리
     *
     *  3. Singleton 으로 구현한 Date 객체를 호출 하여 설정 된 날짜를 모든 view 와 class 에서 동기화
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
     *
     */
    private ActivitySetmealitemBinding binding;
    private MealSetViewModel mealSetViewModel;

    private RecyclerView mRecyclerView;
    private MealItemAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * get ViewModel, set binding part
         */
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);
        binding = ActivitySetmealitemBinding.inflate(LayoutInflater.from(this));
        binding.setMealSetViewModel(mealSetViewModel);
        binding.setDate(Date.getInstance());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        /**
         * recyclerView , adapter setting
         */
        mAdapter = new MealItemAdapter(this);
        mRecyclerView = binding.rvMealSetting;
        mRecyclerView.setAdapter(mAdapter);

        /**
         * ViewModel Data observer
         * submit list to adapter , view data update and show
         * Todo Food Activity 에서 ViewModel 에 liveData 를 update 해도 SetMealActivity 는 백스텍 에 있다 돌아 오기 때문에 observer 가 작동 하지 않는 문제가 있음
         */
        mealSetViewModel.getMealsLiveData().observe(this, new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                mAdapter.submitList(meals);
                Toast.makeText(SetMealItemActivity.this,"food callback",Toast.LENGTH_SHORT).show();
            }
        });
        //event
        binding.btnMealBack.setOnClickListener((v) ->{
            finish();
        });


        /**
         * observer :
         *  1. Signal that ControlViewState send
         *
         *  2. get intent signal from ControlViewState class livedata
         *  -> active intent another activity by intent signal
         *
         *  3. get finish signal from ControlViewState class livedata
         *  -> finish Activity by finish signal
         */
        //intent state observer
        ControlViewState.getInstance().getStateSignalLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer signal) {
                switch(signal){
                    case ControlViewState.INTENT_SETMEAL_TO_FOOD :
                        startActivity(new Intent(SetMealItemActivity.this,FoodActivity.class));
                        break;
                    case ControlViewState.ADAPTER_DATA_CHANGED: // testing

                    case ControlViewState.ACTIVITY_FINISH:
                        Toast.makeText(SetMealItemActivity.this,"food callback",Toast.LENGTH_SHORT).show();

                        break;

                }
            }
        });
    }
}
