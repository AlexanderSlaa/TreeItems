package com.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class TreeQHashSet<K, Q extends HashMap> extends HashMap<K, Q> {

    private final Supplier<Q> collectorSupplier;

    public TreeQHashSet(Supplier<Q> collectorSupplier){
        this.collectorSupplier = collectorSupplier;
    }

    @SuppressWarnings("unused")
    public <K2,V> V put(K key, K2 key2, V value) {
        if(!containsKey(key)){
            put(key, (Q) collectorSupplier.get());
        }
        return (V) super.get(key).put(key2,value);
    }

    @Override
    public Q get(Object key) {
        if(!containsKey(key)){
            put((K) key, collectorSupplier.get());
        }
        return super.get(key);
    }

    @SafeVarargs
    public final <V> ArrayList<V> valueSet(K ... filters){
        ArrayList<V> valueSet = new ArrayList<>();
        if(filters.length > 0){
            for (K filter : filters) {
                valueSet.addAll((Collection<? extends V>) get(filter).values());
            }
        }else{
            values().forEach(k2VHashMap -> valueSet.addAll((Collection<? extends V>) k2VHashMap.values()));
        }
        return valueSet;
    }

    public <K2, V> TreeQHashSet<K, Q> sort(Q map, BiFunction<K2,V, K> sortFunction){
        map.forEach((k2, v) -> put(sortFunction.apply((K2)k2,(V)v),k2, v));
        return this;
    }
    public <V> TreeQHashSet<K, Q> sort(Q map, Function<V, K> sortFunction){
        map.forEach((k2, v) -> put(sortFunction.apply((V) v),k2, v));
        return this;
    }

    @SuppressWarnings("unused")
    public <V>  void safeFor(K section, TreeSetInterface<V> handler){
        if(containsKey(section)){
            valueSet(section).forEach(o -> {
                handler.manipulate((V)o);
            });
        }
    }


    public interface TreeSetInterface<V>{
        void manipulate(V v);
    }

}
