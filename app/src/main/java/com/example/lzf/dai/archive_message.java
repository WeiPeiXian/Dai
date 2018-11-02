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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lzf on 2017/9/28.
 */

public class archive_message extends AppCompatActivity implements View.OnClickListener{
    private  Button edit;
    private Button back;
    private TextView title;
    private TextView ed1;
    private TextView ed2;
    private TextView ed3;
    private TextView ed4;
    private TextView ed5;
    private TextView ed6;
    private SharedPreferences sp;
    private String token;
    private TextView ed7;
    private TextView ed8;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_message);
        title = (TextView)findViewById(R.id.title);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        edit= (Button) findViewById(R.id.title_edit);
        back=(Button)findViewById(R.id.title_back);
        edit.setText("修改");
        title.setText("学生建档信息");
        ed1= (TextView) findViewById(R.id.name);
        ed2= (TextView) findViewById(R.id.studentID);
        ed3= (TextView) findViewById(R.id.family);
        ed4= (TextView) findViewById(R.id.study);
        ed5= (TextView) findViewById(R.id.health);
        ed6= (TextView) findViewById(R.id.other);
        ed7= (TextView) findViewById(R.id.jiandang);
        ed8 = (TextView)findViewById(R.id.life);
        ed7.setMinHeight(300);
        ed7.setMinimumHeight(300);
        ed8.setMinHeight(300);
        ed8.setMinimumHeight(300);
        ed3.setMinHeight(300);
        ed3.setMinimumHeight(300);
        ed4.setMinHeight(300);
        ed4.setMinimumHeight(300);
        ed5.setMinHeight(300);
        ed5.setMinimumHeight(300);
        ed6.setMinHeight(300);
        ed6.setMinimumHeight(300);
        Intent it=getIntent();
        Bundle bd=it.getExtras();
        String studentid = bd.getString("studentID");
        String name=bd.getString("name");
        String family=bd.getString("family");
        String study=bd.getString("study");
        String health=bd.getString("health");
        String other=bd.getString("other");
        String life = bd.getString("life");
        String jiandang=bd.getString("jiandang");
        ed1.setText(name);
        ed2.setText(studentid);
        ed3.setText(family);
        ed4.setText(study);
        ed5.setText(health);
        ed6.setText(other);
        ed7.setText(jiandang);
        ed8.setText(life);
        edit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_edit:
                Intent it4= new Intent(archive_message.this,archive_changing.class);
                Bundle bd1=new Bundle();
                bd1.putString("name",String.valueOf(ed1.getText()));
                System.out.println(String.valueOf(ed1.getText()));
                bd1.putString("studentID",String.valueOf(ed2.getText()));
                bd1.putString("family", String.valueOf(ed3.getText()));
                bd1.putString("study", String.valueOf(ed4.getText()));
                bd1.putString("health", String.valueOf(ed5.getText()));
                bd1.putString("other", String.valueOf(ed6.getText()));
                bd1.putString("jiangdangyiju", String.valueOf(ed7.getText()));
                bd1.putString("life",String.valueOf(ed8.getText()));
                it4.putExtras(bd1);
                startActivity(it4);
                break;
            case R.id.title_back:
                Intent it2= new Intent(archive_message.this,main_student_message.class);
                Bundle bd2=new Bundle();
                bd2.putString("studentID", String.valueOf(ed2.getText()));
                it2.putExtras(bd2);
                startActivity(it2);
        }
    }
}
