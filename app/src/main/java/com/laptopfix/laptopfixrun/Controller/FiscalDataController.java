package com.laptopfix.laptopfixrun.Controller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.FiscalData;
import com.laptopfix.laptopfixrun.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FiscalDataController implements Response.Listener<String>, Response.ErrorListener {

    private StringRequest request;
    private Context context;
    private VolleyListener mVolleyListener;
    private Customer customer;

    public FiscalDataController(Context context) {
        this.context = context;
    }

    public void get(final String customer){
        String url = Constants.URL + CommunicationPath.FISCAL_DATA_GET;
        request = new StringRequest(Request.Method.POST, url, this, this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("customer", customer);
                return map;
            }
        };
        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void update(final FiscalData fiscalData){
        String url = Constants.URL + CommunicationPath.FISCAL_DATA_UPDATE;
        request = new StringRequest(Request.Method.POST, url, this, this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("customer", fiscalData.getCustomer());
                map.put("name", fiscalData.getName());
                map.put("address", fiscalData.getAddress());
                map.put("phone", fiscalData.getPhone());
                map.put("rfc", fiscalData.getRfc());
                map.put("cfdi", fiscalData.getCfdi());
                map.put("email", fiscalData.getEmail());
                return map;
            }
        };
        Communication.getmInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            switch (jsonObject.getInt("code")){
                case CommunicationCode.CODE_GET_FISCAL_DATA:
                    JSONObject data = jsonObject.getJSONObject("data");
                    FiscalData fiscalData = new FiscalData();
                    fiscalData.setCustomer(data.getString("customer"));
                    fiscalData.setName(data.getString("name"));
                    fiscalData.setAddress(data.getString("address"));
                    fiscalData.setPhone(data.getString("phone"));
                    fiscalData.setRfc(data.getString("rfc"));
                    fiscalData.setCfdi(data.getString("cfdi"));
                    fiscalData.setEmail(data.getString("email"));
                    mVolleyListener.onSuccess(CommunicationCode.CODE_GET_FISCAL_DATA, fiscalData);
                    break;
                case CommunicationCode.CODE_UPDATE_FISCAL_DATA:
                    mVolleyListener.onSuccess(CommunicationCode.CODE_UPDATE_FISCAL_DATA);
                    break;
            }
        } catch (JSONException e) {
            mVolleyListener.onFailure(e.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mVolleyListener.onFailure(error.toString());
    }

    public void setmVolleyListener(VolleyListener mVolleyListener) {
        this.mVolleyListener = mVolleyListener;
    }
}