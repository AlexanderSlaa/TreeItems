package com.tree;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TreeQSet<K,V> extends HashMap<K, ArrayList<V>> {

    private final Supplier<? extends ArrayList<V>> collectorSupplier;

    public TreeQSet(Supplier<? extends ArrayList<V>> collectorSupplier){
        this.collectorSupplier = collectorSupplier;
    }

    @SuppressWarnings("unused")
    @SafeVarargs
    public final void addAllOn(K key, V... values){
        if(!containsKey(key)){
            populate(key);
        }
        get(key).addAll(Arrays.asList(values));
    }

    public K findKey(Predicate<K> keyPredicate){
        for (K k : keySet()) {
            if(keyPredicate.test(k)){
                return k;
            }
        }
        return null;
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
            put(key, collectorSupplier.get());
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
        for (Entry<K, ? extends ArrayList<V>> entry : this.entrySet()) {
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
    public void Search(Predicate<K> conditional, Consumer<ArrayList<V>> consumer){
        for (K k : keySet()) {
            if(conditional.test(k)){
                consumer.accept(get(k));
                break;
            }
        }
    }


    @SuppressWarnings("unused")
    public Optional<ArrayList<V>> Search(Predicate<K> conditional){
        Optional<ArrayList<V>> arrayList = Optional.empty();
        for (K k : keySet()) {
            if(conditional.test(k)){
                arrayList = Optional.of(get(k));
                break;
            }
        }
        return arrayList;
    }

    public <Q> Q Search(BiSearcher<K,ArrayList<V>, Q> biSearcher){
        for (K k : keySet()) {
            if(biSearcher.condition(k, get(k))){
                return biSearcher.onResult(k, get(k));
            }
        }
        return biSearcher.onNoResult();
    }

    public <Q> Q Search(Searcher<K, Q> biSearcher){
        for (K k : keySet()) {
            if(biSearcher.condition(k)){
                return biSearcher.onResult(k);
            }
        }
        return biSearcher.onNoResult();
    }

    public void safeForBranch(K branch, Consumer<V> consumer){
        if(containsKey(branch)){
            branch(branch).forEach(consumer);
        }
    }

}
