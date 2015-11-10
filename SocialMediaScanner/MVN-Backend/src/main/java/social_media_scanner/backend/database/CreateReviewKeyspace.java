package social_media_scanner.backend.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DefaultRetryPolicy;

/**
 *  
 * Used to create the keyspace and tables,
 * call getinstance() to get instance, then call
 * init()
 *
 * @author Charlie
 *
 */


public class CreateReviewKeyspace {
	
	private Cluster cluster;
	private Session current_session;
	private String host;
	private String keyspace_name;
	private String review_table;
	private String inverted_table;
	boolean created = false;
	
	private static CreateReviewKeyspace singleton = new CreateReviewKeyspace();
	
	public static void main(String[] args) {
		System.out.print("TEST");
		CreateReviewKeyspace test = CreateReviewKeyspace.getInstance();
		test.init();
		System.out.print("SUCCESS");

	}
	
	public static CreateReviewKeyspace getInstance() {
		
		return singleton;
	}
	
	private CreateReviewKeyspace() {
		host = "127.0.0.1";
		keyspace_name = "review_keyspace2";
		review_table = "review_table2";
		inverted_table = "inverted_table2";
	}
	
	private CreateReviewKeyspace(String host_i, String keyspace_name_i, String review_table_i, 
				String inverted_table_i) {
		host = host_i;
		keyspace_name = keyspace_name_i;
		review_table = review_table_i;
		inverted_table = inverted_table_i;
	}
	
	public void init() {
		if(!created) {
			this.createKeyspace();
			this.connect();
			this.createReviewTable();
			this.createInvertedTable();
			this.createIndex();
			this.close();
			created = true;
		}
	}	
	
	public void close() {
		
		current_session.close();
		cluster.close();
	}
	
	public void createKeyspace() {

		cluster = Cluster
        	    .builder()
        	    .addContactPoint(host)
        	    .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
        	    .build();
		current_session = cluster.connect();
		current_session.execute("CREATE KEYSPACE IF NOT EXISTS " + keyspace_name + " WITH replication" +
			       "= {'class':'SimpleStrategy', 'replication_factor':1};");
	
	}

	public Session connect() {
		Cluster cluster;
        cluster = Cluster
        	    .builder()
        	    .addContactPoint(host)
        	    .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
        	    .build();
        current_session = cluster.connect(keyspace_name);      
        return current_session;
	}
	
	public void createReviewTable() {
		current_session.execute( 
				" CREATE TABLE IF NOT EXISTS " + keyspace_name + "." + review_table + " (" +
						   "review_id varchar," +
						   "company_name varchar, " +
						   "json varchar, " +
						   "PRIMARY KEY(review_id));"
				);	
	}	
	
	public void createIndex() {
		
		current_session.execute("CREATE INDEX IF NOT EXISTS " + "company2" + 
				" ON " + keyspace_name + "." +  review_table + " (company_name);" );

	}
	
	public void createInvertedTable() {
		current_session.execute(
				"CREATE TABLE IF NOT EXISTS " + keyspace_name + "." + inverted_table + " (" +
						"review_word varchar, " +
						"review_id_set set<varchar>, " +
						"PRIMARY KEY(review_word));"
				);	
	}
}
