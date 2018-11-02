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
import java.util.Calendar;
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

public class archive_changing extends AppCompatActivity implements View.OnClickListener{
    private Button submit;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private EditText ed1;
    private EditText ed2;
    private EditText ed3;
    private SharedPreferences sp;
    private String token;
    private EditText ed4;
    private EditText ed5;
    private EditText ed6;
    private TextView name;
    private TextView studentID;
    private String name_edit;
    private String studentid;
    private Button back;

    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive_making);
        submit= (Button) findViewById(R.id.title_edit);
        submit.setText("提交");
        submit.setOnClickListener(this);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        back = (Button)findViewById(R.id.title_back);
        back.setOnClickListener(this);
        ed1= (EditText) findViewById(R.id.jiating);
        ed2= (EditText) findViewById(R.id.xuexi);
        ed3= (EditText) findViewById(R.id.shenxin);
        ed4= (EditText) findViewById(R.id.qita);
        ed5= (EditText) findViewById(R.id.jiandang);
        ed6 = (EditText)findViewById(R.id.life);
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
        name= (TextView) findViewById(R.id.name);
        studentID = (TextView) findViewById(R.id.studentID);
        Intent it1 = getIntent();
        Bundle bd1 = it1.getExtras();
        name_edit = bd1.getString("name");
        studentid = bd1.getString("studentID");
        name.setText(name_edit);
        studentID.setText(studentid);
        ed1.setText(bd1.getString("family"));
        ed2.setText(bd1.getString("study"));
        ed3.setText(bd1.getString("health"));
        ed4.setText(bd1.getString("other"));
        ed5.setText(bd1.getString("jiangdangyiju"));
        ed6.setText(bd1.getString("life"));

    }

    @Override
    public void onClick(View view) {
        Intent itm = getIntent();
        Bundle bd = itm.getExtras();
        if (view.getId() == R.id.title_back){
            Intent it4= new Intent(archive_changing.this,archive_message.class);
            Bundle bd1=new Bundle();
            bd1.putString("name",name_edit);
            bd1.putString("studentID",studentid);
            bd1.putString("family", String.valueOf(ed1.getText()));
            bd1.putString("study", String.valueOf(ed2.getText()));
            bd1.putString("health", String.valueOf(ed3.getText()));
            bd1.putString("other", String.valueOf(ed4.getText()));
            bd1.putString("jiandang", String.valueOf(ed5.getText()));
            bd1.putString("life",String.valueOf(ed6.getText()));
            it4.putExtras(bd1);
            startActivity(it4);
        }
        if (view.getId() == R.id.title_edit) {
            {
                AlertDialog dlgShowBack = new AlertDialog.Builder(this).create();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                dlgShowBack.setTitle("修改");
                dlgShowBack.setIcon(android.R.drawable.ic_dialog_info);
                dlgShowBack.setMessage("确认修改?");
                dlgShowBack.setButton(DialogInterface.BUTTON_NEGATIVE,"是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                String jiating = String.valueOf(ed1.getText());
                                String xuexi = String.valueOf(ed2.getText());
                                String shenxin = String.valueOf(ed3.getText());
                                String qita = String.valueOf(ed4.getText());
                                String jiandang = String.valueOf(ed5.getText());

                                final JSONObject post = new JSONObject();
                                try {
                                    post.put("studentId", studentid);
                                    post.put("name", name_edit);
                                    post.put("studyCondition", xuexi);
                                    post.put("familyCondition", jiating);
                                    post.put("healthCondition", shenxin);
                                    post.put("otherCondition", qita);
                                    post.put("bulidingBasis", jiandang);
                                    post.put("lifeCondition",String.valueOf(ed6.getText()));
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
                                                .url("http://180.76.249.233:8080/newhelp/api/archiveStudent")
                                                .header("Authorization",token)
                                                .put(requestBody)
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
                                                                        Toast.makeText(archive_changing.this, finalMessage, Toast.LENGTH_LONG).show();
                                                                    }
                                                                };
                                                                runOnUiThread(run);

                                                                final Intent open_page = new Intent(archive_changing.this, main_student_message.class);
                                                                Bundle bd1 = new Bundle();

                                                                bd1.putString("studentID",String.valueOf(studentID.getText()));
                                                                open_page.putExtras(bd1);
                                                                startActivity(open_page);
                                                            }
                                                            else {
                                                                final String su = success;
                                                                final String finalMessage1 = message;
                                                                Runnable run = new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(archive_changing.this, finalMessage1, Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(archive_changing.this,"错误代码"+response.code(), Toast.LENGTH_SHORT).show();
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



            }

    }
}
