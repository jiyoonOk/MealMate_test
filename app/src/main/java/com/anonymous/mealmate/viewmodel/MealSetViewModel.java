package com.anonymous.mealmate.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.repository.FoodRepository;
import com.anonymous.mealmate.model.repository.MealRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealSetViewModel extends AndroidViewModel {
    /**
     * class info :
     * ViewModel class , extends AndroidViewModel
     * Android ViewModel class 는 기존 viewModel class 의 observer 뿐 아니라 Android System 을 통하여 LifeCycle logic 을 정의 하고 있음.
     * -> activity 와 독립된 LifeCycle 을 가진다.
     * <p>
     * ViewModelProvider 를 통하여  view 와 connect , do not use constructor
     */
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    //private final MealFoodRepository mealFoodRepository;
    //db에서 받아오는 데이터가 아닌 업로할 데이터

    //Todo 다른 곳에서 생성된 같은 class 의 뷰 모델 끼리 통신이 안될 수 있는 이슈!! -> repository 에서 해결해보기
    //Todo complete!! 뷰모델 끼리 별개의 class 로 기능 하고 있음. -> liveData static으로 해결
    private static MutableLiveData<List<Meal>> mealsLiveData = new MutableLiveData<>();
    private static List<Meal> currentMealList = new ArrayList<Meal>(Arrays.asList(new Meal(), new Meal(), new Meal()));

    private static int CURSOR_NONE = 1;
    private static int positionCursor = CURSOR_NONE;


    public MealSetViewModel(Application application) {
        super(application);
        mealRepository = MealRepository.getInstance(application);
        foodRepository = FoodRepository.getInstance(application);

        //초기 값 세팅
        mealsLiveData.postValue(currentMealList);
    }

    private void addFoodToMeal(Food food) {

    }

    /*
        return to observer
        옵저버가 관측할 객체 foodLiveData
     */
    public LiveData<List<Meal>> getMealsLiveData() {
        return mealsLiveData;
    }


    /**
     * Binding Method Info :
     * bind int activity_setmealitem.xml
     * - onAddMealTime()
     * - onLoadPreset()
     * - onCreatePreset()
     */
    //xml binding method, do not erase
    public void onAddMealTime() {
        // 완성된 메소드
        // 끼니를 추가하는 method
        //새로운 list 생성하여 같은 주소참조하는 것을 막아 livedata 작동하도록 설계
        //매우 중요 ** 라이브데이터 갱신로직에서
        List<Meal> updatedMeal = new ArrayList<>(mealsLiveData.getValue());
        updatedMeal.add(new Meal());
        //mutable 테이터를 다시 세팅
        mealsLiveData.setValue(updatedMeal);


        Log.d(getClass().getName(), "onAddMealTime: activated");
    }

    public void onDeleteMealTime(Meal meal) {
        //끼니 삭제하는 mothd
        List<Meal> updatedMeal = new ArrayList<>(mealsLiveData.getValue());
        updatedMeal.remove(meal);
        mealsLiveData.setValue(updatedMeal);
    }

    public void callAddFoodCycle(int position) {
        positionCursor = position;
        // Food 객체를 받아와 끼니에 맞는 foodsList에 add , position : 끼니 변수
        //Foods foods = foodsList.get(position);
        //foods.add(food);

        // MainActivity에 intent signal 전달 observer 작동
        ControlViewState.getInstance().activeIntentSignal(ControlViewState.INTENT_SETMEAL_TO_FOOD);
    }

    public void finishAddFoodCycle(Food food) {
        //**매우중요
        // view 를 먼저 넘겨주고 리스트를 업데이트 해줘야 적절한 acitivity 가 observe한다
        ControlViewState.getInstance().activeIntentSignal(ControlViewState.ACTIVITY_FINISH);


        Toast.makeText(getApplication().getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        //data unpacking
        // ex) mealsLiveData.getValue().get(positionCursor).getFoodList().toList();
        Meal selectedMeal = mealsLiveData.getValue().get(positionCursor);
        List<Food> currentList = selectedMeal.getFoodList().toList();


        //옵저버 작동을 위해 별개주소를 가진 새로운 리스트 생성, 기존리스트에 add하는 방식으로는 observer 작동이 안된다.
        List<Food> updatedList = new ArrayList<>(currentList);
        // index 추가
        updatedList.add(food);


        //데이터 변경, observer 작동
        selectedMeal.getFoodList().setList(updatedList);

        //positionCursor 초기화
        positionCursor = CURSOR_NONE;

        // active intent signal

        Log.e(this.getClass().getName(), "finishAddFoodCycle method executed , please check the data stream " + food.getFoodName());
    }

    public void testNotifyData() {
        ControlViewState.getInstance().activeActivityLifeCycleSignal(ControlViewState.ADAPTER_DATA_CHANGED);
        Toast.makeText(getApplication().getApplicationContext(), "test22", Toast.LENGTH_SHORT).show();
    }

    public void onDeleteFood(Meal meal, Food food) {
        List<Food> updatedFoodList = new ArrayList<>(meal.getFoodList().toList());
        updatedFoodList.remove(food);
        meal.getFoodList().setList(updatedFoodList);
        // Toast.makeText(getApplication().getApplicationContext(), "test" + meal.getFoodList().toList(), Toast.LENGTH_SHORT).show();
    }

    //xml binding method, do not erase
    public void onLoadPreset(View view) {

    }

    //xml binding method, do not erase
    public void onCreatePreset() {

    }

    public void onSaveMeal(Meal meal) {
        //meal 먼저 insert 하고 food insert 해야 오류가 없을 것
        //mealFoodRepository.insertMeal(meal);
//        mealRepository.insertMeal(meal);
//        for (int i = 0; i < meal.getFoodList().size(); i++ ){
//            Food food = meal.getFoodList().get(i);
//            foodRepository.insertFood(food);
//        }
    }

    public void onFoodAdded(Food food) {

    }

}
