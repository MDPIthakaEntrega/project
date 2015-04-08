package database;

import java.io.IOException;
import service.ResponseStruct;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public abstract class API {
	
	static protected Session current_session;
	static protected String host;
	static protected String keyspace_name;
	static protected String review_table;
	static protected String inverted_table;

	static public void initializeDatabase(String host_i, String keyspace_name_i, String
			review_table_i, String inverted_table_i) {
		host = host_i;
		keyspace_name = keyspace_name_i;
		review_table = review_table_i;
		inverted_table = inverted_table_i;
		CreateReviewKeyspace init_session = new CreateReviewKeyspace(host, keyspace_name,
				review_table, inverted_table);
		current_session = init_session.connect();
	}
	
	public abstract void init(String folder_location_i) throws IOException;

	public abstract void insert(String response, String company_name) throws JSONException, ParseException;
 
	public abstract JSONObject formatReview(Row current_row, List<String> attributes) throws JSONException;

	public void insert(ResponseStruct responses) throws JSONException, ParseException {
		// TODO Auto-generated method stub
	} 

	
}