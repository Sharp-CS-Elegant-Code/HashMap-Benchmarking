package com.sharpcselegantcode.HashMap.impl;

public abstract class ProbingFunction {

    protected int i;

    public ProbingFunction() {reset();}

    protected abstract int next(int h, int m);

    protected void reset(){
        i = 0;
    }

}
