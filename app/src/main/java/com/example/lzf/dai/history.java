package com.example.lzf.dai;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by lzf on 2017/10/31.
 */

public class history extends AppCompatActivity implements View.OnClickListener{
    private ListView list_histo;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    //    boolean isSpinnerFirst = true;
    private TextView title;
    private String teacher;
    private Button title_back;
    private String nianji;
    private String guanzhu;
    private String bangfu;
    private Spinner grade;
    private Spinner help_type;
    private Spinner attention_type;
    private String[] studentid;
    private List<Data> mData;
    private Data[] data1;
    private String[] historyid;
    private SharedPreferences sp;
    private String token;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        title = (TextView) findViewById(R.id.title);
        title.setText("历史当前助困");
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sp.getString("token","");
        grade = (Spinner) findViewById(R.id.s1);
        help_type = (Spinner) findViewById(R.id.s2);
        attention_type = (Spinner) findViewById(R.id.s3);
        title_back = (Button) findViewById(R.id.title_back);
        title_back.setOnClickListener(this);
        teacher = sp.getString("USER_NAME","");
        help_type.setSelection(0, true);
        grade.setSelection(0, true);
        attention_type.setSelection(0, true);
        get_list();
        getspinner();
    }
    public void search(){
        final Intent intentn=new Intent(history.this,h_main_student_message.class);
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
//            System.out.println(guanzhu);
            if ((bangfu!="null")&&(bangfu !=null)){
                post.put("helpType",bangfu);
            }
            else
                post.put("helpType",null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(post.toString());

        new Thread() {
            public void run() {
                if (!post.toString().equals(("{\"teacherId\":\""+teacher+"\"}"))){
                    okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                            .connectTimeout(60, TimeUnit.SECONDS).build();//设置连接超时时间.build();
                    RequestBody requestBody = RequestBody.create(JSON, post.toString());
                    final okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("http://180.76.249.233:8080/newhelp/api/historyArchives").header("Authorization",token)
                            .post(requestBody)
                            .build();
                    okhttp3.Call call = client.newCall(request);
                    call.enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            System.out.println("连接失败");
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                            System.out.println("1111222112"+"11");
                            final String ne = response.body().string();
                            System.out.println(ne);
                            if (response.code() == 200) {
                                System.out.println(ne);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            JSONTokener tokener = new JSONTokener(ne);
                                            JSONObject object = (JSONObject) tokener.nextValue();
                                            JSONArray data = object.getJSONArray("data");
                                            final String[] historyid = new String[data.length()];
                                            final String[] studentid = new String[data.length()];
                                            String[] strs = new String[data.length()];
                                            data1 = new Data[data.length()];
                                            LayoutInflater inflater =getLayoutInflater();
                                            mData = new ArrayList<Data>();
                                            for (int i = 0; i < data.length(); i++) {
                                                JSONObject jsonObject = (JSONObject) data.get(i);
                                                String name = jsonObject.getString("name");
                                                historyid[i] = jsonObject.getString("historyArchiveId");
                                                studentid[i] = jsonObject.getString("studentId");
                                                String grade = jsonObject.getString("grade");
                                                String help = jsonObject.getString("helpType");
                                                String atten = jsonObject.getString("attentionType");
                                                String time = jsonObject.getString("destoryingTime");
                                                if (time == "null"||time == null){
                                                    time = "-";
                                                }
                                                data1[i] = new Data(name,grade,help,atten,time);
                                                mData.add(data1[i]);
                                                strs[i] = name;
                                            }
                                            DataAdapter adapter = new DataAdapter(inflater,mData);
                                            list_histo = (ListView) findViewById(R.id.list_his);
                                            list_histo.setAdapter(adapter);
                                            list_histo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    bdf.putString("historyid", historyid[i]);
                                                    bdf.putString("studentid", studentid[i]);
                                                    //bdf.putInt("num", i);
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
        final Intent intentn=new Intent(history.this,h_main_student_message.class);
        final Bundle bdf=new Bundle();
        okhttp3.OkHttpClient mOkHttpClient = new okhttp3.OkHttpClient();
        System.out.println(teacher);
        final okhttp3.Request request = new okhttp3.Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/historyArchives/"+teacher).header("Authorization",token)
                .build();
        okhttp3.Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("lzf", e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String res= response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out
                                    .println(res);
                            JSONTokener tokener=new
                                    JSONTokener(res);
                            JSONObject object= (JSONObject) tokener.nextValue();
                            JSONArray data=object.getJSONArray("data");
                            studentid = new String[data.length()];
                            historyid = new String[data.length()];
                            String[] strs=new String[data.length()];
                            data1 = new Data[data.length()];
                            LayoutInflater inflater =getLayoutInflater();
                            mData = new ArrayList<Data>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = (JSONObject) data.get(i);
                                String name = jsonObject.getString("name");
                                historyid[i] = jsonObject.getString("historyArchiveId");
                                studentid[i] = jsonObject.getString("studentId");
                                String grade = jsonObject.getString("grade");
                                String help = jsonObject.getString("helpType");
                                String atten = jsonObject.getString("attentionType");
                                String time = jsonObject.getString("destoryingTime");
                                if (time == "null"||time == null){
                                    time = "-";
                                }
                                data1[i] = new Data(name,grade,help,atten,time);
                                mData.add(data1[i]);
                                strs[i] = name;
                            }
                            DataAdapter adapter = new DataAdapter(inflater,mData);
                            list_histo= (ListView) findViewById(R.id.list_his);
                            list_histo.setAdapter(adapter);
                            list_histo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    bdf.putString("historyid", historyid[i]);
                                    bdf.putString("studentid", studentid[i]);
                                    //bdf.putInt("num", i);
                                    intentn.putExtras(bdf);
                                    startActivity(intentn);
                                }
                            });
//                            listn.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                                @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
//                                @Override
//                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    initPopWindow(view);
//                                    return false;
//                                }
//                            });

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
//                pre_nianji = nianji;
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


    //    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
//    private void initPopWindow(View v) {
//        View view = LayoutInflater.from(history.this).inflate(R.layout.item_popip, null, false);
//        Button btn_zhou = (Button) view.findViewById(R.id.btn_zhou);
//        Button btn_mian = (Button) view.findViewById(R.id.btn_mian);
//        Button btn_yan = (Button) view.findViewById(R.id.btn_yan);
//        //1.构造一个PopupWindow，参数依次是加载的View，宽高
//        final PopupWindow popWindow = new PopupWindow(view,
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//
//
//        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
//        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
//        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
//        popWindow.setTouchable(true);
//        popWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
//        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效
//
//
//        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
//        popWindow.showAsDropDown(v, 50, 0);
//
//        //设置popupWindow里的按钮的事件
//        btn_zhou.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(history.this, "你点击了周记录", Toast.LENGTH_SHORT).show();
//                Intent intentzhou=new Intent(history.this,record_week.class);
//                startActivity(intentzhou);
//            }
//        });
//        btn_mian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(history.this, "你点击了面谈", Toast.LENGTH_SHORT).show();
//                popWindow.dismiss();
//                Intent intentmian=new Intent(history.this,record_week.class);
//                startActivity(intentmian);
//            }
//        });
//        btn_yan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(history.this, "你点击了研讨", Toast.LENGTH_SHORT).show();
//                popWindow.dismiss();
//                Intent intentyan=new Intent(history.this,record_week.class);
//                startActivity(intentyan);
//            }
//        });
//    }
//
    @Override
    public void onClick(View view) {

        if(view.getId()== R.id.title_back) {
            Intent it = new Intent(history.this, open_page.class);
            startActivity(it);
        }

    }
}

