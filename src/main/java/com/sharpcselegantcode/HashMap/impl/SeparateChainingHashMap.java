package com.sharpcselegantcode.HashMap.impl;

import com.sharpcselegantcode.HashMap.HashMap;

@SuppressWarnings("all")
public class SeparateChainingHashMap<K, V> implements HashMap<K, V> {

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static class Node<K, V> {
        K key;
        V value;
        int hash;
        Node<K, V> next;

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

    public SeparateChainingHashMap(){
        this(16);
    }

    public SeparateChainingHashMap(int initialCapacity){
        table = new Node[initialCapacity];
        this.size = 0;
    }

    @Override
    public V get(K key) {
        Node<K, V> n;
        for(n = table[hash(key) % table.length]; n != null && !n.key.equals(key); n = n.next);
        return n != null ? n.value : null;
    }

    @Override
    public V put(K key, V value) {
        size++;
        ensureCapacity();
        int i;
        Node<K, V> n;
        for(n = table[(i = hash(key) % table.length)]; n != null && !n.key.equals(key) && n.next != null; n = n.next);
        if (n == null) table[i] = Node.create(key, value);
        else n.next = Node.create(key, value);
        return n != null ? n.value : null;
    }

    @Override
    public void delete(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.lang.String toString(){
        StringBuilder stringBuilder = new StringBuilder("[");
        for (Node<K, V> node: table){
            stringBuilder.append(node + ", ");
            if (node == null || node.next == null) continue;
            for(; (node = node.next) != null; stringBuilder.append(node + ", "));
        }
        return stringBuilder.subSequence(0, stringBuilder.length() - 2) + "]";

    }

    private void ensureCapacity(){
        if (((float)size / table.length) < DEFAULT_LOAD_FACTOR) return;
        SeparateChainingHashMap<K, V> hashMap = new SeparateChainingHashMap(table.length * 2);
        for (Node<K, V> node: table){
            for (Node<K, V> node2 = node; node2 != null; hashMap.put(node2.key, node2.value), node2 = node2.next);
        }
        this.table = hashMap.table;
    }
}
