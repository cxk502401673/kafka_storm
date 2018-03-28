import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * create by chenxiaokai on 18/3/28
 **/

public class MyProducer {
    private static final Logger logger = LoggerFactory.getLogger(MyProducer.class);

    public static void main(String[] args) {
        MyProducer MyProducer=new MyProducer();
        MyProducer.sendMessage(1);
    }
    public Properties createProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "master:9092,slave1:9092,slave2:9092");
        properties.put("acks", "all");
        properties.put("retries", 0); // 消息发送请求失败重试次数
        properties.put("batch.size", 2000);
        properties.put("linger.ms", 1); // 消息逗留在缓冲区的时间，等待更多的消息进入缓冲区一起发送，减少请求发送次数
        properties.put("buffer.memory", 33554432); // 内存缓冲区的总量
        // 如果发送到不同分区，并且不想采用默认的Utils.abs(key.hashCode) % numPartitions分区方式，则需要自己自定义分区逻辑
       // properties.put("partitioner.class", "com.cxk.producter.MyPartitioner");
        properties.put("partitioner.class", "org.apache.kafka.clients.producer.internals.DefaultPartitioner");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    public void sendMessage() {
        Properties properties = createProperties();
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        int i = 0;
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(2);
                String key = Integer.toString(i);
                String value = "times: " + key;
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("test", key, value);
                producer.send(record, new Callback() {

                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if (e != null) {
                            logger.warn("send record error {}", e);
                        }
                        logger.info("offset: {}, partition: {}", metadata.offset(), metadata.partition());
                    }
                });
                i++;
            }
        } catch (Exception e) {
            logger.warn("{}", e);
        } finally {
            producer.close();
        }

    }
    public  void sendMessage(Integer times
    ) {
        Properties properties = createProperties();
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        int i = 0;
        try {
            for(int j=0;j<times;j++){
                TimeUnit.SECONDS.sleep(2);
                String key = Integer.toString(i);
                String value = "times: " + key;
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("test", key, value);
                producer.send(record, new Callback() {

                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if (e != null) {
                            logger.warn("send record error {}", e);
                        }
                        logger.info("offset: {}, partition: {}", metadata.offset(), metadata.partition());
                    }
                });
                i++;
            }

        } catch (Exception e) {
            logger.warn("{}", e);
        } finally {
            producer.close();
        }

    }
}
