package com.anonymous.mealmate.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.databinding.FragmentCalendarBinding;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.view.adapter.MealAdapter;
import com.anonymous.mealmate.viewmodel.MealCheckViewModel;

import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {
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
     *  Date instance : 날짜 정보를 저장 하는 Singleton class Date 호출 하여 observer 를 통하여 view data update
     */
    private FragmentCalendarBinding binding;
    private MealCheckViewModel mealCheckViewModel;
    private RecyclerView mRecyclerView;
    private MealAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * binding , viewModel setting part
         */
        mealCheckViewModel = new ViewModelProvider(this).get(MealCheckViewModel.class);
        binding = FragmentCalendarBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        binding.setMealCheckViewModel(mealCheckViewModel);
        binding.setDate(Date.getInstance()); // 싱글톤으로 static 구현된 date 가져옴

        /**
         * recyclerView , adapter setting part
         */
        mAdapter = new MealAdapter(getContext());
        mRecyclerView=binding.includeMealList.rvMealLoading;
        mRecyclerView.setAdapter(mAdapter);

        /**
         * observer setting part
         *
         * date 객체를 observe 하여 view 를 update
         */
        mealCheckViewModel.getAllMeals().observe(getViewLifecycleOwner(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                mAdapter.submitList(meals);
            }
        });
        Date.getInstance().getDateChangedSignalLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.includeMealList.includeDatetimeInfo.setDate(Date.getInstance());
            }
        });

        /**
         *  return view that binding setting completed
         * */
        return binding.getRoot();
    }
}
