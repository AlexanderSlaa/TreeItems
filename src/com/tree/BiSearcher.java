package com.tree;

public interface BiSearcher<K,V,Q> {

    public Q onResult(K k, V v);

    public boolean condition(K k, V v);

    public Q onNoResult();


}
