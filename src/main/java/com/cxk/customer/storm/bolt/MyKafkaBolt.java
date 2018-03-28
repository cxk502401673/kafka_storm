package com.cxk.customer.storm.bolt;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MyKafkaBolt implements IBasicBolt {
    public static final Logger logger = LoggerFactory.getLogger(MyKafkaBolt.class);
    @Override
    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(Tuple input, BasicOutputCollector arg1) {
        String kafkaMsg = input.getString(0);
        System.out.println("bolt" + kafkaMsg);

    }

    @Override
    public void prepare(Map arg0, TopologyContext arg1) {
        // TODO Auto-generated method stub

    }
}