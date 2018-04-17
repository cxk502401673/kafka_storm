package com.cxk.customer.storm.bolt;

import com.cxk.customer.storm.entity.Student;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * 18/3/30
 *
 * @author chenxiaokai
 **/
public class StudentBolt  extends BaseBasicBolt{
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Student student=(Student)input.getValueByField("student");
        System.out.println("接收到spout节点传来的数据:"+student);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        System.out.println("我是brxxxanch1的代码");
    }
}
