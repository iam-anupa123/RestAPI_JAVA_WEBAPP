/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.dao;

/**
 *
 * @author Anupa Vitharana
 */
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GenericDAO<T> {
   
    protected static final Map<String, Object> storage = new ConcurrentHashMap<>();

    public abstract void create(T entity);
    public abstract T getById(String id);
    public abstract List<T> getAll();
    public abstract void delete(String id);
}
