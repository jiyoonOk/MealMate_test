package com.anonymous.mealmate.model.repository;

import android.content.Context;

import androidx.room.Room;

import com.anonymous.mealmate.model.dao.MealFoodDao;
import com.anonymous.mealmate.model.database.AppDatabase;

public class MealFoodRepository {
    private static MealFoodRepository instance = null;
    //private final MealFoodDao mealFoodDao;
    private final AppDatabase db;

    public static MealFoodRepository getInstance(Context context){
        if(instance == null){
            synchronized (MealRepository.class){
                if(instance == null){
                    instance = new MealFoodRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public MealFoodRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
        //mealFoodDao = db.mealFoodDao();
    }


}
