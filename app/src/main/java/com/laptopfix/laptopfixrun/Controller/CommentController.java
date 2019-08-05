package com.laptopfix.laptopfixrun.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Model.Comment;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CommentController {

    private VolleyListener mListener;
    private StringRequest request;
    private Context context;
    private AlertDialog dialog;

    public CommentController(Context context) {
        this.context = context;
    }

    public void insert(final Comment comment){
        createDialog(context.getString(R.string.waitAMoment));

        String url = Common.URL + CommunicationPath.COMMENT_INSERT;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        dialog.dismiss();
                        if(mListener != null){
                            getComments(CommunicationCode.CODE_COMMENT_INSERT);
                        }
                    }else{
                        dialog.dismiss();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("comment", comment.getComment());
                map.put("score", String.valueOf(comment.getScore()));
                map.put("idCus", String.valueOf(comment.getCustomer().getId()));
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void getComments(final int code){
        createDialog(context.getString(R.string.loading_comments));

        String url = Common.URL + CommunicationPath.GET_COMMENTS;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        ArrayList<Comment> comments = new ArrayList<>();
                        JSONArray array = jsonObject.getJSONArray("comments");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject data = array.getJSONObject(i);

                            Comment comment = new Comment();
                            comment.setComment(data.getString("comment"));
                            comment.setScore(data.getInt("score"));
                            comment.setDateComment(data.getString("date"));

                            Customer customer = new Customer();
                            customer.setName(data.getString("nameCus"));
                            comment.setCustomer(customer);

                            comments.add(comment);
                        }
                        dialog.dismiss();
                        if(mListener != null){
                            mListener.requestGetComments(comments, code);
                        }
                    }else{
                        dialog.dismiss();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    private void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setMessage(message)
                .build();
        dialog.show();
    }

    public interface VolleyListener {
        void requestGetComments(ArrayList<Comment> comments, int code);
    }

    public void setVolleyListener(VolleyListener volleyListener) {
        this.mListener = volleyListener;
    }
}