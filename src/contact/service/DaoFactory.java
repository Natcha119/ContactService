package contact.service;

import org.objectweb.asm.commons.StaticInitMerger;

import contact.service.DaoFactory;
import contact.service.jpa.JpaDaoFactory;
import contact.service.mem.MemContactDao;
import contact.service.mem.MemDaoFactory;

/**
 * Abstract class represent factory of DAO 
 * which has two type
 * ;JPA and Mem
 * @author Natcha Chidchob 5510546026
 * @version 23.9.2014
 *
 */
public abstract class DaoFactory {
	/**The DAO factory*/
	private static DaoFactory factory;
	
	/*
	 * Give the current DAO factory
	 * @return recent DAO factory
	 */
	public static DaoFactory getInstance() {
		if (factory == null) factory = new MemDaoFactory();
		return factory;
	}
	
	/*
	 * Give all contact of current DAO
	 * @return the contact 
	 */
	public abstract ContactDao getContactDao();

	/*
	 * To save current data in xml form before closing server.
	 */
	public abstract void shutdown() ;
}
