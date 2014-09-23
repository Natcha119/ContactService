package entity;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A person is a contact with a name, title, and email.
 * title is text to display for this contact in a list of contacts,
 * such as a nickname or company name.
 */
@Entity 
@XmlRootElement(name="contacts")
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlAttribute
	private long id;
	@XmlElement(required=true,nillable=false)
	/**Title of contact*/
	private String title;
	/**Name of contact*/
	private String name;
	/**Email of contact*/
	private String email;
	/** URL of photo */
	private String photoUrl;
	
	public Contact() { }
	
	/**
	 * Initialize information of new contact 
	 * @param title of contact, his name and email*/
	public Contact(String title, String name, String email ) {
		this.title = title;
		this.name = name;
		this.email = email;
		this.photoUrl = "";
	}

	/**
	 * Set id of contact.
	 * @param idid of contact
	 */
	public Contact(long id) {
		this.id = id;
	}

	/**
	 * Give URL of photo.
	 * @return URL
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}

	/**
	 * Set URL of photo
	 * @param photo
	 */
	public void setPhotoUrl(String photo) {
		this.photoUrl = photo;
	}

    /**
     * Give contact name
     * @return name
     */
	public String getName() {
		return name;
	}

	/**
	 * Set contact name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Give contact title
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of contact
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Give email of contact
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the contact email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * Give contact id
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set id of contact
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return String.format("[%ld] %s (%s)", id, name, title);
	}
	
	/** Two contacts are equal if they have the same id,
	 * even if other attributes differ.
	 * @param other another contact to compare to this one.
	 */
	public boolean equals(Object other) {
		if (other == null || other.getClass() != this.getClass()) return false;
		Contact contact = (Contact) other;
		return contact.getId() == this.getId();
	}
	
	/**
	 * Update this contact's data from another Contact.
	 * The id field of the update must either be 0 or the same value as this contact!
	 * @param update the source of update values
	 */
	public void applyUpdate(Contact update) {
		if (update == null) return;
		if (update.getId() != 0 && update.getId() != this.getId() )
			throw new IllegalArgumentException("Update contact must have same id as contact to update");
		// Since title is used to display contacts, don't allow empty title
		if (! isEmpty( update.getTitle()) ) this.setTitle(update.getTitle()); // empty nickname is ok
		// other attributes: allow an empty string as a way of deleting an attribute in update (this is hacky)
		else this.setTitle(" ");
		if (update.getName() != null ) this.setName(update.getName()); 
		else this.setName(" ");
		if (update.getEmail() != null) this.setEmail(update.getEmail());
		else this.setEmail(" ");
		if (update.getPhotoUrl() != null) this.setPhotoUrl(update.getPhotoUrl());
		else this.setPhotoUrl(" ");
	}
	
	/**
	 * Test if a string is null or only whitespace.
	 * @param arg the string to test
	 * @return true if string variable is null or contains only whitespace
	 */
	private static boolean isEmpty(String arg) {
		return arg == null || arg.matches("\\s*") ;
	}
}