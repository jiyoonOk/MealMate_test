package com.anonymous.mealmate.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.anonymous.mealmate.model.entity.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    void insertFood(Food food);

    @Update
    void updateFood(Food food);

    @Delete
    void deleteFood(Food food);

    @Query("SELECT * FROM food")
    LiveData<List<Food>> getAllFoods();

    // 특정 음식의 좋아요 여부를 가져옵니다.
    @Query("SELECT * FROM food WHERE foodLike = 1")
    LiveData<List<Food>> getLikedFoods();

    @Query("SELECT * FROM food WHERE foodName = :foodName")
    LiveData<List<Food>> getFoodByName(String foodName);

    @Query("SELECT * FROM food WHERE foodName = :foodName LIMIT 1")
    Food getFoodByNameSync(String foodName);
}
