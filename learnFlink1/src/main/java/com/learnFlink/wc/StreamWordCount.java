package com.learnFlink.wc;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Stream
 *
 * @author 44380
 * @version 2022~07~19~22:57
 */
public class StreamWordCount {
    public static void main(String[] args) {
        // 1. 创建流式执行环境
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2. 读取文本流
        // 从参数中提取主机名和端口号
        ParameterTool parameterTool=ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");
        DataStreamSource<String> lineDataStream = executionEnvironment.socketTextStream(host, port);
        // 3. 转换计算
        SingleOutputStreamOperator<Tuple2<String, Long>> wordAndOneTuple = lineDataStream.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            String[] s = line.split(" ");
            for (String word : s) {
                out.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));
        // 4. 分组
        KeyedStream<Tuple2<String, Long>, String> tuple2StringKeyedStream = wordAndOneTuple.keyBy(data -> data.f0);
        // 5. 求和
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = tuple2StringKeyedStream.sum(1);
        // 6. 打印
        sum.print();
        try {
            executionEnvironment.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
