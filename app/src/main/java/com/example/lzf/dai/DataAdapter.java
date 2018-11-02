package com.example.lzf.dai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ASUS on 2018/1/17.
 */

public class DataAdapter extends BaseAdapter {
    private List<Data> mData;
    private LayoutInflater mInflater;
    public DataAdapter(LayoutInflater mInflater,List<Data> mData){
        this.mData=mData;
        this.mInflater = mInflater;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewStudent = mInflater.inflate(R.layout.listview,null);
        //获得学生对象
        Data data = mData.get(i);
        //获得自定义布局中每一个控件的对象。
        TextView name = (TextView) viewStudent.findViewById(R.id.name);
        TextView grade = (TextView) viewStudent.findViewById(R.id.grade);
        TextView help = (TextView) viewStudent.findViewById(R.id.help);
        TextView attention = (TextView) viewStudent.findViewById(R.id.attention);
        TextView time = (TextView) viewStudent.findViewById(R.id.time);
        //将数据一一添加到自定义的布局中。
        name.setText(data.getName());
        grade.setText(data.getGrade());
        help.setText(data.getHelp());
        attention.setText(data.getAttention());
        time.setText(data.getTime());
        return viewStudent ;
    }
    public View getView(int i){

        View viewStudent = mInflater.inflate(R.layout.listview,null);
        //获得学生对象
        Data data = mData.get(i);
        //获得自定义布局中每一个控件的对象。
        TextView name = (TextView) viewStudent.findViewById(R.id.name);
        TextView grade = (TextView) viewStudent.findViewById(R.id.grade);
        TextView help = (TextView) viewStudent.findViewById(R.id.help);
        TextView attention = (TextView) viewStudent.findViewById(R.id.attention);
        TextView time = (TextView) viewStudent.findViewById(R.id.time);
        //将数据一一添加到自定义的布局中。
        name.setText(data.getName());
        grade.setText(data.getGrade());
        help.setText(data.getHelp());
        attention.setText(data.getAttention());
        time.setText(data.getTime());
        return viewStudent ;
    }

}
