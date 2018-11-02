package com.example.lzf.dai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.content.*;
import android.widget.*;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LogoActivity extends AppCompatActivity {
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private EditText etUserName;
    private EditText etUserPassword;
    private Button btnLogin;
    private SharedPreferences sp;
    private String userName;
    private String userPassword;
    private CheckBox rem_pw;
    private CheckBox auto_login;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_activty);
        btnLogin = (Button) findViewById(R.id.btn_login);
        etUserName = (EditText) findViewById(R.id.et_userName);
        etUserPassword = (EditText) findViewById(R.id.et_password);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        rem_pw = (CheckBox) findViewById(R.id.cb_checkbox);
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
        init();

        if (sp.getBoolean("ISCHECK", false)) {
            rem_pw.setChecked(true);
            etUserName.setText(sp.getString("USER_NAME", ""));
            etUserPassword.setText(sp.getString("PASSWORD", ""));
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                auto_login.setChecked(true);
                userName = sp.getString("USER_NAME", "");
                userPassword = sp.getString("PASSWORD", "");
                login();

            }
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                userName = etUserName.getText().toString();
                userPassword = etUserPassword.getText().toString();
                login();
            }
        });
        //监听记住密码多选框按钮事件
        rem_pw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pw.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                } else {
                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

    }
    private void login(){

        final JSONObject post = new JSONObject();
        try {
            post.put("teacherId", userName);
            post.put("password", userPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(100, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(60, TimeUnit.SECONDS).build();//设置连接超时时间.build();
        RequestBody requestBody = RequestBody.create(JSON, post.toString());
        final Request request = new Request.Builder()
                .url("http://180.76.249.233:8080/newhelp/api/login")
                .post(requestBody)
                .build();
        new Thread() {
            public void run() {
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("连接失败");
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        System.out.println(response.code());
                        if (response.code() == 200) {
                            final String ne = response.body().string();
                            final Runnable runable = new Runnable() {
                                public void run() {
                                    json = ne;
                                    JSONTokener tokener = new JSONTokener(json);
                                    System.out.println(json);
                                    JSONObject object = null;
                                    try {
                                        object = (JSONObject) tokener.nextValue();
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    String success = null;
                                    try {
                                        success = object.getString("success");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (success == "true") {
                                        final String su = success;
                                        Runnable run = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (su == "true") {
                                                    Toast.makeText(LogoActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(LogoActivity.this, "用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        };
                                        runOnUiThread(run);
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = object.getJSONObject("data");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        String duty = null;
                                        String name = null;
                                        try {
                                            duty = jsonObject.getString("duty");
                                            System.out.println(duty);
                                            name = jsonObject.getString("name");
                                            System.out.println(name);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("USER_NAME", userName);
                                        editor.putString("PASSWORD", userPassword);
                                        editor.putString("DUTY", duty);
                                        editor.putString("NAME", name);
                                        try {
                                            editor.putString("token", jsonObject.getString("token"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        editor.commit();
                                        //跳转界面
                                        Intent intent = new Intent(LogoActivity.this, open_page.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("user", userName);
                                        bundle.putString("password", userPassword);
                                        bundle.putString("duty", duty);
                                        bundle.putString("name", name);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        final String su = success;
                                        Runnable run = new Runnable() {
                                            @Override
                                            public void run() {
                                                if (su == "true") {
                                                    Toast.makeText(LogoActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(LogoActivity.this, "用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
                                                }
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


                        } else {
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LogoActivity.this, "错误代码" + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            };
                            runOnUiThread(runnable);
                        }
                    }
                });
            }
        }.start();

    }
    private void init(){
        EditText userName = (EditText) findViewById(R.id.et_userName);
        EditText password = (EditText) findViewById(R.id.et_password);
        ImageView unameClear = (ImageView) findViewById(R.id.iv_unameClear);
        ImageView pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);

        EditTextClearTools.addClearListener(userName,unameClear);
        EditTextClearTools.addClearListener(password,pwdClear);
    }

}
