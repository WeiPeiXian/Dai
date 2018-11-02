package com.example.lzf.dai;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.lzf.dai.MyDatePicker.spliteString;

/**
 * Created by ASUS on 2017/12/21.
 */

public class add_record_face extends AppCompatActivity implements View.OnClickListener{
    private Button edit;
    private Button back;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private TextView Name;
    private TextView Studentid;
    private TextView title;
    private EditText ed1;
    private EditText ed3;
    private EditText ed5;
    private EditText ed6;
    private String studentid;
    private String jilu;
    private String name;
    private String date;
    private SharedPreferences sp;
    private String token ;
    private String date_result;
    private MyDatePicker datePicker;
    private Button tv_showCurrentDate1;

    private void initMyDatePicker(String m) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");//格式为 2013年9月3日 14:44
        Date curDate = new Date(System.currentTimeMillis());
        String currentDate = formatter.format(curDate);
//        System.out.println(currentDate);

        if (m==null||m.equals("")) {
            System.out.println("1");
            datePicker = new MyDatePicker(this, new MyDatePicker.ResultHandler() {
                @Override
                public void handle(String time) {

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = (c.get(Calendar.MONTH) + 1);
                    int day = (c.get(Calendar.DAY_OF_MONTH));
                    date = year + "-" + month + "-" + day;
                    if (time == null) {
                        tv_showCurrentDate1.setText(date);
//                        if (date_result!=null||!date_result.equals("")){
//                            tv_showCurrentDate1.setText(date_result);
//                        }
                    } else {
                        String date = spliteString(time, "日", "index", "front");

                        String yearStr = spliteString(date, "年", "index", "front"); // 年份
                        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

                        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
                        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日
                        String dat = yearStr + "-" + monthStr + "-" + dayStr;
                        tv_showCurrentDate1.setText(dat);
                    }
                }
            }, currentDate);
        }
        else {
            System.out.println("2");
            String yy = m.substring(0, 4);
            String mm = m.substring(5, 7);
            System.out.println(mm);
            String dd = m.substring(8, 10);
            System.out.println(dd);
            String d = yy + "年" + mm + "月" + dd + "日";
            System.out.println(d);
//            System.out.println(d);
            datePicker = new MyDatePicker(this, new MyDatePicker.ResultHandler() {
                @Override
                public void handle(String time) {

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = (c.get(Calendar.MONTH) + 1);
                    int day = (c.get(Calendar.DAY_OF_MONTH));
                    date = year + "-" + month + "-" + day;
                    if (time == null) {
                        tv_showCurrentDate1.setText(date_result);
                    } else {
                        String date = spliteString(time, "日", "index", "front");

                        String yearStr = spliteString(date, "年", "index", "front"); // 年份
                        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

                        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
                        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日
                        String dat = yearStr + "-" + monthStr + "-" + dayStr;
                        tv_showCurrentDate1.setText(dat);
                    }
                }
            }, d);
        }
    }
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record_face);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        edit = (Button) findViewById(R.id.title_edit);
        back = (Button) findViewById(R.id.title_back);
        edit.setText("提交");
        edit.setOnClickListener(this);
        tv_showCurrentDate1 = (Button) findViewById(R.id.data);
        tv_showCurrentDate1.setOnClickListener(this);
        Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month= (c.get(Calendar.MONTH)+1);
        int day=(c.get(Calendar.DAY_OF_MONTH));
        if(month<10){
            date = year+"-0"+month+"-"+day;
        }
        else
            date = year+"-"+month+"-"+day;
        tv_showCurrentDate1.setText(date);
        back.setOnClickListener(this);
        tv_showCurrentDate1.setText(date);
        Name = (TextView)findViewById(R.id.name);
        Studentid= (TextView)findViewById(R.id.studentID);
        title= (TextView)findViewById(R.id.title);
        ed1= (EditText) findViewById(R.id.recorder);
        ed3= (EditText) findViewById(R.id.address);
        ed5= (EditText) findViewById(R.id.mainmessage);
        ed6= (EditText) findViewById(R.id.other);
        ed5.setMinHeight(600);
        ed5.setMinimumHeight(600);
        ed6.setMinHeight(100);
        ed6.setMinimumHeight(100);
        Intent it1 = getIntent();
        Bundle bd1 = it1.getExtras();
        Name.setText(bd1.getString("name"));
        Studentid.setText(bd1.getString("studentID"));
        name = bd1.getString("name");
        studentid = bd1.getString("studentID");
        jilu = bd1.getString("record");
        title.setText(jilu);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.title_back) {
            Intent it = new Intent(add_record_face.this, record.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("studentID",studentid);
            bundle1.putString("name",name);
            bundle1.putString("jilu",jilu);
            bundle1.putInt("from",2);
            it.putExtras(bundle1);
            startActivity(it);
        }


        if(view.getId()== R.id.title_edit) {
            {
                AlertDialog dlgShowBack = new AlertDialog.Builder(this).create();
                dlgShowBack.setTitle("添加");
                dlgShowBack.setIcon(android.R.drawable.ic_dialog_info);
                dlgShowBack.setMessage("确认添加?");
                dlgShowBack.setButton(DialogInterface.BUTTON_NEGATIVE,"是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                final JSONObject post = new JSONObject();
                                try {
                                    post.put("studentId", studentid);
                                    post.put("recordName", jilu);
                                    date_result = String.valueOf(tv_showCurrentDate1.getText());
                                    post.put("recordTime", date_result);
                                    post.put("location", String.valueOf(ed3.getText()));
                                    post.put("witness", "");
                                    post.put("recorder", String.valueOf(ed1.getText()));
                                    post.put("participant", "");
                                    post.put("content",String.valueOf(ed5.getText()));
                                    post.put("comment",String.valueOf(ed6.getText()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new Thread() {
                                    public void run() {
                                        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                                                .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                                                .connectTimeout(60, TimeUnit.SECONDS).build();//设置连接超时时间.build();
                                        RequestBody requestBody = RequestBody.create(JSON, post.toString());
                                        final Request request = new Request.Builder()
                                                .url("http://180.76.249.233:8080/newhelp/api/record")
                                                .post(requestBody)
                                                .header("Authorization",token)
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
                                                                        Toast.makeText(add_record_face.this, finalMessage, Toast.LENGTH_LONG).show();
                                                                    }
                                                                };
                                                                runOnUiThread(run);
                                                                Intent it = new Intent(add_record_face.this, record.class);
                                                                Bundle bundle1 = new Bundle();
                                                                bundle1.putString("name",name);
                                                                bundle1.putString("studentID",studentid);
                                                                bundle1.putString("jilu",jilu);
                                                                bundle1.putInt("from",2);
                                                                it.putExtras(bundle1);
                                                                startActivity(it);

                                                            }
                                                            else {
                                                                final String su = success;
                                                                final String finalMessage1 = message;
                                                                Runnable run = new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(add_record_face.this, finalMessage1, Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(add_record_face.this,"错误代码"+response.code(), Toast.LENGTH_SHORT).show();
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
        if(view.getId()== R.id.data) {
            date_result = String.valueOf(tv_showCurrentDate1.getText());
            System.out.println(date_result);
            initMyDatePicker(date_result);
        }
    }
}