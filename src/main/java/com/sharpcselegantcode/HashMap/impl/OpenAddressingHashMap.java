package com.sharpcselegantcode.HashMap.impl;

import com.sharpcselegantcode.HashMap.HashMap;

import java.util.Arrays;

@SuppressWarnings("all")
public class OpenAddressingHashMap<K, V> implements HashMap<K, V> {

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static class Node<K, V>{
        K key;
        V value;
        int hash;

        private Node() {}

        static <K, V> Node create(K key, V value){
            Node node = new Node();
            node.key = key;
            node.value = value;
            node.hash = hash(key);
            return node;
        }

        @Override
        public java.lang.String toString(){ return "{" + key.toString() + ", " + value.toString() + "}";}
    }

    private static int hash(Object key){
        return Math.abs(HashMap.hash(key));
    }

    private int size;

    private Node[] table;

    private ProbingFunction probingFunction;

    public OpenAddressingHashMap(ProbingFunction probingFunction){ this(16, probingFunction); }

    public OpenAddressingHashMap(int initialCapacity, ProbingFunction probingFunction){
        table = new Node[initialCapacity];
        this.probingFunction = probingFunction;
        this.size = 0;
    }

    @Override
    public V get(K key) {
        probingFunction.reset();
        Node<K, V> n;
        while((n = table[probingFunction.next(hash(key), table.length)]) != null && !n.key.equals(key));
        return n != null ? n.value : null;
    }

    @Override
    public V put(K key, V value) {
        ensureCapacity();
        probingFunction.reset();
        size++;
        return putVal(key, value);
    }

    private V putVal(K key, V value){
        int i;
        Node<K, V> n, prev;
        while ((prev = table[i = (probingFunction.next((n = Node.create(key, value)).hash, table.length))]) != null
                && !prev.key.equals(key));
        table[i] = n;
        return prev != null ? prev.value : null;
    }

    private V putVal2(K key, V value){
        int i;
        Node<K, V> n, prev;
        if ((prev = table[i = (probingFunction.next((n = Node.create(key, value)).hash, table.length))]) == null
                || prev.key.equals(key)) {
            table[i] = n;
            return prev != null ? prev.value : null;
        }
        return putVal2(key, value);
    }

    @Override
    public void delete(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString(){
        return Arrays.toString(table);
    }

    private void ensureCapacity(){
        if (((float)size / table.length) < DEFAULT_LOAD_FACTOR) return;
        OpenAddressingHashMap<K, V> hashMap = new OpenAddressingHashMap(table.length * 2, probingFunction);
        for (Node<K, V> node: table){
            if (node == null) continue;
            hashMap.put(node.key, node.value);
        }
        this.table = hashMap.table;
    }

}
