package com.anonymous.mealmate.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.anonymous.mealmate.feature.ControlViewState;
import com.anonymous.mealmate.feature.Date;
import com.anonymous.mealmate.model.entity.Food;
import com.anonymous.mealmate.model.entity.Meal;
import com.anonymous.mealmate.model.repository.MealRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MealCheckViewModel extends AndroidViewModel {
    private final MealRepository mealRepository;
    private LiveData<List<Meal>> allMeals;





    public MealCheckViewModel (Application application) {
        super(application);
        mealRepository = MealRepository.getInstance(application);

        /*repository = new MealRepository(application);*/
        allMeals = mealRepository.getAllMeals();

    }


    public LiveData<List<Meal>> getAllMeals() { return allMeals; }

    public void insert(Meal meal) { mealRepository.insertMeal(meal); }

//    public void insert(){
//        mealRepository.insertMeal(new Meal("230524",0));
//    }

    public void update(Meal meal) { mealRepository.updateMeal(meal); }

    public void delete(Meal meal) { mealRepository.deleteMeal(meal); }


    //binding method, do not erase
    public void onLoadMeal(View view) {
        String selectedDateTime = Date.getInstance().getDateToString();


        List<Food> foods = new ArrayList<>();
        foods.add(new Food("김치"));
        foods.add(new Food("삼겹살"));
        foods.add(new Food("계란"));


        Meal meal =new Meal(selectedDateTime,1,0,foods);
        mealRepository.insertMeal(meal);
    }

    public String getFoodListToString(Meal meal){
        String str="";
        Food food = null;
        for(int i=0; i<meal.getFoodList().size();i++) {
            food = meal.getFoodList().get(i);
            str +=i+". "+food.getFoodName()+"\n";
        }
        return str;
    }


    //binding method
    public void onMealChecked(Meal meal){
        //체크 변경 method
        if(meal.getChecked()==Meal.CHECKED)
            meal.setChecked(Meal.UNCHECKED);
        else if(meal.getChecked()==Meal.UNCHECKED)
            meal.setChecked(Meal.CHECKED);

        mealRepository.updateMeal(meal);
    }


    public void onDateSelected( View view, int year, int month, int dayOfMonth){
        //정상 작동
        // 완성
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);

        Date date = Date.getInstance();
        date.set(year,month,dayOfMonth,calendar.get(Calendar.DAY_OF_WEEK));

        allMeals = mealRepository.getMealsByDate(date.getDateToString());


        mealRepository.getMealsByDate(date.getDateToString());


        //binding.includeMealList.includeDatetimeInfo.setDate(date);
        //날짜를 전달받아 repository에 알리는 메소드
        //Toast.makeText(getApplication().getApplicationContext(), "test",Toast.LENGTH_SHORT).show();
        //Date date = Date.getInstance();
        //date.set(month,dayOfMonth,dayOfWeek);
        //repository에 날짜 전달하여 liveData 갱신하는 메소드
        //mealRepository.setMealDate(date.getDateToString());
    }


//    @BindingAdapter("app:colorChanged")
//    public static void colorChanged(View view, Boolean checked){
//        // 식사 체크 여부 확인하여 drawble바꿔주는 바인딩 어댑터
//        //view.setBackgroundResource( checked ? R.drawable.btn_back : R.drawable.btn_cancle) ;
//    }

}
