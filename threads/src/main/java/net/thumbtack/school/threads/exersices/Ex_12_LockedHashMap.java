package net.thumbtack.school.threads.exersices;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


// Да, данная реализация будет хуже ConcurrentHashMap, т.к. в ней блокируется вся HashMap,
// в то время как в конкурентной HashMap блокируется лишь её часть.
//
// "По умолчанию, уровень одновременности равен 16, и соответственно Map разделяется на 16 частей и каждая часть управляется отдельной блокировкой.
// Это означает, что 16 потоков могут работать с Map одновременно, пока они работают с разными частями Map.
// Это делает ConcurrentHashMap высокопроизводительным, в тоже время не ухудшая потоко-безопасность."

public class Ex_12_LockedHashMap {

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<Object, Object> map;

    public Ex_12_LockedHashMap(Map<Object, Object> map) {
        this.map = map;
    }

    public Object put(Object key, Object value) {
        lock.writeLock().lock();
        try {
            map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
        return key;
    }

    public Object get(Object key) {
        lock.readLock().lock();
        try {
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Object remove(Object key) {
        lock.writeLock().lock();
        try {
            return map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }


    public boolean remove(Object key, Object value) {
        lock.writeLock().lock();
        try {
            return map.remove(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Object replace(Object key, Object newValue) {
        lock.writeLock().lock();
        try {
            return map.replace(key, newValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return map.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsKey(Object key) {
        lock.readLock().unlock();
        try {
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsValue(Object value) {
        lock.readLock().unlock();
        try {
            return map.containsValue(value);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            map.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return map.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}