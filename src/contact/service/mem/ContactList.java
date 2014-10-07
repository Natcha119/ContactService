package contact.service.mem;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

<<<<<<< HEAD
import contact.entity.Contact;
=======
import entity.Contact;
>>>>>>> 277beb88cb230902f6a4656fa331e45c985f9eea
/**
 * This class use for make common ArrayList
 * to store XML object
 * @author Natcha Chidchob 5510546026
 * @version 23.9.2014
 * */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactList {
	/**List to keep contact.*/
	private List<Contact> conArr;
	
	/**
	 * Initialize the contact list.*/
	public ContactList(){
		conArr = new ArrayList<Contact>();
	}
	
	/*
	 * Set the contact list
	 * @param all the contact*/
	public void setList(List<Contact> allContact){
		conArr = allContact;
	}
	
	/*
	 * To tell the list of contact
	 * @return list of contact**/
	public List<Contact> getList(){
		return conArr;
	}
}
