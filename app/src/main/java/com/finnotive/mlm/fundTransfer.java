package com.finnotive.mlm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class fundTransfer extends AppCompatActivity {
    private EditText recipient_id,amount;
    private RegistrationModel registrationModel;
    private Button sendmoney;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundtransfer);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registrationModel = new RegistrationModel();
        recipient_id=findViewById(R.id.recipient_id);
        amount=findViewById(R.id.amount);
        builder = new AlertDialog.Builder(this);
        sendmoney=findViewById(R.id.sendmoney);
        sendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check()) {
                    getdata();
                }
            }
        });

    }





    Boolean check() {
        registrationModel.setMobile(recipient_id.getText().toString());
        registrationModel.setPassword(amount.getText().toString());
        if (TextUtils.isEmpty(registrationModel.getMobile())) {
            recipient_id.setError("please enter recipient number");
            return false;
        }


        if (TextUtils.isEmpty(registrationModel.getPassword())) {
            amount.setError("please enter a amount");
            return false;
        }

        return true;
    }







    void getdata() {


        Toast.makeText(getApplicationContext(), "please wait.....", Toast.LENGTH_SHORT).show();
        try {


            StringRequest sr = new StringRequest(Request.Method.POST, Constrains.fundTransfer, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //  ProgressBarUtil.dismiss();
                    //   Toast.makeText(getApplicationContext(), "responce " + response.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("MyApp", "response " + response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String res=jsonObject.getString("amount");
                        if(!res.equalsIgnoreCase(null)){
                            msg();
                        }

                    } catch (Exception e) {
                        Log.d("MyApp", "status" + e.toString());
                        //  Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //  ProgressBarUtil.dismiss();
                    Log.d("MyApp", error.toString());
                    //   Log.d("MyApp", error.getMessage());

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    //SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES,getActivity().MODE_PRIVATE);
                    params.put("user_id", SharedpreferenceUtility.getInstance(getApplicationContext()).getString("primaryNumber"));
                    params.put("recipient_id", recipient_id.getText().toString());
                    params.put("amount", amount.getText().toString());

                    return params;

                }




            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            sr.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(sr);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
    void msg() {
        builder.setMessage("Thanks").setTitle("Transfered");

        //Setting message manually and performing action on button click
        builder.setMessage("You have transfered amount successfully")
                .setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(),Dashboad.class));
                        finish();

                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(),Dashboad.class));
                        finish();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Success");
        alert.show();
    }

}


