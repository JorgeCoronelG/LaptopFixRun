package com.laptopfix.laptopfixrun.Util;

import com.laptopfix.laptopfixrun.Remote.IGoogleAPI;
import com.laptopfix.laptopfixrun.Remote.RetrofitClient;

public class Common {

    public static final String URL = "http://192.168.0.5/LaptopFixRun/";

    public static final double LATITUDE_LAPTOP_FIX = 20.583994;
    public static final double LONGITUDE_LAPTOP_FIX = -100.395728;
    public static final int WIDTH_ROUTE = 10;
    public static final int SUNDAY = 1;
    public static final int SATURDAY = 7;
    public static final int WORKING_SATURDAY_START_LF = 10;
    public static final int WORKING_SATURDAY_FINISH_LF = 14;
    public static final int WORKING_ALL_WEEK_START_LF = 9;
    public static final int WORKING_ALL_WEEK_FINISH_LF = 19;

    public static final int TYPE_USER_LAPTOP_FIX = 1;
    public static final int TYPE_USER_CUSTOMER = 2;

    public static final String CUSTOMER_TABLE = "Customers";
    public static final String LAPTOP_FIX_TABLE = "Laptopfix";

    public static final String baseURL = "https://maps.googleapis.com";
    public static IGoogleAPI getGoogleAPI(){
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

}