package com.cxk.customer.storm.entity;

import lombok.Data;

/**
 * 18/3/30
 *
 * @author chenxiaokai
 *
 *      未实现java序列化接口！
 **/
@Data
public class Student {
    private String name;
    //该字段 不序列化传输
    private String  password;
    public Student(){

    }
    public Student(String name){
            this.name=name;
    }
    @Override
    public String toString(){
        return "Student [name="+name+"],[password="+password+"]";
    }





}
