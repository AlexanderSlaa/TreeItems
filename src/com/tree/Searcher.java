package com.tree;

public interface Searcher<T,Q> {

    public Q onResult(T t);

    public boolean condition(T t);

    public Q onNoResult();



}
