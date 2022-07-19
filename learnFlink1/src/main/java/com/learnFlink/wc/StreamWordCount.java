package com.learnFlink.wc;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

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
        executionEnvironment.socketTextStream("")
    }
}
