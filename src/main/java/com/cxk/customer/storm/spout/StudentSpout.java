package com.cxk.customer.storm.spout;

import com.cxk.customer.storm.entity.Student;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;

/**
 * 18/3/30
 *
 * @author chenxiaokai
 **/
public class StudentSpout extends BaseRichSpout {
    SpoutOutputCollector collector;
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector=collector;
    }

    @Override
    public void nextTuple() {
        Student student=new Student("cxk");
        student.setPassword("xxxxxx");
        collector.emit(new Values(student));
        Utils.sleep(2000);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("student"));
    }
}
