package com.example.lzf.dai;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

/**
 * Created by lzf on 2017/10/16.
 */

public class open_page extends AppCompatActivity implements View.OnClickListener{
    private ImageButton now;
    private ImageButton add;
    private ImageButton history;
    private TextView editText;
    private TextView NAME;
    private Button exit;
    private SharedPreferences sp;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open);
        ActionBar actionbar=getSupportActionBar();{
            if(actionbar!=null){
                actionbar.hide();
            }
        }
        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        editText = (TextView) findViewById(R.id.title);
        NAME = (TextView)findViewById(R.id.name);
        NAME.setText(sp.getString("NAME",""));
        exit = (Button)findViewById(R.id.back);
        now= (ImageButton) findViewById(R.id.now);
        add= (ImageButton) findViewById(R.id.add);
        history= (ImageButton) findViewById(R.id.history);
        exit.setOnClickListener(this);
        editText.setText("移动助困");
        now.setOnClickListener(this);
        add.setOnClickListener(this);
        history.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.back){
            AlertDialog dlgShowBack = new AlertDialog.Builder(this).create();
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
            dlgShowBack.setTitle("退出");
            dlgShowBack.setIcon(android.R.drawable.ic_dialog_info);
            dlgShowBack.setMessage("确认退出?");
            dlgShowBack.setButton(DialogInterface.BUTTON_NEGATIVE,"是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_NEGATIVE:
                            Intent itd = new Intent(open_page.this, LogoActivity.class);
                            sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                            startActivity(itd);
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
        if (view.getId()==R.id.add){
            Intent itd=new Intent(open_page.this,add_new.class);
            Bundle open = new Bundle();
            open.putInt("from",1);
            open.putInt("edit_or_not",0);
            itd.putExtras(open);
            startActivity(itd);
        }
        if (view.getId()==R.id.now){
            Intent ita=new Intent(open_page.this,now_page.class);
            startActivity(ita);
        }
        if (view.getId()==R.id.history){
            Intent itb=new Intent(open_page.this,history.class);
            startActivity(itb);
        }
    }
}
