package com.example.lzf.dai;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lzf on 2017/10/16.
 */

public class add_new extends AppCompatActivity implements View.OnClickListener {
    private Button bt;
    private Button back;
    private EditText ed2;
    private EditText ed3;
    private EditText ed4;
    private EditText ed5;
//    private EditText ed6;
    private String date;
    private String guanzhu;
    private Spinner attention;
    private MySpinner mySpinner;
    private static final String[] m={"学业困难","家庭困难","心理困难"};
    boolean isSpinnerFirst = true ;
    private SharedPreferences sp;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new);

        bt = (Button) findViewById(R.id.title_edit);
        back = (Button) findViewById(R.id.title_back);
        attention =(Spinner)findViewById(R.id.addspinner2);
        //attention.setSelection(0, true);
        bt.setText("下一步");
        sp=this.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        bt.setOnClickListener(this);
        back.setOnClickListener(this);
        Calendar c=Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month= (c.get(Calendar.MONTH)+1);
        int day=(c.get(Calendar.DAY_OF_MONTH));
        date = year+"-"+month+"-"+day;

        ed2= (EditText) findViewById(R.id.addEditText2);
        ed3= (EditText) findViewById(R.id.addEditText3);
        ed4= (EditText) findViewById(R.id.addEditText7);
        ed5= (EditText) findViewById(R.id.addEditText8);
        ed4.setText(sp.getString("NAME",""));
        ed5.setText(sp.getString("DUTY",""));
        mySpinner=(MySpinner) findViewById(R.id.button);
        mySpinner.initContent(m);
        Intent it1 = getIntent();
        Bundle bd1 = it1.getExtras();
        int edit_or_not = bd1.getInt("edit_or_not");
        if (edit_or_not == 1){
            String n1 = bd1.getString("name");
            String n2 = bd1.getString("studentID");
            String n3 = bd1.getString("bangfu");
            String n4 = bd1.getString("gaunzhuleixing");
            String n5 = bd1.getString("jiandangren");
            String n6 = bd1.getString("jiandangrenzhiwu");
            String jiating = bd1.getString("jiating");
            String jiandang = bd1.getString("jiandang");
            String xuexi = bd1.getString("xuexi");
            String shenxin = bd1.getString("shenxin");
            String qita = bd1.getString("qita");
            String shenghuo = bd1.getString("shenghuo");
            System.out.println(jiating+jiandang);
            ed2.setText(n1);
            ed3.setText(n2);
            ed4.setText(n5);
            ed5.setText(n6);
            mySpinner.setText(n3,null);
            setSpinnerItemSelectedByValue(attention,n4);
        }
        attention.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
//                if (isSpinnerFirst) {
//                    view.setVisibility(View.INVISIBLE) ;
//                }
//                isSpinnerFirst =false;//仅在添加修改时使用
                String[] spinner_value = getResources().getStringArray(R.array.guanzhu);
                guanzhu = spinner_value[pos];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
   }

    public  void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i+1);// 默认选中项

                break;
            }
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int num =  bundle.getInt("from");
        int edit_or_not = bundle.getInt("edit_or_not");
        System.out.println(""+num);
        switch (view.getId()) {
            case R.id.title_edit:
                Intent ti0 = new Intent(add_new.this, archive_making.class);
                Bundle bd2=new Bundle();
                bd2.putString("name", String.valueOf(ed2.getText()));
                bd2.putString("studentID", String.valueOf(ed3.getText()));
                bd2.putString("jiandangren", String.valueOf(ed4.getText()));
                bd2.putString("jiandangrenzhiwu", String.valueOf(ed5.getText()));
                bd2.putString("date",date);
                bd2.putString("gaunzhuleixing",guanzhu);
                bd2.putInt("from",num);
                bd2.putInt("edit_or_not",edit_or_not);
                bd2.putString("bangfu",String.valueOf(mySpinner.getText()));
                if(edit_or_not==1){
                    System.out.println("edit");
                    String jiating = bundle.getString("jiating");
                    String jiandang = bundle.getString("jiandang");
                    String xuexi = bundle.getString("xuexi");
                    String shenxin = bundle.getString("shenxin");
                    String qita = bundle.getString("qita");
                    String shenghuo = bundle.getString("shenghuo");
                    System.out.println(jiating+jiandang);
                    bd2.putString("jiating",jiating);
                    bd2.putString("xuexi",xuexi);
                    bd2.putString("shenxin",shenxin);
                    bd2.putString("qita",qita);
                    bd2.putString("jiandang",jiandang);
                    bd2.putString("shenghuo",shenghuo);
                }

                ti0.putExtras(bd2);
                startActivity(ti0);
                break;
            case R.id.title_back:
                if (num == 1) {
                    Intent it = new Intent(add_new.this, open_page.class);
                    startActivity(it);
                }
                else if (num == 2){
                    Intent it = new Intent(add_new.this, now_page.class);
                    startActivity(it);
                }
                break;

        }
    }
}
