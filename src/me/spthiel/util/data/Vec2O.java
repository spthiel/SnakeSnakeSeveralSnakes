package me.spthiel.util.data;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Vec2O<K> extends Entry<K,K> implements Iterable<K> {

    public Vec2O() {
        super();
    }

    public Vec2O(K key, K value) {
        super(key,value);
    }
    
    @Override
    public Iterator<K> iterator() {
    
        return new Iterator<>() {
            
            private boolean next = true;
            private boolean first = true;
            
            @Override
            public boolean hasNext() {
        
                return next;
            }
    
            @Override
            public K next() {
        
                if(first) {
                    first = false;
                    return getKey();
                } else {
                    next = false;
                    return getValue();
                }
            }
        };
    }

    public Stream<K> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
