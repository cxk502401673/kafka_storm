package com.cxk.kafka.util;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 18/3/28
 *
 * @author chenxiaokai
 **/
public class KafkaUtil {
    /**
     *
     * @param topicName
     * @param partitions
     * @param replicationFactor   控制消息保存在几个broker(服务器)上，一般情况下等于broker的个数
     * @throws Exception
     */
    public static void createTopic(String topicName,Integer partitions,Integer replicationFactor)throws  Exception{
        ZkUtils zkUtils = ZkUtils.apply("master:2181,slave1：2181,slave2：2181", 30000, 30000,
                JaasUtils.isZkSecurityEnabled());
        // 创建一个单分区单副本名为t1的topic
        //replicationFactor :备份 数量
        AdminUtils.createTopic(zkUtils, topicName, partitions,
                replicationFactor, new Properties(), RackAwareMode.Enforced$.MODULE$);
        zkUtils.close();
    }

    // 删除


    public static void main(String[] args) {
        try {
           // createTopic("test",1,1);
            ZkUtils zkUtils = ZkUtils.apply("localhost:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
            // 删除topic 't1'
            AdminUtils.deleteTopic(zkUtils, "t1");
            zkUtils.close();
//查询
// 获取topic 'test'的topic属性属性
            Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "test");
// 查询topic-level属性
            Iterator it = props.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry entry=(Map.Entry)it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                System.out.println(key + " = " + value);
            }
            zkUtils.close();

//修改
// 增加topic级别属性
            props.put("min.cleanable.dirty.ratio", "0.3");
// 删除topic级别属性
            props.remove("max.message.bytes");
// 修改topic 'test'的属性
            AdminUtils.changeTopicConfig(zkUtils, "test", props);
            zkUtils.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
