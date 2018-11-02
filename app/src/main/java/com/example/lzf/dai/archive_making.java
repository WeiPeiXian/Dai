package com.example.lzf.dai;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * Created by lzf on 2017/9/28.
 */

public class archive_making extends AppCompatActivity implements View.OnClickListener{
    private Button submit;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;
    private EditText ed5;
    private SharedPreferences  sp;

    private EditText ed6;
    private TextView name;
    private TextView studentID;
    private Button back;
    private String token ;
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_making);
        back = (Button) findViewById(R.id.title_back);
        submit= (Button) findViewById(R.id.title_edit);
        submit.setText("提交");
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        ed1= (EditText) findViewById(R.id.jiating);
        ed2= (EditText) findViewById(R.id.xuexi);
        ed3= (EditText) findViewById(R.id.shenxin);
        ed4= (EditText) findViewById(R.id.qita);
        ed5= (EditText) findViewById(R.id.jiandang);
        ed6= (EditText) findViewById(R.id.life);
        name= (TextView) findViewById(R.id.name);
        ed1.setMinHeight(300);
        ed1.setMinimumHeight(300);
        ed2.setMinHeight(300);
        ed2.setMinimumHeight(300);
        ed3.setMinHeight(300);
        ed3.setMinimumHeight(300);
        ed4.setMinHeight(300);
        ed4.setMinimumHeight(300);
        ed5.setMinHeight(300);
        ed5.setMinimumHeight(300);
        ed6.setMinHeight(300);
        ed6.setMinimumHeight(300);
        studentID = (TextView)findViewById(R.id.studentID);
        Intent it1 = getIntent();
        Bundle bd1 = it1.getExtras();
        int edit_or_not = bd1.getInt("edit_or_not");
        name.setText(bd1.getString("name"));
        studentID.setText(bd1.getString("studentID"));
        if (edit_or_not == 1){
            String n1 = bd1.getString("jiating");
            String n2 = bd1.getString("xuexi");
            String n3 = bd1.getString("shenxin");
            String n4 = bd1.getString("qita");
            String n5 = bd1.getString("jiandang");
            String n6 = bd1.getString("shenghuo");

            ed1.setText(n1);
            ed2.setText(n2);
            ed3.setText(n3);
            ed4.setText(n4);
            ed5.setText(n5);
            ed6.setText(n6);
        }
    }
    public void submit_ok(){
        Intent itm = getIntent();
        Bundle bd = itm.getExtras();
        String name = bd.getString("name");
        String studentID = bd.getString("studentID");
        String jiandangren = bd.getString("jiandangren");
        String jiandangrenzhiwu = bd.getString("jiandangrenzhiwu");
        String gaunzhuleixing = bd.getString("gaunzhuleixing");
        String bang = bd.getString("bangfu");
        String date = bd.getString("date");
        String jiating = String.valueOf(ed1.getText());
        String xuexi = String.valueOf(ed2.getText());
        String shenxin = String.valueOf(ed3.getText());
        String qita = String.valueOf(ed4.getText());
        String jiandang = String.valueOf(ed5.getText());
        String shenghuo = String.valueOf(ed6.getText());
//        final Intent open_page = new Intent(archive_making.this, open_page.class);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder formbody = new FormBody.Builder();
        final JSONObject post = new JSONObject();
        try {
            String teacher = sp.getString("USER_NAME","");
            post.put("studentId", studentID);
            post.put("teacherId", teacher);
            post.put("studyCondition", xuexi);
            post.put("familyCondition", jiating);
            post.put("healthCondition", shenxin);
            post.put("otherCondition", qita);
            post.put("helpType",bang);
            post.put("bulidingBasis", jiandang);
            post.put("bulidingTime", date);
            post.put("lifeCondition", shenghuo);
            post.put("bulidingPerson", jiandangren);
            post.put("bulidingPersonDuty", jiandangrenzhiwu);
            post.put("attentionType",gaunzhuleixing);
            post.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread() {
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                        .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                        .connectTimeout(60, TimeUnit.SECONDS).build();//设置连接超时时间.build();
                RequestBody requestBody = RequestBody.create(JSON, post.toString());
                FormBody.Builder formbody = new FormBody.Builder();
                formbody.add("json", post.toString());
                FormBody formBody = formbody.build();
                final Request request = new Request.Builder()
                        .url("http://180.76.249.233:8080/newhelp/api/archiveStudent").header("Authorization",token)
                        .post(requestBody)
                        .build();
//格式一
                Call call = client.newCall(request);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("连接失败");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.code() == 200) {
                            final String ne = response.body().string();
                            System.out.println(ne);
                        }
                    }
                });
//                        两种格式任选一
//   格式二                     try {
//                            Response response=client.newCall(request).execute();
//                            if(response.isSuccessful()){
//                                System.out.println(response.body().string());
//
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        Intent itm = getIntent();
        Bundle bd = itm.getExtras();
        final int from = bd.getInt("from");
        if (view.getId() == R.id.title_edit) {
            AlertDialog dlgShowBack = new AlertDialog.Builder(this).create();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dlgShowBack.setTitle("添加");
            dlgShowBack.setIcon(android.R.drawable.ic_dialog_info);
            dlgShowBack.setMessage("确认添加?");
            dlgShowBack.setButton(DialogInterface.BUTTON_NEGATIVE,"是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            Intent itm = getIntent();
                            Bundle bd = itm.getExtras();
                            String name = bd.getString("name");
                            String studentID = bd.getString("studentID");
                            String jiandangren = bd.getString("jiandangren");
                            String jiandangrenzhiwu = bd.getString("jiandangrenzhiwu");
                            String gaunzhuleixing = bd.getString("gaunzhuleixing");
                            String bang = bd.getString("bangfu");
                            String date = bd.getString("date");
                            String jiating = String.valueOf(ed1.getText());
                            String xuexi = String.valueOf(ed2.getText());
                            String shenxin = String.valueOf(ed3.getText());
                            String qita = String.valueOf(ed4.getText());
                            String jiandang = String.valueOf(ed5.getText());
                            String shenghuo = String.valueOf(ed6.getText());
                            final JSONObject post = new JSONObject();
                            try {
                                String teacher = sp.getString("USER_NAME","");
                                post.put("studentId", studentID);
                                post.put("teacherId", teacher);
                                post.put("studyCondition", xuexi);
                                post.put("familyCondition", jiating);
                                post.put("healthCondition", shenxin);
                                post.put("otherCondition", qita);
                                post.put("helpType",bang);
                                post.put("bulidingBasis", jiandang);
                                post.put("bulidingTime", date);
                                post.put("lifeCondition", shenghuo);
                                post.put("bulidingPerson", jiandangren);
                                post.put("bulidingPersonDuty", jiandangrenzhiwu);
                                post.put("attentionType",gaunzhuleixing);
                                post.put("name", name);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new Thread() {
                                public void run() {
                                    OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                                            .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                                            .connectTimeout(60, TimeUnit.SECONDS).build();//设置连接超时时间.build();
                                    RequestBody requestBody = RequestBody.create(JSON, post.toString());
                                    FormBody.Builder formbody = new FormBody.Builder();
                                    formbody.add("json", post.toString());
                                    final Request request = new Request.Builder()
                                            .url("http://180.76.249.233:8080/newhelp/api/archiveStudent").header("Authorization",token)
                                            .post(requestBody)
                                            .build();
//格式一
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
                                                System.out.println(ne);
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
                                                                        Toast.makeText(archive_making.this, finalMessage, Toast.LENGTH_LONG).show();
                                                                }
                                                            };
                                                            runOnUiThread(run);

                                                            Intent open_page = null;
                                                            switch (from) {
                                                                case 1:
                                                                    open_page = new Intent(archive_making.this, open_page.class);
                                                                    break;
                                                                case 2:
                                                                    open_page = new Intent(archive_making.this, now_page.class);
                                                                    break;
                                                            }
                                                            startActivity(open_page);
                                                        }
                                                        else {
                                                            final String su = success;
                                                            final String finalMessage1 = message;
                                                            Runnable run = new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                        Toast.makeText(archive_making.this, finalMessage1, Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(archive_making.this,"错误代码"+response.code(), Toast.LENGTH_SHORT).show();
                                                    }
                                                };
                                                runOnUiThread(runnable);
                                            }
                                        }
                                    });
                                }
                            }.start();

                            break;
                        default:
                            break;
                    }

                }
            });
            dlgShowBack.setButton(DialogInterface.BUTTON_POSITIVE,"否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            break;
                        default:
                            break;
                    }

                }
            });
            dlgShowBack.show();
            Button btnPositive =
                    dlgShowBack.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
            Button btnNegative =
                    dlgShowBack.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
            btnNegative.setTextSize(20);
            btnPositive.setTextSize(20);




        }
        if (view.getId() == R.id.title_back) {
            String name = bd.getString("name");
            String studentID = bd.getString("studentID");
            String jiandangren = bd.getString("jiandangren");
            String jiandangrenzhiwu = bd.getString("jiandangrenzhiwu");
            String gaunzhuleixing = bd.getString("gaunzhuleixing");
            String bang = bd.getString("bangfu");
            String jiating = String.valueOf(ed1.getText());
            String xuexi = String.valueOf(ed2.getText());
            String shenxin = String.valueOf(ed3.getText());
            String qita = String.valueOf(ed4.getText());
            String jiandang = String.valueOf(ed5.getText());
            String shenghuo = String.valueOf(ed6.getText());
            System.out.println(jiandang+jiating+shenghuo);
            final Intent backing = new Intent(archive_making.this, add_new.class);
            Bundle ba = new Bundle();
            ba.putInt("from",from);
            ba.putInt("edit_or_not",1);
            ba.putString("name",name);
            ba.putString("studentID",studentID);
            ba.putString("jiandangren",jiandangren);
            ba.putString("jiandangrenzhiwu",jiandangrenzhiwu);
            ba.putString("gaunzhuleixing",gaunzhuleixing);
            ba.putString("jiating",jiating);
            ba.putString("bangfu",bang);
            ba.putString("xuexi",xuexi);
            ba.putString("shenxin",shenxin);
            ba.putString("qita",qita);
            ba.putString("jiandang",jiandang);
            ba.putString("shenghuo",shenghuo);
            backing.putExtras(ba);

            startActivity(backing);
        }
    }
}
