package service;

import java.util.List;

import entity.Contact;

public interface ContactDao {
	
	public Contact find(long id);
	
	public List<Contact> findAll();
	
	public boolean delete(long id);
	
	public boolean save(Contact contact);
	
	public boolean update(Contact update);	
	
	public List<Contact> findByTitle(String prefix);
}
