package com.example.lzf.dai;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lzf on 2017/11/5.
 */
public class record extends AppCompatActivity implements View.OnClickListener{
    private ListView list_test;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private Button back;
    private Button add;
    private Spinner record;
    private String jilu;
    private String studentid;
    private String name;
    private SharedPreferences sp;
    private String token;
    private TextView na;
    private TextView stu;
    public  void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i);// 默认选中项
                get_list();
                break;
            }
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        String[] strs= {};
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,strs);
        list_test= (ListView) findViewById(R.id.list_now);
        na= (TextView) findViewById(R.id.name);
        stu= (TextView) findViewById(R.id.studentid);
        list_test.setAdapter(adapter);
        record =(Spinner)findViewById(R.id.record);
        add=(Button)findViewById(R.id.title_edit);
        back=(Button)findViewById(R.id.title_back);
        back.setText("个人信息");
        add.setText("增加");
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        int i = bd.getInt("from");
        switch (i){
            case 2:
                jilu = bd.getString("jilu");
                System.out.println(jilu);
                setSpinnerItemSelectedByValue(record,jilu);
                break;
        }

        record.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] spinner_value = getResources().getStringArray(R.array.record);
                jilu = spinner_value[pos];
                get_list();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void get_list(){
        Intent intent = null;
        switch (jilu){
            case "周联系简易记录表":
                intent = new Intent(record.this,record_week.class);
                break;
            case "面谈记录表":
                intent = new Intent(record.this,record_face.class);
                break;
            case "家长联系记录表":
                intent = new Intent(record.this,record_family.class);
                break;
            case "研讨及总结记录":
                intent = new Intent(record.this,record_message.class);
                break;
        }
        final Intent intentn= intent;
        final Bundle bdf=new Bundle();
        Intent it=getIntent();
        Bundle bd=it.getExtras();
        studentid = bd.getString("studentID");
        name = bd.getString("name");
        na.setText("       "+name);
        stu.setText(studentid);
        System.out.println(studentid);
        System.out.println(name);

        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/records/"+jilu+"/"+studentid).header("Authorization",token)
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
                System.out.println(res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONTokener tokener=new
                                    JSONTokener(res);
                            JSONObject object= (JSONObject) tokener.nextValue();
                            JSONArray data=object.getJSONArray("data");
                            final String archiveid[] = new String[data.length()];
                            String[] strs=new String[data.length()];
                            for(int i=0;i<data.length();i++){
                                JSONObject jsonObject = (JSONObject) data.get(i);
                                String name=jsonObject.getString("recordTime");
                                archiveid[i] =jsonObject.getString("recordId");
                                strs[i]=name;
                            }
                            ArrayAdapter<String> adapter =new ArrayAdapter<String>(record.this,android.R.layout.simple_expandable_list_item_1,strs);
                            list_test= (ListView) findViewById(R.id.list_now);
                            list_test.setAdapter(adapter);
                            list_test.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    bdf.putString("recordid",archiveid[i]);
                                    bdf.putString("name",name);
                                    bdf.putString("record",jilu);
                                    System.out.println(jilu);
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

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.title_back) {
            Intent it = new Intent(record.this, main_student_message.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("studentID",studentid);
            bundle1.putString("name",name);
            it.putExtras(bundle1);
            startActivity(it);
        }
        if(view.getId()== R.id.title_edit) {
            Intent it = null;
            switch ( jilu){
                case "周联系简易记录表":
                    it = new Intent(record.this,add_record_week.class);
                    break;
                case "面谈记录表":
                    it = new Intent(record.this,add_record_face.class);
                    break;
                case "家长联系记录表":
                    it = new Intent(record.this,add_record_family.class);
                    break;
                case "研讨及总结记录":
                    it = new Intent(record.this,add_record_message.class);
                    break;
            }
            Bundle bundle1 = new Bundle();
            bundle1.putString("name",name);
            bundle1.putString("studentID",studentid);
            bundle1.putString("record",jilu);
            it.putExtras(bundle1);
            startActivity(it);
        }
    }
}
