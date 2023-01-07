package com.sharpcselegantcode.jmh;

import com.sharpcselegantcode.HashMap.impl.ProbingFunction;

import java.util.LinkedList;

public class ProbingFunctions {

    final static ProbingFunction linearProbing = new ProbingFunction() {
        @Override
        protected int next(int h, int m) {
            return (h + i++)%m;
        }
    };

    final static ProbingFunction fibonacciProbing = new ProbingFunction() {

        int j = 1;

        @Override
        protected int next(int h, int m) {
            int next = i;
            j += i;
            i = j - i;
            return (h + next)%m;
        }

         public void reset(){
            j = 1;
            i = 0;
        }
    };

    /*
     Via Wikipedia
     */
    public static long roundUp2(long v){
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v |= v >> 32;
        v++;
        return v;
    }

}
