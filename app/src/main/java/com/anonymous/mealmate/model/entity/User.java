package com.anonymous.mealmate.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {


    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "User_name")
    private String name;

    @ColumnInfo(name = "User_gender")
    private int gender;

    @ColumnInfo(name = "User_height")
    private int height;

    @ColumnInfo(name = "User_weight")
    private int weight;

    @ColumnInfo(name = "User_activityRatio")
    private int activityRatio;

    @ColumnInfo(name = "User_purpose")
    private int purpose;

    //@ColumnInfo(name = "User_target_weight")
    @ColumnInfo(name = "User_targetWeight")
    private int targetWeight;

    @ColumnInfo(name = "User_age")
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Ignore
    private String purposeToString = null;

    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    public static final int ACTIVITY_RATIO_NONE = 1;
    public static final int ACTIVITY_RATIO_ROW = 2;
    public static final int ACTIVITY_RATIO_MIDDLE = 3;
    public static final int ACTIVITY_RATIO_HIGH = 4;
    public static final int ACTIVITY_RATIO_MAX = 5;

    public static final int PURPOSE_DIET = 1;
    public static final int PURPOSE_MAINTAIN = 2;
    public static final int PURPOSE_BURK = 3;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getActivityRatio() {
        return activityRatio;
    }

    public int getPurpose() {
        return purpose;
    }

    public int getTargetWeight() {
        return targetWeight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setActivityRatio(int activityRatio) {
        this.activityRatio = activityRatio;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public void setTargetWeight(int targetWeight) {
        this.targetWeight = targetWeight;
    }

    public User(String name, int gender, int height, int weight, int activityRatio, int purpose) {
        this.name = name;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.targetWeight = 70;
        this.activityRatio = activityRatio;
        this.purpose = purpose;

        setPostPurposeToString(purpose);
    }
    @Ignore
    public User(){
        //test용
//        this.name="";
//        this.gender=0;
//        this.height=0;
//        this.weight=0;
//        this.activityRatio=0;
//        this.purpose=0;
    }
    public static Boolean isUserDataCorrect(User user){
        //유효성 검사 null 검사는 구현 안함 -> 이후 추가 요망
        if(user.getName()==null || user.getName() =="")
            return false;
        if(user.getAge()<0 || user.getAge()>100)
            return false;
        if(!(user.getGender()==User.GENDER_MALE || user.getGender() == User.GENDER_FEMALE))
            return false;
        if(user.getActivityRatio()<User.ACTIVITY_RATIO_NONE||user.getActivityRatio()>User.ACTIVITY_RATIO_MAX)
            return false;
        if(user.getPurpose()<User.PURPOSE_DIET || user.getPurpose()>PURPOSE_BURK)
            return false;
        //targetWeight 도 검사 하는 로직 추가 요망
        //if(targetWeight<0) return false;
        return true;
    }
    @Ignore
    private Boolean setPostPurposeToString(int purpose) {
        switch (purpose) {
            case PURPOSE_BURK:
                purposeToString = "벌크업";
                return true;
            case PURPOSE_DIET:
                purposeToString = "다이어트";
                return true;
            case PURPOSE_MAINTAIN:
                purposeToString = "유지";
                return true;
            default:
                return false;
        }
    }

    @Ignore
    public void setPurposeToString(String purpose) {
        this.purposeToString = purpose;
    }

    @Ignore
    public String getPurposeToString() {
        return purposeToString;
    }
}
