package com.anonymous.mealmate.feature;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anonymous.mealmate.model.entity.User;

public class BenedictCalculer {

    private User user;


    //성별 상수
    final static String GENDER_MALE = "남자";
    final static String GENDER_FEMALE = "여자";
    //남자일 경우
    final static int BENEDICT_GENDER_MALE_1 = 66;//남자 일경우 공식 첫번째 들어가는 상수
    final static double BENEDICT_GENDER_MALE_2 = 13.7;//남자 일경우 공식 두번째 들어가는 상수
    final static int BENEDICT_GENDER_MALE_3 = 5;
    final static double BENEDICT_GENDER_MALE_4 = 6.8;
    //여자일 경우
    final static int BENEDICT_GENDER_FEMALE_1 = 655;
    final static double BENEDICT_GENDER_FEMALE_2 = 9.6;
    final static double BENEDICT_GENDER_FEMALE_3 = 1.7;
    final static double BENEDICT_GENDER_FEMALE_4 = 4.7;
    //활동량 상수-->해리스 베네딕트 공식으로 계산된 칼로리에 곱함
    final static double SEDENTARY = 1.2;//활동량이 거의 없음
    final static double LIGHT = 1.375;//가벼운 활동
    final static double MODERATE = 1.55;//보통 활동
    final static double ACTIVE = 1.725;//활동적임
    final static double VERYACTIVE = 1.9;//매우 활동적
    //목적 상수-->
    final static double DIET = 0.8;
    final static double BULK = 1.2;
    final static double MAINTAIN = 1;
    double kcal;
    /*
       1.Activity에서 입력받은 내용을 UserInfoViewModel에 전달
       2.UserInfoViewModel은 전달받은 내용을 Model에 전달
       3.User_kcal의 경우 입력받은 데이터를 토대로 가공 및 생성
       4.처음에 User_height,USer_weight,User_gender,User_age를 이용하여 베네딕트 공식 적용하여 kcal산출
       5.산출받은 kcal는 Activity(활동량)에 따라 다시 산출
       6.마지막으로 목적에 따라 kcal를 산출
     */
    private MutableLiveData<String> User_name = new MutableLiveData<>();//사용자 이름
    private MutableLiveData<Double> User_height = new MutableLiveData<>();//사용자 키
    private MutableLiveData<Double> User_weight = new MutableLiveData<>();//사용자 몸무게
    private MutableLiveData<String> User_gender = new MutableLiveData<>();//사용자 성별
    private MutableLiveData<Integer> User_age = new MutableLiveData<>();//사용자 나이
    private MutableLiveData<Double> User_kcal = new MutableLiveData<>();//사용자 칼로리
    private MutableLiveData<Integer> User_activityRatio = new MutableLiveData<>();//사용자 활동량
    private MutableLiveData<Integer> User_purpose = new MutableLiveData<>();//사용자 목적

    public LiveData<String> getUserName() {
        return User_name;
    }

    public LiveData<Double> getHeight() {
        return User_height;
    }

    public LiveData<Double> getWeight() {
        return User_weight;
    }

    public LiveData<String> getGender() {
        return User_gender;
    }

    public LiveData<Double> getUserCalories() {
        return User_kcal;
    }

    public LiveData<Integer> getUserAge() {
        return User_age;
    }

    public LiveData<Integer> getUserActivity() {
        return User_activityRatio;
    }

    public LiveData<Integer> getUserPurpose() {
        return User_purpose;
    }

    public void saveUserInfo(String name, double userHeight, double userWeight, String userGender, int userAge, int userActivity, int userPurpose) {
        User_name.setValue(name);
        User_height.setValue(userHeight);
        User_weight.setValue(userWeight);
        User_gender.setValue(userGender);
        User_age.setValue(userAge);
        User_purpose.setValue(userPurpose);
        User_activityRatio.setValue(userActivity);
        //userActivity.setValue(userActivity);
        // 필요한 계산 로직 수행
        //double calculatedCalories = calculateCalories(userHeight, userWeight, userGender,userAge,userActivity,userPurpose);

        setUserBenedictCalories(userHeight, userWeight, userGender, userAge);
        setUserActivityCalories(kcal, userActivity);
        setUserPurposeCalories(kcal, userPurpose);

        User_kcal.setValue(kcal);
    }


    //베네딕트 공식을 이용한 칼로리 첫 가공. 키 , 몸무게 , 성별 ,나이
    private void setUserBenedictCalories(double height, double weight, String gender, int age) {
        double BenedictKcal = kcal;
        if (gender.equals(GENDER_MALE)) {
            BenedictKcal = BENEDICT_GENDER_MALE_1 + (BENEDICT_GENDER_MALE_2 * weight) + (BENEDICT_GENDER_MALE_3 * height) - (BENEDICT_GENDER_MALE_4 - age);
        } else if (gender.equals(GENDER_FEMALE)) {
            BenedictKcal = BENEDICT_GENDER_FEMALE_1 + (BENEDICT_GENDER_FEMALE_2 * weight) + (BENEDICT_GENDER_FEMALE_3 * height) - (BENEDICT_GENDER_FEMALE_4 * age);
        }
        kcal = BenedictKcal;

    }

    //베네딕트에서 받은 칼로리를 다시 가공
    private void setUserActivityCalories(double BenedictKcal, int activityRatio) {
        double ActivityKcal = BenedictKcal;
        switch (activityRatio) {
            case 1:
                ActivityKcal *= SEDENTARY;
                break;//거의없음

            case 2:
                ActivityKcal *= LIGHT;
                break;//가벼운 활동

            case 3:
                ActivityKcal *= MODERATE;
                break;//보통

            case 4:
                ActivityKcal *= ACTIVE;
                break;//많이

            case 5:
                ActivityKcal *= VERYACTIVE;
                break;//매우 많이
        }
        kcal = ActivityKcal;
    }

    //베네딕트+활동량으로 산출된 칼로리를 목적에 따라 다시 가공(최종 가공)
    private void setUserPurposeCalories(double ActivityKcal, int purpose) {

        double PurposeKcal = ActivityKcal;

        switch (purpose) {
            case 1:
                PurposeKcal *= DIET;
                break;//다이어트 일 경우

            case 2:
                PurposeKcal *= BULK;
                break;//벌크업일 경우

            case 3:
                PurposeKcal *= MAINTAIN;
                break;//유지,관리 일 경우
        }
        kcal = PurposeKcal;
    }

    private double getUserBenedictCalories(double height, double weight, String gender, int age) {
        double BenedictKcal = kcal;
        if (gender.equals(GENDER_MALE)) {
            BenedictKcal = BENEDICT_GENDER_MALE_1 + (BENEDICT_GENDER_MALE_2 * weight) + (BENEDICT_GENDER_MALE_3 * height) - (BENEDICT_GENDER_MALE_4 - age);
        } else if (gender.equals(GENDER_FEMALE)) {
            BenedictKcal = BENEDICT_GENDER_FEMALE_1 + (BENEDICT_GENDER_FEMALE_2 * weight) + (BENEDICT_GENDER_FEMALE_3 * height) - (BENEDICT_GENDER_FEMALE_4 * age);
        }
        kcal = BenedictKcal;
        return kcal;
    }

    private double getUserActivityCalories(double BenedictKcal, int activityRatio) {
        double ActivityKcal = BenedictKcal;
        switch (activityRatio) {
            case 1:
                ActivityKcal *= SEDENTARY;
                break;//거의없음

            case 2:
                ActivityKcal *= LIGHT;
                break;//가벼운 활동

            case 3:
                ActivityKcal *= MODERATE;
                break;//보통

            case 4:
                ActivityKcal *= ACTIVE;
                break;//많이

            case 5:
                ActivityKcal *= VERYACTIVE;
                break;//매우 많이
        }
        kcal = ActivityKcal;

        return kcal;
    }

    private double getUserPurposeCalories(double ActivityKcal, int purpose) {

        double PurposeKcal = ActivityKcal;

        switch (purpose) {
            case 1:
                PurposeKcal *= DIET;
                break;//다이어트 일 경우

            case 2:
                PurposeKcal *= BULK;
                break;//벌크업일 경우

            case 3:
                PurposeKcal *= MAINTAIN;
                break;//유지,관리 일 경우
        }
        kcal = PurposeKcal;

        return kcal;
    }

    private double calculateCalories(double height, double weight, String gender, int age, int activity, int purpose) {
        //해리스 배네딕트 공식 적용하기
        double kcal = 0;
        //성별 검사
        if (gender.equals(GENDER_MALE)) {
            kcal = BENEDICT_GENDER_MALE_1 + (BENEDICT_GENDER_MALE_2 * weight) + (BENEDICT_GENDER_MALE_3 * height) - (BENEDICT_GENDER_MALE_4 - age);
        } else if (gender.equals(GENDER_FEMALE)) {
            kcal = BENEDICT_GENDER_FEMALE_1 + (BENEDICT_GENDER_FEMALE_2 * weight) + (BENEDICT_GENDER_FEMALE_3 * height) - (BENEDICT_GENDER_FEMALE_4 * age);
        }
        //활동량에 따른 칼로리 계산하기
        switch (activity) {
            case 1:
                kcal *= SEDENTARY;
                break;//거의없음

            case 2:
                kcal *= LIGHT;
                break;//가벼운 활동

            case 3:
                kcal *= MODERATE;
                break;//보통

            case 4:
                kcal *= ACTIVE;
                break;//많이

            case 5:
                kcal *= VERYACTIVE;
                break;//매우 많이
        }
//목적에 따른 칼로리 계산하기
        switch (purpose) {
            case 1:
                kcal *= DIET;
                break;//다이어트 일 경우

            case 2:
                kcal *= BULK;
                break;//벌크업일 경우

            case 3:
                kcal *= MAINTAIN;
                break;//유지,관리 일 경우
        }
        return kcal;
    }
}
