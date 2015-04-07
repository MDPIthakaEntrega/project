package database;

import java.util.List;

import org.json.JSONException;

public interface Data {

	//searches files to find the API's that have been integrated
	public void init(String folder_location_i) throws InstantiationException, IllegalAccessException, ClassNotFoundException;
	
	public void initializeDatabase(String host_i, String keyspace_name_i, String
			review_table_i, String inverted_table_i);
	
	public void insertData(List<String> json, List<String> api_names, String company_name) throws InstantiationException, IllegalAccessException, JSONException, ClassNotFoundException;
	
	public abstract String select(String search, String company_name) throws JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException;
	
	
	public abstract String select(String search, String company_name, List<String> attributes) throws JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException;
	
}
