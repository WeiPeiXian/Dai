package com.example.lzf.dai;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzf on 2017/10/16.
 */

public class now_page extends AppCompatActivity implements View.OnClickListener {
    private ListView listn;
    private SharedPreferences sp;
    private String teacher;
    private Data[] data1;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private TextView title;
    private Button title_edit;
    private Button title_back;
    private String nianji;
    private String guanzhu;
    private String bangfu;
    private Spinner grade;
    private Spinner help_type;
    private Spinner attention_type;
    private String token;
    private String[] studentid;
    private List<Data> mData;
    private int[] num;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_page);
        title = (TextView)findViewById(R.id.title) ;
        title.setText("当前助困");
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        teacher = sp.getString("USER_NAME","");
        grade = (Spinner)findViewById(R.id.s1);
        help_type = (Spinner)findViewById(R.id.s2);
        attention_type= (Spinner)findViewById(R.id.s3);
        title_edit = (Button)findViewById(R.id.title_edit);
        title_edit.setText("增加");
        title_back=(Button)findViewById(R.id.title_back) ;
        title_back.setOnClickListener(this);
        title_edit.setOnClickListener(this);
        help_type.setSelection(0, true);
        grade.setSelection(0, true);
        attention_type.setSelection(0, true);
        ActionBar actionbar=getSupportActionBar();{
            if(actionbar!=null){
                actionbar.hide();
            }
        }
        get_list();
        getspinner();
    }
    public void search(){
        final Intent intentn=new Intent(now_page.this,record.class);
        final Bundle bdf=new Bundle();
        final JSONObject post = new JSONObject();
        try {
            post.put("teacherId",teacher);
            if (nianji!="null"&&nianji != null){
                post.put("grade", nianji);
            }
            else
                post.put("grade", null);
            if(guanzhu!="null"&&guanzhu!=null){
                post.put("attentionType", guanzhu);
            }
            else
                post.put("attentionType", null);
            if ((bangfu!="null")&&(bangfu !=null)){
                post.put("helpType",bangfu);
            }
            else
                post.put("helpType",null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(post.toString());
        final String sw = post.toString();
        new Thread() {
            public void run() {
                if (!sw.equals("{\"teacherId\":\""+teacher+"\"}")){
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                        .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                        .connectTimeout(60, TimeUnit.SECONDS).build();//设置连接超时时间.build();
                RequestBody requestBody = RequestBody.create(JSON, post.toString());
                final Request request = new Request.Builder()
                        .url("http://180.76.249.233:8080/newhelp/api/archiveStudents")
                        .header("Authorization",token)
                        .post(requestBody)
                        .build();
//                System.out.println("1111");
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        JSONTokener tokener = new
                                                JSONTokener(ne);
                                        JSONObject object = (JSONObject) tokener.nextValue();
                                        JSONArray data = object.getJSONArray("data");
                                        final String[] studentid = new String[data.length()];
                                        final String[] strs = new String[data.length()];
                                        data1 = new Data[data.length()];
                                        LayoutInflater inflater =getLayoutInflater();
                                        mData = new ArrayList<Data>();
                                        for (int i = 0; i < data.length(); i++) {
                                            JSONObject jsonObject = (JSONObject) data.get(i);
                                            String name = jsonObject.getString("name");
                                            String grade = jsonObject.getString("grade");
                                            String help = jsonObject.getString("helpType");
                                            String atten = jsonObject.getString("attentionType");
                                            String time = jsonObject.getString("lastRecordTime");
                                            studentid[i] =jsonObject.getString("studentId");
                                            data1[i] = new Data(name,grade,help,atten,time);
                                            boolean b = jsonObject.getBoolean("highlight");
                                            if(b){
                                                num[i]=1;
                                            }
                                            strs[i] = name;
                                        }
                                        for(int i=0;i<data.length();i++){
                                            if (num[i]==1){
                                                mData.add(data1[i]);
                                                System.out.println(i+"true");
                                            }
                                        }
                                        for(int i=0;i<data.length();i++){
                                            if (num[i]==0){
                                                mData.add(data1[i]);
                                                System.out.println(i+"FALSE");
                                            }
                                        }
                                        DataAdapter adapter = new DataAdapter(inflater,mData);
                                        listn = (ListView) findViewById(R.id.list_now);
                                        listn.setAdapter(adapter);
                                        listn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                bdf.putString("studentID", studentid[i]);
                                                bdf.putString("name",strs[i]);
                                                bdf.putInt("from",1);
                                                intentn.putExtras(bdf);
                                                startActivity(intentn);
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }
            else get_list();
            }
        }.start();
    }
    public void get_list(){
        final Intent intentn=new Intent(now_page.this,record.class);
        final Bundle bdf=new Bundle();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/archiveStudents/"+teacher)
                .header("Authorization",token)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("lzf", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res= response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONTokener tokener=new
                                    JSONTokener(res);
                            JSONObject object= (JSONObject) tokener.nextValue();
                            JSONArray data=object.getJSONArray("data");
                            studentid = new String[data.length()];

                            final String[] strs=new String[data.length()];
                            data1 = new Data[data.length()];
                            num = new  int[data.length()];
                            for(int i =0;i<data.length();i++){
                                num[i]=0;
                            }
                            LayoutInflater inflater =getLayoutInflater();
                            mData = new ArrayList<Data>();
                            for(int i=0;i<data.length();i++){
                                JSONObject jsonObject = (JSONObject) data.get(i);
                                String name=jsonObject.getString("name");
                                String grade = jsonObject.getString("grade");
                                String help = jsonObject.getString("helpType");
                                String atten = jsonObject.getString("attentionType");
                                String time = jsonObject.getString("lastRecordTime");

                                studentid[i] =jsonObject.getString("studentId");
                                data1[i]= new Data(name,grade,help,atten,time);
                                String b = jsonObject.getString("highlight");
                                if(b=="true"){
                                    num[i]=1;
                                }
                                strs[i]=name;
                            }

                            DataAdapter adapter = new DataAdapter(inflater,mData);

                            listn= (ListView) findViewById(R.id.list_now);
                            listn.setAdapter(adapter);
                            for(int i=0;i<data.length();i++){
                                if (num[i]==1){
                                    mData.add(data1[i]);
                                    System.out.println(i+"true");
                                }
                            }

                            for(int i=0;i<data.length();i++){
                                if (num[i]==0){
                                    mData.add(data1[i]);
                                    System.out.println(i+"FALSE");
                                }
                            }
                            listn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    bdf.putString("studentID",studentid[i]);
                                    bdf.putString("name",strs[i]);
                                    bdf.putInt("from",1);
                                    intentn.putExtras(bdf);
                                    startActivity(intentn);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
    public void getspinner(){
        grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] spinner_value = getResources().getStringArray(R.array.nianji);
                nianji = spinner_value[pos];
                if(pos==0){
                    nianji =null;
                }
                search();
                System.out.println(nianji);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        help_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] spinner_value = getResources().getStringArray(R.array.bangfuleixing);
//                pre_bangfu = bangfu;
                bangfu = spinner_value[pos];
                if(pos==0){
                    bangfu =null;
                }
                search();
                System.out.println(bangfu);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        attention_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] spinner_value = getResources().getStringArray(R.array.guanzhu);
                guanzhu = spinner_value[pos];
                if(pos==0){
                    guanzhu = null;
                }
                search();
                System.out.println(guanzhu);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
//            }
//        });
//        btn_mian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(now_page.this, "你点击了面谈", Toast.LENGTH_SHORT).show();
//                popWindow.dismiss();
//                Intent intentmian=new Intent(now_page.this,record.class);
//                startActivity(intentmian);
//            }
//        });
//        btn_yan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(now_page.this, "你点击了研讨", Toast.LENGTH_SHORT).show();
//                popWindow.dismiss();
//                Intent intentyan=new Intent(now_page.this,record.class);
//                startActivity(intentyan);
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.title_edit){
            Intent itd=new Intent(now_page.this,add_new.class);
            Bundle open = new Bundle();
            open.putInt("from",2);
            open.putInt("edit_or_not",0);
            itd.putExtras(open);
            startActivity(itd);
        }
        if(view.getId()== R.id.title_back) {
            Intent it5 = new Intent(now_page.this, open_page.class);
            startActivity(it5);
        }

    }
}


