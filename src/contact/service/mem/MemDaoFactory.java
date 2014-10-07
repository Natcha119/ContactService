package contact.service.mem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

<<<<<<< HEAD
import contact.entity.Contact;
import contact.service.DaoFactory;
=======
import entity.Contact;

import service.DaoFactory;
>>>>>>> 277beb88cb230902f6a4656fa331e45c985f9eea

/**
 * Manage instances of Data Access Objects (DAO) used in the app.
 * This enables you to change the implementation of the actual ContactDao
 * without changing the rest of your application.
 * 
 * @author jim
 */
public class MemDaoFactory extends DaoFactory{
	// singleton instance of this factory
	private static MemDaoFactory factory;
	/**The instance of MemContactDao*/
	private MemContactDao daoInstance;
<<<<<<< HEAD
	/**The path of file*/
	private String ex_path = "C:\\MyJava\\XMLFile.xml";
	
=======
>>>>>>> 277beb88cb230902f6a4656fa331e45c985f9eea
	
	/*
	 * Initialize the instance of MemContactDao.
	 */
	public MemDaoFactory() {
		daoInstance = new MemContactDao();
		
		try {
			JAXBContext context = JAXBContext.newInstance(ContactList.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
<<<<<<< HEAD
			File fileIn = new File(ex_path);
=======
			File fileIn = new File("D:\\MyJava\\XMLFile.xml");
>>>>>>> 277beb88cb230902f6a4656fa331e45c985f9eea
			ContactList list = (ContactList)unmarshaller.unmarshal(fileIn);
			
			for(int i = 0;i < list.getList().size();i++){
				daoInstance.save(list.getList().get(i));
			}
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the instance of DaoFactory.
	 * @return instance of DaoFactory.
	 */
	public static MemDaoFactory getInstance() {
		if (factory == null) factory = new MemDaoFactory();
		return factory;
	}
	
	/**
	 * Get the instance of ContactDao.
	 * @return the instance of ContactDao.
	 */
	public MemContactDao getContactDao() {
		return daoInstance;
	}

	@Override
	public void shutdown() {
		List<Contact> allContact = daoInstance.findAll();
		ContactList conList = new ContactList();
		conList.setList(allContact);
		
		try {
			JAXBContext context = JAXBContext.newInstance(ContactList.class);
			Marshaller marshaller = context.createMarshaller();
<<<<<<< HEAD
			File file = new File(ex_path);
=======
			File file = new File("D:\\MyJava\\XMLFile.xml");
>>>>>>> 277beb88cb230902f6a4656fa331e45c985f9eea
			marshaller.marshal(conList, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
}
