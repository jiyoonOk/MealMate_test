package com.anonymous.mealmate.viewmodel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anonymous.mealmate.api.FoodApiHelper;
import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.repository.FoodApiRepository;
import com.anonymous.mealmate.model.repository.FoodRepository;

import java.util.List;

import retrofit2.Call;

public class FoodViewModel extends AndroidViewModel {
    /**
     * class info :
     *  1. ViewModel class that extended AndroidViewModel
     *  2. MVVM 패턴 적용 , data binding 사용, observer 를 위한 liveData
     *  3. xml 에 onclick 으로 method binding , activity 에 data Stream 전달 과정 없이 ViewModel 로 data 전달.
     *  -> activity 는 data 와 완전히 분리 되어 자유 로운 View state control 이 가능
     *
     *  NOTE !! :
     *   MVVM ViewModel application  Control Flow example
     *    xml onclick -> ViewModel method execute -> data update ( with Model ) -> ViewModel liveData updated
     *    -> activity or fragment observer active -> update xml data
     *
     *  constructor :
     *   1. parameter 로 application 받아 super(application) -> AndroidViewModel
     *   2. connect with Repository coded Singleton class
     *   3. get LiveData from Repository connected Database and hold data as LiveData to observe at activity
     *
     *   connected XML list
     *   1. adapter_food.xml
     *   2. fragment_food.xml
     *   3. adapter_mealsubitem.xml
     */

    private final FoodRepository foodRepository;
    private final FoodApiRepository foodApiRepository;

    private final MutableLiveData<Food> selectedFood = new MutableLiveData<>(); // 선택된 음식을 나타내는 LiveData 추가
    private final MutableLiveData<Food> foodLiveData = new MutableLiveData<>(); // 특정 음식의 좋아요 여부를 나타내는 LiveData 추가
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final LiveData<List<Food>> allFoods;
    private final LiveData<List<Food>> searchResults;   // 검색 결과를 나타내는 LiveData 추가

    public FoodViewModel(Application application) {
        super(application);
        foodRepository = FoodRepository.getInstance(application);
        foodApiRepository = FoodApiRepository.getInstance(application);

        searchResults = foodApiRepository.getSearchResults();
        // 모든 음식 정보를 가져옴.
        allFoods = foodRepository.getAllFoods();
    }

    //TODO : FoodApi
    public Call<FoodApiHelper.ResponseClass> getFoodNtrItdntList(String foodName) { // 사용자가 특정 음식 이름을 입력하면 그 음식의 영양 정보를 반환함.
        return foodApiRepository.getFoodNtrItdntList(foodName);
    }

    /*// 사용자가 음식 이름을 입력하면 그와 관련된 모든 음식을 검색 결과로 반환함.
    public LiveData<List<Food>> onSearchFood(String foodName) {
        return foodApiRepository.searchFood(foodName);
    }*/

    /**
     *  xml binding method, do not erase
     *  binding Text complete, enable to user
     *
     *  connected fragment_food.xml
     *  id: btn_food_search
     *  input Food name to search
     *
     *  get data from FoodRepository.class
     */
    public void onSearchFood(View view, String inputFoodName) {
        //Food Name 전달 받아 repository에 데이터 요청하는 메소드
        //db search logic

        //foodRepository.searchFood(foodName);

        //api repository 검색,


        //TODO 레파지토리에 food 검색 기능 연결 -> complete
        //TODO 이후 조건 받아 foodapi에서 받게하기
        LiveData<List<Food>> searchResults = foodApiRepository.searchFood(inputFoodName);


        //test

    }


    // 검색결과와 에러메시지를 가져옴.
    public LiveData<List<Food>> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }


    //TODO : Food
    public LiveData<List<Food>> getAllFoods() {
        return allFoods;
    }

    // DB에 넣을 때 같은 음식이 있는지 확인하고, 없으면 삽입.
    public void insert(Food food) {
        new Thread(() -> {
            try {
                Food existingFood = foodRepository.getFoodByNameSync(food.getFoodName());
                if (existingFood == null) {
                    foodRepository.insertFood(food);
                }
            } catch (Exception e) {
                // 예외를 로그에 기록하거나 사용자에게 오류 메시지를 표시합니다.
            }
        }).start();
    }


    public void update(Food food) {
        foodRepository.updateFood(food);
    }

    public void delete(Food food) {
        foodRepository.deleteFood(food);
    }

    // 특정 음식의 좋아요 여부를 가져옵니다.
    public LiveData<List<Food>> getLikedFoods() {
        return foodRepository.getUserLikedFoods();
    }


    // 특정 음식의 좋아요 여부를 업데이트합니다.
    public void setClickedFood(Food food) {
        foodLiveData.setValue(food);
    }


    public void selectFood(Food food) {
        selectedFood.setValue(food);
    } // 선택된 음식을 설정하는 메소드 추가


    public LiveData<Food> getSelectedFood() {
        return selectedFood;
    } // 선택된 음식을 가져오는 메소드 추가

    //xml binding method, do not erase

    /**
     * xml binding method, do not erase
     * binding test complete, enable to use
     *
     * Food Adapter에 하트 버튼을 눌렀을때 Food db를 업데이트 해주는 메서드
     */
    public void onLikeStateChange(Food food) {
        if (food.getFoodLike() == Food.FOOD_NOT_LIKE)
            food.setFoodLike(Food.FOOD_LIKE);
        else if (food.getFoodLike() == Food.FOOD_LIKE) {
            food.setFoodLike(Food.FOOD_NOT_LIKE);
        }
        // todo db 연결 logic 추가
    }

    public void onDeleteFood(Food food) {
        foodRepository.deleteFood(food);
    }

    public void onAddFood() {
        ControlViewState.getInstance().activeIntentSignal(ControlViewState.ACTIVITY_FINISH);
        Toast.makeText(getApplication().getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
    }
}
