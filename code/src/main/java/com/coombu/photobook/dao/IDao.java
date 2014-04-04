package com.coombu.photobook.dao;

/*
 * Created on Oct 27, 2013
 * 
 */

import java.io.Serializable;

/**
 * @author Fekade Aytaged
 * 
 *  
 */
public interface IDao {
    /**
     * @param o
     * @return Object
     */
    public Object create(Object o);

    /**
     * @param objectClass
     * @param id
     * @return Object
     */
    public Object findById(Class<?> objectClass, Serializable id);

    /**
     * @param o
     */
    public void update(Object o);

    /**
     * @param o
     * @return Id of the object
     */
    public Serializable save(Object o);
    /**
     * @param o
     */
    public void delete(Object o);

    /**
     * @param o
     */
    public void saveOrUpdate(Object o);

}