package com.example.lzf.dai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

/**
 * Created by zkc on 2017/12/7.
 */

public class family extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private TextView ed1;
    private TextView ed2;
    private TextView ed3;
    private TextView ed4;
    private TextView ed5;
    private TextView ed6;
    private TextView ed7;
    private TextView ed8;
    private TextView ed9;
    private TextView ed10;
    private TextView ed11;
    private TextView ed12;
    private TextView ed13;
    private TextView ed14;
    private SharedPreferences  sp;
    private String token ;
    private String studentid;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family);
        back= (Button) findViewById(R.id.title_back);
        back.setText("返回");
        back.setOnClickListener(this);
        ed1= (TextView) findViewById(R.id.address);
        ed2= (TextView) findViewById(R.id.tel);
        ed3= (TextView) findViewById(R.id.fname);
        ed4= (TextView) findViewById(R.id.funit);
        ed5= (TextView) findViewById(R.id.funitaddress);
        ed6= (TextView) findViewById(R.id.fduty);
        ed7= (TextView) findViewById(R.id.fpostcode);
        ed8= (TextView) findViewById(R.id.ftel);
        ed9= (TextView) findViewById(R.id.mname);
        ed10= (TextView) findViewById(R.id.munit);
        ed11= (TextView) findViewById(R.id.munitaddress);
        ed12= (TextView) findViewById(R.id.mduty);
        ed13= (TextView) findViewById(R.id.mpostcode);
        ed14= (TextView) findViewById(R.id.mtel);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        Intent it=getIntent();
        Bundle bd=it.getExtras();
        studentid = bd.getString("studentID");
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/baseStudent/family/"+studentid).header("Authorization",token)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("lzf", e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String res= response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONTokener tokener=new JSONTokener(res);

                        try {
                            JSONObject object= (JSONObject) tokener.nextValue();

                            JSONObject data=object.getJSONObject("data");
                            String address=data.getString("familyAddress");
                            String tel=data.getString("familyTelNumber");
                            String fname=data.getString("fatherName");
                            String funit=data.getString("fatherWorkUnit");
                            String funitaddress=data.getString("fatherWorkUnitAddress");
                            String fduty=data.getString("fatherDuty");
                            String fpostcode=data.getString("fatherPostcode");
                            String ftel=data.getString("fatherTelNumber");
                            String mname=data.getString("motherName");
                            String munit=data.getString("motherWorkUnit");
                            String munitaddress=data.getString("motherWorkUnitAddress");
                            String mduty=data.getString("motherDuty");
                            String mpostcode=data.getString("motherPostcode");
                            String mtel=data.getString("motherTelNumber");
                            ed1.setText(address);
                            ed2.setText(tel);
                            ed3.setText(fname);
                            ed4.setText(funit);
                            ed5.setText(funitaddress);
                            ed6.setText(fduty);
                            ed7.setText(fpostcode);
                            ed8.setText(ftel);
                            ed9.setText(mname);
                            ed10.setText(munit);
                            ed11.setText(munitaddress);
                            ed12.setText(mduty);
                            ed13.setText(mpostcode);
                            ed14.setText(mtel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                Intent it= new Intent(family.this,main_student_message.class);
                Bundle bd=new Bundle();
                bd.putString("studentID", studentid);
                it.putExtras(bd);
                startActivity(it);
                break;
        }
    }
}
