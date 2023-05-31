package com.anonymous.mealmate.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.anonymous.mealmate.model.entity.MealFood;

import java.util.List;


@Dao
public interface MealFoodDao {
    @Insert
    void insertMealFood(MealFood mealFood);

    @Update
    void updateMealFood(MealFood mealFood);

    @Delete
    void deleteMealFood(MealFood mealFood);

    // 특정 Meal에 대한 모든 MealFood를 가져옵니다.
//    @Query("SELECT * FROM mealFood WHERE mealIndex = :mealIndex")
//    LiveData<List<MealFood>> getMealFoodsByMealIndex(int mealIndex);
//
//    // 체크된 MealFood를 가져옵니다.
//    @Query("SELECT * FROM mealFood WHERE checked = 1")
//    LiveData<List<MealFood>> getCheckedMealFoods();
}
