package com.anonymous.mealmate.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.anonymous.mealmate.databinding.FragmentHomeBinding;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.entity.User;
import com.anonymous.mealmate.view.adapter.MealAdapter;
import com.anonymous.mealmate.viewmodel.MealCheckViewModel;

import java.util.List;

public class HomeFragment extends Fragment {
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
     *
     */

    private FragmentHomeBinding binding;
    private MealCheckViewModel mealCheckViewModel;
    private MealAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /**
         * binding , viewModel setting part
         */
        mealCheckViewModel = new ViewModelProvider(this).get(MealCheckViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setMealCheckViewModel(mealCheckViewModel);
        binding.setDate(Date.getInstance()); // 오늘 날짜로 기본 세팅
        // todo 임의 로 설정한 user data 이후 viewModel class 에서 유저 정보 저장후 아래 코드 삭제
        binding.setUser(new User("서현승",User.GENDER_MALE,178,74,User.ACTIVITY_RATIO_HIGH,User.PURPOSE_DIET));


        /**
         * recyclerView , adapter setting part
         */
        //recycler view setup
        mRecyclerView = binding.includeMealList.rvMealLoading;
        mAdapter = new MealAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        // recycler view set end


        /**
         * observer setting part
         */
        mealCheckViewModel.getAllMeals().observe(getViewLifecycleOwner(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                mAdapter.submitList(meals);
            }
        });

        /**
         *  return view that binding setting completed
         * */
        return binding.getRoot();
    }
}

