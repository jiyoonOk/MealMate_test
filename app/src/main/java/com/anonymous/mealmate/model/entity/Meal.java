package com.anonymous.mealmate.model.entity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

//@Entity(tableName = "meal"
//        //foreignKeys = @ForeignKey(entity = Food.class, parentColumns = "foodIndex", childColumns = "foodIndex")
//)
@Entity(tableName = "meal", indices = {@Index("mealDate")})
public class Meal {
    //0529 backend 통합완료
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="mealIndex")
    private int mealIndex;

    @ColumnInfo(name ="mealDate")
    private String mealDate;

    @ColumnInfo(name ="mealCnt")
    private int mealCnt;

    @ColumnInfo(name = "checked")
    private int checked;


    @Ignore
    public static final int CHECKED = 1;
    @Ignore
    public static final int UNCHECKED= 0;

    @Ignore
    public Meal(String mealDate, int mealCnt) {
        this.mealDate = mealDate;
        this.mealCnt = mealCnt;
        checked = UNCHECKED;
//        this.mealFoodAmount = mealFoodAmount;
//        this.foodIndex = foodIndex;
//        this.checked = checked;
    }

    public Meal( String mealDate, int mealCnt, int checked) {

        this.mealDate = mealDate;
        this.mealCnt = mealCnt;
        this.checked = checked;
    }
    public Meal( String mealDate, int mealCnt, int checked,List<Food> foodList) {

        this.mealDate = mealDate;
        this.mealCnt = mealCnt;
        this.checked = checked;
        this.foodList.setList(foodList);
    }
    @Ignore
    public Meal(){

    }


    // getter and setter methods...
    public int getMealIndex() {return mealIndex;}
    public void setMealIndex(int mealIndex) {this.mealIndex = mealIndex;}
    public String getMealDate() {return mealDate;}

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }

    public int getMealCnt() {return mealCnt;}
    public void setMealCnt(int mealNum) {
        this.mealCnt = mealNum;
    }

    public void setChecked(int checked) {this.checked = checked;}

    public int getChecked() {return checked;}

    @Ignore
    private FoodList foodList = new Meal.FoodList();
    @Ignore
    public FoodList getFoodList(){
        return foodList;
    }

    public class FoodList{
        // setMealItemActivity 에서 데이터 수정해야하므로 public setter 클래스 작성
        // 이 클래스 자체에선 DB 연동이 없다. 인스턴스로 데이터를 가지고 있다가 DB에 업로드 하기 위한 중간다리 class

        //식단 내 음식들을 저장하는 부분
        // 한끼의 데이터를 정의한 클래스
        // List 클래스가 아니지만 List와 비슷하게 동작하도록 설계 할 것

        // 같은 역할하도록 설계
        //private List<List<Food>> foods;
        //private List<Foods> food;

        //builder 패턴 적용 예정 : 생성자 오버로딩 대신 builder innerclass 생성하여 초기값 설정에 편리함 더한다.

        //method chaining setter method 에 void 반환자 대신 객체를 반환해 method.chaining 기법을 사용 할 수 있게 한다.
        @Ignore
        private MutableLiveData<List<Food>> foodsLiveData = new MutableLiveData<>();

        private MutableLiveData<List<MealFood>> mealFoodsLiveData = new MutableLiveData<>();
        @Ignore
        public FoodList(){
            //초기 값 세팅
            foodsLiveData.postValue(new ArrayList<>());
        }
        @Ignore
        public FoodList(List<Food> foods){
            this.foodsLiveData.setValue(foods);
        }

//        meal.getFoodList().toList().get(i); return Food
//        meal.getFoodList().size(); return int
        @Ignore
        public LiveData<List<Food>> toLivaData() {
            // liveData로 return 하여 다형성 제공
            return foodsLiveData;
        }
        @Ignore
        public List<Food> toList(){
            // List로 인스턴스를 리턴해줌
            return foodsLiveData.getValue();
        }

        @Ignore
        public Food toSingleItem(int FoodNum){
            //FoodNum의 사이즈 오류 검사하여 이상시 nullpointException

            if(FoodNum<= foodsLiveData.getValue().size()&&FoodNum>=0)
                return foodsLiveData.getValue().get(FoodNum);
            return null;
        }
        @Ignore
        public FoodList add(Food food){
            foodsLiveData.getValue().add(food);
            return this;
        }
        @Ignore
        public FoodList remove(Food food){
            foodsLiveData.getValue().remove(food);
            return this;
        }
        @Ignore
        public int size(){
            return foodsLiveData.getValue().size();
        }
        @Ignore
        public FoodList setList(List<Food> foods){
            this.foodsLiveData.setValue(foods);
            return this;
        }

        @Ignore
        public Food get(int position){
            return foodsLiveData.getValue().get(position);
        }

        @Ignore
        public String toStringList(){
            String str = "";
            for(int i=0;i< foodList.size();i++){
                str += i+". "+ foodList.get(i).getFoodName()+ "\n";
            }
            return "1. 사과 \n2. 김치\n 3.바나나";
        }
    }
}