package com.example.lzf.dai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by zkc on 2017/12/21.
 */

public class h_record_week extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private TextView title;
    private TextView ed1;
    private TextView ed2;
    private EditText ed3;
    private EditText ed4;
    private EditText ed5;
    private EditText ed6;
    private EditText ed7;
    private EditText ed8;
    private String recordid;
    private String historyid;
    private String studentid;
    private String name;
    private String jl;
    private SharedPreferences sp;
    private String token ;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_record_week);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        back= (Button) findViewById(R.id.title_back);
        back.setText("返回");
        back.setOnClickListener(this);
        ed1= (TextView) findViewById(R.id.name);
        ed2= (TextView) findViewById(R.id.studentID);
        ed3= (EditText) findViewById(R.id.recorder);
        ed4= (EditText) findViewById(R.id.time);
        ed5= (EditText) findViewById(R.id.address);
        ed6= (EditText) findViewById(R.id.type);
        ed7= (EditText) findViewById(R.id.mainmessage);
        ed8= (EditText) findViewById(R.id.other);
        ed7.setMinHeight(600);
        ed7.setMinimumHeight(600);
        ed8.setMinHeight(300);
        ed8.setMinimumHeight(300);

        title = (TextView)findViewById(R.id.title);
        Intent it=getIntent();
        Bundle bd=it.getExtras();
        recordid = bd.getString("recordid");
        System.out.println(recordid);
        studentid = bd.getString("studentid");
        name = bd.getString("name");
        jl = bd.getString("record");
        historyid= bd.getString("historyid");
        ed1.setText(name);
        title.setText(jl);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/historyRecord/"+recordid).header("Authorization",token)
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
                System.out.println(res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONTokener tokener=new JSONTokener(res);
                        try {
                            JSONObject object= (JSONObject) tokener.nextValue();
                            JSONObject data=object.getJSONObject("data");
                            String time=data.getString("recordTime");
                            String location=data.getString("location");
                            String recorder=data.getString("recorder");
                            String way=data.getString("way");
                            String content=data.getString("content");
                            String comment=data.getString("comment");
                            ed1.setText(name);
                            ed2.setText(studentid);
                            ed3.setText(recorder);
                            ed4.setText(time);
                            ed5.setText(location);
                            ed6.setText(way);
                            ed7.setText(content);
                            ed8.setText(comment);
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
                Intent it= new Intent(h_record_week.this,h_record.class);
                Bundle bd=new Bundle();
                bd.putString("studentid", studentid);
                bd.putString("historyid",historyid);
                bd.putString("name",name);
                bd.putString("jilu",jl);
                bd.putInt("from",2);
                it.putExtras(bd);
                startActivity(it);
                break;
        }
    }
}
