package database;
import java.util.Set;

import org.json.JSONObject;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;


public class CreateReviewKeyspace {
	
	private Session current_session;
	private String host;
	private String keyspace_name;
	private String review_table;
	private String inverted_table;
	
	CreateReviewKeyspace() {
		host = "127.0.0.1";
		keyspace_name = "review_keyspace";
		review_table = "review_table2";
		inverted_table = "inverted_table";
	}
	
	CreateReviewKeyspace(String host_i, String keyspace_name_i, String review_table_i, 
				String inverted_table_i) {
		host = host_i;
		keyspace_name = keyspace_name_i;
		//can change to allow parameters
		review_table = review_table_i;
		inverted_table = inverted_table_i;
	}
	
	public void init() {
		this.connect();
		this.createKeyspace();
		this.createReviewTable();
		this.createInvertedTable();
		this.createIndex();
	}
	
	
	public static void main(String[] args) {
//		System.out.print("TEST");
//		CreateReviewKeyspace initial = new CreateReviewKeyspace();
//		initial.createKeyspace("127.0.0.1", "review_keyspace");
//		Session test = initial.connect();
//		initial.createTable(test, "review_table2");
//		initial.createIndex(test, "company2", "review_table2");
//		initial.createInvertedTable();
//		initial.test(test);
//		System.out.print("SUCCESS");

	}
	
	public void createKeyspace() {
		Cluster cluster;
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
				" CREATE TABLE IF NOT EXISTS " + review_table + " (" +
						   "review_id varchar," +
						   "company_name varchar, " +
						   "json varchar, " +
						   "PRIMARY KEY(review_id));"
				);	
	}	
	
	public void createIndex() {
		
		current_session.execute("CREATE INDEX IF NOT EXISTS " + inverted_table + 
				" ON review_table2 (company_name);" );

	}
	
	public void createInvertedTable() {
		current_session.execute(
				"CREATE TABLE IF NOT EXISTS " + inverted_table + " (" +
						"review_word varchar, " +
						"review_id_set set<varchar>, " +
						"PRIMARY KEY(review_word));"
				);	
	}
	
//	public void test(Session current_session) {
//		String insert = "CREATE TABLE IF NOT EXISTS " + "inverted_table" + " (" +
//				"review_word varchar, " +
//				"review_id_set set<varchar>, " +
//				"PRIMARY KEY(review_word));";
//	}
}
