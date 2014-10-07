package contact.service.jpa;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

<<<<<<< HEAD
=======
import service.ContactDao;
import service.DaoFactory;

>>>>>>> 277beb88cb230902f6a4656fa331e45c985f9eea
import contact.service.*;
import contact.service.mem.MemContactDao;

/**
 * JpaDaoFactory is a factory for DAO that use the Java Persistence API (JPA)
 * to persist objects.
 * The factory depends on the configuration information in META-INF/persistence.xml.
 * 
 * @see contact.service.DaoFactory
 * @version 2014.09.19
 * @author jim
 */
public class JpaDaoFactory extends DaoFactory {
	private static final String PERSISTENCE_UNIT = "contacts";
	/** instance of the entity DAO */
	private ContactDao contactDao;
<<<<<<< HEAD
	/**The factory to create entity in each type.*/
	private final EntityManagerFactory emf;
	/**Manager of entity*/
	private EntityManager em;
	/**Logger to show what happen*/
=======
	private final EntityManagerFactory emf;
	private EntityManager em;
>>>>>>> 277beb88cb230902f6a4656fa331e45c985f9eea
	private static Logger logger;
	
	static {
		logger = Logger.getLogger(JpaDaoFactory.class.getName());
	}
	
<<<<<<< HEAD
	/*
	 * Initialize the factory of JPA
	 */
=======
>>>>>>> 277beb88cb230902f6a4656fa331e45c985f9eea
	public JpaDaoFactory() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		em = emf.createEntityManager();
		contactDao = new JpaContactDao( em );
	}
	
	@Override
	public ContactDao getContactDao() {
		return contactDao;
	}
	
	@Override
	public void shutdown() {
		try {
			if (em != null && em.isOpen()) em.close();
			if (emf != null && emf.isOpen()) emf.close();
		} catch (IllegalStateException ex) {
			logger.log(Level.SEVERE, ex.toString());

		}
	}
}
