package com.anonymous.mealmate.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.anonymous.mealmate.R;
import com.anonymous.mealmate.databinding.AdapterMealBinding;
import com.anonymous.mealmate.databinding.AdapterMealitemBinding;
import com.anonymous.mealmate.model.entity.Food;

import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.viewmodel.MealSetViewModel;

import java.util.List;


public class MealItemAdapter extends ListAdapter<Meal, MealItemAdapter.MealItemViewHolder> {
    //Todo 생성자를 context만  매개변수로 받기 -> complete
    //Todo ViewModel 을 interface 화 하여 apdater 에서 변수병을 interfaece로 교체 , 다형성 및 재사용성 확보

    /**
     *    동작 순서 :
     *      생성자 실행후 -> onCreateViewHolder  -> FoodViewHolder 생성자 실행후 view hold,  메모리 유지
     *      -> item list 갱신 받고  onBindViewHolder method 사용 하여 item 별로 기능 차이 설정 data update , position 값을 이용해 item 구분
     *
     *     NOTE:!!
     *      context 의 제대로 된 사용법 :
     *      context 는 각종 Owner class 로 convert 가능함 :
     *      -> 여러 매개 변수를 사용 하지 않고  context or application 전달 하여 convert 해서 사용
     *      ex) (LifeCycleOwner)context , (ViewModelStoreOwner)context
     *
     *     class info :
     *      adapter class, MVVM 패턴 적용, data binding 사용 하여 처리, parameter 로 context 를 받아 viewModel 와 직접 연결,
     *      activity 에서 observe 한 신호를 감지 하여 view 의 data 를 update 해준다.
     *
     *     constructor :
     *       parameter 로 context 를 받아서 생성
     *       아이템 구별 로직인 DiffUtil.ItemCallBack 생성 하여 연결 super()
     *       adapter 내부 에서 viewModelProvider 연결
     *       viewModel 연결 한다.
     *
     *     onCreateViewHolder method :
     *       초기 1회만 호출
     *       ViewHolder 생성, 메모리 유지 static (recycle)
     *       binding 에 viewModel, lifecycleOwner 연결후 hold
     *
     *     onBindViewHolder method :
     *       연결된 데이터 가 갱신 될때 마다 호출
     *       list 의 size 에 의존해 호출 되며 따라서 item 의 position 값을 가지고 있음
     *       item 별로 구분 되는 logic 작성, ex) item bind 호출
     *
     *     ViewHolder class :
     *      초기에 생성 되어 메모리 에 hold
     *      adapter 들의 공통 logic 을 정의 한다. ex) item bind location
     *
     *     features :
     *      2차원 어댑터 , adapter 에서 또다른 adapter 를 정의. MealSubItemAdapter.class
     *
     */
    private Context context;
    private ViewModelProvider viewModelProvider;
    private MealSetViewModel mealSetViewModel;

    public MealItemAdapter(@NonNull Context context) {
        super(new DiffUtil.ItemCallback<Meal>() {
            @Override // Todo 정확한 비교로직 작성 필요
            public boolean areItemsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Meal oldItem, @NonNull Meal newItem) {
                return false;
            }
        });
        this.context = context;
        viewModelProvider = new ViewModelProvider((ViewModelStoreOwner) context);
        mealSetViewModel = viewModelProvider.get(MealSetViewModel.class);
    }

    public static class MealItemViewHolder extends RecyclerView.ViewHolder {


        // 이후 변수 변경이 없으므로 final 설정
        private final RecyclerView rvMealSubItem;
        private final MealSubItemAdapter mealSubItemAdapter;
        private final AdapterMealitemBinding binding;

        public MealItemViewHolder(@NonNull AdapterMealitemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            /**
             * use context to get ViewModel
             */
            mealSubItemAdapter = new MealSubItemAdapter(context);
            rvMealSubItem = binding.rvMealSubItem;
            rvMealSubItem.setAdapter(mealSubItemAdapter);
        }

        public void bind(Meal meal) {
            //list 내부 자료형이 Food 가맞는지 확인해주는 검사 메소드 작성필요
            try {
                binding.setMeal(meal);
                mealSubItemAdapter.submitList(meal.getFoodList().toList());
                //subitem에 meal 정보 넘겨줌
                mealSubItemAdapter.setMeal(meal);
            } catch (Exception e) {
                Log.d("bind Error", "take care of the value of lists type");
            }
        }

        public AdapterMealitemBinding getBinding() {
            return binding;
        }
        public MealSubItemAdapter getMealSubItemAdapter() {
            return mealSubItemAdapter;
        }
    }


    @NonNull
    @Override
    public MealItemAdapter.MealItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //초기 1회만 실행되서 ViewHolder 세팅시작하는 메서드
        /**
         *  use context to binding setup
         */
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AdapterMealitemBinding binding = AdapterMealitemBinding.inflate(layoutInflater, parent, false);
        binding.setLifecycleOwner((LifecycleOwner) context);
        binding.setMealSetViewModel(mealSetViewModel);

        return new MealItemViewHolder(binding, context);
    }


    @Override
    public void onBindViewHolder(@NonNull MealItemAdapter.MealItemViewHolder holder, int position) {
        //item 별 구분되는 기능 list item 개수만큼 호출됨
        Meal currentMeal = getItem(position);
        holder.bind(currentMeal);

        //item의 variable에 postion값을 저장시킴
        holder.getBinding().setPosition(position);
        holder.getBinding().getMeal().getFoodList().toLivaData().observe((LifecycleOwner) context, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                holder.getMealSubItemAdapter().submitList(foods);
            }
        });
    }
}
