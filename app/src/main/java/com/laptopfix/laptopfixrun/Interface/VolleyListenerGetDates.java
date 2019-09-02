package com.laptopfix.laptopfixrun.Interface;

import com.laptopfix.laptopfixrun.Model.DateLF;

import java.util.ArrayList;

public interface VolleyListenerGetDates {

    void onSuccess(ArrayList<DateLF> dates, int code);
    void onFailure(String message);

}