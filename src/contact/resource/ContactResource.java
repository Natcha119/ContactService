package contact.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import contact.entity.Contact;
import contact.service.ContactDao;
import contact.service.DaoFactory;
import contact.service.mem.MemContactDao;
import contact.service.mem.MemDaoFactory;





@Path("/contacts")
/*
 * @author Natcha Chidchob 5510546026
 * @version 4.10.2014
 * Map the request to request code
 * and also work with ETag.
 */
public class ContactResource {
	/**DAO of contact*/
	ContactDao dao;
	/**Contact which we interest*/
	Contact contact;
	/**Collect all the cache to handle it.*/
	CacheControl cc;
	
	
	@Context
	/**URI information*/
	UriInfo uriInfo;
	
	/*
	 * Initialize element of class
	 */
	public ContactResource() {
		dao = DaoFactory.getInstance().getContactDao();
		cc = new CacheControl();
		cc.setMaxAge(86400);
		cc.setPrivate(true);
	}
	
	/*
	 * To bring all contacts in list
	 * @return all contact
	 */
	private Response getAllContacts() {
		java.util.List<Contact> list = dao.findAll();
		if(list.isEmpty())
			return Response.status(Response.Status.NOT_FOUND).build();
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
	public Response getContact(@HeaderParam("If-None-Match") String nm, @PathParam("id") long id ) {
		contact = dao.find(id);
		if(contact == null)
			return Response.noContent().build();
		String etag = contact.hashCode()+"";
		String check_etag = "\""+etag+"\"";
		if(nm != null && check_etag.equals(nm)){
			return Response.status(Status.NOT_MODIFIED).cacheControl(cc).tag(etag).build();
		}
		return Response.ok(contact).cacheControl(cc).tag(etag).build();
	}
	/*
	 * Bring contact which map with title
	 * @param title
	 * @return contact with relate title
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getContactsByTitle(@QueryParam("title") String title) {
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
			if(is_save){
				EntityTag etag = new EntityTag(contact.hashCode()+"");
				return Response.created(new URI("http://localhost:8080/contacts/"+contact.getId())).cacheControl(cc).tag(etag).build();
			}
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
	@HeaderParam("If-Match") String match, JAXBElement<Contact> element, @Context UriInfo uriInfo,@PathParam("id") long id ) throws URISyntaxException 
	{
		Contact con1 = dao.find(id);
		String oldTag = con1.hashCode() + "";
		if (( "\"" + oldTag + "\"").equals(match)) {
			contact = element.getValue();
			dao.update( contact );
			return Response.ok().cacheControl(cc).tag(oldTag).build();	
		}
		return Response.status(Status.PRECONDITION_FAILED).cacheControl(cc).tag(oldTag).build();
	}
	
	/*
	 * Delete contact by id
	 * @param contact id.
	 */
	@DELETE
	@Path("{id}")
	public Response del(@HeaderParam("If-Match") String im, @PathParam("id") long id)
	{
		contact = dao.find(id);
		if(contact == null)
			return Response.status(Status.BAD_REQUEST).build();
		String tag = contact.hashCode() + "";
		if (( "\"" + tag + "\"").equals(im)) {
			dao.delete(id);
			return Response.ok().cacheControl(cc).tag(tag).build();
		}
		return Response.status(Status.PRECONDITION_FAILED).cacheControl(cc).tag(tag).build();
	}
	
}
