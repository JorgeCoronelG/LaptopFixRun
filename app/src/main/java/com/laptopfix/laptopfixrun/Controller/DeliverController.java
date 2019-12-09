package com.laptopfix.laptopfixrun.Controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Deliver;
import com.laptopfix.laptopfixrun.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeliverController implements Response.Listener<String>, Response.ErrorListener {

    private StringRequest request;
    private Context context;
    private VolleyListener mVolleyListener;

    public DeliverController(Context context) {
        this.context = context;
    }

    public void insert(final Deliver deliver, final String idTech, final String baseService){
        String url = Constants.URL + CommunicationPath.DELIVER_INSERT;
        request = new StringRequest(Request.Method.POST, url, this, this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("dateH", deliver.getDateHome().getId());
                map.put("desc", deliver.getDescDel());
                map.put("cost", deliver.getCostDel());
                map.put("idTech", idTech);
                map.put("baseService", baseService);
                return map;
            }
        };
        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void getBaseService(){
        String url = Constants.URL + CommunicationPath.GET_BASE_SERVICE;
        request = new StringRequest(Request.Method.POST, url, this, this);
        Communication.getmInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (jsonObject.getInt("code")){
                case CommunicationCode.CODE_DELIVER_INSERT:
                    mVolleyListener.onSuccess(CommunicationCode.CODE_DELIVER_INSERT);
                    break;
                case CommunicationCode.CODE_GET_BASE_SERVICE:
                    String baseService = jsonObject.getString("baseService");
                    mVolleyListener.onSuccess(CommunicationCode.CODE_GET_BASE_SERVICE, baseService);
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