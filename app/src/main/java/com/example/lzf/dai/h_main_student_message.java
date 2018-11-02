package com.example.lzf.dai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class h_main_student_message extends AppCompatActivity implements View.OnClickListener{
    private Button back;
    private Button jd;
    private Button jt;
    private Button jl;
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
    private TextView ed15;
    private TextView ed16;
    private TextView ed17;
    private TextView title;
    private SharedPreferences sp;
    private String token;
    private String family;
    private String study;
    private String health;
    private String other;
    private String chudangyiju;
    private String historyid;
    private String life;
    @Override
    //获取信息
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_main_student_message);
        ActionBar actionbar=getSupportActionBar();{
            if(actionbar!=null){
                actionbar.hide();
            }
        }
        title = (TextView) findViewById(R.id.title) ;
        title.setText("帮扶学生信息");
        back= (Button) findViewById(R.id.title_back);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        back.setText("返回");
        jl = (Button)findViewById(R.id.record);
        jl.setOnClickListener(this);
        ed1= (TextView) findViewById(R.id.fname);
        ed2= (TextView) findViewById(R.id.fnum);
        ed3= (TextView) findViewById(R.id.fnianji);
        ed4= (TextView) findViewById(R.id.fbanji);
        ed5= (TextView) findViewById(R.id.fxingbie);
        ed6= (TextView) findViewById(R.id.fmingzu);
        ed7= (TextView) findViewById(R.id.fzhengzhi);
        ed8 = (TextView)findViewById(R.id.fzhiwu);
        ed9= (TextView) findViewById(R.id.fjiating);
        ed10= (TextView) findViewById(R.id.fsushe);
        ed11= (TextView) findViewById(R.id.flianxi);
        ed12 = (TextView)findViewById(R.id.fjiandang);
        ed13 = (TextView)findViewById(R.id.fjiandangren);
        ed14 = (TextView)findViewById(R.id.fbangfu);
        ed15 = (TextView)findViewById(R.id.fguanzhu);
        ed16 = (TextView)findViewById(R.id.friqi);
        ed17 = (TextView)findViewById(R.id.fchudang);
        Intent itm=getIntent();
        Bundle bdm=itm.getExtras();
        historyid = bdm.getString("historyid");
        String studentid = bdm.getString("studentid");

        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/historyArchive/"+historyid).header("Authorization",token)
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
                        JSONTokener tokener=new
                                JSONTokener(res);

                        try {
                            JSONObject object= (JSONObject) tokener.nextValue();

                            JSONObject data=object.getJSONObject("data");
                                String jiandang=data.getString("bulidingTime");
                                String chudang=data.getString("destoryingTime");
                                String buildman=data.getString("bulidingPerson");
                                String buildmanduty=data.getString("bulidingPersonDuty");
                                String bangfu=data.getString("helpType");
                                String guanzhu=data.getString("attentionType");
                                life = data.getString("lifeCondition");
                                family=data.getString("familyCondition");
                                study=data.getString("studyCondition");
                                health=data.getString("healthCondition");
                                other=data.getString("otherCondition");
//                                jiandangyiju=data.getString("bulidingBasis");
                                chudangyiju=data.getString("destoryingBasis");
                                ed12.setText(buildman);
                                ed13.setText(buildmanduty);
                                ed14.setText(bangfu);
                                ed15.setText(guanzhu);
                                ed16.setText(jiandang);
                                ed17.setText(chudang);
                            } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        OkHttpClient mOkHttpClient1 = new OkHttpClient();
        final Request request1 = new Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/baseStudent/personal/"+studentid).header("Authorization",token)
                .build();
        Call call1 = mOkHttpClient1.newCall(request1);
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) { Log.e("lzf", e.getMessage()); }

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
                            String zhengzhi=data.getString("politicalStatus");
                            String studentid = data.getString("studentId");
                            String xingbie=data.getString("sex");
                            String name=data.getString("name");
                            String nianji=data.getString("grade");
                            String banji=data.getString("studentClass");
                            String mingzu=data.getString("ethnicGroup");
                            String duty =data.getString("duty");
                            String zhuzhi=data.getString("familyAddress");
                            String sushe=data.getString("dormitory");
                            String lianxi=data.getString("contactWay");
                            ed7.setText(judge(zhengzhi));
                            ed1.setText(judge(name));
                            ed2.setText(judge(studentid));
                            ed3.setText(judge(nianji));
                            ed4.setText(judge(banji));
                            ed5.setText(judge(xingbie));
                            ed6.setText(judge(mingzu));
                            if (duty==null||duty.trim().isEmpty()){
                                ed8.setText("-");
                            }
                            else {
                                ed8.setText(duty);
                            }
                            ed9.setText(judge(zhuzhi));
                            ed10.setText(judge(sushe));
                            ed11.setText(judge(lianxi));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        });


        jd= (Button) findViewById(R.id.jiandangxinxi);
        jt= (Button) findViewById(R.id.jiatingxinxi);
        jd.setOnClickListener(this);
        jt.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    public String judge(String m){
        if (m==null||m.trim().isEmpty()||m=="null"){
            return "-";
        }
        else {
            return m;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.jiatingxinxi:
                Intent it2 =new Intent(h_main_student_message.this,h_family.class);
                Bundle bd2=new Bundle();
                bd2.putString("studentID", String.valueOf(ed2.getText()));
                bd2.putString("historyid",historyid);
                it2.putExtras(bd2);
                startActivity(it2);
                break;
            case R.id.jiandangxinxi:
                Intent it3 =new Intent(h_main_student_message.this,h_archive_message.class);
                Bundle bd3=new Bundle();
                bd3.putString("studentID", String.valueOf(ed2.getText()));
                bd3.putString("name", String.valueOf(ed1.getText()));
                bd3.putString("family",family);
                bd3.putString("life",life);
                bd3.putString("study",study);
                bd3.putString("health",health);
                bd3.putString("other",other);
                bd3.putString("jiandang",chudangyiju);
                bd3.putString("historyid",historyid);
                it3.putExtras(bd3);
                startActivity(it3);
                break;
            case R.id.record:
                Intent it6= new Intent(h_main_student_message.this,h_record.class);
                Bundle bundle=new Bundle();
                bundle.putString("historyid",historyid);
                bundle.putString("studentid",String.valueOf(ed2.getText()));
                bundle.putString("name",String.valueOf(ed1.getText()));
                bundle.putInt("from",1);
                it6.putExtras(bundle);
                startActivity(it6);
                break;
            case R.id.title_back:
                Intent it5= new Intent(h_main_student_message.this,history.class);
                startActivity(it5);
        }
    }
}



