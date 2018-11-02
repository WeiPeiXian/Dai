package com.example.lzf.dai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by lzf on 2017/9/28.
 */

public class h_archive_message extends AppCompatActivity implements View.OnClickListener{
//    private  Button edit;
    private Button back;
    private TextView title;
    private TextView ed1;
    private TextView ed2;
    private TextView ed3;
    private TextView ed4;
    private TextView ed5;
    private TextView ed6;
    private TextView ed7;
    private TextView ed8;
    private TextView ed9;
    private String historyid;
    private SharedPreferences sp ;
    private String token  ;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_archive_message);
        title = (TextView)findViewById(R.id.title);
//        edit= (Button) findViewById(R.id.title_edit);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        back=(Button)findViewById(R.id.title_back);
        title.setText("学生建档信息");
        ed1= (TextView) findViewById(R.id.name);
        ed2= (TextView) findViewById(R.id.studentID);
        ed3= (TextView) findViewById(R.id.family);
        ed4= (TextView) findViewById(R.id.study);
        ed5= (TextView) findViewById(R.id.health);
        ed6= (TextView) findViewById(R.id.other);
        ed7= (TextView) findViewById(R.id.jiandang);
        ed8 = (TextView)findViewById(R.id.life);
        ed9 = (TextView)findViewById(R.id.idd);
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
        ed9.setText("除档依据");

        Intent it=getIntent();
        Bundle bd=it.getExtras();
        historyid = bd.getString("historyid");
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
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                Intent it2= new Intent(h_archive_message.this,h_main_student_message.class);
                Bundle bd2=new Bundle();
                bd2.putString("studentid", String.valueOf(ed2.getText()));
                bd2.putString("historyid", historyid);
                it2.putExtras(bd2);
                startActivity(it2);
        }
    }
}
