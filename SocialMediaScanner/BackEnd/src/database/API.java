package database;

import java.io.IOException;

import service.ResponseStruct;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public abstract class API {
	
	static protected Session current_session;
	static protected String host;
	static protected String keyspace_name;
	static protected String review_table;
	static protected String inverted_table;
	static protected PreparedStatement preparedStmt; 

	static public void initializeDatabase(String host_i, String keyspace_name_i, String
			review_table_i, String inverted_table_i) {
		host = host_i;
		keyspace_name = keyspace_name_i;
		review_table = review_table_i;
		inverted_table = inverted_table_i;
		CreateReviewKeyspace init_session = new CreateReviewKeyspace(host, keyspace_name,
				review_table, inverted_table);
		current_session = init_session.connect();
		
		preparedStmt = current_session.prepare("INSERT INTO " +  review_table + 
	    		" (review_id, company_name, json)" + "VALUES (?,?,?) IF NOT EXISTS;");
		
	}
	
	public abstract void init(String folder_location_i, List<String> attributes) throws IOException;

	public abstract void insert(String response, String company_name) throws JSONException, ParseException;
 
	public abstract JSONObject formatReview(Row current_row, List<String> attributes) throws JSONException;

	public void insert(ResponseStruct responses) throws JSONException, ParseException {
		// TODO Auto-generated method stub
	}

	public void test() {
		System.out.println(this.getClass().getSimpleName());
	}

	
}