package com.cxk.customer.storm.topology;

/**
 * 18/3/30
 *
 * @author chenxiaokai
 **/

import org.apache.storm.Config;
import org.apache.storm.Constants;
import org.apache.storm.LocalCluster;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 局部定时器
 *
 * 数字累加求和
 * https://www.cnblogs.com/DreamDrive/p/6671496.html
 *
 * @author
 *
 */
public class LocalTopologySumTimer2 {


    /**
     * spout需要继承baserichspout，实现未实现的方法
     * @author Administrator
     *
     */
    public static class MySpout extends BaseRichSpout {
        private Map conf;
        private TopologyContext context;
        private SpoutOutputCollector collector;

        /**
         * 初始化方法，只会执行一次
         * 在这里面可以写一个初始化的代码
         * Map conf：其实里面保存的是topology的一些配置信息
         * TopologyContext context：topology的上下文，类似于servletcontext
         * SpoutOutputCollector collector：发射器，负责向外发射数据(tuple)
         */
        @Override
        public void open(Map conf, TopologyContext context,
                         SpoutOutputCollector collector) {
            this.conf = conf;
            this.context = context;
            this.collector = collector;
        }

        int num = 1;
        /**
         * 这个方法是spout中最重要的方法，
         * 这个方法会被storm框架循环调用，可以理解为这个方法是在一个while循环之内
         * 每调用一次，会向外发射一条数据
         */
        @Override
        public void nextTuple() {
            System.out.println("spout发射："+num);
            //把数据封装到values中，称为一个tuple，发射出去
            this.collector.emit(new Values(num++));
            Utils.sleep(1000);
        }

        /**
         * 声明输出字段
         */
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            //给values中的数据起个名字，方便后面的bolt从这个values中取数据
            //fields中定义的参数和values中传递的数值是一一对应的
            declarer.declare(new Fields("num"));
        }

    }


    /**
     * 自定义bolt需要实现baserichbolt
     * @author Administrator
     *
     */
    public static class MyBolt extends BaseRichBolt {
        private Map stormConf;
        private TopologyContext context;
        private OutputCollector collector;

        /**
         * 和spout中的open方法意义一样
         */
        @Override
        public void prepare(Map stormConf, TopologyContext context,
                            OutputCollector collector) {
            this.stormConf = stormConf;
            this.context = context;
            this.collector = collector;
        }

        int sum = 0;
        /**
         * 是bolt中最重要的方法，当spout发射一个tuple出来，execute也会被调用，需要对spout发射出来的tuple进行处理
         */
        @Override
        public void execute(Tuple input) {
            if(input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)){
                //如果满足，就说明这个tuple是系统几倍的组件发送的，也就意味着定时时间到了
                System.out.println("定时任务执行了。");

            }else{
                //input.getInteger(0);//也可以根据角标获取tuple中的数据
                Integer value = input.getIntegerByField("num");
                sum+=value;
                System.out.println("和："+sum);
            }

        }

        /**
         * 声明输出字段
         */
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            //在这没必要定义了，因为execute方法中没有向外发射tuple，所以就不需要声明了。
            //如果nextTuple或者execute方法中向外发射了tuple，那么declareOutputFields必须要声明，否则不需要声明
        }

        /**
         * 局部定时任务
         * 只针对当前的bolt  对其他的bolt中没有影响
         * 加对系统级别tuple的判断只需要在当前bolt中判断就可以...其他bolt不需要..
         * 这种在工作中最常用....
         * 全局定时任务在 main方法中 设置  局部的定时任务只需要在Bolt类中覆盖getComponentConfiguration()方法
         * 这个还是比较有用,有意思的
         */
        @Override
        public Map<String, Object> getComponentConfiguration() {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 5);
            return hashMap;
        }
    }
    /**
     * 注意：在组装topology的时候，组件的id在定义的时候，名称不能以__开头。__是系统保留的
     * @param args
     */
    public static void main(String[] args) {
        //组装topology
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("spout1", new MySpout());
        //.shuffleGrouping("spout1"); 表示让MyBolt接收MySpout发射出来的tuple
        topologyBuilder.setBolt("bolt1", new MyBolt()).shuffleGrouping("spout1");

        //创建本地storm集群
        LocalCluster localCluster = new LocalCluster();
        Config config = new Config();
        localCluster.submitTopology("sumTopology", config, topologyBuilder.createTopology());

    }


}
