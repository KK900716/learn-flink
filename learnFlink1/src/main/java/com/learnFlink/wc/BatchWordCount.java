package com.learnFlink.wc;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;


/**
 * This is one program
 * @author 44380
 * @version 2022~07~17~23:53
 */
public class BatchWordCount {
    public static void main(String[] args) {
        // 1. 创建执行环境
        ExecutionEnvironment executionEnvironment = ExecutionEnvironment.getExecutionEnvironment();
        // 2. 从文件中读取数据
        DataSource<String> lineStringDataSource = executionEnvironment.readTextFile("input/words.txt");
        // 3. 将美航数据进行分词，转换成二元组类型
        FlatMapOperator<String, Tuple2<String, Long>> wordAndOneTuple = lineStringDataSource.flatMap((String line, Collector<Tuple2<String, Long>> out) -> {
            // 将一行文本进行分词
            String[] words = line.split(" ");
            // 将每个单词转换成二元组输出
            for (String word : words) {
                out.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));
        // 4. 按照word进行分组
        UnsortedGrouping<Tuple2<String, Long>> wordAndOneGroup = wordAndOneTuple.groupBy(0);
        // 5. 分组内进行聚合统计
        AggregateOperator<Tuple2<String, Long>> sum = wordAndOneGroup.sum(1);
        // 6. 结果的打印输出
        try {
            sum.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
