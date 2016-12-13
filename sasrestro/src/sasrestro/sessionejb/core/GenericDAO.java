package sasrestro.sessionejb.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import sasrestro.misc.JSFMessageUtil;

/**
 * @author nebula
 * 
 */
public abstract class GenericDAO<T> {

	/*
	 * private static final EntityManagerFactory emf =
	 * Persistence.createEntityManagerFactory("icanPU"); private EntityManager
	 * em;
	 */
	/*
	 * Persistence context is injected with following @PersistenceContext
	 * annotation. This uses all persistence configurations as specified in the
	 * persistence.xml.
	 * 
	 * Note this kind of injection can only be done for JTA data sources.
	 */
	@PersistenceContext(unitName = "sasrestroPU")
	private EntityManager em;
	private Class<T> entityClass;

	public EntityManager getEntityManager() {
		return this.em;
	}

	public void joinTransaction() {
		/* em = emf.createEntityManager(); */
		em.joinTransaction();
	}

	public GenericDAO(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public void save(T entity) {
		em.persist(entity);
	}

	// Added by Sudeep for bulk Insert of List object.
	public void saveList(List<T> objList) {
		for (Iterator<T> iterator = objList.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			em.persist(t);
		}
	}

	public void delete(Object id, Class<T> classe) {
		T entityToBeRemoved = em.getReference(classe, id);

		em.remove(entityToBeRemoved);
	}

	public T update(T entity) {
		return em.merge(entity);
	}

	public int truncateUsingNative(String tableName) {
		Query qry = em.createNativeQuery("TRUNCATE TABLE " + tableName);

		return qry.executeUpdate();
	}

	// Added by Sudeep for bulk Update of List object.
	public void updateList(List<T> entity) {
		for (Iterator<T> iterator = entity.iterator(); iterator.hasNext();) {
			T t = (T) iterator.next();
			em.merge(t);
		}
	}

	public T find(int entityID) {
		// em.getEntityManagerFactory().getCache().evict(entityClass, entityID);
		return em.find(entityClass, entityID);
	}

	public T find(long entityID) {
		// em.getEntityManagerFactory().getCache().evict(entityClass, entityID);
		return em.find(entityClass, entityID);
	}

	public T find(Object compositePkObject) {
		// em.getEntityManagerFactory().getCache().evict(entityClass, entityID);
		return em.find(entityClass, compositePkObject);
	}

	public T findReferenceOnly(int entityID) {
		return em.getReference(entityClass, entityID);
	}

	// Using the unchecked because JPA does not have a
	// em.getCriteriaBuilder().createQuery()<T> method
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll() {
		CriteriaQuery cq = null;
		if (isDbAccessible()) {
			try {
				cq = em.getCriteriaBuilder().createQuery();
				cq.select(cq.from(entityClass));
				return em.createQuery(cq).getResultList();
			} catch (org.eclipse.persistence.exceptions.DatabaseException ex) {
				System.out.println("The zzz error is :" + ex.toString());
				JSFMessageUtil jsfMessageUtil = new JSFMessageUtil();
				jsfMessageUtil
						.sendErrorMessageToUser("Database Server is unavailable or not accessible! Please, contact your system admin!");
				return null;
			}
		}
		return null;
	}

	private boolean isDbAccessible() {
		return em.isOpen();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllWithGivenCondition(String namedQuery,
			Map<String, Object> parameters) {
		List<T> result = null;
		Query query = em.createNamedQuery(namedQuery);

		if (parameters != null && !parameters.isEmpty()) {
			populateQueryParameters(query, parameters);
		}

		result = (List<T>) query.getResultList();

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAllWithGivenConditionLazyLoading(String namedQuery,
			Map<String, Object> parameters,int startingAt, int maxPerPage) {
		List<T> result = null;
		Query query = em.createNamedQuery(namedQuery);

		if (parameters != null && !parameters.isEmpty()) {
			populateQueryParameters(query, parameters);
		}
		query.setFirstResult(startingAt);
		query.setMaxResults(maxPerPage);
		
		result = (List<T>) query.getResultList();

		return result;
		
		}

	@SuppressWarnings("unchecked")
	public List<T> findAllWithGivenConditionJpql(String jpql,
			Map<String, Object> parameters) {
		List<T> result = null;
		Query query = em.createQuery(jpql);

		if (parameters != null && !parameters.isEmpty()) {
			populateQueryParameters(query, parameters);
		}

		result = (List<T>) query.getResultList();

		return result;
	}

	@SuppressWarnings("unchecked")
	public T findOneWithGivenConditionJpql(String jpql,
			Map<String, Object> parameters) {
		Query query = em.createQuery(jpql);

		if (parameters != null && !parameters.isEmpty()) {
			populateQueryParameters(query, parameters);
		}
		return (T) query.getSingleResult();
	}

	// Using the unchecked because JPA does not have a
	// query.getSingleResult()<T> method
	@SuppressWarnings("unchecked")
	protected T findOneResult(String namedQuery, Map<String, Object> parameters) {
		T result = null;

		try {
			if (!em.isOpen()) {
				JSFMessageUtil jsfMessageUtil = new JSFMessageUtil();
				jsfMessageUtil
						.sendErrorMessageToUser("Database Server is unavailable or not accessible! Please, contact your system admin!");
			} else {
				Query query = em.createNamedQuery(namedQuery);

				// Method that will populate parameters if they are passed not
				// null and empty
				if (parameters != null && !parameters.isEmpty()) {
					populateQueryParameters(query, parameters);
				}

				result = (T) query.getSingleResult();
			}

		} catch (NoResultException e) {
			// JSFMessageUtil jsfMessageUtil = new JSFMessageUtil();
			// jsfMessageUtil.sendErrorMessageToUser("No Information Found...!");

			// e.printStackTrace();
			return null;
		} catch (org.eclipse.persistence.exceptions.DatabaseException e) {
			JSFMessageUtil jsfMessageUtil = new JSFMessageUtil();
			jsfMessageUtil
					.sendErrorMessageToUser("Database Server is unavailable or not accessible!");
			e.printStackTrace();
		}

		return result;
	}

	private void populateQueryParameters(Query query,
			Map<String, Object> parameters) {
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * @param startingAt
	 * @param maxPerPage
	 * @param t
	 * @return list of persisted entities which belong to this class t
	 */
	@SuppressWarnings("unchecked")
	public List<T> getAllLazyEntities(int startingAt, int maxPerPage, Class<T> t) {
		// regular query that will search for players in the db
		Query query = getEntityManager().createQuery(
				"select p from " + t.getName() + " p");
		query.setFirstResult(startingAt);
		query.setMaxResults(maxPerPage);

		return query.getResultList();
	}

	/**
	 * @param clazz
	 * @return count of existing entity rows from backend
	 */
	public int countTotalRows(Class<T> clazz) {
		Query query = getEntityManager().createQuery(
				"select COUNT(p) from " + clazz.getName() + " p");

		Number result = (Number) query.getSingleResult();

		return result.intValue();
	}

	/**
	 * @return count of existing entity rows from backend acccording to given
	 *         condition
	 */
	public int countTotalRowsWithCond(Class<T> clazz, String Cond) {
		Query query = getEntityManager()
				.createQuery(
						"select COUNT(p) from " + clazz.getName() + " p "
								+ Cond + "  ");

		Number result = (Number) query.getSingleResult();

		return result.intValue();
	}
}