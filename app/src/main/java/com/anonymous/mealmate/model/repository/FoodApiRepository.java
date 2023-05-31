package com.anonymous.mealmate.model.repository;


import static com.anonymous.mealmate.constants.Constants.*;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anonymous.mealmate.api.FoodApiHelper;

import com.anonymous.mealmate.model.entity.Food;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodApiRepository {
    private FoodApiHelper foodApiHelper;
    private static FoodApiRepository instance = null;
    private FoodApiHelper.ApiService apiService;
    private MutableLiveData<List<Food>> searchResults;  //LiveData는 변경 불가능한 데이터, MutableLiveData 데이터 변경 가능.
    // API 호출 결과가 searchResults에 저장 -> MutableLiveData를 관찰하고 있는 UI 컴포넌트가 자동으로 업데이트

    private Context context;

    // FoodApiHelper 클래스의 인스턴스를 가져오고, apiService와 searchResults 초기화.
    public FoodApiRepository(Context context) {
        this.context = context;
        foodApiHelper = FoodApiHelper.getInstance(context);

        apiService = foodApiHelper.getApiService();
        searchResults = new MutableLiveData<>();
    }

    // 싱글톤으로 관리
    public static FoodApiRepository getInstance(Context context) {
        if (instance == null) {
            synchronized(FoodApiRepository.class) {
                if(instance == null) {
                    instance = new FoodApiRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    // foodName으로 API 호출하여 검색 -> 결과는 Food 객체 리스트로 변환 -> searchResults에 저장
    public LiveData<List<Food>> searchFood(String foodName) {

        Call<FoodApiHelper.ResponseClass> call = apiService.getFoodNtrItdntList( SERVICE_KEY, foodName, null, null, null, null, RESPONSE_TYPE );
        call.enqueue(new Callback<FoodApiHelper.ResponseClass>() {    // Callback 인터페이스를 구현한 익명 클래스 정의. 응답을 받았을 때 호출되는 메소드를 정의.
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            // onResponse 메소드: 응답을 성공적으로 받았을 때 호출되는 메소드
            public void onResponse(Call<FoodApiHelper.ResponseClass> call, Response<FoodApiHelper.ResponseClass> response) {
                Optional.ofNullable(response.body())
                        .map(FoodApiHelper.ResponseClass::getBody)
                        .map(FoodApiHelper.ResponseClass.Body::getItems)
                        .ifPresent(items -> {
                            List<Food> foods = convertItemsToFood(items);
                            // UI Thread에서 실행됨을 확실히 하기 위해 postValue를 사용합니다.
                            searchResults.postValue(foods);
                        });
            }

            @Override
            public void onFailure(Call<FoodApiHelper.ResponseClass> call, Throwable t) {
                searchResults.postValue(Collections.singletonList(new Food("Error: " + t.getMessage(), 0, 0, 0, 0, 0, "")));
            }
        });
        return searchResults;
    }

    // SearchResults getter.
    public LiveData<List<Food>> getSearchResults() {
        return searchResults;
    }

    // API 호출 메소드 정의.
    public Call<FoodApiHelper.ResponseClass> getFoodNtrItdntList(String foodName) {
        return apiService.getFoodNtrItdntList( SERVICE_KEY, foodName, null, null, null, null, RESPONSE_TYPE );
    }

    // API 호출 결과를 Food 객체 리스트로 변환.
    private List<Food> convertItemsToFood(List<FoodApiHelper.ResponseClass.Body.Item> items) {
        List<Food> foods = new ArrayList<>();
        for (FoodApiHelper.ResponseClass.Body.Item item : items) {
            foods.add(new Food(
                    item.getFood_name(),
                    item.getFood_1serving(),
                    item.getFood_kcal(),
                    item.getFood_carbohydrates(),
                    item.getFood_protein(),
                    item.getFood_fat(),
                    item.getFood_company()
                    )
            );
        }
        return foods;
    }
}




//package com.anonymous.mealmate.model.repository;
//
//
//import static com.anonymous.mealmate.constants.Constants.*;
//import android.content.Context;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.anonymous.mealmate.api.FoodApiHelper;
//
//import com.anonymous.mealmate.model.entity.Food;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class FoodApiRepository {
//    private FoodApiHelper foodApiHelper;
//    private static FoodApiRepository instance = null;
//    private FoodApiHelper.ApiService apiService;
//    private MutableLiveData<List<Food>> searchResults;  //LiveData는 변경 불가능한 데이터, MutableLiveData 데이터 변경 가능.
//    // API 호출 결과가 searchResults에 저장 -> MutableLiveData를 관찰하고 있는 UI 컴포넌트가 자동으로 업데이트
//
//    private Context context;
//
//    // FoodApiHelper 클래스의 인스턴스를 가져오고, apiService와 searchResults 초기화.
//    public FoodApiRepository(Context context) {
//        this.context = context;
//        foodApiHelper = FoodApiHelper.getInstance(context);
//
//        apiService = foodApiHelper.getApiService();
//        searchResults = new MutableLiveData<>();
//    }
//
//    // 싱글톤으로 관리
//    public static FoodApiRepository getInstance(Context context) {
//        if (instance == null) {
//            synchronized(FoodApiRepository.class) {
//                if(instance == null) {
//                    instance = new FoodApiRepository(context.getApplicationContext());
//                }
//            }
//        }
//        return instance;
//    }
//
//    // foodName으로 API 호출하여 검색 -> 결과는 Food 객체 리스트로 변환 -> searchResults에 저장
//    public LiveData<List<Food>> searchFood(String foodName) {
//
//        Call<FoodApiHelper.ResponseClass> call = apiService.getFoodNtrItdntList( SERVICE_KEY, foodName, null, null, null, null, RESPONSE_TYPE );
//        call.enqueue(new Callback<FoodApiHelper.ResponseClass>() {    // Callback 인터페이스를 구현한 익명 클래스 정의. 응답을 받았을 때 호출되는 메소드를 정의.
//            @Override
//            // onResponse 메소드: 응답을 성공적으로 받았을 때 호출되는 메소드
//            public void onResponse(Call<FoodApiHelper.ResponseClass> call, Response<FoodApiHelper.ResponseClass> response) {
//                if (response.isSuccessful() && response.body() != null && response.body().getBody() != null) {
//                    List<FoodApiHelper.ResponseClass.Body.Item> items = response.body().getBody().getItems();
//                    if (items != null) {
//                        List<Food> foods = convertItemsToFood(items);
//                        searchResults.setValue(foods);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FoodApiHelper.ResponseClass> call, Throwable t) {
//                searchResults.postValue(Collections.singletonList(new Food("Error: " + t.getMessage(), 0, 0, 0, 0, 0, "")));
//            }
//        });
//        return searchResults;
//    }
//
//    // SearchResults getter.
//    public LiveData<List<Food>> getSearchResults() {
//        return searchResults;
//    }
//
//    // API 호출 메소드 정의.
//    public Call<FoodApiHelper.ResponseClass> getFoodNtrItdntList(String foodName) {
//        return apiService.getFoodNtrItdntList( SERVICE_KEY, foodName, null, null, null, null, RESPONSE_TYPE );
//    }
//
//    // API 호출 결과를 Food 객체 리스트로 변환.
//    private List<Food> convertItemsToFood(List<FoodApiHelper.ResponseClass.Body.Item> items) {
//        List<Food> foods = new ArrayList<>();
//        for (FoodApiHelper.ResponseClass.Body.Item item : items) {
//            foods.add(new Food(
//                    item.getFood_name(),
//                    item.getFood_1serving(),
//                    item.getFood_kcal(),
//                    item.getFood_carbohydrates(),
//                    item.getFood_protein(),
//                    item.getFood_fat(),
//                    item.getFood_company()
//            ));
//        }
//        return foods;
//    }
//}
//
