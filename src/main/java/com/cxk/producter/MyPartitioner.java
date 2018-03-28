package com.cxk.producter;

import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 18/3/28
 * 自定义 分区数量的计算方式
 * @author chenxiaokai
 **/
public class MyPartitioner extends DefaultPartitioner {
    public static final Logger logger = LoggerFactory.getLogger(MyPartitioner.class);


    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int partition = 0;
        int offset = Integer.valueOf((String) key);
        if (offset >= 0) {
            partition = Integer.valueOf((String) key) % cluster.partitionCountForTopic(topic);
            // logger.info("key {}, partition {}", key, partition);
        }
        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {
        super.configure(configs);

    }
}
