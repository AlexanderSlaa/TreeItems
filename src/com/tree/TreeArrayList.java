package com.tree;

import javax.sql.RowSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class TreeArrayList<K,V> extends HashMap<K, ArrayList<V>> {

    @SuppressWarnings("unused")
    @SafeVarargs
    public final void addAllOn(K key, V... values){
        if(!containsKey(key)){
            populate(key);
        }
        get(key).addAll(Arrays.asList(values));
    }

    @SuppressWarnings("unused")
    public void addOn(K key, V value){
        if(!containsKey(key)){
            populate(key);
        }
        get(key).add(value);
    }

    public void populate(K key){
        if(!containsKey(key)) {
            put(key, new ArrayList<>());
        }
    }

    @SuppressWarnings("unused")
    public void populateAll(Collection<K> keys){
        keys.forEach(this::populate);
    }

    @SuppressWarnings("unused")
    public ArrayList<V> branch(K key){
        return get(key);
    }

    @SuppressWarnings("unused")
    public K getBranchOf(V value){
        return getBranchOf(v -> v.equals(value));
    }

    @SuppressWarnings("unused")
    public K getBranchOf(Function<V, Boolean> inspector){
        K key = null;
        boolean found = false;
        for (Entry<K, ArrayList<V>> entry : this.entrySet()) {
            K k = entry.getKey();
            ArrayList<V> vs = entry.getValue();
            for (V v : vs) {
                if (inspector.apply(v)) {
                    key = k;
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        return key;
    }

    @SuppressWarnings("unused")
    public void Search(Function<K, Boolean> conditional, Consumer<ArrayList<V>> consumer){
        for (K k : keySet()) {
            if(conditional.apply(k)){
                consumer.accept(get(k));
                break;
            }
        }
    }

    public void safeForBranch(K branch, Consumer<V> consumer){
        if(containsKey(branch)){
            branch(branch).forEach(consumer);
        }
    }

//    public ArrayList<HorizonSet<K,V>> toRowSet(){
//        HashMap<K, HorizonSet<K,V>> table = new HashMap<>();
//        keySet().forEach(k -> {
//            table.put(k, new HorizonSet<>());
//        });
//
//        table.values().forEach();
//
//    }
//
//    public static class HorizonSet<K,V> extends HashMap<K,V> {
//
//    }

}
