package contact.service;

import contact.service.DaoFactory;

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
		if(factory == null){
			factory = new contact.service.mem.MemDaoFactory();
			String factoryClass = System.getProperty("contact.daofactory");
			if(factoryClass != null) {
				try{
					//load and instatiate this class of run time
					ClassLoader loader = DaoFactory.class.getClassLoader();
					factory = (DaoFactory)loader.loadClass(factoryClass).newInstance();
				}
				catch(InstantiationException | IllegalAccessException | ClassNotFoundException e){
					e.printStackTrace();
				}
			}
			factory = new contact.service.mem.MemDaoFactory();
		}
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
