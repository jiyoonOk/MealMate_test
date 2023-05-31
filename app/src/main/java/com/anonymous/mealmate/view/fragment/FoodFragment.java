package com.anonymous.mealmate.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.R;
import com.anonymous.mealmate.databinding.FragmentFoodBinding;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.view.adapter.FoodAdapter;
import com.anonymous.mealmate.viewmodel.FoodViewModel;

import java.util.List;

public class FoodFragment extends Fragment {
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
     *  두가지 의  observer 가 하나의 adapter 에 변경 신호 {ex) submitList} 전송중
     *  같은 뷰에 성격이 다른 { ex) data from db , data form api } data 전송중
     */
    private FoodAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FoodViewModel foodViewModel;
    private FragmentFoodBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * binding , viewModel setting part
         */
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        binding = FragmentFoodBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setFoodViewModel(foodViewModel);


        /**
         * recyclerView , adapter setting part
         */
        mAdapter = new FoodAdapter(getContext());
        mRecyclerView = binding.rvFoodSearch;
        mRecyclerView.setAdapter(mAdapter);

        /**
         * observer setting part
         */
        foodViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                mAdapter.setFoods(foods);
                //todo test 용 Toast , 디버깅후 삭제
                Toast.makeText(getContext(), "APIAcessComplete!!", Toast.LENGTH_SHORT).show();
            }
        });
        foodViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                mAdapter.submitList(foods);
                //todo test 용 Toast , 디버깅후 삭제
                Toast.makeText(getContext(), "foodListAcessComplete!!", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         *  return view that binding setting completed
         * */
        return binding.getRoot();
    }
}
