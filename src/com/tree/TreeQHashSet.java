package com.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class TreeQHashSet<K, K2, V> extends HashMap<K, HashMap<K2, V>> {

    private final Supplier<? extends HashMap<K2, V>> collectorSupplier;

    public TreeQHashSet(Supplier<? extends HashMap<K2, V>> collectorSupplier){
        this.collectorSupplier = collectorSupplier;
    }

    @SuppressWarnings("unused")
    public V put(K key, K2 key2, V value) {
        if(!containsKey(key)){
            put(key, collectorSupplier.get());
        }
        return super.get(key).put(key2,value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<K2, V> get(Object key) {
        if(!containsKey(key)){
            put((K) key, collectorSupplier.get());
        }
        return super.get(key);
    }

    @SafeVarargs
    public final ArrayList<V> valueSet(K ... filters){
        ArrayList<V> valueSet = new ArrayList<>();
        if(filters.length > 0){
            for (K filter : filters) {
                valueSet.addAll(get(filter).values());
            }
        }else{
            values().forEach(k2VHashMap -> valueSet.addAll(k2VHashMap.values()));
        }
        return valueSet;
    }

    @SuppressWarnings("unused")
    public void safeFor(K section, TreeSetInterface<V> handler){
        if(containsKey(section)){
            valueSet(section).forEach(handler::manipulate);
        }
    }


    public interface TreeSetInterface<V>{
        void manipulate(V v);
    }

}
