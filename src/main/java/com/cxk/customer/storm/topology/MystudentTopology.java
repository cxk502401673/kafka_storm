package com.cxk.customer.storm.topology;


import com.cxk.customer.storm.bolt.StudentBolt;
import com.cxk.customer.storm.entity.Student;
import com.cxk.customer.storm.kryo.StudentSerializable;
import com.cxk.customer.storm.spout.StudentSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cxk
 */
public class MystudentTopology {
    public static final Logger logger = LoggerFactory.getLogger(MystudentTopology.class);
    public static void main(String[] args) {


        TopologyBuilder builder=new TopologyBuilder();
        //引用spout，并发度设为1
        builder.setSpout("spout", new StudentSpout(),1);
        //设置MyKafkaBolt的接收方式为 shuffleGrouping
        builder.setBolt("bolt1", new StudentBolt(),1).shuffleGrouping("spout");

        Config config =new Config();
        //上线之前都要改成false否则日志会非常多
        config.setDebug(true);
        // 设置work数量
        config.setNumWorkers(2);
        //禁止序列化器回退到Java的序列化机制
       // setFallBackOnJavaSerialization(config,false);

        //注册 student类，采用storm自带的Kryo序列化序列化器,StudentSerializable 实现了只序列化部分字段（未完成）
        config.registerSerialization(Student.class, StudentSerializable.class);
        if(args.length>0){

            try {
                StormSubmitter.submitTopology(args[0], config, builder.createTopology());
            } catch ( Exception e) {
               logger.error("提交拓扑失败",e);
            }

        }else{

            LocalCluster localCluster=new LocalCluster();
            localCluster.submitTopology("mytopology", config,  builder.createTopology());
            //本地模式在一个进程里面模拟一个storm集群的所有功能
        }



    }
}
