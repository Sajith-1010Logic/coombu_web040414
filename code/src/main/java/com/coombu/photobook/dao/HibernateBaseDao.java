/*
 * Created on Oct 27, 2013
 *
 */
package com.coombu.photobook.dao;



import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Base class for Hibernate DAOs that provides convenience methods for using the
 * Hibernate API.
 * 
 * @author Fekade Aytaged
 */
@Repository
public class HibernateBaseDao implements IDao {
    /**
     * Hibernate SessionFactory to use
     */
	@Autowired
    private SessionFactory sessionFactory;

    /**
     * @return sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory
     *            The sessionFactory to set.
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return the current Hibernate Session
     */
    public Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

    /**
     * @param sqlString
     * @return Hibernate Query object
     */
    public Query createSQLQuery(String sqlString) {
        return getCurrentSession().createSQLQuery(sqlString);
    }

    /**
     * @param query
     * @return Hibernate Query object
     */
    public Query getNamedExternalQuery(String query) {
        return getCurrentSession().getNamedQuery(query);
    }

    /**
     * @param hqlString
     * @return Hibernate Query object
     */
    public Query createHQLQuery(String hqlString) {
        return getCurrentSession().createQuery(hqlString);
    }

    /**
     * Returns the Connection object used by Hibernate.
     * 
     * @return the JDBC connection in use by the Session
     */
/*    public Connection getSessionJDBCConnection() {
        return getCurrentSession().connection();
    }
*/
    /**
     * @param o
     * @return the generated identifier
     */
        @Override
    public Object create(Object o) {
        return getCurrentSession().save(o);
    }

    /**
     * @param o
     */
        @Override
    public void update(Object o) {
        getCurrentSession().update(o);
    }
    /**
     * @param o
     */
        @Override
    public Serializable save(Object o) {
        return getCurrentSession().save(o);
    }
    /**
     * @param o
     */
        @Override
    public void delete(Object o) {
        getCurrentSession().delete(o);
    }

    /**
     * @param objectClass
     * @param id
     * @return the persistent instance
     */
    public Object findById(Class<?> objectClass, int id) {
        return findById(objectClass, new Integer(id));
    }
    /**
     * @param objectClass
     * @param id
     * @return the persistent instance
     */
    public Object findById(Class<?> objectClass, short id) {
        return findById(objectClass, new Short(id));
    }

    /**
     * @param objectClass
     * @param id
     * @return the persistent instance
     */
        @Override
    public Object findById(Class<?> objectClass, Serializable id) {
        return getCurrentSession().get(objectClass, id);
    }

    /**
     * Retrieves an Object by Named Query using the given parameter and type.
     * 
     * @param queryName
     * @param paramName
     * @param object
     * @param hibernateType
     * 
     * @return the single result or null
     */
    public Object retrieveObjectByParameterForNamedQuery(String queryName,
            String paramName, Object object, Type hibernateType) {
        Query q = getCurrentSession().getNamedQuery(queryName);
        q.setParameter(paramName, object, hibernateType);
        return q.uniqueResult();
    }

    /**
     * Retrieves a List of Objects by Named Query using the given parameter and
     * type.
     * 
     * @param queryName
     * @param paramName
     * @param object
     * @param hibernateType
     * 
     * @return the result list
     */
    
	public List<?> retrieveListByParameterForNamedQuery(String queryName,
            String paramName, Object object, Type hibernateType) {
        Query q = getCurrentSession().getNamedQuery(queryName);
        q.setParameter(paramName, object, hibernateType);
        return q.list();
    }

    /**
     * Retrieves an Object by Named Query using the given parameters and types.
     * 
     * @param queryName
     * @param paramNames
     * @param objects
     * @param hibernateTypes
     * @return the single result or null
     */
    public Object retrieveObjectByParametersForNamedQuery(String queryName,
            String[] paramNames, Object[] objects, Type[] hibernateTypes) {
        Query q = getCurrentSession().getNamedQuery(queryName);
        for (int i = 0; i < paramNames.length; i++) {
            q.setParameter(paramNames[i], objects[i], hibernateTypes[i]);
        }
        return q.uniqueResult();
    }

    /**
     * Retrieves a List of Objects by Named Query using the given parameters and
     * types.
     * 
     * @param queryName
     * @param paramNames
     * @param objects
     * @param hibernateTypes
     * @return the result list
     */
    @SuppressWarnings("unchecked")
	public List<Object> retrieveListByParametersForNamedQuery(String queryName,
            String[] paramNames, Object[] objects, Type[] hibernateTypes) {
        Query q = getCurrentSession().getNamedQuery(queryName);
        for (int i = 0; i < paramNames.length; i++) {
            q.setParameter(paramNames[i], objects[i], hibernateTypes[i]);
        }
        return q.list();
    }

	public Object merge(Object obj) {
		return getCurrentSession().merge(obj);
	}

	@Override
	public void saveOrUpdate(Object obj) 
	{
		getCurrentSession().saveOrUpdate(obj);
	}


}