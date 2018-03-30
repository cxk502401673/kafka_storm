package com.cxk.customer.storm.topology;


import com.cxk.customer.storm.bolt.MyKafkaBolt;
import com.cxk.customer.storm.entity.Student;
import com.cxk.customer.storm.kryo.StudentSerializable;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cxk
 */
public class MyKafkaTopology {
    public static final Logger logger = LoggerFactory.getLogger(MyKafkaTopology.class);
    public static void main(String[] args) {

        String topic ="MyTopic";
        // hosts文件中已经 写了master，slave1 slave2对应关系
        ZkHosts zkhosts  = new ZkHosts("master:2181,slave1:2181,slave2:2181");

        SpoutConfig spoutConfig = new SpoutConfig(zkhosts, topic,
                "/MyKafka", //偏移量offset的根目录
                "myTopic");//子目录对应一个应用
        List<String> zkServers=new ArrayList<String>();

        for(String host:zkhosts.brokerZkStr.split(","))
        {
            zkServers.add(host.split(":")[0]);
        }

        spoutConfig.zkServers=zkServers;
        spoutConfig.zkPort=2181;
        // 0.10.0之前版本是这么些的
      //  spoutConfig.forceFromStart=true;//从头开始消费，实际上是要改成false的

      //  storm0.10.0版本的是ignoreZkOffsets（即forceFromStart ）
        spoutConfig.ignoreZkOffsets=false;
        spoutConfig.socketTimeoutMs=60;
        //定义输出为string类型
        spoutConfig.scheme=new SchemeAsMultiScheme(new StringScheme());
        TopologyBuilder builder=new TopologyBuilder();
        //引用spout，并发度设为1
        builder.setSpout("spout", new KafkaSpout(spoutConfig),1);
        //设置MyKafkaBolt的接收方式为 shuffleGrouping
        builder.setBolt("bolt1", new MyKafkaBolt(),1).shuffleGrouping("spout");

        Config config =new Config();
        //上线之前都要改成false否则日志会非常多
        config.setDebug(true);
        // 设置work数量
        config.setNumWorkers(1);
        //禁止序列化器回退到Java的序列化机制
       // setFallBackOnJavaSerialization(config,false);

        //注册 student类，采用storm自带的Kryo序列化序列化器,StudentSerializable 实现了只序列化部分字段
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
