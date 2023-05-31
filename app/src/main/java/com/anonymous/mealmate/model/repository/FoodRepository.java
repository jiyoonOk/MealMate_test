package com.anonymous.mealmate.model.repository;

import static com.anonymous.mealmate.constants.Constants.DATABASE_NAME;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.anonymous.mealmate.model.dao.FoodDao;
import com.anonymous.mealmate.model.database.AppDatabase;
import com.anonymous.mealmate.model.entity.Food;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodRepository {
    private static FoodRepository instance = null;
    private final FoodDao foodDao;
    private final AppDatabase db;
    private final ExecutorService executorService;  // DB 작업을 위한 스레드 풀.
    // Java에서 제공하는 스레드 풀 관리 서비스. 스레드 풀은 여러 스레드를 미리 생성해두고, 필요할 때마다 이들을 재사용하는 방식으로 동작.

    public static FoodRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (FoodRepository.class) {
                if (instance == null) {
                    instance = new FoodRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public FoodRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        foodDao = db.foodDao();
        executorService = Executors.newFixedThreadPool(4);
    }
//    public FoodRepository(Context context) {
//        db = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
//        foodDao = db.foodDao();
//    }

    // 이 클래스는 새로운 스레드에서 데이터베이스 작업을 수행.
    // 이는 Room 라이브러리의 중요한 특징. 메인 스레드에서 데이터베이스 작업을 하면 UI가 멈추는 현상이 발생할 수 있기 때문.
    // 이로 인해 사용자 경험이 저하 가능성. 따라서 Room 라이브러리는 메인 스레드에서 데이터베이스 작업을 하지 못하도록 막음.
    public void insertFood(Food food) {
        Executors.newSingleThreadExecutor().execute(() -> {
            foodDao.insertFood(food);
        });
    }

    public void updateFood(Food food) {
        Executors.newSingleThreadExecutor().execute(() -> {
            foodDao.updateFood(food);
        });
    }

    public void deleteFood(Food food) {
        Executors.newSingleThreadExecutor().execute(() -> {
            foodDao.deleteFood(food);
        });
    }

    // LiveData를 이용하여 데이터를 가져옵니다.
    public LiveData<List<Food>> getAllFoods() {
        return foodDao.getAllFoods();
    }

    // 특정 음식의 좋아요 여부를 가져옵니다.
    public LiveData<List<Food>> getUserLikedFoods() {
        return foodDao.getLikedFoods();
    }

    // foodName과 일치하는 음식리스트 가져옴.
    public LiveData<List<Food>> getFoodByExactName(String foodName) {
        return foodDao.getFoodByName(foodName);
    }

    // 비동기적으로 FoodName과 일치하는 음식을 가져옴. insert 전 검사하려고.
    public Food getFoodByNameSync(String foodName) {
        return foodDao.getFoodByNameSync(foodName);
    }

//
//    // 특정 음식의 좋아요 여부를 가져옵니다.
//    public LiveData<List<Food>> getLikedFoods() {
//        return foodDao.getLikedFoods();
//    }
//
//    public LiveData<List<Food>> getFoodByName(String foodName) {
//        return foodDao.getFoodByName(foodName);
//    }
}
