package com.example.lzf.dai;
public class Data {
//    名字 年级 帮扶类型 关注状态 末次记录时jian
    private String name;
    private String grade;
    private String help;
    private String attention;
    private String time;
    public Data(String name, String grade, String help,String attention,String time) {
        this.name = name;
        this.attention=attention;
        this.help = help;
        this.grade =grade;
        this.time = time;
    }
    public void setName(String name){
        this.name =name;
    }
    public String getName(){
        return name;
    }
    public void setGrade(String grade){
        this.grade = grade;
    }
    public String getGrade(){
        return grade;
    }
    public void setHelp(String help){
        this.help=help;
    }
    public String getHelp(){
        return help;
    }
    public void setAttention(String attention){

        this.attention = attention;
    }
    public String getAttention(){
        return attention;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }



}
