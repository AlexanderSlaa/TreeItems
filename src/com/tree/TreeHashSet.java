package com.tree;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unused")
public class TreeHashSet<K, K2, V> extends HashMap<K, HashMap<K2, V>> {

    @SuppressWarnings("unused")
    public V put(K key, K2 key2, V value) {
        if(!containsKey(key2)){
            put(key, new HashMap<>());
        }
        return super.get(key).put(key2,value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<K2, V> get(Object key) {
        if(!containsKey(key)){
            put((K) key, new HashMap<>());
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
