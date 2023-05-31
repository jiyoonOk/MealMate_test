package com.anonymous.mealmate.feature;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.lang.Integer;

public class Date { //date entity  do not erase

    // 싱글톤 적용하여 하나의 날짜를 모든 클래스에서 사용할 필요성이 있음 static으로 구현
    private static Date instance = null;
    private  final static int ERROR_CODE = -1;

    private  int  year = ERROR_CODE;
    private   int month= ERROR_CODE;
    private   int dayOfMonth =ERROR_CODE;
    private   int dayOfWeek=ERROR_CODE;

    private String dateToString = null;
    private String dayOfWeekToString = "";




    public final static int WEEK_SUNDAY = 1;
    public final static int WEEK_MONDAY = 2 ;
    public final static int WEEK_TUESDAY = 3;
    public final static int WEEK_WEDNESDAY = 4;
    public final static int WEEK_THURSDAY = 5;
    public final static int WEEK_FRIDAY = 6;
    public final static int WEEK_SATURDAY = 7;

    private MutableLiveData<Integer> dateChangedSignalLiveData = new MutableLiveData<>(0);

    public LiveData<Integer> getDateChangedSignalLiveData() {
        return dateChangedSignalLiveData;
    }

    private void activeDateChangedSignal(){
        dateChangedSignalLiveData.setValue(dateChangedSignalLiveData.getValue().intValue()==1?0:1);
    }

    public void set(int year,int month,int dayOfMonth, int dayOfWeek){
        this.year = year;
        this.month=month;
        this.dayOfMonth=dayOfMonth;
        this.dayOfWeek=dayOfWeek;

        setDayOfWeekToString();
        activeDateChangedSignal();
    }
    public static Date getInstance(){
        if(instance == null){
            synchronized (Date.class){
                if(instance == null){
                    instance = new Date();
                }
            }
        }
        return instance;
    }
    public Date(){ // 초기 실행 위한 생성자, 오늘 날짜로 초기화 해줌
        setToDayDateTime();
        //liveData 초기화
        dateChangedSignalLiveData.postValue(0);
    }
    public Date setToDayDateTime(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        setDayOfWeekToString();


        activeDateChangedSignal();

        return this;
    }

    public String getDayOfWeekToString() {
        return dayOfWeekToString;
    }
    private Date setDayOfWeekToString(){
        switch (dayOfWeek){
            case WEEK_SUNDAY: dayOfWeekToString ="일"; break;
            case WEEK_MONDAY: dayOfWeekToString ="월"; break;
            case WEEK_TUESDAY:dayOfWeekToString="화";  break;
            case WEEK_WEDNESDAY:dayOfWeekToString="수"; break;
            case WEEK_THURSDAY:dayOfWeekToString="목"; break;
            case WEEK_FRIDAY:dayOfWeekToString="금"; break;
            case WEEK_SATURDAY:dayOfWeekToString="토"; break;
            case ERROR_CODE: dayOfWeekToString = "None"; break;
        }

        return this;
    }
    public String getDateToString(){
        String date = null;
        if(month!=ERROR_CODE&&dayOfMonth!=ERROR_CODE&&dayOfWeek!=ERROR_CODE) {
            SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
            date = format.format(new java.util.Date(year, month, dayOfMonth));

        }else
            date = "";
        return date;
    }




    public int getYear() {
        return year;
    }

    public Date setYear(int year) {
        this.year = year;
        return this;
    }

    public Date setMonth(int month) {
        this.month = month;
        return this;
    }
    public Date setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        return this;
    }

    public Date setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }


}
