package fred.pittnotesdb.DAOs;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class DAOFacade<T> implements Serializable {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(DAOFacade.class.getName());

    /**
     * Represents the class of entities that we are working with
     */
    private Class<T> entityClass;

    /**
     * EntityManager to interact with database
     */
    private EntityManager em;

    /**
     * Id to user in serialization
     */
    private static final long serialVersionUID = 1L;

    /**
     * Flag for if transactions should be used. Note that this should only be
     * set if using transaction-type="RESOURCE_LOCAL"
     */
    private boolean useTransactions;

    
    /**
     * Constructor
     *
     * @param entityClass The entity class we are working with
     */
    public DAOFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.useTransactions = false;
    }

    /**
     * Constructor
     *
     * @param entityClass The entity class we are working with
     * @param em The entity manager to use
     */
    public DAOFacade(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
        this.useTransactions = false;
    }

    /**
     * Constructor
     *
     * @param entityClass The entity class we are working with
     * @param useTransactions True if transactions should be used, false
     * otherwise
     */
    public DAOFacade(Class<T> entityClass, boolean useTransactions) {
        this.entityClass = entityClass;
        this.useTransactions = useTransactions;
    }

    /**
     * Constructor
     *
     * @param entityClass The entity class we are working with
     * @param em The entity manager to use
     * @param useTransactions True if transactions should be used, false
     * otherwise
     */
    public DAOFacade(Class<T> entityClass, EntityManager em, boolean useTransactions) {
        this.entityClass = entityClass;
        this.em = em;
        this.useTransactions = useTransactions;
    }
    
    public Class getEntityClass(){
        return entityClass;
    }

    /**
     * Sets the entity manager
     *
     * @param em The entity manager to user
     */
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     * Gets the entity manager being used
     *
     * @return The entity manager being used
     */
    public EntityManager getEntityManager() {
        return em;
    }


    /**
     * Gets whether or not transactions are being used
     *
     * @return True if transactions are being used, false otherwise
     */
    public boolean isUseTransactions() {
        return useTransactions;
    }

    /**
     * Sets whether or not transactions are being used
     *
     * @param useTransactions True if transactions are to be used, false
     * otherwise
     */
    public void setUseTransactions(boolean useTransactions) {
        this.useTransactions = useTransactions;
    }

    /**
     * This will persist the entity
     *
     * @param obj The object to persist
     * @return The newly persisted object, null on error
     */
    public T create(T obj) {
        checkEntityManager();
        try {
            if (useTransactions) {
                em.getTransaction().begin();
            }
            em.persist(obj);
            if (useTransactions) {
                em.getTransaction().commit();
            }
            logger.log(Level.FINEST, "Created {0}: {1}", new Object[]{obj.getClass().getSimpleName(), obj});
         
            return obj;
        } catch (ConstraintViolationException e) {
            logger.log(Level.WARNING, getConstraintViolationErrorMessage(e));
            logger.log(Level.SEVERE, "Could not update " + obj.getClass().getSimpleName() + ": " + obj, e);
            return null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not create " + obj.getClass().getSimpleName() + ": " + obj, e);
            return null;
        }
    }

    /**
     * This will delete a object form the database. The entity will no longer
     * persist
     *
     * @param obj The entity to delete
     * @return True if the operation completed successfully, false otherwise
     */
    public boolean delete(T obj) {
       checkEntityManager();
        try {
            String objStr = obj.toString();
            obj = update(obj);
            if (useTransactions) {
                em.getTransaction().begin();
            }
            em.remove(obj);
            if (useTransactions) {
                em.getTransaction().commit();
            }
            logger.log(Level.FINEST, "Deleted {0}: {1}", new Object[]{obj.getClass().getSimpleName(), objStr});
           
            return true;
        } catch (ConstraintViolationException e) {
            logger.log(Level.WARNING, getConstraintViolationErrorMessage(e));
            logger.log(Level.SEVERE, "Could not update " + obj.getClass().getSimpleName() + ": " + obj, e);
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not delete: " + obj.getClass().getSimpleName() + ": " + obj, e);
            return false;
        }
    }
    
    public boolean deleteAll(){
        checkEntityManager();
        try {
            String name = entityClass.getSimpleName();
            Query q1 = em.createQuery("DELETE FROM " + name);
            if (useTransactions) {
                em.getTransaction().begin();
            }
            q1.executeUpdate();
            if (useTransactions) {
                em.getTransaction().commit();
            }
            logger.log(Level.FINEST, "Deleted {0}", name);
       
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not delete all", e);
            return false;
        }
        return true;
    }

    /**
     * This will update an object in the database.
     *
     * @param obj The entity to update
     * @return The updated entity, null on failure
     */
    public T update(T obj) {
        checkEntityManager();
        try {
            if (useTransactions) {
                em.getTransaction().begin();
            }
            obj = em.merge(obj);
            if (useTransactions) {
                em.getTransaction().commit();
            }
            logger.log(Level.FINEST, "Updated {0}: {1}", new Object[]{obj.getClass().getSimpleName(), obj});

            return obj;
        } catch (ConstraintViolationException e) {
            logger.log(Level.WARNING, getConstraintViolationErrorMessage(e));
            logger.log(Level.SEVERE, "Could not update " + obj.getClass().getSimpleName() + ": " + obj, e);
            return null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not update " + obj.getClass().getSimpleName() + ": " + obj, e);
            return null;
        }
    }

    /**
     * Determines if a entity is in the database
     *
     * @param id The id of the entity
     * @return True if the entity is currently in the database, false otherwise
     */
    public boolean exists(Integer id) {
        return find(id) != null;
    }

 

    /**
     * Gets the current data and time as a string
     *
     * @param format The format for the time
     * @return The current time in the requested format
     */
    public static String getCurrentDateTimeString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Finds an entity based off its identity key
     *
     * @param idForEntity The value of the key
     * @return The requested entity, or null if the entity could not be found
     */
    public T find(Object idForEntity) {
        checkEntityManager();
        if (idForEntity == null) {
            logger.log(Level.WARNING, "A null id key was passed in when trying to find a entity using BaseDao");
            return null;
        }
        return (T) getEntityManager().find(entityClass, idForEntity);
    }

    /**
     * Finds an entity based off its identity key
     * @param idForEntity The value of the key
     * @param clazz Class of the entity you want
     * @return The requested entity, or null if the entity could not be found
     */
    public <X> X find(Object idForEntity, Class<X> clazz) {
        checkEntityManager();
        if (idForEntity == null) {
            logger.log(Level.WARNING, "A null id key was passed in when trying to find a entity using BaseDao");
            return null;
        }
        return (X) getEntityManager().find(clazz, idForEntity);
    }
    
    /**
     * Finds all entities of a certain type
     *
     * @return A list of all the entities, null if the entity is not in the
     * database or on error
     */
    public List<T> findAll() {
        checkEntityManager();
        javax.persistence.criteria.CriteriaQuery cq = 
                getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    /**
     * Finds entities based off a range. This is useful for paging. Say there
     * are 10 rows in the database for a entity. Say you only want to display
     * the second through the fifth entities. You would pass in 2 for
     * rangeStart, and 5 for rangeEnd.
     *
     * @param rangeStart The starting index of results to return
     * @param rangeEnd The end index of results to return
     * @return A list of entities within this range
     */
    public List<T> findRange(int rangeStart, int rangeEnd) {
        checkEntityManager();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(rangeEnd - rangeStart);
        q.setFirstResult(rangeStart);
        return q.getResultList();
    }

    /**
     * Returns the number of rows in the database for a entity type
     *
     * @return The number of entities in the database
     */
    public int count() {
        checkEntityManager();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Empties the EntityManager cache for all entities of the type that this
     * DAO class is managing. For example, if this class is managing the Account
     * entity, then this function will remove all Account objects from the
     * cache. The end result is that when an entity is needed, it will need to
     * be pulled form the database thus giving you an updated version of it. Use
     * this function if you want to make sure that the entities you are
     * interacting with are 100 percent the same as whats in the database.
     * Sometimes the cache will cause the entities to become old. This is
     * especially true if the database is/can be updated from outside the
     * application.
     */
    public void emptyCacheForEntity() {
        checkEntityManager();
        getCache().evict(entityClass);
    }

    /**
     * Returns the cache of the underlying EntityManager
     *
     * @return The cache of the underlying EntityManager
     */
    public Cache getCache() {
        checkEntityManager();
        return em.getEntityManagerFactory().getCache();
    }

    /**
     * Gets the string representation for constraint violations. For example if
     * you try to delete a entity but it has a foreign key restraint, this
     * string will tell you that info
     *
     * @param e The ConstraintViolationExcetion that was thrown and will contain
     * the text
     * @return The ConstraintViolationException text
     */
    private String getConstraintViolationErrorMessage(ConstraintViolationException e) {
        StringBuilder builder = new StringBuilder("Constraint violation: ");
        for (ConstraintViolation v : e.getConstraintViolations()) {
            StringBuilder append = builder.append("\n").append(v.getInvalidValue()).append(" ").append(v.getMessage());
        }
        return builder.toString();
    }

   
    private void checkEntityManager(){
        if(em == null){
            throw new RuntimeException("Entity Manager was never set");
        }
    }

}
