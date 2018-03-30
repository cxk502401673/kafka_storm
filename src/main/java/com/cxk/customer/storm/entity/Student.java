package com.cxk.customer.storm.entity;

/**
 * 18/3/30
 *
 * @author chenxiaokai
 *
 *      未实现java序列化接口！
 **/
public class Student {
    private String name;
    public Student(){

    }
    public Student(String name){
            this.name=name;
    }
    @Override
    public String toString(){
        return "Student [name="+name+"]";
    }
}
