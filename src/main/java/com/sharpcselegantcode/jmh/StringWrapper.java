package com.sharpcselegantcode.jmh;

public class StringWrapper {
    String s;

    public StringWrapper(String s){this.s = s;}

    @Override
    public boolean equals(Object o){
        return o instanceof String && s.equals(o)
                || o instanceof StringWrapper && o.equals(s);
    }

    @Override
    public int hashCode(){
        return s != null && s.length() > 0 ? (int) s.charAt(0) : 0;
//     return s.hashCode();
    }

    @Override
    public String toString(){
        return s;
    }
}
