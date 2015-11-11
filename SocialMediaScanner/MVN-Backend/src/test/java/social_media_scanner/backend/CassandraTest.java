package social_media_scanner.backend;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.datastax.driver.core.Session;

import social_media_scanner.backend.database.CreateReviewKeyspace;


public class CassandraTest {

	
	@Test
	public void createKeyspace() {
		
		CreateReviewKeyspace testCreateDefault = CreateReviewKeyspace.getDefaultInstance();
		testCreateDefault.init();	
		Session testConnectDefault = testCreateDefault.connect();
		assertTrue("Connect to keyspace failed!", testConnectDefault != null);
		testCreateDefault.close();
		
		CreateReviewKeyspace testCreate = CreateReviewKeyspace.getInstance();
		testCreate.init();
		Session testConnect = testCreate.connect();
		assertTrue("Connect to keyspace failed!", testConnect != null);
		testCreate.close();
		
	}
		
}
