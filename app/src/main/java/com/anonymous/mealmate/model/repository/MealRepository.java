package com.anonymous.mealmate.model.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.anonymous.mealmate.model.dao.MealDao;
import com.anonymous.mealmate.model.database.AppDatabase;
import com.anonymous.mealmate.model.entity.Meal;

import java.util.List;
import java.util.concurrent.Executors;

public class MealRepository {
    private static MealRepository instance = null;
    private final MealDao mealDao;
    private final AppDatabase db;

    /* 싱글톤 패턴을 적용하여, getInstance()를 통해 객체를 가져옴.
     why? 여러 뷰모델에서 하나의 레포지토리를 공유함으로 데이터 충돌 방지
     - 레포지토리는 앱이 실행되는 동안 계속 존재. (뷰모델은 액티비티가 살아있는 동안만 존재)
     Application의 context를 전달함으로써 계속해서 Application의 변화에 따라 부모격인 context에 반영*/
    public static MealRepository getInstance(Context context) {
        if (instance == null) {
            synchronized(MealRepository.class) {
                if(instance == null) {
                    instance = new MealRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public MealRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
        mealDao = db.mealDao();
    }

    // 이 클래스는 새로운 스레드에서 데이터베이스 작업을 수행.
    public void insertMeal(Meal meal) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mealDao.insertMeal(meal);
        });
    }

    public void updateMeal(Meal meal) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mealDao.updateMeal(meal);
        });
    }

    public void deleteMeal(Meal meal) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mealDao.deleteMeal(meal);
        });
    }

    // LiveData를 이용하여 데이터를 가져옵니다.
    public LiveData<List<Meal>> getAllMeals() {
        return mealDao.getAllMeals();
    }

    // 날짜를 기준으로 Meal을 가져오는 메서드
    public LiveData<List<Meal>> getMealsByDate(String mealDate) {
        return mealDao.getMealsByDate(mealDate);
    }

//    public LiveData<List<Meal>> getMealsByFoodIndex(int foodIndex) {
//        return mealDao.getMealsByFoodIndex(foodIndex);
//    }

    public LiveData<List<Meal>> getCheckedMeals() {
        return mealDao.getCheckedMeals();
    }

    public void setMealDate(String date){

    }
}
