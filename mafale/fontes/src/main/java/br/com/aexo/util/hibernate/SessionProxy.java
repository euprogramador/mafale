package br.com.aexo.util.hibernate;

import java.io.Serializable;
import java.sql.Connection;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LobHelper;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TypeHelper;
import org.hibernate.UnknownProfileException;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
@SuppressWarnings({"deprecation","rawtypes"})
public class SessionProxy implements Session {

	private static final long serialVersionUID = 1L;
	
	private final Session session;

	public SessionProxy(Session session) {
		this.session = session;
	}

	public EntityMode getEntityMode() {
		return session.getEntityMode();
	}

	public Session getSession(EntityMode entityMode) {
		return session.getSession(entityMode);
	}

	public void flush() throws HibernateException {
		session.flush();
	}

	public void setFlushMode(FlushMode flushMode) {
		session.setFlushMode(flushMode);
	}

	public FlushMode getFlushMode() {
		return session.getFlushMode();
	}

	public void setCacheMode(CacheMode cacheMode) {
		session.setCacheMode(cacheMode);
	}

	public CacheMode getCacheMode() {
		return session.getCacheMode();
	}

	public SessionFactory getSessionFactory() {
		return session.getSessionFactory();
	}


	public Connection connection() throws HibernateException {
		return session.connection();
	}

	public Connection close() throws HibernateException {
		return session.close();
	}

	public void cancelQuery() throws HibernateException {
		session.cancelQuery();
	}

	public boolean isOpen() {
		return session.isOpen();
	}

	public boolean isConnected() {
		return session.isConnected();
	}

	public boolean isDirty() throws HibernateException {
		return session.isDirty();
	}

	public boolean isDefaultReadOnly() {
		return session.isDefaultReadOnly();
	}

	public void setDefaultReadOnly(boolean readOnly) {
		session.setDefaultReadOnly(readOnly);
	}

	public Serializable getIdentifier(Object object) throws HibernateException {
		return session.getIdentifier(object);
	}

	public boolean contains(Object object) {
		return session.contains(object);
	}

	public void evict(Object object) throws HibernateException {
		session.evict(object);
	}

	public Object load(Class theClass, Serializable id, LockMode lockMode) throws HibernateException {
		return session.load(theClass, id, lockMode);
	}

	public Object load(Class theClass, Serializable id, LockOptions lockOptions) throws HibernateException {
		return session.load(theClass, id, lockOptions);
	}

	public Object load(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
		return session.load(entityName, id, lockMode);
	}

	public Object load(String entityName, Serializable id, LockOptions lockOptions) throws HibernateException {
		return session.load(entityName, id, lockOptions);
	}

	public Object load(Class theClass, Serializable id) throws HibernateException {
		return session.load(theClass, id);
	}

	public Object load(String entityName, Serializable id) throws HibernateException {
		return session.load(entityName, id);
	}

	public void load(Object object, Serializable id) throws HibernateException {
		session.load(object, id);
	}

	public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException {
		session.replicate(object, replicationMode);
	}

	public void replicate(String entityName, Object object, ReplicationMode replicationMode) throws HibernateException {
		session.replicate(entityName, object, replicationMode);
	}

	public Serializable save(Object object) throws HibernateException {
		return session.save(object);
	}

	public Serializable save(String entityName, Object object) throws HibernateException {
		return session.save(entityName, object);
	}

	public void saveOrUpdate(Object object) throws HibernateException {
		session.saveOrUpdate(object);
	}

	public void saveOrUpdate(String entityName, Object object) throws HibernateException {
		session.saveOrUpdate(entityName, object);
	}

	public void update(Object object) throws HibernateException {
		session.update(object);
	}

	public void update(String entityName, Object object) throws HibernateException {
		session.update(entityName, object);
	}

	public Object merge(Object object) throws HibernateException {
		return session.merge(object);
	}

	public Object merge(String entityName, Object object) throws HibernateException {
		return session.merge(entityName, object);
	}

	public void persist(Object object) throws HibernateException {
		session.persist(object);
	}

	public void persist(String entityName, Object object) throws HibernateException {
		session.persist(entityName, object);
	}

	public void delete(Object object) throws HibernateException {
		session.delete(object);
	}

	public void delete(String entityName, Object object) throws HibernateException {
		session.delete(entityName, object);
	}

	public void lock(Object object, LockMode lockMode) throws HibernateException {
		session.lock(object, lockMode);
	}

	public void lock(String entityName, Object object, LockMode lockMode) throws HibernateException {
		session.lock(entityName, object, lockMode);
	}

	public LockRequest buildLockRequest(LockOptions lockOptions) {
		return session.buildLockRequest(lockOptions);
	}

	public void refresh(Object object) throws HibernateException {
		session.refresh(object);
	}

	public void refresh(Object object, LockMode lockMode) throws HibernateException {
		session.refresh(object, lockMode);
	}

	public void refresh(Object object, LockOptions lockOptions) throws HibernateException {
		session.refresh(object, lockOptions);
	}

	public LockMode getCurrentLockMode(Object object) throws HibernateException {
		return session.getCurrentLockMode(object);
	}

	public Transaction beginTransaction() throws HibernateException {
		return session.beginTransaction();
	}

	public Transaction getTransaction() {
		return session.getTransaction();
	}

	public Criteria createCriteria(Class persistentClass) {
		return session.createCriteria(persistentClass);
	}

	public Criteria createCriteria(Class persistentClass, String alias) {
		return session.createCriteria(persistentClass, alias);
	}

	public Criteria createCriteria(String entityName) {
		return session.createCriteria(entityName);
	}

	public Criteria createCriteria(String entityName, String alias) {
		return session.createCriteria(entityName, alias);
	}

	public Query createQuery(String queryString) throws HibernateException {
		return session.createQuery(queryString);
	}

	public SQLQuery createSQLQuery(String queryString) throws HibernateException {
		return session.createSQLQuery(queryString);
	}

	public Query createFilter(Object collection, String queryString) throws HibernateException {
		return session.createFilter(collection, queryString);
	}

	public Query getNamedQuery(String queryName) throws HibernateException {
		return session.getNamedQuery(queryName);
	}

	public void clear() {
		session.clear();
	}

	public Object get( Class clazz, Serializable id) throws HibernateException {
		return session.get(clazz, id);
	}

	public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException {
		return session.get(clazz, id, lockMode);
	}

	public Object get(Class clazz, Serializable id, LockOptions lockOptions) throws HibernateException {
		return session.get(clazz, id, lockOptions);
	}

	public Object get(String entityName, Serializable id) throws HibernateException {
		return session.get(entityName, id);
	}

	public Object get(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
		return session.get(entityName, id, lockMode);
	}

	public Object get(String entityName, Serializable id, LockOptions lockOptions) throws HibernateException {
		return session.get(entityName, id, lockOptions);
	}

	public String getEntityName(Object object) throws HibernateException {
		return session.getEntityName(object);
	}

	public Filter enableFilter(String filterName) {
		return session.enableFilter(filterName);
	}

	public Filter getEnabledFilter(String filterName) {
		return session.getEnabledFilter(filterName);
	}

	public void disableFilter(String filterName) {
		session.disableFilter(filterName);
	}

	public SessionStatistics getStatistics() {
		return session.getStatistics();
	}

	public boolean isReadOnly(Object entityOrProxy) {
		return session.isReadOnly(entityOrProxy);
	}

	public void setReadOnly(Object entityOrProxy, boolean readOnly) {
		session.setReadOnly(entityOrProxy, readOnly);
	}

	public void doWork(Work work) throws HibernateException {
		session.doWork(work);
	}

	public Connection disconnect() throws HibernateException {
		return session.disconnect();
	}

	public void reconnect() throws HibernateException {
		session.reconnect();
	}

	public void reconnect(Connection connection) throws HibernateException {
		session.reconnect(connection);
	}

	public boolean isFetchProfileEnabled(String name) throws UnknownProfileException {
		return session.isFetchProfileEnabled(name);
	}

	public void enableFetchProfile(String name) throws UnknownProfileException {
		session.enableFetchProfile(name);
	}

	public void disableFetchProfile(String name) throws UnknownProfileException {
		session.disableFetchProfile(name);
	}

	public TypeHelper getTypeHelper() {
		return session.getTypeHelper();
	}

	public LobHelper getLobHelper() {
		return session.getLobHelper();
	}

}
