package contact.service;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import contact.entity.Contact;
import contact.JettyMain;
import contact.resource.ContactResource;
import contact.service.ContactDao;
import contact.service.mem.MemContactDao;

/**
 * This class use for testing HTTP method for
 * contactDao, which contain the test for GET PUT POST DELTE
 * and have two cases for each test(PASS or FAIL)
 * @author Natcha Chidchob 5510546026
 * @version 27.9.2014
 */
public class ContactDaoTest {
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
	 * To test GET method in case of PASS
	 * by getting contact id is 1
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void testPassGet() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse resp = client.GET("http://localhost:8080/"+"contacts/1");
		assertEquals("Return 200 OK, GET test PASS", Status.OK.getStatusCode(), resp.getStatus());
	}
	
	/**
	 * To test GET method in case of FAIL
	 * by getting not existing contact id
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void testFailGet() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse resp = client.GET("http://localhost:8080/"+"contacts/12");
		assertEquals("Return 204 No Content, GET test FAIL!", Status.NO_CONTENT.getStatusCode(), resp.getStatus());
	}
	
	/**
	 * To test POST method in case of PASS
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
	}
	
	/**
	 * To test POST method in case of FAIL
	 * by POST the new contact with existing id
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 */
	@Test
	public void testFailPost() throws InterruptedException, TimeoutException, ExecutionException {
		 StringContentProvider message = new StringContentProvider("<contact id=\"1\">" +
					"<title>Test Contact</title>" +
					"<name>Kaofaang</name>" +
					"<email>eiei@gmail.com</email>" +
					"</contact>");
		 Request req = client.newRequest("http://localhost:8080/"+"contacts");
		 req.method(HttpMethod.POST);
		 req.content(message,"application/xml");
		 
		 ContentResponse resp = req.send();
		 
		 assertEquals("Return 409 CONFLICT", Status.CONFLICT.getStatusCode(), resp.getStatus());
	}
	
	/**
	 * To test PUT method in case of PASS
	 * by update contact with existing id
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 */
	@Test
	public void testPassPut() throws InterruptedException, TimeoutException, ExecutionException {
		StringContentProvider content = new StringContentProvider("<contact id=\"1\">" +
				"<title>Test Contact</title>" +
				"<name>Kaofaang</name>" +
				"<email>eiei@gmail.com</email>" +
				"</contact>");
		Request req = client.newRequest("http://localhost:8080/"+"contacts/1");
		req.method(HttpMethod.PUT);
		req.content(content, "application/xml");
		
		ContentResponse resp = req.send();
		
		assertEquals("Return 200 OK, PUT test PASS", Status.OK.getStatusCode(), resp.getStatus());
	}

	/**
	 * To test PUT method in case of FAIL
	 * by update contact with not existing id
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 */
	@Test
	public void testFailPut() throws InterruptedException, TimeoutException, ExecutionException {
		StringContentProvider content = new StringContentProvider("<contact/123>" +
				"<title>Test Contact</title>" +
				"<name>Kaofaang</name>" +
				"<email>eiei@gmail.com</email>" +
				"</contact>");
		Request req = client.newRequest("http://localhost:8080/"+"contacts/123");
		req.method(HttpMethod.PUT);
		req.content(content, "application/xml");
		
		ContentResponse resp = req.send();
		
		assertEquals("Return BAD REQUEST, PUT test FAIL!", Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
	}
	
	/**
	 * To test DELETE method in case of PASS
	 * by delete contact with existing id
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 */
	@Test 
	public void testPassDel() throws InterruptedException, TimeoutException, ExecutionException {
		Request req = client.newRequest("http://localhost:8080/"+"contacts/5");
		req.method(HttpMethod.DELETE);
	
		ContentResponse resp = req.send();
		
		assertEquals("Return 200 OK, DELETE test PASS", Status.OK.getStatusCode(), resp.getStatus());
	}
	
	/**
	 * To test DELETE method in case of FAIL
	 * by delete contact with not existing id
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws ExecutionException
	 */
	@Test
	public void testFailDel() throws InterruptedException, TimeoutException, ExecutionException {
		Request req = client.newRequest("http://localhost:8080/"+"contacts/9999");
		req.method(HttpMethod.DELETE);
	
		ContentResponse resp = req.send();
		
		assertEquals("Return 400 BAD REQUEST, DELETE test FAIL!", Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
	}
	
}
