package greeter.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import service.ContactDao;
import service.DaoFactory;

import contact.service.mem.MemContactDao;
import contact.service.mem.MemDaoFactory;



import entity.Contact;


@Path("/contacts")
/*
 * @author Natcha Chidchob 5510546026
 * @version 16.9.2014
 * Map the request to request code.
 */
public class ContactResource {
	/**DAO of contact*/
	ContactDao dao;
	/**Contact which we interest*/
	Contact contact;
	
	@Context
	/**URI information*/
	UriInfo uriInfo;
	
	/*
	 * Initialize element of class
	 */
	public ContactResource() {
		dao = DaoFactory.getInstance().getContactDao();
	}
	
	/*
	 * To bring all contacts in list
	 * @return all contact
	 */
	public Response getAllContacts() {
		java.util.List<Contact> list = dao.findAll();
		return Response.ok(new GenericEntity<java.util.List<Contact>>(list){}).build();
	}
	
	/*
	 * Tell contact by id
	 * @param id
	 * @return contact
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getContact(@PathParam("id") long id ) {
		contact = dao.find(id);
		if(contact != null)
			return Response.ok(contact).build();
		return Response.noContent().build();
	}

	/*
	 * Bring contact which map with title
	 * @param title
	 * @return contact with relate title
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContactsByTitle(@QueryParam("q") String title) {
		if(title == null)
			return getAllContacts();
		java.util.List<Contact> list = dao.findAll();
		ArrayList<Contact> found = new ArrayList<Contact>();
		for (int i = 0; i < list.size(); i++) {
			contact = list.get(i);
			if(contact.getTitle().contains(title))
				found.add(contact);
		}
		if(found.isEmpty())
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(new GenericEntity<ArrayList<Contact>>(found){}).build();
	}
	
	/*
	 * Create new contact
	 * @param  element,uri Information
	 * @return uri of new contact
	 * @throws URISyntaxException 
	 */
	@POST
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Response post( 
	JAXBElement<Contact> element, @Context UriInfo uriInfo ) throws URISyntaxException 
	{
		Contact contact = element.getValue();
		if(dao.find(contact.getId()) == null){
			boolean is_save = dao.save( contact );
			if(is_save)
				return Response.created(new URI("http://localhost:8080/contacts/"+contact.getId())).build();
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.status(Status.CONFLICT).build();
	}
	
	/*
	 * Update contact with id
	 * @param element,uri Information,id
	 * @return uri of updated contact
	 * @throws URISyntaxException 
	 */
	@PUT
	@Path("{id}")
	@Consumes( MediaType.APPLICATION_XML)
	@Produces({ MediaType.APPLICATION_XML })
	public Response put( 
	JAXBElement<Contact> element, @Context UriInfo uriInfo,@PathParam("id") long id ) throws URISyntaxException 
	{
		Contact contact = element.getValue();
		boolean is_update = false;
		if(contact.getId() == id){
			is_update = dao.update( contact );
			if(is_update)
				return Response.ok().build();
		}
		return Response.status(Status.BAD_REQUEST).build();
	}
	
	/*
	 * Delete contact by id
	 * @param contact id
	 */
	@DELETE
	@Path("{id}")
	public Response del(@PathParam("id") long id)
	{
		if(dao.find(id) != null){
			dao.delete(id);
			return Response.ok().build();
		}
		else
			return Response.status(Status.BAD_REQUEST).build();
	}
	
}
