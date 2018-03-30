package com.cxk.customer.storm.kryo;
import com.cxk.customer.storm.entity.Student;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


/**
 * 18/3/30
 *
 * @author chenxiaokai
 *   未完成
 *
 **/
public class StudentSerializable extends Serializer<Student>{
    @Override
    public void write(Kryo kryo, Output output, Student student) {
        output.writeString(student.getName());
        kryo.writeObject(output, student.getPassword());
    }

    @Override
    public Student read(Kryo kryo, Input input, Class<Student> args) {
        Student student=new Student();
        student.setName(input.readString());
        student.setPassword(kryo.readObject(input,String.class));

        return student;
    }
}
