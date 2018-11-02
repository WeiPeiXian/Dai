package com.example.lzf.dai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lzf on 2017/9/28.
 */

public class change_student_message extends AppCompatActivity implements View.OnClickListener{
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private Button tj;
    private Button back;
    private TextView name;
    private TextView studentId;
    private EditText nianji;
    private EditText banji;
    private EditText zhengzhi;
    private EditText zhiwu;
    private EditText address;
    private EditText sushe;
    private EditText contactWay;
    private EditText jiandangren;
    private EditText jiandangrenduty;
    private Spinner guanzhuzhuangtai;
    private SharedPreferences sp;
    private String token;
    private MySpinner bangfuleixing;
    private static final String[] m={"学业困难","家庭困难","心理困难"};
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_student_message);
        tj= (Button) findViewById(R.id.title_edit);
        tj.setText("提交");
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        tj.setOnClickListener(this);
        back= (Button) findViewById(R.id.title_back);
        back.setOnClickListener(this);
        guanzhuzhuangtai = (Spinner) findViewById(R.id.addspinner2);
        bangfuleixing = (MySpinner) findViewById((R.id.button));

        bangfuleixing.initContent(m);
        name= (TextView) findViewById(R.id.fname);
        studentId= (TextView) findViewById(R.id.fnum);
        nianji = (EditText)findViewById(R.id.fnianji);
        banji = (EditText)findViewById(R.id.fbanji);
        zhengzhi = (EditText)findViewById(R.id.fzhengzhi);
        zhiwu = (EditText)findViewById(R.id.fzhiwu);
        address = (EditText)findViewById(R.id.fjiating);
        sushe = (EditText)findViewById(R.id.fsushe);
        contactWay = (EditText)findViewById(R.id.flianxi);
        jiandangren = (EditText)findViewById(R.id.fjiandang);

        jiandangrenduty = (EditText)findViewById(R.id.fjiandangren);

        Intent it2=getIntent();
        Bundle bd2=it2.getExtras();
        String n1= (String) bd2.get("name");
        String n2= (String) bd2.get("sdtuentId");
        String n3= (String) bd2.get("nianji");
        String n4= (String) bd2.get("banji");
        String n5= (String) bd2.get("zhengzhi");
        String n6= (String) bd2.get("zhiwu");
        String n7= (String) bd2.get("adress");
        String n8= (String) bd2.get("sushe");
        String n9= (String) bd2.get("contactWay");
        String n10= (String) bd2.get("jiandangren");
        String n11= (String) bd2.get("jiandangrenduty");
        String n12= (String) bd2.get("guanzhu");
        bangfuleixing.setText(bd2.getString("bangfu"));
        name.setText(n1);
        studentId.setText(n2);
        nianji.setText(n3);
        banji.setText(n4);
        zhengzhi.setText(n5);
        zhiwu.setText(n6);
        address.setText(n7);
        sushe.setText(n8);
        contactWay.setText(n9);
        jiandangren.setText(n10);
        jiandangrenduty.setText(n11);
        setSpinnerItemSelectedByValue(guanzhuzhuangtai,n12);
    }

    public  void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i);// 默认选中项

                break;
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_edit:

                final String n1 = String.valueOf(studentId.getText());
                final String n2 = String.valueOf(name.getText());
                String n3 = String.valueOf(nianji.getText());
                String n4 = String.valueOf(banji.getText());
                String n5 = String.valueOf(zhengzhi.getText());
                String n6 = String.valueOf(zhiwu.getText());
                String n7 = String.valueOf(address.getText());
                String n8 = String.valueOf(sushe.getText());
                String n9 = String.valueOf(contactWay.getText());
                String n10 = String.valueOf(jiandangren.getText());
                String n11 = String.valueOf(jiandangrenduty.getText());
                String n12 = String.valueOf(bangfuleixing.getText());
                String n13 = (String)guanzhuzhuangtai.getSelectedItem();

                OkHttpClient okHttpClient  = new OkHttpClient.Builder().build();
                JSONObject base_student = new JSONObject();
                try {
                    base_student.put("studentId",n1);
                    base_student.put("name",n2);
                    base_student.put("grade",n3);
                    base_student.put("studentClass",n4);
                    base_student.put("politicalStatus",n5);
                    base_student.put("duty",n6);
                    base_student.put("familyAddress",n7);
                    base_student.put("dormitory",n8);
                    base_student.put("contactWay",n9);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject teacher = new JSONObject();
                try {
                    String teacherID = sp.getString("USER_NAME","");
                    String pw= sp.getString("PASSWORD","");
                    teacher.put("teacherId",teacherID);
                    teacher.put("password",pw);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject test = new JSONObject();
                try {
                    test.put("baseStudent",base_student);
                    test.put("teacher",teacher);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                FormBody.Builder formbody= new FormBody.Builder();
                formbody.add("json",test.toString());
                FormBody formBody= formbody.build();

                final Request request = new Request.Builder()
                        .url("http://180.76.249.233:8080/newhelp/api/baseStudent").header("Authorization",token)
                        .post(formBody)
                        .build();

                Call call = okHttpClient.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("连接失败");
                    }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if(response.code()==200) {
                            final String ne =response.body().string();
                            System.out.println(ne+"个人信息更新成功");

                        }
                    }
                });

                OkHttpClient okHttpClient1 = new OkHttpClient.Builder().build();
                FormBody.Builder formbody1 = new FormBody.Builder();
                final JSONObject jiandang = new JSONObject();
                try {
                    jiandang.put("studentId", n1);
                    jiandang.put("name", n2);
                    jiandang.put("grade", n3);
                    jiandang.put("politicalStatus", n5);
                    jiandang.put("studentClass", n4);
                    jiandang.put("duty",n6);
                    jiandang.put("dormitory", n8);
                    jiandang.put("familyAddress", n7);
                    jiandang.put("contactWay", n9);
                    jiandang.put("bulidingPerson", n10);
                    jiandang.put("bulidingPersonDuty", n11);
                    jiandang.put("helpType",n12);
                    jiandang.put("attentionType",n13);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new Thread() {
                    public void run() {
                        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                                .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                                .connectTimeout(60, TimeUnit.SECONDS).build();//设置连接超时时间.build();
                        RequestBody requestBody = RequestBody.create(JSON, jiandang.toString());
                        FormBody.Builder formbody = new FormBody.Builder();
                        formbody.add("json", jiandang.toString());
                        FormBody formBody = formbody.build();
                        final Request request = new Request.Builder()
                                .url("http://180.76.249.233:8080/newhelp/api/archiveStudent").header("Authorization",token)
                                .put(requestBody)
                                .build();
                        Call call = client.newCall(request);

                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                System.out.println("连接失败");
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                if (response.code() == 200) {{
                                    final String ne = response.body().string();
                                    final Runnable runable = new Runnable(){
                                        public void run() {

                                            JSONTokener tokener = new JSONTokener(ne);
                                            JSONObject object = null;
                                            try {
                                                object = (JSONObject) tokener.nextValue();
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                            String success =null;
                                            try {
                                                success = object.getString("success");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            String message =null;
                                            try {
                                                message = object.getString("message");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if(success == "true")
                                            {
                                                final String su = success;
                                                final String finalMessage = message;
                                                Runnable run = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(change_student_message.this, finalMessage, Toast.LENGTH_LONG).show();
                                                    }
                                                };
                                                runOnUiThread(run);
                                                Intent it2=new Intent(change_student_message.this,main_student_message.class);
                                                Bundle bd=new Bundle();
                                                bd.putString("studentID", n1);
                                                it2.putExtras(bd);
                                                startActivity(it2);

                                            }
                                            else {
                                                final String su = success;
                                                final String finalMessage1 = message;
                                                Runnable run = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(change_student_message.this, finalMessage1, Toast.LENGTH_SHORT).show();
                                                    }
                                                };
                                                runOnUiThread(run);
                                            }


                                        }

                                    };
                                    Thread thread = new Thread(runable);
                                    thread.start();
                                    try {
                                        thread.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                }
                                else {
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(change_student_message.this,"错误代码"+response.code(), Toast.LENGTH_SHORT).show();
                                        }
                                    };
                                    runOnUiThread(runnable);
                                }
                            }
                        });
                    }
                }.start();

                break;
            case R.id.title_back:
                Intent it= new Intent(change_student_message.this,main_student_message.class);
                Bundle bd1=new Bundle();
                bd1.putString("studentID", String.valueOf(studentId.getText()));
                it.putExtras(bd1);
                startActivity(it);
                break;
        }
    }
}
