package com.jeffreyromero.liss.repositories;

import java.util.ArrayList;

public interface ListsDataSource<T> {

    boolean contains(String key);

    void clearAll();

    T get(String key);

    T get(int position);

    ArrayList<T> getAll();

    void put(T t);

    void remove(String key);

    ArrayList<String> getAllKeys();

    void renameKey(String oldKey, String newKey);

    boolean isEmpty();

}
