package com.laptopfix.laptopfixrun.Interface;

import com.laptopfix.laptopfixrun.Model.DateHome;

public interface VolleyListenerInsertDateHome {

    void onSuccess(DateHome dateHome, int code);
    void onFailure(String error);

}