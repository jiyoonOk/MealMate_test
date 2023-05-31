package com.anonymous.mealmate.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.databinding.FragmentFoodBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.view.adapter.FoodAdapter;
import com.anonymous.mealmate.viewmodel.FoodViewModel;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import java.util.List;

public class FoodActivity extends AppCompatActivity {
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
     *  Android LifeCycle 에 의해 Control 호출, System 에 의해 관리 된다.
     *
     * features:
     *  1. ControlViewState 에서 신호를 받아 observe
     *      observer 사용 하여 수신 받은 signal 에 따라 다른 기능을 수행 하는 logic 작성
     *  -> View State Handling
     *
     *  2. 두가지 의  observer 가 하나의 adapter 에 변경 신호 {ex) submitList} 전송중
     *    -> 같은 뷰에 성격이 다른 { ex) data from db , data form api } data 전송중
     *
     * Note :!!
     *  if DataBinding enabled
     *  Android Studio auto generated Binding classes  ex) ActivityMainBinding.class , FragmentFoodBinding.class
     *  binding class need to set LifeCycleOwner and XML variable instances ex) ViewModel
     *  -> activity must setting these things
     *
     *  how to receive view that binding class owned , to inflate
     *  -> binding.getRoot() : View type
     *  setContentView(binding.getRoot());
     */

    private FragmentFoodBinding binding;
    private FoodViewModel foodViewModel;

    private MealSetViewModel mealSetViewModel;
    private RecyclerView mRecyclerView;
    private FoodAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         *  ViewModel connecting, binding setup part
         */
        mealSetViewModel = new ViewModelProvider(this).get(MealSetViewModel.class);
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        binding = FragmentFoodBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        binding.setFoodViewModel(foodViewModel);
        setContentView(binding.getRoot());


        /**
         * recyclerView , adapter setting part
         */
        mRecyclerView = binding.rvFoodSearch;
        mAdapter = new FoodAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        /**
         *  two different observer update same adapter
         *  두개의 observer 가 같은 adapter 를 update
         *  -> the view can show information that came from another places ex) db, api
         */
        foodViewModel.getSearchResults().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                mAdapter.setFoods(foods);
                Log.d(getClass().getName(), "onChanged: APIAccessComplete!! ");
            }
        });
        foodViewModel.getAllFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                mAdapter.submitList(foods);
                Log.d(getClass().getName(), "onChanged: DBAccessComplete!!");
            }
        });


        /**
         * Signal that ControlViewState send
         * get activity state signal from ControlViewState class livedata
         * finish activity by finish signal
         */
        ControlViewState.getInstance().getStateSignalLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer signal) {
                switch (signal){
                    case ControlViewState.ACTIVITY_FINISH:
                        ControlViewState.getInstance().deactivateSignal();
                        finish();
                        break;
                }
            }
        });
    }
}
