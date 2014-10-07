package contact.service;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.validation.constraints.AssertTrue;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import contact.JettyMain;
import contact.entity.Contact;
import contact.service.ContactDao;
import contact.service.mem.MemContactDao;

/*
 * The test unit to test whether if 
 * in situation of single user, is ETag
 * help in working correctly.
 * @author Natcha Chidchob5510546026
 * @version 4.10.2014
 */
public class ETagTest {
	/**the contactDao we interest*/
	ContactDao dao;
	/**contact number1*/
	Contact contact1;
	/**contact number2*/
	Contact contact2;
	/**contact number3*/
	Contact contact3;
	/**client for http*/
	private static HttpClient client;
	
	/**
	 * Set up contactDao to be MemContactDao
	 * and set example contact for test.
	 */
	@Before
	public void setUp() {
		// create a new DAO for each test and create some sample contacts
		dao = new MemContactDao();
		contact1 = new Contact("contact1", "Joe Contact", "joe@microsoft.com");
		contact2 = new Contact("contact2", "Sally Contract", "sally@foo.com");
		contact3 = new Contact("contact3", "Foo Bar", "foo@barclub.com");
	}
	
	/**
	 * Save all example contacts
	 */
	private void saveAllContacts() {
		dao.save(contact1);
		dao.save(contact2);
		dao.save(contact3);
	}
	

	/**
	 * Run server and client
	 * @throws Exception
	 */
	@BeforeClass
	public static void doFirst() throws Exception {
		JettyMain.startServer(8080);
		client = new HttpClient();
		client.start();
	}
	
	/**
	 * Close the server
	 */
	@AfterClass
	public static void doLast() {
		JettyMain.stopServer();
	}
	
	
	/**
	 * To test GET method whether it has ETag or not.
	 * by getting contact id is 1
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void testPassGet() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse resp = client.GET("http://localhost:8080/"+"contacts/1");
		assertEquals("Return 200 OK, GET test PASS", Status.OK.getStatusCode(), resp.getStatus());
		String etag = resp.getHeaders().get("ETag");
		assertTrue("Have the ETag",etag != null);
	}
	
	/**
	 * To test POST method whether it has ETag or not.
	 * by POST the new contact with new id
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 */
	@Test
	public void testPassPost() throws InterruptedException, TimeoutException, ExecutionException {
		 StringContentProvider message = new StringContentProvider("<contact id=\"5\">" +
					"<title>Test Contact</title>" +
					"<name>Kaofaang</name>" +
					"<email>eiei@gmail.com</email>" +
					"</contact>");
		 Request req = client.newRequest("http://localhost:8080/"+"contacts");
		 req.method(HttpMethod.POST);
		 req.content(message,"application/xml");
		 
		 ContentResponse resp = req.send();
		 
		 assertEquals("Return 201 Created, Create new contact", Status.CREATED.getStatusCode(), resp.getStatus());
		 String etag = resp.getHeaders().get("ETag");
		 assertTrue("Have the ETag",etag != null);
	}
}
