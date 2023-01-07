package com.sharpcselegantcode.jmh;

import com.sharpcselegantcode.HashMap.impl.DynamicArrayChainingHashMap;
import com.sharpcselegantcode.HashMap.impl.OpenAddressingHashMap;
import com.sharpcselegantcode.HashMap.impl.ProbingFunction;
import com.sharpcselegantcode.HashMap.impl.SeparateChainingHashMap;
import com.sharpcselegantcode.jmh.ProbingFunctions;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

public class Testing {

    public static void main(java.lang.String[] args){
        OpenAddressingHashMap<StringWrapper, StringWrapper> hashMap = new OpenAddressingHashMap<>(1, ProbingFunctions.linearProbing);
        HashMapBenchmarkRunner.WordPool.words.forEach(word -> hashMap.put(word, word));
        HashMapBenchmarkRunner.WordPool.words.stream().map(hashMap::get).forEach(
                word -> System.out.println(word)
        );
        //testSC();
        new Scanner(System.in).next();

    }

    public static void testSC(){
        String s = "a";
        String s1 = "aa";
        String s2 = "aaa";
        String s3 = "aaaa";
        String s4 = "aaaaa";
        DynamicArrayChainingHashMap<String, Integer> hashMap = new DynamicArrayChainingHashMap<String, Integer>(1);
        hashMap.put(s, 1);
        hashMap.put(s1, 2);
        hashMap.put(s2, 3);
        hashMap.put(s3, 4);
        hashMap.put(s4, 5);
        System.out.println(hashMap.get(s));
        System.out.println(hashMap.get(s1));
        System.out.println(hashMap.get(s2));
        System.out.println(hashMap.get(s3));
        System.out.println(hashMap.get(s4));
        System.out.println(hashMap);
    }

}
