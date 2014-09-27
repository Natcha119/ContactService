package contact.service;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response.Status;

import greeter.resource.ContactResource;

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

import service.ContactDao;

import entity.Contact;
import entity.JettyMain;
import contact.service.mem.MemContactDao;


public class ContactDaoTest {
	ContactDao dao;
	Contact contact1;
	Contact contact2;
	Contact contact3;
	private static String serviceUrl;
	private static HttpClient client;
	
	@Before
	public void setUp() {
		// create a new DAO for each test and create some sample contacts
		dao = new MemContactDao();
		contact1 = new Contact("contact1", "Joe Contact", "joe@microsoft.com");
		contact2 = new Contact("contact2", "Sally Contract", "sally@foo.com");
		contact3 = new Contact("contact3", "Foo Bar", "foo@barclub.com");
	}
	
	private void saveAllContacts() {
		dao.save(contact1);
		dao.save(contact2);
		dao.save(contact3);
	}
	
	@BeforeClass
	public static void doFirst() throws Exception {
		serviceUrl = JettyMain.startServer(8080);
		client = new HttpClient();
		client.start();
	}
	
	@AfterClass
	public static void doLast() {
		JettyMain.stopServer();
	}
	
	@Test
	public void testPassGet() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse resp = client.GET("http://localhost:8080/"+"contacts/1");
		assertEquals("Return 200 OK, GET test PASS", Status.OK.getStatusCode(), resp.getStatus());
	}
	
	@Test
	public void testFailGet() throws InterruptedException, ExecutionException, TimeoutException {
		ContentResponse resp = client.GET("http://localhost:8080/"+"contacts/12");
		assertEquals("Return 204 No Content, GET test FAIL!", Status.NO_CONTENT.getStatusCode(), resp.getStatus());
	}
	
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
	
	@Test 
	public void testPassDel() throws InterruptedException, TimeoutException, ExecutionException {
		Request req = client.newRequest("http://localhost:8080/"+"contacts/5");
		req.method(HttpMethod.DELETE);
	
		ContentResponse resp = req.send();
		
		assertEquals("Return 200 OK, DELETE test PASS", Status.OK.getStatusCode(), resp.getStatus());
	}
	
	@Test
	public void testFailDel() throws InterruptedException, TimeoutException, ExecutionException {
		Request req = client.newRequest("http://localhost:8080/"+"contacts/9999");
		req.method(HttpMethod.DELETE);
	
		ContentResponse resp = req.send();
		
		assertEquals("Return 400 BAD REQUEST, DELETE test FAIL!", Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
	}
	
}
