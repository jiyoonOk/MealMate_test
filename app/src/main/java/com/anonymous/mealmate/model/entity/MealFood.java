package com.anonymous.mealmate.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "mealFood", primaryKeys = {"mealIndex", "foodIndex"}, foreignKeys = {@ForeignKey(entity = Meal.class,
        parentColumns = "mealIndex", childColumns = "mealIndex"), @ForeignKey(entity = Food.class,
        parentColumns = "foodIndex", childColumns = "foodIndex")}, indices = {@Index("mealIndex")})
public class MealFood {
    private int mealIndex;
    private int foodIndex;
    private int mealFoodAmount;
    private int checked;

    public MealFood(int mealIndex, int foodIndex, int mealFoodAmount, int checked) {
        this.mealIndex = mealIndex;
        this.foodIndex = foodIndex;
        this.mealFoodAmount = mealFoodAmount;
        this.checked = checked;
    }

    // getter and setter methods...
    public int getMealIndex() {
        return mealIndex;
    }

    public int getFoodIndex() {
        return foodIndex;
    }

    public int getMealFoodAmount() {
        return mealFoodAmount;
    }

    public void setMealFoodAmount(int mealFoodAmount) {
        this.mealFoodAmount = mealFoodAmount;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public void onPlusMealFoodAmount(){
        mealFoodAmount++;
    }
    public void onMinusMealFoodAmount(){
        mealFoodAmount--;
    }
}