package com.sharpcselegantcode.jmh;

import com.sharpcselegantcode.HashMap.impl.DynamicArrayChainingHashMap;
import com.sharpcselegantcode.HashMap.impl.OpenAddressingHashMap;
import com.sharpcselegantcode.HashMap.impl.ProbingFunction;
import com.sharpcselegantcode.HashMap.impl.SeparateChainingHashMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HashMapBenchmarkRunner {

    final static String WORDS_DIRECTORY = "./src/main/resources/works_of_shakespeare.txt";

    @State(Scope.Benchmark)
    public static class WordPool {
        final static List<StringWrapper> words = getWords();
        static List<StringWrapper> getWords() {
            List<StringWrapper> list = new LinkedList<>();
            Scanner sc;
            try {
                sc = new Scanner(new File(HashMapBenchmarkRunner.WORDS_DIRECTORY));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            while(sc.hasNext()){
                list.add(new StringWrapper(sc.next()));
            }
            return list;
        }
    }

    @State(Scope.Benchmark)
    public static class ExecutionPlan{

        public final static ProbingFunction[] probingFunctions = {ProbingFunctions.linearProbing, /*ProbingFunctions.quadraticProbing,*/ ProbingFunctions.fibonacciProbing};

        @Param({"0", "1"})
        public int probingFunctionId;

    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(".*")
                .addProfiler(GCProfiler.class)
                .result("./benchmarkResultsBadHash.csv")
                .resultFormat(ResultFormatType.CSV)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    @Fork(value = 3, warmups = 2)
    @Warmup(iterations = 5)
    @BenchmarkMode(Mode.All)
    public void separateChainingHashMap(Blackhole blackhole){
        SeparateChainingHashMap<StringWrapper, StringWrapper> separateChainingHashMap = new SeparateChainingHashMap<>();
        WordPool.words.forEach(word -> separateChainingHashMap.put(word, word));
        blackhole.consume(WordPool.words.stream().map(separateChainingHashMap::get));
    }

    @Benchmark
    @Fork(value = 3, warmups = 2)
    @Warmup(iterations = 5)
    @BenchmarkMode(Mode.All)
    public void dynamicArraySeparateChaining(Blackhole blackhole){
        DynamicArrayChainingHashMap<StringWrapper, StringWrapper> separateChainingHashMap = new DynamicArrayChainingHashMap<>();
        WordPool.words.forEach(word -> separateChainingHashMap.put(word, word));
        blackhole.consume(WordPool.words.stream().map(separateChainingHashMap::get));
    }

    @Benchmark
    @Fork(value = 3, warmups = 2)
    @Warmup(iterations = 5)
    @BenchmarkMode(Mode.All)
    public void openAddressingHashMap(ExecutionPlan plan, Blackhole blackhole){
        OpenAddressingHashMap<StringWrapper, StringWrapper> hashMap = new OpenAddressingHashMap<>(ExecutionPlan.probingFunctions[plan.probingFunctionId]);
        WordPool.words.forEach(word -> hashMap.put(word, word));
        blackhole.consume(WordPool.words.stream().map(hashMap::get));
    }

    @Benchmark
    @Fork(value = 3, warmups = 2)
    @Warmup(iterations = 5)
    @BenchmarkMode(Mode.All)
    public void javaHashMap(Blackhole blackhole){
        HashMap<StringWrapper, StringWrapper> hashMap = new HashMap<>();
        WordPool.words.forEach(word -> hashMap.put(word, word));
        blackhole.consume(WordPool.words.stream().map(hashMap::get));
    }
}
