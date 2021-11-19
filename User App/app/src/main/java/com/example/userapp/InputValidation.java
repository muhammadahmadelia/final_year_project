package com.example.userapp;

import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InputValidation {
    public boolean isValidEmail(String email){
        if(!email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        return false;
    }

    public boolean isValidPassword(String password){
        if(!password.isEmpty() || !(password.length() <= 4) || !(password.length() > 10))
            return true;
        return false;
    }

    public boolean isValidName(String name){
        if(name.isEmpty())
            return false;
        return true;
    }

    public String getDate(){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        return format.format(date);
    }

    String getMonth(int month){
        String m = "";
        switch (month){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return m;
    }
}
