package com.sharpcselegantcode.HashMap.impl;

import com.sharpcselegantcode.HashMap.HashMap;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DynamicArrayChainingHashMap<K, V> implements HashMap<K, V> {

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static class Node<K, V> {
        K key;
        V value;
        int hash;
        SeparateChainingHashMap.Node<K, V> next;

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

    private ArrayList<Node<K, V>>[] table;

    public DynamicArrayChainingHashMap(){
        this(16);
    }

    public DynamicArrayChainingHashMap(int initialCapacity){
        table = (ArrayList<Node<K, V>>[]) Array.newInstance(ArrayList.class, initialCapacity);
        this.size = 0;
    }

    @Override
    public V get(K key) {
        ArrayList<Node<K, V>> nodes = table[(hash(key) % table.length)];
        return nodes == null ? null : nodes.parallelStream()
                .filter(node -> node.key.equals(key))
                .map(node -> node.value)
                .findFirst()
                .orElse(null);
    }

    @Override
    public V put(K key, V value) {
        size++;
        ensureCapacity();
        Node<K, V> node = Node.create(key, value);
        ArrayList<Node<K,V>> nodes = table[(hash(key) % table.length)];
        if (nodes == null) {
            nodes = new ArrayList<>();
            nodes.add(node);
            table[(hash(key) % table.length)] = nodes;
            return null;
        }
        Node<K, V> prev = nodes.parallelStream()
                .filter(node2 -> node2.key.equals(key))
                .findFirst()
                .orElse(null);
        if(prev == null){
            nodes.add(node);
            return null;
        }
        V prevVal = prev.value;
        prev.value = node.value;
        return prevVal;
    }

    @Override
    public void delete(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.lang.String toString(){
        StringBuilder stringBuilder = new StringBuilder("[");
        for (ArrayList<Node<K, V>> nodes: table){
            if (nodes == null) continue;
            nodes.forEach(node -> stringBuilder.append("{").append(node.key).append(", ").append(node.value).append("}").append(", "));
        }
        return stringBuilder.subSequence(0, Math.max(0, stringBuilder.length() - 2)) + "]";

    }

    private void ensureCapacity(){
        if (((float)size / table.length) < DEFAULT_LOAD_FACTOR) return;
        DynamicArrayChainingHashMap<K, V> hashMap = new DynamicArrayChainingHashMap<>(table.length * 2);
        for (ArrayList<Node<K, V>> nodes: table){
            if (nodes == null) continue;
            nodes.forEach(node -> hashMap.put(node.key, node.value));
        }
        this.table = hashMap.table;
    }

}
