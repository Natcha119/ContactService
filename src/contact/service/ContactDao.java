package contact.service;

import java.util.List;

import contact.entity.Contact;

/*
 * The interface to access and handle with contact
 * which use CRUD.
 * @author Natcha Chidchob 5510546026
 * @version 7.10.2014
 */
public interface ContactDao {
	/*
	 * To find each contact by using id of contact.
	 * @param id of contact.
	 * @return contact
	 */
	public Contact find(long id);
	
	/*
	 * To access all contact information.
	 * @return list of contact.
	 */
	public List<Contact> findAll();
	
	/*
	 * Delete contact by id
	 * @param id of contact
	 * @return true if delete that id successfully 
	 */
	public boolean delete(long id);
	
	/*
	 * To save the new contact into list
	 * @param contact
	 * @return true if the new contact can be saved
	 */
	public boolean save(Contact contact);
	
	/*
	 * Update the existing contact's information.
	 * @param contact to update
	 * @return true if there was updated
	 */
	public boolean update(Contact update);	
	
	/*
	 * Find all possible contact by their title.
	 * @param title.
	 * @return list of contact.
	 */
	public List<Contact> findByTitle(String prefix);
}
