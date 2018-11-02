package com.example.lzf.dai;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ASUS on 2017/12/27.
 */

public class MyDatePicker implements DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener {

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(String time);
    }
    private String[] mDisplayMonths = {"1", "2", "3","4", "5", "6","7", "8", "9","10", "11", "12"};
    private DatePicker datePicker;
    private TextView tv_ok;
    private TextView tv_cancle;

    private ResultHandler handler;
    private String dateTime;
    private Context context;
    private String initDateTime;
    private Dialog datePickerDialog;
    public MyDatePicker(Context context, ResultHandler resultHandler, String initDateTime) {
        this.context = context;
        this.handler = resultHandler;
        this.initDateTime = initDateTime;
        initDialog();
    }
    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.mytime_dialog);
            datePickerDialog.setCancelable(false);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.dialog_date);
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            window.setAttributes(lp);
        }
        initView();
    }

    private void initView() {
        datePicker = (DatePicker) datePickerDialog.findViewById(R.id.datepicker);
        tv_ok = (TextView) datePickerDialog.findViewById(R.id.tv_ok);
        tv_cancle = (TextView) datePickerDialog.findViewById(R.id.tv_cancle);
//        ((NumberPicker)((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(1)).setDisplayedValues(mDisplayMonths);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle( dateTime );
                datePickerDialog.dismiss();
            }
        });
//        ((NumberPicker)((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(1)).setDisplayedValues(mDisplayMonths);
        datePickerDialog.show();
        initDate(datePicker);
    }


    public void initDate(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime = calendar.get(Calendar.YEAR) + "年"
                    + calendar.get(Calendar.MONTH) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
                    ;
        }

        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
    }



    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();
        String date = spliteString(initDateTime, "日", "index", "front");

        String yearStr = spliteString(date, "年", "index", "front"); // 年份
        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

//        String hourStr = spliteString(time, ":", "index", "front"); // 时
//        String minuteStr = spliteString(time, ":", "index", "back"); // 分

        int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
        int currentDay = Integer.valueOf(dayStr.trim()).intValue();
//        int currentHour = Integer.valueOf(hourStr.trim()).intValue();
//        int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

        calendar.set(currentYear, currentMonth, currentDay);
        return calendar;
    }

    /**
     * 截取子串
     *
     * @param srcStr
     *            源串
     * @param pattern
     *            匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern,
                                      String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();

        calendar.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        dateTime = sdf.format(calendar.getTime());

    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
        onDateChanged(null, 0, 0, 0);
    }

}
