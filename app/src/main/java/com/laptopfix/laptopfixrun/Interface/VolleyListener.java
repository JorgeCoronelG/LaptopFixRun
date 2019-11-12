package com.laptopfix.laptopfixrun.Interface;

public interface VolleyListener {

    void onSuccess(int code);
    void onSuccess(int code, Object object);
    void onFailure(String error);

}