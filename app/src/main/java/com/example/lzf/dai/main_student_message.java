package com.example.lzf.dai;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class main_student_message extends AppCompatActivity implements View.OnClickListener{
//    private String[] names = new String[]{"姓名"};
//    private String[] phones = new String[]{"电话"};
    private Button edit;
    private Button back;
    private Button jd;
    private Button jt;
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
    private TextView title;
    private TextView ed15;
    private TextView ed16;
    private String family;
    private String study;
    private String health;
    private String other;
    private String jiandangyiju;
    private String life;
    private TextView ed17;
//    private Button jilu;
    private Button chudang;
    private String chudnagliyou;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private String teacher;
    private String pw;
    private String Name;
    private String DUTY;
    private SharedPreferences sp;
    private String token;
    @Override
    //获取信息
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_student_message);
        sp =this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token =  sp.getString("token","");
        teacher = sp.getString("USER_NAME","");
        pw = sp.getString("PASSWORD","");
         Name = sp.getString("NAME","");
         DUTY = sp.getString("DUTY","");
        edit = (Button) findViewById(R.id.title_edit);
        edit.setText("修改");
        title = (TextView) findViewById(R.id.title) ;
        title.setText("帮扶学生信息");
        back= (Button) findViewById(R.id.title_back);
        back.setText("当前助困");
        chudang= (Button) findViewById(R.id.chudang);
//        jilu = (Button) findViewById(R.id.record);
        back= (Button) findViewById(R.id.title_back);
        ed1= (TextView) findViewById(R.id.fname);
        ed2= (TextView) findViewById(R.id.fnum);
        ed3= (TextView) findViewById(R.id.fnianji);
        ed4= (TextView) findViewById(R.id.fbanji);
        ed5= (TextView) findViewById(R.id.fxingbie);
        ed6= (TextView) findViewById(R.id.fmingzu);
        ed7= (TextView) findViewById(R.id.fzhengzhi);
        ed8= (TextView) findViewById(R.id.fjiating);
        ed9= (TextView) findViewById(R.id.fsushe);
        ed10= (TextView) findViewById(R.id.flianxi);
        ed11 = (TextView)findViewById(R.id.fguanzhu);
        ed12 = (TextView)findViewById(R.id.fbangfu);
        ed13 = (TextView)findViewById(R.id.fzhiwu);
        ed14 = (TextView)findViewById(R.id.fjiandang);
        ed15 =(TextView)findViewById(R.id.fjiandangren);
//        ed16 =(TextView)findViewById(R.id.fchudang);
        ed17 =(TextView)findViewById(R.id.friqi);

        Intent itm=getIntent();
        Bundle bdm=itm.getExtras();
        String studentid = bdm.getString("studentID");
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/baseStudent/all/"+studentid).header("Authorization",token)
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
                                String name=data.getString("name");
                                String nianji=data.getString("grade");
                                String banji=data.getString("studentClass");
                                String xingbie=data.getString("sex");
                                String mingzu=data.getString("ethnicGroup");
                                String zhengzhi=data.getString("politicalStatus");
                                String zhuzhi=data.getString("familyAddress");
                                String sushe=data.getString("dormitory");
                                String lianxi=data.getString("contactWay");
                                String studentid = data.getString("studentId");
                                String duty =data.getString("duty") ;

                                ed1.setText(judge(name));
                                ed2.setText(judge(studentid));
                                ed3.setText(judge(nianji));
                                ed4.setText(judge(banji));
                                ed5.setText(judge(xingbie));
                                ed6.setText(judge(mingzu));
                                ed7.setText(judge(zhengzhi));
                                ed8.setText(judge(zhuzhi));
                                ed9.setText(judge(sushe));
                                ed10.setText(judge(lianxi));
                                ed13.setText(judge(duty));

                            } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        OkHttpClient mOkHttpClient1 = new OkHttpClient();
        final Request request1 = new Request.Builder().get()
                .url("http://180.76.249.233:8080/newhelp/api/archiveStudent/"+studentid).header("Authorization",token)
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
                            String guanzhu=data.getString("attentionType");
                            String bangfu=data.getString("helpType");
                            String jiandang = data.getString("bulidingPerson");
                            String bulidingPersonDuty = data.getString("bulidingPersonDuty");
                            String lastRecordTime = data.getString("lastRecordTime");
                            life = data.getString("lifeCondition");
                            family=data.getString("familyCondition");
                            study=data.getString("studyCondition");
                            health=data.getString("healthCondition");
                            other=data.getString("otherCondition");
                            jiandangyiju=data.getString("bulidingBasis");
                            String build_time = data.getString("bulidingTime");
                            ed17.setText(judge(build_time));
                            ed12.setText(judge(bangfu));
                            ed11.setText(judge(guanzhu));
                            ed14.setText(judge(jiandang));
                            ed15.setText(judge(bulidingPersonDuty));
//                            ed16.setText(judge(lastRecordTime));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        });

        jt= (Button) findViewById(R.id.jiatingxinxi);
        jd= (Button) findViewById(R.id.jiandangxinxi);
        edit.setOnClickListener(this);
        jd.setOnClickListener(this);
        jt.setOnClickListener(this);
        back.setOnClickListener(this);
//        jilu.setOnClickListener(this);
        chudang.setOnClickListener(this);

//        List<Map<String,Object>> listitme= new ArrayList<Map<String, Object>>();
//        for(int i=0;i<names.length;i++){
//            Map<String,Object> map = new HashMap<String,Object>();
//            map.put("tupian",R.mipmap.ic_launcher);
//            map.put("names",names[i]);
//            map.put("phones",phones[i]);
//            listitme.add(map);
//        }
//        SimpleAdapter sat =new SimpleAdapter(main_student_message.this,listitme,R.layout.adpter,new String[]{"tupian","names","phones"},new int[]{R.id.imgtou, R.id.name, R.id.says});
//        ListView listview = (ListView) findViewById(R.id.listView);
//        listview.setAdapter(sat);
    }
    public String judge(String m){
        if (m==null||m.trim().isEmpty()||m=="null"){
            return "-";
        }
        else {
            return m;
        }

    }
    public void delete_record(){
        final JSONObject post = new JSONObject();
        String date;
        Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month= (c.get(Calendar.MONTH)+1);
        int day=(c.get(Calendar.DAY_OF_MONTH));
        date = year+"-"+month+"-"+day;
        try {
            post.put("studentId",String.valueOf(ed2.getText()) );
            post.put("destoryingTime",date);
            post.put("destoryingBasis",chudnagliyou);
            post.put("destoryingRecorder",teacher);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(post.toString());
        new Thread() {
            public void run() {
                okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                        .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                        .connectTimeout(60, TimeUnit.SECONDS).build();//设置连接超时时间.build();
                RequestBody requestBody = RequestBody.create(JSON, post.toString());
                final okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://180.76.249.233:8080/newhelp/api/archiveStudent").header("Authorization",token)
                        .delete(requestBody)
                        .build();
                okhttp3.Call call = client.newCall(request);
                call.enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        System.out.println("连接失败");
                    }
                    @Override
                    public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {
                        if (response.code() == 200) {
                            final String ne = response.body().string();
                            System.out.println(ne);
                        }
                    }
                });
            }
        }.start();
    }
    //点击出事件
    private void popmenu(View view){
        PopupMenu popupMenu = new PopupMenu(main_student_message.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.sample_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {



                return false;
            }
        });
    }
    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    delete_record();
                    Intent it1 =new Intent(main_student_message.this,now_page.class);
                    startActivity(it1);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_edit:
                Intent itm=getIntent();
                Bundle bdm=itm.getExtras();
                Intent it = new Intent(main_student_message.this,change_student_message.class);
                Bundle bd1=new Bundle();
                bd1.putString("name", String.valueOf(ed1.getText()));
                bd1.putString("sdtuentId", String.valueOf(ed2.getText()));
                bd1.putString("nianji",String.valueOf(ed3.getText()));
                bd1.putString("banji",String.valueOf(ed4.getText()));


                bd1.putString("zhengzhi",String.valueOf(ed7.getText()));

                bd1.putString("adress",String.valueOf(ed8.getText()));
                bd1.putString("sushe",String.valueOf(ed9.getText()));
                bd1.putString("contactWay", String.valueOf(ed10.getText()));
                bd1.putString("guanzhu", String.valueOf(ed11.getText()));
                bd1.putString("bangfu",String.valueOf(ed12.getText()));
                bd1.putString("zhiwu",String.valueOf(ed13.getText()));
                bd1.putString("jiandangren",String.valueOf(ed14.getText()));
                bd1.putString("jiandangrenduty",String.valueOf(ed15.getText()));
                it.putExtras(bd1);
                startActivity(it);
                break;
            case R.id.jiandangxinxi:
                Intent it3 =new Intent(main_student_message.this,archive_message.class);
                Bundle bd3=new Bundle();
                bd3.putString("studentID", String.valueOf(ed2.getText()));
                bd3.putString("name", String.valueOf(ed1.getText()));
                bd3.putString("family",family);
                bd3.putString("life",life);
                bd3.putString("study",study);
                bd3.putString("health",health);
                bd3.putString("other",other);
                bd3.putString("jiandang",jiandangyiju);

                it3.putExtras(bd3);
                startActivity(it3);
                break;
            case R.id.record:
                Intent it6= new Intent(main_student_message.this,record.class);
                Bundle bundle=new Bundle();
                bundle.putString("studentID", String.valueOf(ed2.getText()));
                bundle.putString("name",String.valueOf(ed1.getText()));
                bundle.putInt("from",1);
                it6.putExtras(bundle);
                startActivity(it6);
                break;
            case R.id.title_back:
                Intent it5= new Intent(main_student_message.this,now_page.class);
                startActivity(it5);
                break;
            case R.id.jiatingxinxi:
                Intent it2 =new Intent(main_student_message.this,family.class);
                Bundle bd2=new Bundle();
                bd2.putString("studentID", String.valueOf(ed2.getText()));
                it2.putExtras(bd2);
                startActivity(it2);
                break;
            case R.id.chudang:

                AlertDialog dlgShowBack = new AlertDialog.Builder(this).create();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                dlgShowBack.setTitle("确认");
                dlgShowBack.setIcon(android.R.drawable.ic_dialog_info);
                dlgShowBack.setMessage("是否除档?");
                final EditText et=new EditText(this);
//                EditText et2=new EditText(this);
                et.setHint("除档理由");
                dlgShowBack.setView(et);
                dlgShowBack.setButton(DialogInterface.BUTTON_NEGATIVE,"是", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        chudnagliyou = String.valueOf(et.getText());
                        delete_record();
                        Intent it1 =new Intent(main_student_message.this,now_page.class);
                        startActivity(it1);
                        break;
                    default:
                        break;
                }
            }
        }
        );
                dlgShowBack.setButton(DialogInterface.BUTTON_POSITIVE,"否", dialogClickListener);
                dlgShowBack.show();
                Button btnPositive =
                        dlgShowBack.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                Button btnNegative =
                        dlgShowBack.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setTextSize(20);
                btnPositive.setTextSize(20);
                break;
        }
    }
}



